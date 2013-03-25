package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;
import ru.efive.medicine.niidg.trfu.uifaces.beans.AddressSelectModal;
import ru.efive.medicine.niidg.trfu.uifaces.beans.ProcessorModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.wf.core.ActionResult;

@Named("biomaterialDonor")
@ConversationScoped
public class BiomaterialDonorBean extends AbstractDocumentHolderBean<BiomaterialDonor, Integer> {
	
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
		setDocument(sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).get(BiomaterialDonor.class, id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		BiomaterialDonor donor = new BiomaterialDonor();
		donor.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		donor.setCreated(created);
		donor.setAuthor(sessionManagement.getLoggedUser());
		
		/*HistoryEntry historyEntry = new HistoryEntry();
		historyEntry.setCreated(created);
		historyEntry.setStartDate(created);
		historyEntry.setOwner(sessionManagement.getLoggedUser());
		historyEntry.setDocType(donor.getType());
		historyEntry.setParentId(donor.getId());
		historyEntry.setActionId(0);
		historyEntry.setFromStatusId(1);
		historyEntry.setEndDate(created);
		historyEntry.setProcessed(true);
		historyEntry.setCommentary("");
		Set<HistoryEntry> history = new HashSet<HistoryEntry>();
		history.add(historyEntry);
		donor.setHistory(history);*/
		
		setDocument(donor);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			BiomaterialDonor donor = sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).update(BiomaterialDonor.class, getDocument());
			if (donor == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(donor);
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
			BiomaterialDonor donor = dao.save(BiomaterialDonor.class, getDocument());
			if (donor == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				donor.setNumber(StringUtils.right("00000" + donor.getId(), 5));
				donor = dao.save(BiomaterialDonor.class, donor);
				setDocument(donor);
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
		biomaterialDonorList.markNeedRefresh();
		biomaterialDonorByNumberList.markNeedRefresh();
		biomaterialDonorByDateList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	biomaterialDonorList.markNeedRefresh();
		biomaterialDonorByNumberList.markNeedRefresh();
		biomaterialDonorByDateList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	biomaterialDonorList.markNeedRefresh();
		biomaterialDonorByNumberList.markNeedRefresh();
		biomaterialDonorByDateList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    public void copyAddress() {
    	BiomaterialDonor donor = getDocument();
    	donor.setFactAddress(donor.getRegistrationAddress());
    	setDocument(donor);
    }
    
	public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
    private ProcessorModalBean processorModal = new ProcessorModalBean() {
		@Override
		protected void doInit() {
			setProcessedData(getDocument());
			if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
		}
		@Override
		protected void doPostProcess(ActionResult actionResult) {
			BiomaterialDonor donor = (BiomaterialDonor) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				Set<HistoryEntry> history = donor.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(getHistoryEntry());
				donor.setHistory(history);
			}
			setDocument(donor);
			BiomaterialDonorBean.this.save();
		}
	};
    
    public AddressSelectModal getRegistrationAddressSelect() {
		return registrationAddressSelect;
	}
    public AddressSelectModal getFactAddressSelect() {
    	return factAddressSelect;
    }
    
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("biomaterialDonorList")
	BiomaterialDonorListBean biomaterialDonorList = new BiomaterialDonorListBean();
	@Inject @Named("biomaterialDonorByNumberList")
	BiomaterialDonorByNumberListBean biomaterialDonorByNumberList = new BiomaterialDonorByNumberListBean();
	@Inject @Named("biomaterialDonorByDateList")
	BiomaterialDonorByDateListBean biomaterialDonorByDateList = new BiomaterialDonorByDateListBean();
	
	private AddressSelectModal registrationAddressSelect = new AddressSelectModal() {
		@Override
		protected void doSave() {
			StringBuffer buffer = new StringBuffer();
			if (StringUtils.isNotEmpty(getCountry())) { buffer.append(getCountry()); }
			if (StringUtils.isNotEmpty(getState())) { buffer.append(" ").append(getState()); }
			if (StringUtils.isNotEmpty(getDistrict())) { buffer.append(" ").append(getDistrict()); }
			if (StringUtils.isNotEmpty(getCity())) { buffer.append(" г. ").append(getCity()); }
			if (StringUtils.isNotEmpty(getStreet())) { buffer.append(" ул. ").append(getStreet()); }
			if (StringUtils.isNotEmpty(getHouse())) { buffer.append(" дом ").append(getHouse()); }
			if (StringUtils.isNotEmpty(getBuilding())) { buffer.append(" к. ").append(getBuilding()); }
			if (StringUtils.isNotEmpty(getConstruction())) { buffer.append(" стр. ").append(getConstruction()); }
			if (StringUtils.isNotEmpty(getFlat())) { buffer.append(" кв. ").append(getFlat()); }
			BiomaterialDonor donor = getDocument();
			donor.setRegistrationAddress(buffer.toString().trim());
			setDocument(donor);
			
		}
	};
	private AddressSelectModal factAddressSelect = new AddressSelectModal() {
		@Override
		protected void doSave() {
			StringBuffer buffer = new StringBuffer();
			if (StringUtils.isNotEmpty(getCountry())) { buffer.append(getCountry()); }
			if (StringUtils.isNotEmpty(getState())) { buffer.append(" ").append(getState()); }
			if (StringUtils.isNotEmpty(getDistrict())) { buffer.append(" ").append(getDistrict()); }
			if (StringUtils.isNotEmpty(getCity())) { buffer.append(" г. ").append(getCity()); }
			if (StringUtils.isNotEmpty(getStreet())) { buffer.append(" ул. ").append(getStreet()); }
			if (StringUtils.isNotEmpty(getHouse())) { buffer.append(" дом ").append(getHouse()); }
			if (StringUtils.isNotEmpty(getBuilding())) { buffer.append(" к. ").append(getBuilding()); }
			if (StringUtils.isNotEmpty(getConstruction())) { buffer.append(" стр. ").append(getConstruction()); }
			if (StringUtils.isNotEmpty(getFlat())) { buffer.append(" кв. ").append(getFlat()); }
			BiomaterialDonor donor = getDocument();
			donor.setFactAddress(buffer.toString().trim());
			setDocument(donor);
		}
	};
	
	
	private static final long serialVersionUID = 8070559401705301872L;
}