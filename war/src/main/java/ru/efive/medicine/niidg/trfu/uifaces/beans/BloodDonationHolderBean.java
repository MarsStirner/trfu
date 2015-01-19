package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.*;
import ru.efive.medicine.niidg.trfu.data.entity.operational.OperationalRoom;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.PropertiesEditorBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListByAppointmentHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListByRoleTypeHolderBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.wf.util.OperationalHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named("bloodDonation")
@ConversationScoped
public class BloodDonationHolderBean extends AbstractDocumentHolderBean<BloodDonationRequest, Integer> {
    //TODO logger

    @Override
    public String edit() {
        final String result = super.edit();
        final BloodDonationRequest document = getDocument();
        final boolean isStatusGT1 = document.getStatusId() > 1;
        boolean isEqIds = false;

        if (document.getTransfusiologist() != null) {
            isEqIds = sessionManagement.getLoggedUser().getId() == document.getTransfusiologist().getId();
        }

        final boolean isOperational = sessionManagement.isOperational();
        if (isOperational) {
            final OperationalRoom currentOperationalRoom = operational.getCurrentOperationalRoom();
            if (currentOperationalRoom != null) {
                System.out.println("Donation edit with operational ARM : " + currentOperationalRoom.toString());
                // Добавляем в документ данные о использованной операционной
                if (document.getOperationalRoom() == null) {
                    document.setOperationalRoom(currentOperationalRoom);

                    // если уже есть хотя-бы одна заполненная Система крови -она остается, новые СК (из операционной) по количеству типов не добавляются

                    if (document.getBloodSystems().isEmpty()) {
                        for (BloodSystemType bloodSystemType : currentOperationalRoom.getBloodTypes()) {
                            // на каждый ТСК (тип системы крови) создаем запись СК (системы крови)
                            final BloodSystem currentBloodSystem = new BloodSystem();
                            currentBloodSystem.setType(bloodSystemType);
                            currentBloodSystem.setCount(1);
                            // в качестве автора СК указывать автора операционной (Skype: Воронина->Упатов 15.01.2015
                            // 14:33)
                            currentBloodSystem.setUserId(currentOperationalRoom.getAuthor().getId());
                            document.getBloodSystems().add(currentBloodSystem);
                            System.out.println("Add blood system : " + currentBloodSystem.toString());
                        }
                    } else {
                        System.out.println("Donation edit with operational ARM: blood systems already exists");
                    }
                } else {
                    System.out.println("Donation edit with operational ARM : OperationalRoom is already exists! id = " +
                            "" + document.getOperationalRoom().getId());
                }
            } else {
                System.out.println("Donation edit with operational ARM : operationalRoom not set!");
            }
        }
        if (isEditState()) {
            // если уже есть хотя-бы один фактический вид донорства - он остается, новые фактические виды донорства не добавляются
            if (isStatusGT1 && (isEqIds || isOperational || isTransfusiologist() || isHeadNurse()) && document.getFactEntries().isEmpty()) {
                // Проставляем фактических видов донорства столько, сколько есть рекомендуемых с теми-же типами и объемами
                System.out.println("Add fact BloodDonationEntries");
                for (BloodDonationEntry currentRecommendedEntry : document.getEntryList()) {
                    final BloodDonationEntry currentFactEntry = new BloodDonationEntry();
                    currentFactEntry.setDonationType(currentRecommendedEntry.getDonationType());
                    currentFactEntry.setDose(currentRecommendedEntry.getDose());
                    document.addToFactEntry(currentFactEntry);
                    System.out.println("Added factEntry: "+ currentFactEntry.toString());
                }
            }
            if (document.getAnalysisCount() == 0) {
                document.setAnalysisCount(20);
            }
        }
        setDocument(document);
        return result;
    }

