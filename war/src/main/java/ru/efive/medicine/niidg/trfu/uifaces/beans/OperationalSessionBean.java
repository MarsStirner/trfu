package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.BloodSystemDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.OperationalCrewDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;
import ru.efive.medicine.niidg.trfu.data.entity.OperationalCrew;
import ru.efive.medicine.niidg.trfu.dictionary.OperationalSetup;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@Named("operationalSession")
@SessionScoped
public class OperationalSessionBean implements java.io.Serializable {
    private OperationalSetup currentOperationalSetup;
    private boolean editState = true;
    @Inject @Named("sessionManagement")
    SessionManagementBean sessionManagement;
    @Inject @Named("operational")
    OperationalBean operational;
    private static final long serialVersionUID = 1L;


	public OperationalSetup getCurrentOperationalSetup() {
		return currentOperationalSetup;
	}
	
	public List<BloodSystemType> getCurrentSystems() {
		if (currentOperationalSetup == null || currentOperationalSetup.getSystemTypes().isEmpty()) {
			initDocument();
		}
		return currentOperationalSetup.getSystemTypes();
	}
	
	public OperationalCrew getCurrentCrew() {
		if (currentOperationalSetup == null || currentOperationalSetup.getCrew() == null) {
			initDocument();
		}
		return currentOperationalSetup.getCrew();
	}
	
	public void initDocument() {
		currentOperationalSetup = new OperationalSetup();
		initOperationalCrew();

		currentOperationalSetup.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
		currentOperationalSetup.setUnsaved(true);
		editState = true;
	}

    private void  initOperationalCrew(){
        OperationalCrew crew = new OperationalCrew();
        crew.setAuthor(sessionManagement.getLoggedUser());
        crew.setDeleted(false);
        crew.setRegistered(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
        currentOperationalSetup.setCrew(crew);
    }
	
	public boolean saveDocument() {
		boolean result = false;
		try {
			OperationalCrew crew = sessionManagement.getDAO(OperationalCrewDAOImpl.class, ApplicationHelper.OPERATIONAL_CREW_DAO).save(currentOperationalSetup.getCrew());
			if (crew == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить. Попробуйте повторить позже.", ""));
			}
			else {
				if (currentOperationalSetup.isUnsaved()) {
					currentOperationalSetup.setUnsaved(false);
					operational.addOperationalSetup(currentOperationalSetup);
				}
				
				editState = false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка.", ""));
		}
		return result;
	}
	
	public boolean selected(OperationalSetup operationalSetup) {
		return currentOperationalSetup != null && currentOperationalSetup.equals(operationalSetup);
	}
	
	public void select(OperationalSetup operationalSetup) {
		currentOperationalSetup = operationalSetup;
	}
	
	public boolean isExpired() {
		boolean result = true;
		try {
			if (currentOperationalSetup != null&& currentOperationalSetup.getCreated() != null) {
				Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				calendar.setTime(currentOperationalSetup.getCreated());
				Calendar currentCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
				if (currentCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
					result = false;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean isEditState() {
		return editState;
	}
	
	public void edit() {
		editState = true;
	}

    public boolean isReady(){
        return  currentOperationalSetup.getName()!= null &&
                !currentOperationalSetup.getSystemTypes().isEmpty() &&
                !currentOperationalSetup.getName().isEmpty() &&
                currentOperationalSetup.getCrew().getStaffList()!=null &&
                !currentOperationalSetup.getCrew().getStaffList().isEmpty();
    }
}