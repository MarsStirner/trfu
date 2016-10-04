package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.primefaces.context.RequestContext;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.dao.BloodSystemTypeDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.OperationalDaoImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.operational.OperationalRoom;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Named("operationalSession")
@SessionScoped
public class OperationalSessionBean implements java.io.Serializable {

    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement;

    // Подтвержденный выбор операционной
    private OperationalRoom currentOperationalRoom;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Диалог выбора операционной
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Сброс выбранной операционной
     */
    public void clearSelectedRoom() {
        currentOperationalRoom = null;
    }

    public OperationalRoom getCurrentOperationalRoom() {
       return currentOperationalRoom;
    }

    public void setCurrentOperationalRoom(OperationalRoom selectedOperationalRoom) {
        this.currentOperationalRoom = selectedOperationalRoom;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Получить список текущих операционных на текущую дату
     *
     * @return список доступных операционых
     */
    public List<OperationalRoom> getRoomList() {
        return getRoomList(new Date());
    }

    /**
     * Получить список текущих операционных на заданную дату
     *
     * @return список доступных операционых
     */
    public List<OperationalRoom> getRoomList(final Date checkDate) {
        return sessionManagement.getDAO(OperationalDaoImpl.class, ApplicationHelper.OPERATIONAL_DAO).getRoomList
                (checkDate);
    }


    public List<BloodSystemType> getOperationalBloodSystemTypes() {
        return sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_TYPE_DAO)
                .getByMnem("oper");
    }

    public List<User> getTransfusiologistList() {
        final UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO);
        return userDAO.getUsersByAppointment("Врач-Трансфузиолог", null, false, -1, -1, "lastName", false);
    }

    public List<User> getOperationalNursesList() {
        final UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO);
        return userDAO.getUsersByAppointment("Операционная сестра", null, false, -1, -1, "lastName", false);
    }


    private static final long serialVersionUID = 1L;


    // Структура для создания новых операционных и временного хранения введенных данных

    private OperationalEditor editor = new OperationalEditor();

    public OperationalEditor getEditor() {
        return editor;
    }

    public void setEditor(OperationalEditor editor) {
        this.editor = editor;
    }

    public void createNewEditor() {
        editor = new OperationalEditor();
    }

    public void saveNewRoom() {
        if (editor != null) {
            if (editor.validate()) {
                final OperationalRoom toCreate = editor.createRoom(sessionManagement.getLoggedUser());
                sessionManagement.getDAO(OperationalDaoImpl.class, ApplicationHelper.OPERATIONAL_DAO).save(toCreate);
                createNewEditor();
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('" + OperationalEditor.DIALOG_WIDGET_VAR + "').hide();");
                currentOperationalRoom = toCreate;
            } else {
                System.out.println("Validation fails");
            }
        }
    }

    public class OperationalEditor {

        public static final String DIALOG_WIDGET_VAR = "newOperationalDialogVar";
        private static final String EDITOR_NAME = "opEditor.name";
        private static final String EDITOR_DOCTORS = "opEditor.doctors";
        private static final String EDITOR_NURSES = "opEditor.nurses";
        private static final String EDITOR_BLOOD_SYS_TYPES = "opEditor.bloodSystemType";

        public OperationalEditor() {
        }

        //Выбранные типы систем крови
        private List<BloodSystemType> bloodSystemTypes = new ArrayList<>(4);

        // Выбранные врачи-трансфузиологи
        private List<User> doctors = new ArrayList<>(2);

        // Выбранные медсестры
        private List<User> nurses = new ArrayList<>(2);

        // Наименование операционной
        private String name = "";

        public boolean validate() {
            boolean result = true;
            final FacesContext context = FacesContext.getCurrentInstance();
            if (bloodSystemTypes.isEmpty()) {
                context.addMessage(EDITOR_BLOOD_SYS_TYPES, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Типы " +
                        "систем" + " крови не выбраны", null));
                result = false;
            }
            if (doctors.isEmpty()) {
                context.addMessage(EDITOR_DOCTORS, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Врачи-трансфузиологи не выбраны", null));
                result = false;
            }
            if (nurses.isEmpty()) {
                context.addMessage(EDITOR_NURSES, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Медсестры не " +
                        "выбраны", null));
                result = false;
            }
            if (name.isEmpty()) {
                context.addMessage(EDITOR_NAME, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Имя операционной не "
                        + "задано", null));
                result = false;
            } else {
                for (OperationalRoom operationalRoom : getRoomList()) {
                    if (name.equals(operationalRoom.getName())) {
                        context.addMessage(EDITOR_NAME, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Имя " +
                                "операционной уже используется", null));
                        result = false;
                        break;
                    }
                }
            }
            return result;
        }

        public OperationalRoom createRoom(final User author) {
            final OperationalRoom newInstance = new OperationalRoom(author, name);
            newInstance.setBloodTypes(new HashSet<>(bloodSystemTypes));
            newInstance.setDoctors(new HashSet<>(doctors));
            newInstance.setNurses(new HashSet<>(nurses));
            return newInstance;
        }

        public List<BloodSystemType> getBloodSystemTypes() {
            return bloodSystemTypes;
        }

        public void setBloodSystemTypes(List<BloodSystemType> bloodTypes) {
            this.bloodSystemTypes = bloodTypes;
        }

        public List<User> getDoctors() {
            return doctors;
        }

        public void setDoctors(List<User> doctors) {
            this.doctors = doctors;
        }

        public List<User> getNurses() {
            return nurses;
        }

        public void setNurses(List<User> nurses) {
            this.nurses = nurses;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name.trim();
        }
    }
}