package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import org.apache.commons.lang.StringUtils;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialAdditionalSubstance;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Processing;
import ru.efive.medicine.niidg.trfu.uifaces.beans.ProcessorModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.ReportsManagmentBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.UserSelectModalBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
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
import java.math.BigDecimal;
import java.util.*;

@Named("biomaterial")
@ConversationScoped
public class BiomaterialBean extends AbstractDocumentHolderBean<Biomaterial, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).delete(getDocument());
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
		setDocument(sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).get(Biomaterial.class, id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
		else {
			setReceivedDate(getDocument().getReceived());
			setReceivedTime(getDocument().getReceived());
		}
	}
	
	@Override
	protected void initNewDocument() {
		Biomaterial biomaterial = new Biomaterial();
		biomaterial.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		biomaterial.setCreated(created);
		biomaterial.setAuthor(sessionManagement.getLoggedUser());
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parentId");
		if (parentId != null && !parentId.equals("")) {
			biomaterial.setOperation(sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).get(
					Operation.class, Integer.parseInt(parentId)));
		}
		
		HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(biomaterial.getType());
		historyEntry.setParentId(biomaterial.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");

		biomaterial.addToHistory(historyEntry);

        biomaterial.setInitialVolume(biomaterial.getOperation().getVolume());
		biomaterial.setVolume(biomaterial.getOperation().getVolume());
		
		setDocument(biomaterial);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			Biomaterial biomaterial = getDocument();
			Calendar received = Calendar.getInstance(ApplicationHelper.getLocale());
			if (receivedDate != null) {
				Calendar tmp = Calendar.getInstance(ApplicationHelper.getLocale());
				tmp.setTime(receivedDate);
				received.set(Calendar.YEAR, tmp.get(Calendar.YEAR));
				received.set(Calendar.MONTH, tmp.get(Calendar.MONTH));
				received.set(Calendar.DATE, tmp.get(Calendar.DATE));
				
				if (receivedTime != null) {
					tmp = Calendar.getInstance(ApplicationHelper.getLocale());
					tmp.setTime(receivedTime);
					received.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
					received.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
				}
				else {
					received.set(Calendar.HOUR_OF_DAY, 0);
					received.set(Calendar.MINUTE, 0);
				}
			}
			else {
				received = null;
			}
			biomaterial.setReceived(received == null? null: received.getTime());
			biomaterial = sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).update(Biomaterial.class, biomaterial);
			if (biomaterial == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(biomaterial);
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
			MedicalOperationDAOImpl dao = sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO);
			Biomaterial biomaterial = getDocument();
			if (biomaterial.getOperation() != null && biomaterial.getNumber() == null) {
				int count = new Long(dao.countBiomaterialsByOperation(biomaterial.getOperation(), "", false)).intValue() + 1;
				biomaterial.setNumber(StringUtils.right("00" + count, 3));
			}
			Calendar received = Calendar.getInstance(ApplicationHelper.getLocale());
			if (receivedDate != null) {
				Calendar tmp = Calendar.getInstance(ApplicationHelper.getLocale());
				tmp.setTime(receivedDate);
				received.set(Calendar.YEAR, tmp.get(Calendar.YEAR));
				received.set(Calendar.MONTH, tmp.get(Calendar.MONTH));
				received.set(Calendar.DATE, tmp.get(Calendar.DATE));
				
				if (receivedTime != null) {
					tmp.setTime(receivedTime);
					received.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
					received.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
				}
				else {
					received.set(Calendar.HOUR_OF_DAY, 0);
					received.set(Calendar.MINUTE, 0);
				}
			}
			else {
				received = null;
			}
			biomaterial.setReceived(received == null? null: received.getTime());
			biomaterial = dao.save(Biomaterial.class, getDocument());
			if (biomaterial == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(biomaterial);
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
    protected String doAfterCreate() {
		biomaterialList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	biomaterialList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	biomaterialList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    public void previewBigLabel() {
    	try {
    		Map<String,String> requestProperties = new HashMap<>();
    		requestProperties.put("reportName", "BiomaterialLabel.jrxml");
    		requestProperties.put("docId", Integer.toString(getDocument().getId()));
    		reportsManagement.sqlPrintReportByRequestParams(requestProperties);
    	}
    	catch (Exception e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при формировании этикетки биоматериала", ""));
    		e.printStackTrace();
    	}
    }
    
    public void addProcessing() {
    	Processing processing = new Processing();
    	processing.setAuthor(sessionManagement.getLoggedUser());
    	processing.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
    	processing.setDeleted(false);
    	processing.setProcessingDate(processing.getCreated());
    	processing.setTransfusiologist(processing.getAuthor());
    	getDocument().addProcessing(processing);
    }
    
    public void composeProcessingExecutorSelectModal(Processing processing) {
    	processingExecutorSelectModal.setProcessing(processing);
    	processingExecutorSelectModal.show();
	}
    
    public void processingListener(AjaxBehaviorEvent event) {
		
    }
    
    public void recalcVolume(AjaxBehaviorEvent event) {
    	double volume = 0;
    	if (getDocument().getInitialVolume() > 0) {
    		volume = getDocument().getInitialVolume();
    	}
    	if (getDocument().getAdditionalSubstances() != null && getDocument().getAdditionalSubstances().size() > 0) {
    		for (BiomaterialAdditionalSubstance substance: getDocument().getAdditionalSubstances()) {
    			if (substance != null && substance.getVolume() > 0) {
    				volume += substance.getVolume();
    			}
    		}
    	}
    	
    	if (volume > 0) {
    		getDocument().setVolume(volume);
    	}
    }
	
    public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
    public SplitModalHolder getSplitModal() {
    	return splitModal;
    }
    
    public ProcessingExecutorSelectModal getProcessingExecutorSelect() {
    	return processingExecutorSelectModal;
    }
    
    public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public class SplitModalHolder extends ModalWindowHolderBean {
    	
    	public List<VolumeEntry> getVolumeList() {
    		return volumeList;
    	}
    	
    	public void setVolumeList(List<VolumeEntry> volumeList) {
    		this.volumeList = volumeList;
    	}
    	
    	public void addVolumeEntry() {
    		volumeList.add(new VolumeEntry(0));
    	}
    	
    	@Override
    	protected void doShow() {
    		super.doShow();
    		invalid = false;
    		message = "";
    		volumeList = new ArrayList<>();
    		volumeList.add(new VolumeEntry(0));
    		volumeList.add(new VolumeEntry(0));
    	}
    	
    	@Override
    	protected void doSave() {
    		super.doSave();
    	}
    	
    	public void validateAndCompose() {
    		int sum = 0;
    		for (VolumeEntry volume: volumeList) {
    			if (volume.getVolume() == 0) {
    				invalid = true;
    				message = "Объем нового биоматериала не может быть 0";
    				return;
    			}
    			else if (volume.getVolume() < 0) {
    				invalid = true;
    				message = "Объем нового биоматериала не может быть отрицательным";
    				return;
    			}
    			else {
    				sum += volume.getVolume();
    			}
    		}
    		if (sum == getDocument().getVolume()) {
    			MedicalOperationDAOImpl dao = sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO);
    			int count = 1;
    			for (VolumeEntry volume: volumeList) {
    				Biomaterial biomaterial = getDocument().cloneComponent();
    				biomaterial.setId(0);
    				if (getDocument().getCellCount() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCellCount()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("Количество клеток разделенного компонента: " + v);
	    				biomaterial.setCellCount(v);
    				}
    				if (getDocument().getTnc() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getTnc()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("TNC (x10 в 9 степени): " + v);
	    				biomaterial.setTnc(v);
    				}
    				if (getDocument().getNcPerKg() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getNcPerKg()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("NC/кг (x10 в 8 степени): " + v);
	    				biomaterial.setNcPerKg(v);
    				}
    				if (getDocument().getCd34() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd34()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD34: " + v);
	    				biomaterial.setCd34(v);
    				}
    				if (getDocument().getCd34X6() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd34X6()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD34 (x10 в 6 степени): " + v);
	    				biomaterial.setCd34X6(v);
    				}
    				if (getDocument().getCd34X6PerKg() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd34X6PerKg()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD34 (x10 в 6 степени/кг): " + v);
	    				biomaterial.setCd34X6PerKg(v);
    				}
    				if (getDocument().getCd3() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd3()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD3: " + v);
	    				biomaterial.setCd3(v);
    				}
    				if (getDocument().getCd3X8() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd3X8()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD3 (x10 в 8 степени): " + v);
	    				biomaterial.setCd3X8(v);
    				}
    				if (getDocument().getCd3x8PerKg() > 0) {
    					double v = roundTwoDecimals(2, getDocument().getCd3x8PerKg()*volume.getVolume()/getDocument().getVolume());
	    				System.out.println("CD3 (x10 в 8 степени/кг): " + v);
	    				biomaterial.setCd3x8PerKg(v);
    				}
    				biomaterial.setVolume(volume.getVolume());
    				biomaterial.setInitialVolume(volume.getVolume());
    				biomaterial.setSplit(true);
    				biomaterial.setParentBiomaterial(getDocument());
    				biomaterial.setSplitDate(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
    				
    				if (biomaterial.getOperation() != null) {
    					biomaterial.setNumber(getDocument().getNumber() + "." + StringUtils.right("0" + count, 2));
    					count++;
    				}
    				
    				dao.save(biomaterial);
    			}
    			Biomaterial biomaterial = getDocument();
    			biomaterial.setStatusId(100);
    			setDocument(dao.save(Biomaterial.class, biomaterial));
    			hide();
    		}
    		else {
    			invalid = true;
    			message = "Итоговый объем компонентов не совпадает с исходным";
    		}
    	}
    	
    	private double roundTwoDecimals(int decimalPlaces, double d) {
    		BigDecimal bdTest = new BigDecimal(d);
    		bdTest = bdTest.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    		return bdTest.doubleValue();
    	}
    	
    	public boolean isInvalid() {
    		return invalid;
    	}
    	
    	public String getMessage() {
    		return message;
    	}
    	
    	private List<VolumeEntry> volumeList = new ArrayList<>();
    	
    	private boolean invalid = false;
    	private String message = "";
    	
		private static final long serialVersionUID = -5600215387042330919L;
    }
    
    public class VolumeEntry {
    	
    	public VolumeEntry(int volume) {
    		this.volume = volume;
    	}
    	
    	public void setVolume(int volume) {
			this.volume = volume;
		}
		
		public int getVolume() {
			return volume;
		}
		
    	private int volume;
    }
    
    
    private ProcessorModalBean processorModal = new ProcessorModalBean() {
		@Override
		protected void doInit() {
			setProcessedData(getDocument());
			if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
		}
		@Override
		protected void doPostProcess(ActionResult actionResult) {
			Biomaterial component = (Biomaterial) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				component.addToHistory(getHistoryEntry());
			}
			System.out.println("commentary: " + getHistoryEntry().getCommentary());
			setDocument(component);
			BiomaterialBean.this.save();
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
	
	public class ProcessingExecutorSelectModal extends UserSelectModalBean {
		
		public void setProcessing(Processing processing) {
			this.processing = processing;
		}
		
		public Processing getProcessing() {
			return processing;
		}
		
		@Override
		protected void doSave() {
			super.doSave();
			if (processing != null) processing.setTransfusiologist(getUser());
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			getUserList().setFilter("");
			getUserList().markNeedRefresh();
			setUser(null);
			processing = null;
		}
		
		private Processing processing;
	}
	
	private ProcessingExecutorSelectModal processingExecutorSelectModal = new ProcessingExecutorSelectModal();
	
	private SplitModalHolder splitModal = new SplitModalHolder();
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("biomaterialList")
	private transient BiomaterialListBean biomaterialList = new BiomaterialListBean();
	@Inject @Named("reports")
	private transient ReportsManagmentBean reportsManagement = new ReportsManagmentBean();
	
	/**
	 * Дата/время получения
	 */
	private Date receivedDate;
	private Date receivedTime;
	
	private static final long serialVersionUID = 8070559401705301872L;
}