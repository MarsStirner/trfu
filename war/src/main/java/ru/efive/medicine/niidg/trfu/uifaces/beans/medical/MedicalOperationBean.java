package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import org.apache.commons.lang.StringUtils;
import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.medical.*;
import ru.efive.medicine.niidg.trfu.uifaces.beans.AbstractUserSelectModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DictionaryManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.ProcessorModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListByRoleTypeHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListHolderBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.*;
@Named("medicalOperation")
@ConversationScoped
public class MedicalOperationBean extends AbstractDocumentHolderBean<Operation, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).delete(getDocument());
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
		setDocument(sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).get(Operation.class, id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		} 
	}
	
	@Override
	protected void initNewDocument() {
		Operation operation = new Operation();
		operation.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		operation.setCreated(created);
		operation.setAuthor(sessionManagement.getLoggedUser());
		operation.setOperationType(1);
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentId");
		if (parentId != null && !parentId.equals("")) {
			operation.setDonor(sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).get(
					BiomaterialDonor.class, Integer.parseInt(parentId)));
		}
		OperationReport report = new OperationReport();
		report.setAuthor(sessionManagement.getLoggedUser());
		report.setCreated(created);
		report.setDeleted(false);
		report.setHemodynamicsBefore(new Hemodynamics());
		report.setHemodynamicsAfter(new Hemodynamics());
		for (Classifier classifier: dictionaryManagement.getByCategory("Тип расходного материала")) {
			report.addSupply(new Supply(classifier));
		}
		report.setEritrocyteMass(new EritrocyteMass());
		report.setInitialParameters(new OperationParameters());
		report.setChangedParameters(new OperationParameters());
		for (Classifier classifier: dictionaryManagement.getByCategory("Тип лабораторных измерений")) {
			report.addLaboratoryMeasure(new LaboratoryMeasure(classifier));
		}
		report.setLiquidBalance(new LiquidBalance());
		
		operation.setOperationReport(report);
		
		/*HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(operation.getType());
		historyEntry.setParentId(operation.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");
		Set<HistoryEntry> history = new HashSet<HistoryEntry>();
		history.add(historyEntry);
		operation.setHistory(history);*/
		
		setDocument(operation);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			Operation operation = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).update(Operation.class, getDocument());
			if (operation == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(operation);
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
			MedicalOperationDAOImpl dao = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO);
			Operation operation = dao.save(Operation.class, getDocument());
			if (operation == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				operation.setNumber(StringUtils.right("00000" + operation.getId(), 5));
				operation = dao.save(Operation.class, operation);
				setDocument(operation);
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
	
	public void registerOperation() {
		try {
			getDocument().setFactDate(new Date());
			ActionResult result = IntegrationHelper.processMedicalOperation(getDocument());
			if (!result.isProcessed()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, result.getDescription(), ""));
			}
			else {
				Operation operation = getDocument();
				operation.setRegistrationDate(new Date());
				operation.setSentToMIS(true);
				setDocument(operation);
				MedicalOperationBean.this.save();
			}
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка", ""));
			e.printStackTrace();
		}
	}
	
	@Override
    protected String doAfterCreate() {
		medicalOperationList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	medicalOperationList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	medicalOperationList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    public void operationTypeListener(AjaxBehaviorEvent event) {
    	int type = getDocument().getOperationType();
    	System.out.println("Operation type: " + type);
    	if (type == 0 && (getDocument().getRecipient() == null || getDocument().getRecipient().equals(""))) {
    		System.out.println("Change recipient");
    		getDocument().setRecipient(getDocument().getDonor().getDescription());
    	}
    }
	
	public class DonorSelectModalHolder extends ModalWindowHolderBean {
    	
    	public BiomaterialDonorListBean getDonorList() {
            return donorList;
        }

        public void setDonorList(BiomaterialDonorListBean donorList_) {
        	donorList = donorList_;
        }
    	
		public void select(BiomaterialDonor donor) {
			this.donor = donor;
		}
		
		public boolean selected(BiomaterialDonor donor) {
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

		private BiomaterialDonor donor;
	}
    
    public DonorSelectModalHolder getDonorSelectModal() {
        return donorSelectModal;
    }
	
    public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
    public AbstractUserSelectModalBean getTransfusiologistSelectModal() {
    	return transfusiologistSelectModal;
    }
    
    public boolean isSummaryTabSelected() {
		return summaryTabSelected;
	}
	
	public void setSummaryTabSelected(boolean summaryTabSelected) {
		this.summaryTabSelected = summaryTabSelected;
	}
	
	public String getSummaryTabHeader() {
		return "<span><span>Общая информация</span></span>";
	}
	
	public boolean isOperationReportTabSelected() {
		return operationReportTabSelected;
	}

	public void setOperationReportTabSelected(boolean operationReportTabSelected) {
		this.operationReportTabSelected = operationReportTabSelected;
	}
	
	public String getOperationReportTabHeader() {
		return "<span><span>Процедурный лист</span></span>";
	}
    
    
    private boolean summaryTabSelected = true;
	private boolean operationReportTabSelected = false;
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("medicalOperationList")
	MedicalOperationListBean medicalOperationList = new MedicalOperationListBean();
	@Inject @Named("biomaterialDonorList")
    private transient BiomaterialDonorListBean donorList;
	@Inject @Named("userList")
    private transient UserListHolderBean userList;
	@Inject @Named("dictionaryManagement")
    private transient DictionaryManagementBean dictionaryManagement;
	private UserListByRoleTypeHolderBean transfusiologistUserListBean;
	
	private DonorSelectModalHolder donorSelectModal = new DonorSelectModalHolder();
	private AbstractUserSelectModalBean transfusiologistSelectModal = new AbstractUserSelectModalBean() {
		@Override
		public UserListByRoleTypeHolderBean getUserList() {
			if (transfusiologistUserListBean == null) {
				UserDAOHibernate userDAO = sessionManagement.getDAO(UserDAOHibernate.class, USER_DAO);
				RoleDAOHibernate roleDAO = sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO);
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
	
	private ProcessorModalBean processorModal = new ProcessorModalBean() {
		@Override
		protected void doInit() {
			setProcessedData(getDocument());
			if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
		}
		@Override
		protected void doPostProcess(ActionResult actionResult) {
			Operation operation = (Operation) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				operation.addToHistory(getHistoryEntry());
			}
			setDocument(operation);
			MedicalOperationBean.this.save();
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