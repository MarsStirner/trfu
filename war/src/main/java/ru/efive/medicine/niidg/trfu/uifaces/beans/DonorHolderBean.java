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
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.wf.core.ActionResult;

@Named("donor")
@ConversationScoped
public class DonorHolderBean extends AbstractDocumentHolderBean<Donor, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).delete(getDocument());
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
		setDocument(sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).get(id));
		
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		} else {
			setDonationToday();
		}
	}
	
	@Override
	protected void initNewDocument() {
		Donor donor = new Donor();
		donor.setStatusId(1);
		Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
		donor.setCreated(created);
		donor.setAuthor(sessionManagement.getLoggedUser());
		donor.setCategory(0);
		
		HistoryEntry historyEntry = new HistoryEntry();
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
		donor.setHistory(history);
		
		setDocument(donor);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			Donor donor = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).update(getDocument());
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
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			Donor donor = dao.save(getDocument());
			if (donor == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				donor.setNumber(StringUtils.right("00000" + donor.getId(), 5));
				donor = dao.save(donor);
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
    	donorList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	donorList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	donorList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    public void copyAddress() {
    	Donor donor = getDocument();
    	donor.setFactAddress(donor.getRegistrationAddress());
    	setDocument(donor);
    }
    
    public boolean isDonationToday() {
    	return donationToday;
    }
    
    public boolean isRequestCreationAvailable() {
    	Donor donor = getDocument();
    	if (donor.getStatusId() == -2 && donor.getRejection() != null && donor.getRejection().getRejectionType().getType() == 4) {
    		return false;
    	}
    	/*else if (donor.getStatusId() == -1 && donor.getRejection() != null && donor.getRejection().getRejectionType().getType() == 0) {
    		Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
    		return calendar.getTime().after(donor.getRejection().getExpiration());
    	}*/
    	else {
    		return true;
    	}
    }
    
    public String getDonorAlert() {
    	String result = "";
    	try {
    		if (getDocument() != null && getDocument().getId() != 0) {
    			if (sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).countDocumentsInQuarantineByDonor(getDocument(), null, false) > 0) {
    				result = "Есть компоненты в карантине";
    			}
    		}
    	}
    	catch (Exception e) {
    		
    	}
    	return result;
    }
    
    public void setDonationToday() {
    	Donor donor = getDocument();
    	Date current = new Date();
    	BloodDonationRequestDAOImpl dao = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO);
    	List<BloodDonationRequest> donations = dao.findDonationRequestsByDonorIdAndDateCreated(false, donor.getId(), current, -1, -1, null, true);
    	if (donations.size() > 0) {
    		donationToday = true;
    	} else {
    		donationToday = false;
    	}
    }
	
    /* MODAL HOLDERS */
	
	/* END OF MODAL HOLDERS */
	
	
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
			Donor donor = (Donor) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				Set<HistoryEntry> history = donor.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(getHistoryEntry());
				donor.setHistory(history);
			}
			setDocument(donor);
			DonorHolderBean.this.save();
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
	@Inject @Named("donorList")
	DonorListHolderBean donorList = new DonorListHolderBean();
	private boolean donationToday;
	
	private AddressSelectModal registrationAddressSelect = new AddressSelectModal() {
		@Override
		protected void doSave() {
			StringBuffer buffer = new StringBuffer();
			if (getCity() != null && !getCity().equals("")) { buffer.append("г. ").append(getCity()); }
			if (getStreet() != null && !getStreet().equals("")) { buffer.append(" ул. ").append(getStreet()); }
			if (getHouse() != null && !getHouse().equals("")) { buffer.append(" дом ").append(getHouse()); }
			if (getBuilding() != null && !getBuilding().equals("")) { buffer.append(" к. ").append(getBuilding()); }
			if (getConstruction() != null && !getConstruction().equals("")) { buffer.append(" стр. ").append(getConstruction()); }
			if (getFlat() != null && !getFlat().equals("")) { buffer.append(" кв. ").append(getFlat()); }
			Donor donor = getDocument();
			donor.setRegistrationAddress(buffer.toString());
			setDocument(donor);
			
		}
	};
	private AddressSelectModal factAddressSelect = new AddressSelectModal() {
		@Override
		protected void doSave() {
			StringBuffer buffer = new StringBuffer();
			if (getCity() != null && !getCity().equals("")) { buffer.append("г. ").append(getCity()); }
			if (getStreet() != null && !getStreet().equals("")) { buffer.append(" ул. ").append(getStreet()); }
			if (getHouse() != null && !getHouse().equals("")) { buffer.append(" дом ").append(getHouse()); }
			if (getBuilding() != null && !getBuilding().equals("")) { buffer.append(" к. ").append(getBuilding()); }
			if (getConstruction() != null && !getConstruction().equals("")) { buffer.append(" стр. ").append(getConstruction()); }
			if (getFlat() != null && !getFlat().equals("")) { buffer.append(" кв. ").append(getFlat()); }
			Donor donor = getDocument();
			donor.setFactAddress(buffer.toString());
			setDocument(donor);
		}
	};
	
	private static final long serialVersionUID = 8070559401705301872L;
}