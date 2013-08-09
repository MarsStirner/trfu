package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.*;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListHolderBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.wf.util.OperationalHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

@Named("bloodDonation")
@ConversationScoped
public class BloodDonationHolderBean extends AbstractDocumentHolderBean<BloodDonationRequest, Integer> {
	
	@Override
	public String save() {
		getDocument().setFactDate(new Date());
		String result = super.save();
		return result;
	}
	
	@Override
	public String edit() {
		String result = super.edit();
		boolean isStatusGT1 = getDocument().getStatusId() > 1 ? true : false;
		boolean isEqIds = false;
		if (getDocument().getTransfusiologist() != null) {
			isEqIds = sessionManagement.getLoggedUser().getId() == getDocument().getTransfusiologist().getId() ? true : false;
		}
		boolean isOperational = sessionManagement.isOperational();
		boolean isSizeZero = getDocument().getFactEntries().size() == 0 ? true : false;
		if (isEditState()) {
			if (isStatusGT1 && (isEqIds || isOperational || isTransfusiologist() || isHeadNurse()) && isSizeZero) {
				getDocument().addFactEntry();
				List<BloodDonationEntry> entryList = getDocument().getEntryList();
				if (entryList != null && !entryList.isEmpty()) {
					getDocument().getFactEntryList().get(0).setDonationType(entryList.get(0).getDonationType());
					getDocument().getFactEntryList().get(0).setDose(entryList.get(0).getDose());
				}
			}
			if (getDocument().getAnalysisCount() == 0) {
				getDocument().setAnalysisCount(20);
			}
		}
		return result;
	}
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).delete(getDocument());
			result = true;
		}
		catch (Exception e) {
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
		//request.setTestList(examinationEntryList.getEntriesByExaminationRequest(id));
		DonorRejectionDAOImpl dao = sessionManagement.getDAO(DonorRejectionDAOImpl.class, ApplicationHelper.REJECTION_DAO);
		List<DonorRejection> list = dao.findDocumentsByRequestId("d_" + id);

		if (request != null) {
			if (list.size() > 0) {
				request.setRejection(list.get(0));
			}
			if (request.getAppointment() != null && request.getAppointment().getId() > 0) {
				request.setAdditionalResults(sessionManagement.getDAO(ExternalAnalysisResultDAOImpl.class, "externalAnalysisResultDao").getResultsByAppointmentId(request.getAppointment().getId()));
			}
			if (operational.getCurrentOperationalSetup() != null) {
				if(operational.getCurrentOperationalSetup().getName()!=null&&!operational.getCurrentOperationalSetup().getName().isEmpty()){
					request.setOperational(operational.getCurrentOperationalSetup().getName());
				}
				if(operational.getCurrentOperationalSetup().getCrew()!=null&&operational.getCurrentOperationalSetup().getCrew().getStaff()!=null){
					request.setOperationalCrew(operational.getCurrentOperationalSetup().getCrew());
				}
			}

			if(request.getBloodSystems()==null)
				request.setBloodSystems(new ArrayList<BloodSystem>());
		}	
		
		setDocument(request);
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		} 
	}
	
	@Override
	protected void initNewDocument() {
		BloodDonationRequest bloodDonation = new BloodDonationRequest();
		bloodDonation.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		bloodDonation.setCreated(created);
		bloodDonation.setTherapist(sessionManagement.getLoggedUser());
		/*bloodDonation.setBloodSystem(operationalBean.getCurrentSystem() == null || operationalBean.getCurrentSystem().getId() == 0? null: 
			operationalBean.getCurrentSystem());
		bloodDonation.setOperationalCrew(operationalBean.getCurrentCrew() == null || operationalBean.getCurrentCrew().getId() == 0? null: 
			operationalBean.getCurrentCrew());*/
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentId");
		if (parentId != null && !parentId.equals("")) {
			bloodDonation.setDonor(sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).get(Integer.parseInt(parentId)));
		}
		String examinationId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("examinationID");
		if (examinationId != null && !examinationId.equals("")) {
			ExaminationRequest examination = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).get(Integer.parseInt(examinationId));
			if (examination != null) {
				bloodDonation.setExamination(examination);
				bloodDonation.setDonor(examination.getDonor());
				bloodDonation.setNumber(examination.getNumber());
			}
		}
		
		HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(bloodDonation.getType());
		historyEntry.setParentId(bloodDonation.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");
		Set<HistoryEntry> history = new HashSet<HistoryEntry>();
		history.add(historyEntry);
		bloodDonation.setHistory(history);

        bloodDonation.setBloodSystems(new ArrayList<BloodSystem>());
		setDocument(bloodDonation);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			if (getDocument().getReport() != null && getDocument().getReport().getId() == 0) {
				sessionManagement.getDAO(PheresisDAOImpl.class, ApplicationHelper.PHERESIS_DAO).save(getDocument().getReport());
			}
            if(!(getDocument().getBloodSystems()==null) && !getDocument().getBloodSystems().isEmpty()){
                BloodSystemDAOImpl systemDAO = sessionManagement.getDAO(BloodSystemDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_DAO);
                systemDAO.save(getDocument().getBloodSystems());
            }
			BloodDonationRequest bloodDonation = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).update(getDocument());
			if (bloodDonation == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				if (bloodDonation.getReport() != null) {
					sessionManagement.getDAO(PheresisDAOImpl.class, ApplicationHelper.PHERESIS_DAO).save(bloodDonation.getReport());
				}
				AnalysisDAOImpl dao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
				for (int i = 0; i < getDocument().getTestList().size();i++) {
					dao.save(getDocument().getTestList().get(i));
				}
				for (int i = 0; i < getDocument().getTestImmunoList().size();i++) {
					dao.save(getDocument().getTestImmunoList().get(i));
				}
				result = true;
			}
		}
		catch (Exception e) {
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
			BloodDonationRequest bloodDonation = (BloodDonationRequest) dao.save(getDocument());
			if (bloodDonation == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				if (bloodDonation.getNumber() == null || bloodDonation.getNumber().equals("")) {
					bloodDonation.setNumber(StringUtils.right("00000" + getDocument().getId(), 5));
				}
				bloodDonation = dao.save(bloodDonation);
				setDocument(bloodDonation);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}
	

	
	public boolean isGranulocytePheresis() {
		boolean result = false;
		try {
			if (getDocument().getFactEntries() != null && getDocument().getFactEntries().size() > 0) {
				for (BloodDonationEntry entry: getDocument().getFactEntries()) {
					if (entry.getDonationType() != null && entry.getDonationType().getValue() != null) {
						if (StringUtils.containsIgnoreCase(entry.getDonationType().getValue(), "Гранулоцитаферез")) {
							return true;
						}
					}
				}
			}
			else {
				if (getDocument().getEntries() != null && getDocument().getEntries().size() > 0) {
					for (BloodDonationEntry entry: getDocument().getEntries()) {
						if (entry.getDonationType() != null && entry.getDonationType().getValue() != null) {
							if (StringUtils.containsIgnoreCase(entry.getDonationType().getValue(), "Гранулоцитаферез")) {
								return true;
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean isTransfusiologist() {
		boolean result = false;
		List<Role> roleList = new ArrayList<Role>();
		roleList.addAll(sessionManagement.getLoggedUser().getRoles());
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
				result = true;
			}
			else if (roleList.get(i).getRoleType().equals(RoleType.THERAPIST)) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean isHeadNurse() {
		boolean result = false;
		List<Role> roleList = new ArrayList<Role>();
		roleList.addAll(sessionManagement.getLoggedUser().getRoles());
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
				result = true;
			}
			else if (roleList.get(i).getRoleType().equals(RoleType.HEAD_NURSE)) {
				result = true;
			}
		}
		return result;
	}
	
	public boolean isRegistrator() {
		boolean result = false;
		List<Role> roleList = new ArrayList<Role>();
		roleList.addAll(sessionManagement.getLoggedUser().getRoles());
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
				result = true;
			}
			else if (roleList.get(i).getRoleType().equals(RoleType.REGISTRATOR)) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Доступность действия "Брак по операционной"
	 */
	public boolean isComponentRejectionAvailable() {
		boolean result = false;
		if (getDocument().getStatusId() == 2) {
			for (BloodDonationEntry entry: getDocument().getFactEntryList()) {
				if (entry.getDonationType() != null && StringUtils.contains(entry.getDonationType().getValue(), "ферез")) {
					return result;
				}
			}
			if (getDocument().getFactEntryList() != null && getDocument().getFactEntryList().size() > 0) {
				result = true;
			}
		}
		return result;
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
				ActionResult result = OperationalHelper.operationalReject(getDocument(), description);
				if (!result.isProcessed()) {
					description = result.getDescription();
					editableState = false;
				}
				else {
					description = "Брак по операционной зарегистрирован";
					editableState = false;
				}
			}
			catch (Exception e) {
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
		boolean result = false;
		if (getDocument().getStatusId() == 2 && getDocument().getReport() != null) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Создать компоненты
	 */
	public void componentRegister() {
		try {
			ActionResult result = OperationalHelper.operationalRegisterComponents(getDocument());
			if (!result.isProcessed()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, result.getDescription(), ""));
			}
		}
		catch (Exception e) {
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
        public List<BloodSystemType> getAllTypes(){
            return operational.getCurrentSystems();
        }

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
    		admittedExaminationList.setFilter("");
    		admittedExaminationList.markNeedRefresh();
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

		private ExaminationRequest examinationRequest;
	}
    
    public ParentExaminationSelect getParentExaminationSelect() {
        return parentExaminationSelect;
    }
    
    
    public UserSelectModalBean getRegistratorSelectModal() {
    	return registratorSelectModal;
    }
    public UserSelectModalBean getTransfusiologistSelectModal() {
    	return transfusiologistSelectModal;
    }
    public UserSelectModalBean getStaffNurseSelectModal() {
    	return staffNurseSelectModal;
    }
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("donorList")
    private transient DonorListHolderBean donorList;
	@Inject @Named("userList")
    private transient UserListHolderBean userList;
	@Inject @Named("admittedExaminationList")
    private transient AdmittedExaminationListHolderBean admittedExaminationList;
    @Inject @Named("operationalSession")
    private transient OperationalSessionBean operational;

	private DonorSelectModalHolder donorSelectModal = new DonorSelectModalHolder();
    private BloodSystemSelect bloodSystemTypeSelectModal = new BloodSystemSelect();
	private ParentExaminationSelect parentExaminationSelect = new ParentExaminationSelect();
	private RejectionDescriptionHolder rejectionDescriptionHolder = new RejectionDescriptionHolder();

	private UserSelectModalBean registratorSelectModal = new UserSelectModalBean() {
		public UserListHolderBean getUserList() {
			return userList;
		}
		@Override
		protected void doSave() {
			super.doSave();
			getDocument().setRegistrator(getUser());
		}
		@Override
		protected void doHide() {
			super.doHide();
			userList.setFilter("");
			userList.markNeedRefresh();
			setUser(null);
		}
	};
	private UserSelectModalBean transfusiologistSelectModal = new UserSelectModalBean() {
		@Override
		public UserListHolderBean getUserList() {
			List<User> therapistUserList = getTherapistUserList(userList.getDocuments());
			userList.getDocuments().clear();
			userList.getDocuments().addAll(therapistUserList);
			return userList;
		}
		@Override
		protected void doSave() {
			super.doSave();
			getDocument().setTransfusiologist(getUser());
		}
		@Override
		protected void doHide() {
			super.doHide();
			userList.setFilter("");
			userList.markNeedRefresh();
			setUser(null);
		}
		
		private List<User> getTherapistUserList(List<User> userListDoc) {
			List<User> therapistUserList = new ArrayList<User>();
			for (User user : userListDoc) {
				if(findRoleTherapist(user.getRoleList())) {
					therapistUserList.add(user);
				}
			}
			return therapistUserList;
		}
		
		private boolean findRoleTherapist(List<Role> roleList) {
			boolean result = false;
			for (Role role : roleList) {
				if (RoleType.THERAPIST.equals(role.getRoleType())) {
					result = true;
					break;					
				}
			}
			return result;
		}
	};
	private UserSelectModalBean staffNurseSelectModal = new UserSelectModalBean() {
		public UserListHolderBean getUserList() {
			return userList;
		}
		@Override
		protected void doSave() {
			super.doSave();
			getDocument().setStaffNurse(getUser());
		}
		@Override
		protected void doHide() {
			super.doHide();
			userList.setFilter("");
			userList.markNeedRefresh();
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
				Set<HistoryEntry> history = request.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(getHistoryEntry());
				request.setHistory(history);
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
				for (EditableProperty property: getSelectedAction().getProperties()) {
					if (property.getName().equals(EngineHelper.PROP_WF_RESULT_DESCRIPTION) && property.getValue() != null) {
						setActionResult(property.getValue().toString());
					}
				}
			}
		}
	};
	private static final long serialVersionUID = 8070559401705301872L;
}