    @Override
    protected boolean deleteDocument() {
        boolean result = false;
        try {
            sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).delete(getDocument());
            result = true;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Невозможно удалить документ. Попробуйте повторить позже.", ""));
        }
        return result;
    }

    @Override
    protected Integer getDocumentId() {
        return getDocument().getId();
    }

    @Override
    protected FromStringConverter<Integer> getIdConverter() {
        return FromStringConverter.INTEGER_CONVERTER;
    }

    @Override
    protected void initDocument(Integer id) {
        BloodDonationRequest request = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).get(id);
        DonorRejectionDAOImpl dao = sessionManagement.getDAO(DonorRejectionDAOImpl.class, ApplicationHelper.REJECTION_DAO);
        List<DonorRejection> list = dao.findDocumentsByRequestId("d_" + id);

        if (request != null) {
            if (list.size() > 0) {
                request.setRejection(list.get(0));
            }
            if (request.getAppointment() != null && request.getAppointment().getId() > 0) {
                request.setAdditionalResults(sessionManagement.getDAO(ExternalAnalysisResultDAOImpl.class, "externalAnalysisResultDao").getResultsByAppointmentId(request.getAppointment().getId()));
            }
            if (request.getBloodSystems() == null)
                request.setBloodSystems(new ArrayList<BloodSystem>());
        }
        setDocument(request);
        if (getDocument() == null) {
            setState(STATE_NOT_FOUND);
        }
    }

    @Override
    protected void initNewDocument() {
        final BloodDonationRequest document = new BloodDonationRequest();
        document.setStatusId(1);
        Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
        document.setCreated(created);
        document.setTherapist(sessionManagement.getLoggedUser());
        final String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentId");
        if (StringUtils.isNotEmpty(parentId)) {
            document.setDonor(sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).get(Integer.parseInt(parentId)));
        }
        String examinationId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("examinationID");
        if (StringUtils.isNotEmpty(examinationId)) {
            final ExaminationRequest examination = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).get(Integer.parseInt(examinationId));
            if (examination != null) {
                document.setExamination(examination);
                document.setDonor(examination.getDonor());
                document.setNumber(examination.getNumber());
            }
        }

        HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setCreated(created);
        historyEntry.setStartDate(created);
        historyEntry.setOwner(sessionManagement.getLoggedUser());
        historyEntry.setDocType(document.getType());
        historyEntry.setParentId(document.getId());
        historyEntry.setActionId(0);
        historyEntry.setFromStatusId(1);
        historyEntry.setEndDate(created);
        historyEntry.setProcessed(true);
        historyEntry.setCommentary("");

        document.addToHistory(historyEntry);

        document.setBloodSystems(new ArrayList<BloodSystem>());
        setDocument(document);
    }

    @Override
    protected boolean saveDocument() {
        boolean result = false;
        try {
            if (getDocument().getReport() != null && getDocument().getReport().getId() == 0) {
                sessionManagement.getDAO(PheresisDAOImpl.class, ApplicationHelper.PHERESIS_DAO).save(getDocument().getReport());
            }
            if (!(getDocument().getBloodSystems() == null) && !getDocument().getBloodSystems().isEmpty()) {
                BloodSystemDAOImpl systemDAO = sessionManagement.getDAO(BloodSystemDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_DAO);
                systemDAO.save(getDocument().getBloodSystems());
            }
            BloodDonationRequest bloodDonation = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).update(getDocument());
            if (bloodDonation == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            } else {
                if (bloodDonation.getReport() != null) {
                    sessionManagement.getDAO(PheresisDAOImpl.class, ApplicationHelper.PHERESIS_DAO).save(bloodDonation.getReport());
                }
                AnalysisDAOImpl dao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
                for (int i = 0; i < getDocument().getTestList().size(); i++) {
                    dao.save(getDocument().getTestList().get(i));
                }
                for (int i = 0; i < getDocument().getTestImmunoList().size(); i++) {
                    dao.save(getDocument().getTestImmunoList().get(i));
                }
                result = true;
            }
        } catch (Exception e) {
            result = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка при сохранении документа.", ""));
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected boolean saveNewDocument() {
        boolean result = false;
        try {
            BloodDonationRequestDAOImpl dao = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO);
            BloodDonationRequest bloodDonation = dao.save(getDocument());
            if (bloodDonation == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            } else {
                if (bloodDonation.getNumber() == null || bloodDonation.getNumber().equals("")) {
                    bloodDonation.setNumber(StringUtils.leftPad(String.valueOf(getDocument().getId()), 5, '0'));
                }
                bloodDonation = dao.save(bloodDonation);
                setDocument(bloodDonation);
                result = true;
            }
        } catch (Exception e) {
            result = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка при сохранении документа.", ""));
            e.printStackTrace();
        }
        return result;
    }


    public boolean isGranulocytePheresis() {
        try {
            if (getDocument().getFactEntries() != null && getDocument().getFactEntries().size() > 0) {
                for (BloodDonationEntry entry : getDocument().getFactEntries()) {
                    if (entry.getDonationType() != null && entry.getDonationType().getValue() != null) {
                        if (StringUtils.containsIgnoreCase(entry.getDonationType().getValue(), "Гранулоцитаферез")) {
                            return true;
                        }
                    }
                }
            } else {
                if (getDocument().getEntries() != null && getDocument().getEntries().size() > 0) {
                    for (BloodDonationEntry entry : getDocument().getEntries()) {
                        if (entry.getDonationType() != null && entry.getDonationType().getValue() != null) {
                            if (StringUtils.containsIgnoreCase(entry.getDonationType().getValue(), "Гранулоцитаферез")) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTransfusiologist() {
        for (Role currentRole : sessionManagement.getLoggedUser().getRoles()) {
            if (RoleType.ENTERPRISE_ADMINISTRATION.equals(currentRole.getRoleType())) {
                return true;
            } else if (RoleType.THERAPIST.equals(currentRole.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHeadNurse() {
        for (Role currentRole : sessionManagement.getLoggedUser().getRoles()) {
            if (RoleType.ENTERPRISE_ADMINISTRATION.equals(currentRole.getRoleType())) {
                return true;
            } else if (RoleType.HEAD_NURSE.equals(currentRole.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    public boolean isRegistrator() {
        for (Role currentRole : sessionManagement.getLoggedUser().getRoles()) {
            if (RoleType.ENTERPRISE_ADMINISTRATION.equals(currentRole.getRoleType())) {
                return true;
            } else if (RoleType.REGISTRATOR.equals(currentRole.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Доступность действия "Брак по операционной"
     */
    public boolean isComponentRejectionAvailable() {
        if (getDocument().getStatusId() == 2) {
            for (BloodDonationEntry entry : getDocument().getFactEntryList()) {
                if (entry.getDonationType() != null && StringUtils.contains(entry.getDonationType().getValue(), "ферез")) {
                    return false;
                }
            }
            if (getDocument().getFactEntryList() != null && getDocument().getFactEntryList().size() > 0) {
                return true;
            }
        }
        return false;
    }

    public class RejectionDescriptionHolder extends ModalWindowHolderBean {
        private String description;
        private boolean editableState;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isEditableState() {
            return editableState;
        }

        @Override
        protected void doShow() {
            editableState = true;
        }

        @Override
        protected void doHide() {
            description = null;
        }

        public void compose() {
            try {
                if (StringUtils.isEmpty(description)) {
                    description = "Не указана причина брака";
                    editableState = false;
                    return;
                }
                final String fullNameContragent = (String) propertiesEditorBean.getSelectedProperties().getProperty("reports.institution.name");
                final Contragent currentContragent = sessionManagement.getDAO(ContragentDAOHibernate.class,
                        ApplicationHelper.CONTRAGENT_DAO).getByFullName(fullNameContragent);
                if (currentContragent != null) {
                    System.out.println("No one maker founded by:" + fullNameContragent);
                }
                ActionResult result = OperationalHelper.operationalReject(getDocument(), description, currentContragent);
                if (!result.isProcessed()) {
                    description = result.getDescription();
                    editableState = false;
                } else {
                    description = "Брак по операционной зарегистрирован";
                    editableState = false;
                }
            } catch (Exception e) {
                description = "Ошибка при регистрации брака по операционной";
                editableState = false;
                e.printStackTrace();
            }
        }
    }

    public RejectionDescriptionHolder getRejectionDescriptionHolder() {
        return rejectionDescriptionHolder;
    }

    /**
     * Доступность действия "Создать компоненты"
     */
    public boolean isComponentRegistrationAvailable() {
        return getDocument().getStatusId() == 2 && getDocument().getReport() != null;
    }

    /**
     * Создать компоненты
     */
    public void componentRegister() {
        try {
            final String fullNameContragent = (String) propertiesEditorBean.getSelectedProperties().getProperty("reports.institution.name");
            final Contragent currentContragent = sessionManagement.getDAO(ContragentDAOHibernate.class,
                    ApplicationHelper.CONTRAGENT_DAO).getByFullName(fullNameContragent);
            if (currentContragent != null) {
                System.out.println("No one maker founded by:" + fullNameContragent);
            }
            ActionResult result = OperationalHelper.operationalRegisterComponents(getDocument(), currentContragent);
            if (!result.isProcessed()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, result.getDescription(), ""));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Ошибка при регистрации компонентов", ""));
            e.printStackTrace();
        }
    }

    public ProcessorModalBean getProcessorModal() {
        return processorModal;
    }

    public class DonorSelectModalHolder extends ModalWindowHolderBean {

        public DonorListHolderBean getDonorList() {
            return donorList;
        }

        public void setDonorList(DonorListHolderBean donorList_) {
            donorList = donorList_;
        }

        public void select(Donor donor) {
            this.donor = donor;
        }

        public boolean selected(Donor donor) {
            return this.donor != null && this.donor.equals(donor);
        }

        @Override
        protected void doHide() {
            donorList.setFilter("");
            donorList.markNeedRefresh();
            donor = null;
        }

        @Override
        protected void doSave() {
            super.doSave();
            if (donor != null) {
                getDocument().setDonor(donor);
            }
        }

        private Donor donor;
    }

    public DonorSelectModalHolder getDonorSelectModal() {
        return donorSelectModal;
    }

    public BloodSystemSelect getBloodSystemTypeSelectModal() {
        return bloodSystemTypeSelectModal;
    }

    public class BloodSystemSelect extends ModalWindowHolderBean {

        private BloodSystemType selected;

        public BloodSystemType getSelected() {
            return selected;
        }

        public void select(BloodSystemType selected) {
            this.selected = selected;
        }


        public boolean selected(BloodSystemType row) {
            return this.selected != null && this.selected.equals(row);
        }

        @Override
        protected void doSave() {
            BloodSystem system = new BloodSystem();
            system.setType(selected);
            getDocument().getBloodSystems().add(system);
            super.doSave();
        }

        @Override
        protected void doHide() {
            selected = null;
            super.doHide();
        }
    }

    public class ParentExaminationSelect extends ModalWindowHolderBean {

        public AdmittedExaminationListHolderBean getExaminationList() {
            return admittedExaminationList;
        }

        public void select(ExaminationRequest examinationRequest) {
            this.examinationRequest = examinationRequest;
        }

        public boolean selected(ExaminationRequest examinationRequest) {
            return this.examinationRequest != null && this.examinationRequest.equals(examinationRequest);
        }

        @Override
        protected void doHide() {
            admittedExaminationList.setFilterNumberExamination("");
            admittedExaminationList.setFilterDonorFirstName("");
            admittedExaminationList.setFilterDonorLastName("");
            admittedExaminationList.setFilterDonorMiddleName("");
            examinationRequest = null;
        }

        @Override
        protected void doSave() {
            super.doSave();
            if (examinationRequest != null) {
                getDocument().setExamination(examinationRequest);
                getDocument().setDonor(examinationRequest.getDonor());
                getDocument().setNumber(examinationRequest.getNumber());
            }
        }

        @Override
        protected void doShow() {
            super.doShow();
            admittedExaminationList.reset();
        }

        private ExaminationRequest examinationRequest;
    }

    public ParentExaminationSelect getParentExaminationSelect() {
        return parentExaminationSelect;
    }

    public AbstractUserSelectModalBean getRegistratorSelectModal() {
        return registratorSelectModal;
    }

    public AbstractUserSelectModalBean getTransfusiologistSelectModal() {
        return transfusiologistSelectModal;
    }

    public AbstractUserSelectModalBean getStaffNurseSelectModal() {
        return staffNurseSelectModal;
    }

    @Inject
    @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject
    @Named("donorList")
    private transient DonorListHolderBean donorList;
    @Inject
    @Named("admittedExaminationList")
    private transient AdmittedExaminationListHolderBean admittedExaminationList;
    @Inject
    @Named("operationalSession")
    private transient OperationalSessionBean operational;
    @Inject
    @Named("bloodDonationOperationalList")
    private transient BloodDonationOperationalListHolderBean bloodDonationOperational;
    @Inject
    @Named("propertiesRedactorBean")
    private transient PropertiesEditorBean propertiesEditorBean;

    private DonorSelectModalHolder donorSelectModal = new DonorSelectModalHolder();
    private BloodSystemSelect bloodSystemTypeSelectModal = new BloodSystemSelect();
    private ParentExaminationSelect parentExaminationSelect = new ParentExaminationSelect();
    private RejectionDescriptionHolder rejectionDescriptionHolder = new RejectionDescriptionHolder();

    private UserListByRoleTypeHolderBean registratorUserListBean;
    private UserListByRoleTypeHolderBean transfusiologistUserListBean;
    private UserListByAppointmentHolderBean staffUserListBean;

    private AbstractUserSelectModalBean registratorSelectModal = new AbstractUserSelectModalBean() {
        @Override
        public UserListByRoleTypeHolderBean getUserList() {
            if (registratorUserListBean == null) {
                UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO);
                RoleDAOHibernate roleDAO = sessionManagement.getDAO(RoleDAOHibernate.class, ApplicationHelper.ROLE_DAO);
                registratorUserListBean = new UserListByRoleTypeHolderBean(RoleType.REGISTRATOR, userDAO, roleDAO);
            }
            return registratorUserListBean;
        }

        @Override
        protected void doSave() {
            super.doSave();
            getDocument().setRegistrator(getUser());
        }

        @Override
        protected void doHide() {
            super.doHide();
            registratorUserListBean.setFilter("");
            registratorUserListBean.markNeedRefresh();
            setUser(null);
        }
    };
    private AbstractUserSelectModalBean transfusiologistSelectModal = new AbstractUserSelectModalBean() {
        @Override
        public UserListByRoleTypeHolderBean getUserList() {
            if (transfusiologistUserListBean == null) {
                UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO);
                RoleDAOHibernate roleDAO = sessionManagement.getDAO(RoleDAOHibernate.class, ApplicationHelper.ROLE_DAO);
                transfusiologistUserListBean = new UserListByRoleTypeHolderBean(RoleType.THERAPIST, userDAO, roleDAO);
            }
            return transfusiologistUserListBean;
        }

        @Override
        protected void doSave() {
            super.doSave();
            getDocument().setTransfusiologist(getUser());
        }

        @Override
        protected void doHide() {
            super.doHide();
            transfusiologistUserListBean.setFilter("");
            transfusiologistUserListBean.markNeedRefresh();
            setUser(null);
        }
    };
    private AbstractUserSelectModalBean staffNurseSelectModal = new AbstractUserSelectModalBean() {
        @Override
        public UserListByAppointmentHolderBean getUserList() {
            if (staffUserListBean == null) {
                UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO);
                staffUserListBean = new UserListByAppointmentHolderBean("Операционная сестра", userDAO);
            }
            return staffUserListBean;
        }

        @Override
        protected void doSave() {
            super.doSave();
            getDocument().setStaffNurse(getUser());
        }

        @Override
        protected void doHide() {
            super.doHide();
            staffUserListBean.setFilter("");
            staffUserListBean.markNeedRefresh();
            setUser(null);
        }
    };
    private ProcessorModalBean processorModal = new ProcessorModalBean() {
        @Override
        protected void doInit() {
            if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
            setProcessedData(getDocument());
        }

        @Override
        protected void doPostProcess(ActionResult actionResult) {
            BloodDonationRequest request = (BloodDonationRequest) actionResult.getProcessedData();
            if (getSelectedAction().isHistoryAction()) {
                request.addToHistory(getHistoryEntry());
            }
            if (request.getStatusId() == 2) {
                request.setFactDate(new Date());
            }
            setDocument(request);
            BloodDonationHolderBean.this.save();
        }

        @Override
        protected void doProcessException(ActionResult actionResult) {
            if (getSelectedAction() != null) {
                for (EditableProperty property : getSelectedAction().getProperties()) {
                    if (property.getName().equals(EngineHelper.PROP_WF_RESULT_DESCRIPTION) && property.getValue() != null) {
                        setActionResult(property.getValue().toString());
                    }
                }
            }
        }
    };

    private static final long serialVersionUID = 8070559401705301872L;
}