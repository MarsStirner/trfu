package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Anamnesis;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListHolderBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

@Named("examination")
@ConversationScoped
public class ExaminationHolderBean extends AbstractDocumentHolderBean<ExaminationRequest, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).delete(getDocument());
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
		ExaminationRequest examination = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).get(id);
		if (examination == null) {
			setState(STATE_NOT_FOUND);
		}
		examination.setExaminationEntryList(examinationEntryList.getEntriesByExaminationRequest(id));
		DonorRejectionDAOImpl dao = sessionManagement.getDAO(DonorRejectionDAOImpl.class, ApplicationHelper.REJECTION_DAO);
		List<DonorRejection> list = dao.findDocumentsByRequestId("e_" + id);
		if (list.size() > 0) {
			examination.setRejection(list.get(0));
		}
		if (examination.getAppointment() != null && examination.getAppointment().getId() > 0) {
			examination.setAdditionalResults(sessionManagement.getDAO(ExternalAnalysisResultDAOImpl.class, "externalAnalysisResultDao").getResultsByAppointmentId(examination.getAppointment().getId()));
		}
		setDocument(examination);
	}
	
	@Override
	protected void initNewDocument() {
		ExaminationRequest examination = new ExaminationRequest();
		examination.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		examination.setCreated(created);
		examination.setRegistrator(sessionManagement.getLoggedUser());
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentId");
		if (parentId != null && !parentId.equals("")) {
			examination.setDonor(sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).get(Integer.parseInt(parentId)));
			examination.setExaminationType(examination.getDonor().getCategory());
		}
		
		HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(examination.getType());
		historyEntry.setParentId(examination.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");
		Set<HistoryEntry> history = new HashSet<HistoryEntry>();
		history.add(historyEntry);
		examination.setHistory(history);
		
		setDocument(examination);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			ExaminationRequest examination = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).save(getDocument());
			if (examination == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				if (getDocument().getAnamnesis() != null) {
					sessionManagement.getDAO(AnamnesisDAOImpl.class, ApplicationHelper.ANAMNESIS_DAO).save(getDocument().getAnamnesis());
				}
				AnalysisDAOImpl dao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
				for (int i = 0; i < getDocument().getTestList().size();i++) {
					dao.save(getDocument().getTestList().get(i));
				}
				ExaminationEntryDAOImpl edao = sessionManagement.getDAO(ExaminationEntryDAOImpl.class, ApplicationHelper.EXAMINATION_ENTRY_DAO);
				for (int i = 0; i < getDocument().getExaminationEntryList().size();i++) {
					if (getDocument().getExaminationEntryList().get(i).getId() > 0) {
						edao.save(getDocument().getExaminationEntryList().get(i));
					}
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
			ExaminationRequestDAOImpl dao = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO);
			ExaminationRequest examination = dao.save(getDocument());
			if (examination == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				//if (examination.getStatusId() != 9) {
					examination.setNumber(StringUtils.right("00000" + getDocument().getId(), 5));
					examination = dao.save(examination);
				//}
				ExaminationEntryDAOImpl edao = sessionManagement.getDAO(ExaminationEntryDAOImpl.class, ApplicationHelper.EXAMINATION_ENTRY_DAO);
				for (int i = 0; i < getDocument().getExaminationEntryList().size();i++) {
					if (getDocument().getExaminationEntryList().get(i).getId() > 0) {
						edao.save(getDocument().getExaminationEntryList().get(i));
					}
				}
				setDocument(examination);
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
	
	public boolean isAnamnesisEditable() {
		boolean result = false;
		int statusId = getDocument().getStatusId();
		int loggetUserId = sessionManagement.getLoggedUser().getId();
		if (statusId != 1) {
			if (getDocument().getTherapist() != null && loggetUserId == getDocument().getTherapist().getId()) {
				result = true;
			}
			else if (statusId == 2 || statusId == 4 || statusId == 5) {
				result = true;
			}
		}
		return result;
	}
	
	public void composeDonation() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("blood_donation.xhtml?docAction=create&examinationID=" + getDocumentId());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
			return this.donor == null? false: this.donor.equals(donor);
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
	
    public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
    public UserSelectModalBean getTherapistSelectModal() {
    	return therapistSelectModal;
    }
    public UserSelectModalBean getStaffNurseSelectModal() {
    	return staffNurseSelectModal;
    }
	
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("donorList")
    private transient DonorListHolderBean donorList;
	@Inject @Named("userList")
    private transient UserListHolderBean userList;
	@Inject @Named("examinationEntryList")
    private transient ExaminationEntryListHolderBean examinationEntryList;
	
	private DonorSelectModalHolder donorSelectModal = new DonorSelectModalHolder();
	private UserSelectModalBean therapistSelectModal = new UserSelectModalBean() {
		public UserListHolderBean getUserList() {
			return userList;
		}
		@Override
		protected void doSave() {
			super.doSave();
			getDocument().setTherapist(getUser());
		}
		@Override
		protected void doHide() {
			super.doHide();
			userList.setFilter("");
			userList.markNeedRefresh();
			setUser(null);
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
	
	public ClassifierSelectModalBean getHereditySelectModal() {
    	return hereditySelectModal;
    }
	public ClassifierSelectModalBean getSicknessesSelectModal() {
    	return sicknessesSelectModal;
    }
	
	private ClassifierSelectModalBean hereditySelectModal = new ClassifierSelectModalBean() {
		@Override
		protected void doShow() {
			super.doShow();
			setClassifierList(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByCategory(Classifier.class, "Наследственность", false));
		}
		@Override
		protected void doSave() {
			super.doSave();
			Anamnesis anamnesis = getDocument().getAnamnesis();
			anamnesis.setHeredity(getClassifier().getValue());
			getDocument().setAnamnesis(anamnesis);
		}
		@Override
		protected void doHide() {
			super.doHide();
			setClassifier(null);
		}
	};
	private ClassifierSelectModalBean sicknessesSelectModal = new ClassifierSelectModalBean() {
		@Override
		protected void doShow() {
			super.doShow();
			setClassifierList(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByCategory(Classifier.class, "Перенесенные заболевания", false));
		}
		@Override
		protected void doSave() {
			super.doSave();
			Anamnesis anamnesis = getDocument().getAnamnesis();
			anamnesis.setSicknesses(getClassifier().getValue());
			getDocument().setAnamnesis(anamnesis);
		}
		@Override
		protected void doHide() {
			super.doHide();
			setClassifier(null);
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
			ExaminationRequest request = (ExaminationRequest) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				Set<HistoryEntry> history = request.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(getHistoryEntry());
				request.setHistory(history);
			}
			setDocument(request);
			ExaminationHolderBean.this.save();
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