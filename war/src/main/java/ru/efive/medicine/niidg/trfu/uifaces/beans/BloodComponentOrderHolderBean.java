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
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentMatchCriteria;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

@Named("bloodComponentOrder")
@ConversationScoped
public class BloodComponentOrderHolderBean extends AbstractDocumentHolderBean<BloodComponentOrderRequest, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO).delete(getDocument());
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
		setDocument((BloodComponentOrderRequest)
				sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO).get(id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		BloodComponentOrderRequest bloodComponentOrder = new BloodComponentOrderRequest();
		bloodComponentOrder.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		bloodComponentOrder.setCreated(created);
		bloodComponentOrder.setStaffNurse(sessionManagement.getLoggedUser());
		
		HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(bloodComponentOrder.getType());
		historyEntry.setParentId(bloodComponentOrder.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");
		Set<HistoryEntry> history = new HashSet<HistoryEntry>();
		history.add(historyEntry);
		bloodComponentOrder.setHistory(history);
		
		setDocument(bloodComponentOrder);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			BloodComponentOrderRequest bloodComponentOrder = (BloodComponentOrderRequest)
			sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO).update(getDocument());
			if (bloodComponentOrder == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(bloodComponentOrder);
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
			BloodComponentOrderRequestDAOImpl dao = sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO);
			BloodComponentOrderRequest bloodComponentOrder = (BloodComponentOrderRequest) dao.save(getDocument());
			if (bloodComponentOrder == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				bloodComponentOrder.setNumber(StringUtils.right("00000" + bloodComponentOrder.getId(), 5));
				bloodComponentOrder = dao.save(bloodComponentOrder);
				setDocument(bloodComponentOrder);
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
	
	public class BloodComponentListSelectModalBean extends ModalWindowHolderBean {

		public BloodComponentListModalHolderBean getBloodComponentList() {
			return bloodComponentListModal;
		}

		public List<BloodComponent> getComponents() {
			return components;
		}

		public void setComponents(List<BloodComponent> components) {
			if (components == null) {
				this.components = new ArrayList<BloodComponent>();
			}
			else {
				this.components = components;
			}
		}

		public void select(BloodComponent component) {
			components.add(component);		
		}

		public void unselect(BloodComponent component) {
			components.remove(component);
		}

		public boolean selected(BloodComponent component) {
			return components.contains(component);
		}

		@Override
		protected void doSave() {
			super.doSave();
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			for (BloodComponent component: components) {
				component.setOrderId(getDocumentId());
				dao.save(component);
			}
		}

		@Override
		protected void doShow() {
			super.doShow();
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			components = dao.findComponentsByOrder(getDocumentId());
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			if (bloodComponentListModal != null) {
				bloodComponentListModal.setFilter("");
				bloodComponentListModal.markNeedRefresh();
			}
		}
		
		private List<BloodComponent> components = new ArrayList<BloodComponent>();

		private static final long serialVersionUID = -9107594037615723746L;
	}
	
	public class BloodComponentMatchModal extends ModalWindowHolderBean {
		
		public void setAvailableComponents(List<BloodComponent> availableComponents) {
			this.availableComponents = availableComponents;
		}
		
		public List<BloodComponent> getAvailableComponents() {
			return availableComponents;
		}
		
		public List<BloodComponent> getComponents() {
			return components;
		}
		
		public void setComponents(List<BloodComponent> components) {
			if (components == null) {
				this.components = new ArrayList<BloodComponent>();
			}
			else {
				this.components = components;
			}
		}
		
		public List<BloodComponentMatchCriteria> getCriteriaList() {
			return criteriaList;
		}
		
		public void setCriteriaList(List<BloodComponentMatchCriteria> criteriaList) {
			this.criteriaList = criteriaList;
		}
		
		public BloodGroup getBloodGroup() {
			return bloodGroup;
		}

		public void setBloodGroup(BloodGroup bloodGroup) {
			this.bloodGroup = bloodGroup;
		}

		public boolean isSearchBloodGroup() {
			return searchBloodGroup;
		}

		public void setSearchBloodGroup(boolean searchBloodGroup) {
			this.searchBloodGroup = searchBloodGroup;
		}

		public Classifier getRhesusFactor() {
			return rhesusFactor;
		}

		public void setRhesusFactor(Classifier rhesusFactor) {
			this.rhesusFactor = rhesusFactor;
		}

		public boolean isSearchRhesus() {
			return searchRhesus;
		}

		public void setSearchRhesus(boolean searchRhesus) {
			this.searchRhesus = searchRhesus;
		}

		public void search() {
			List<Analysis> phenotypeList = new ArrayList<Analysis>();
			for (BloodComponentMatchCriteria criteria: criteriaList) {
				if (criteria.isNecessary()) phenotypeList.add(criteria.getPhenotype());
			}
			availableComponents = bloodComponentList.getDocumentsByPhenotypes(phenotypeList, searchBloodGroup, bloodGroup.getValue(), 
					searchRhesus, rhesusFactor.getValue());
		}
		
		public void select(BloodComponent component) {
			components.add(component);		
		}
		
		public void unselect(BloodComponent component) {
			components.remove(component);
		}
		
		public boolean selected(BloodComponent component) {
			return components.contains(component);
		}

		@Override
		protected void doSave() {
			super.doSave();
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			for (BloodComponent component: components) {
				component.setOrderId(getDocumentId());
				dao.save(component);
			}
		}

		@Override
		protected void doShow() {
			super.doShow();
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			components = dao.findComponentsByOrder(getDocumentId());
			
			List<AnalysisType> types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Иммуносерология", false);
			criteriaList = new ArrayList<BloodComponentMatchCriteria>();
			
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis.setValue("-");
				BloodComponentMatchCriteria criteria = new BloodComponentMatchCriteria();
				criteria.setPhenotype(analysis);
				criteria.setNecessary(true);
				criteriaList.add(criteria);
			}
			searchBloodGroup = true;
			searchRhesus = true;
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			availableComponents = null;
			components = null;
			criteriaList = null;
		}
		
		
		private List<BloodComponent> availableComponents = new ArrayList<BloodComponent>();
		private List<BloodComponent> components = new ArrayList<BloodComponent>();
		private List<BloodComponentMatchCriteria> criteriaList = new ArrayList<BloodComponentMatchCriteria>();
		private BloodGroup bloodGroup;
		private boolean searchBloodGroup;
		private Classifier rhesusFactor;
		private boolean searchRhesus;
		
		
		private static final long serialVersionUID = -9107594037615723746L;
	}
    
    public UserSelectModalBean getAttendingDoctorSelectModal() {
    	return attendingDoctorSelectModal;
    }
    
    public BloodComponentListSelectModalBean getComponentSelectModal() {
    	return componentSelectModal;
    }
	
    public BloodComponentMatchModal getComponentMatchModal() {
    	return componentMatchModal;
    }
    
	
    @Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject @Named("bloodComponentListModal")
    private transient BloodComponentListModalHolderBean bloodComponentListModal;
    @Inject @Named("bloodComponentList")
    private transient BloodComponentListHolderBean bloodComponentList;
    
    public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
    private ProcessorModalBean processorModal = new ProcessorModalBean() {
		@Override
		protected void doInit() {
			if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
			setProcessedData(getDocument());
		}
		@Override
		protected void doPostProcess(ActionResult actionResult) {
			BloodComponentOrderRequest request = (BloodComponentOrderRequest) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				Set<HistoryEntry> history = request.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(getHistoryEntry());
				request.setHistory(history);
			}
			//при нажатии на Компонент выдан вычислить общий объем выданных КК
			if (request.getStatusId() == 3) {
				int sumVolume = 0;
				List<BloodComponent> bloodComponents = bloodComponentList.getDocumentsByOrder(request.getId());
				for(BloodComponent bloodComponent: bloodComponents) {
					sumVolume += bloodComponent.getVolume();
				}
				request.setVolume(sumVolume);
			}
			setDocument(request);
			BloodComponentOrderHolderBean.this.save();
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
	
    private BloodComponentListSelectModalBean componentSelectModal = new BloodComponentListSelectModalBean();
    private BloodComponentMatchModal componentMatchModal = new BloodComponentMatchModal();
	private UserSelectModalBean attendingDoctorSelectModal = new UserSelectModalBean() {
		@Override
		protected void doSave() {
			super.doSave();
			//getDocument().setAttendingDoctor(getUser());
		}
		@Override
		protected void doHide() {
			super.doHide();
			getUserList().setFilter("");
			getUserList().markNeedRefresh();
			setUser(null);
		}
	};
	
	private static final long serialVersionUID = 8070559401705301872L;
}