package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodSystemDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

@Named("virusinactivationComponentList")
@SessionScoped
public class VirusinactivationBloodComponetsBean extends AbstractDocumentListHolderBean<BloodComponent> {
	private static final long serialVersionUID = 8942121183249792240L;
	
	// статусы КК - зарегистрирован, в карантине, готов к выдаче, готов к выдаче из карантина
	private List<Integer> bloodComponentStatusIdList = Arrays.asList(new Integer[] { 1, 2, 3, 5});
	// статусы донаций КК - донация, получение результатов анализов, паспортизация
	private List<Integer> donationStatusIdList = Arrays.asList(new Integer[] {2, 3, 4}); 
		
	@Inject @Named("sessionManagement")
	private SessionManagementBean sessionManagement;
	
	private BloodSystem currentBloodSystem;
	private String filter;
	private BloodSystemSelect bloodSystemTypeSelectModal = new BloodSystemSelect();
	private ExpirationDateSelect expirationDateSelectModal = new ExpirationDateSelect();
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	public Sorting initSorting() {
		return new Sorting("parentNumber,number", true);
	}
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).countDocument(filter, bloodComponentStatusIdList, donationStatusIdList, false, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected List<BloodComponent> loadDocuments() {
		List<BloodComponent> result = new ArrayList<BloodComponent>();
		try {
			result = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findDocuments(filter, bloodComponentStatusIdList, donationStatusIdList, false, false, 
					getPagination().getOffset(), getPagination().getPageSize(), "created", false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BloodComponent> getDocuments() {
		return super.getDocuments();
	}
	
	public String getCurrentBloodSystemId() {
		if (currentBloodSystem != null) {
			return String.valueOf(currentBloodSystem.getId()); 
		}
		return "";
	}
	
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public BloodSystemSelect getBloodSystemTypeSelectModal() {
        return bloodSystemTypeSelectModal;
    }
	
	public ExpirationDateSelect getExpirationDateSelectModal() {
		return expirationDateSelectModal;
	}
	
	public String getConsumableMaterial() {
		StringBuffer result = new StringBuffer("");
		if (currentBloodSystem != null) {
			result.append("Лот: ");
			result.append(currentBloodSystem.getSystemLot());
			result.append(". Тип: ");
			result.append(currentBloodSystem.getType().getValue());
			result.append(". Годен до: ");
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			result.append(formatter.format(currentBloodSystem.getExpirationDate()));
		}
		return result.toString();
	}
	
	public class BloodSystemSelect extends ModalWindowHolderBean {
		private static final long serialVersionUID = 5479346746136426256L;
		private BloodSystemType selected = null;
		
		public BloodSystemSelect() {
			setModalVisible(true);
		}
		
		public BloodSystemType getBloodSystemTypeSelected() {
			return selected;
		}
		
        public List<BloodSystemType> getAllTypes(){
            return sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodSystemTypes(false);
        }

        public void select(BloodSystemType selected) {
        	this.selected = selected;
        }
        
        public boolean selected(BloodSystemType row) {
        	if (selected == null) {
        		selected = getAllTypes().get(0);
        	}
        	return this.selected.equals(row);
        }
        
        public void openExpirationDateWindow() {
        	this.hide();
        	expirationDateSelectModal.show();
        }

        @Override
        protected void doSave() {
        	super.doSave();
        }
        
        @Override
        protected void doHide() {
        	super.doHide();
        }
    }
	
	public class ExpirationDateSelect extends ModalWindowHolderBean {
		private static final long serialVersionUID = 7379428814542894175L;
		private static final String ERR_MSG_LOT = "Введите лот";
		private static final String ERR_MSG_DATE = "Введите дату";
		
		private Date enteredExpirationDate = null;
		private String systemLot = null;
		private boolean invalid = false;
    	private String message = "";
    	
		public boolean isInvalid() {
    		return invalid;
    	}
    	
		public String getSystemLot() {
			return systemLot;
		}

		public void setSystemLot(String systemLot) {
			this.systemLot = systemLot;
		}
		
    	public String getMessage() {
    		return message;
    	}
    	
        public Date getEnteredExpirationDate() {
			return enteredExpirationDate;
		}

		public void setEnteredExpirationDate(Date enteredExpirationDate) {
			this.enteredExpirationDate = enteredExpirationDate;
		}
		
		@Override
    	protected void doShow() {
    		super.doShow();
    		invalid = false;
    		message = "";
    	}
		
		public void validateExpirationDate() {
			if("".equals(systemLot)){
				setErrorMessage(ERR_MSG_LOT);
				return;
			}
			if(enteredExpirationDate == null){
				setErrorMessage(ERR_MSG_DATE);
				return;
			} 
			saveBloodSystem();
			this.hide();
        }
		
		private void setErrorMessage(String msg) {
			message = msg;
			invalid = true;
		}
		
		private void saveBloodSystem() {
			currentBloodSystem = new BloodSystem();
			BloodSystemType currentBloodSystemType = bloodSystemTypeSelectModal.getBloodSystemTypeSelected();
			currentBloodSystem.setType(currentBloodSystemType);
			currentBloodSystem.setExpirationDate(enteredExpirationDate);
			currentBloodSystem.setSystemLot(systemLot);
			Integer roleId = sessionManagement.getCurrentRole().getId();
			currentBloodSystem.setRoleId(roleId);
			Integer userId = sessionManagement.getLoggedUser().getId();
			currentBloodSystem.setUserId(userId);
			BloodSystemDAOImpl bloodSystemDAO = sessionManagement.getDAO(BloodSystemDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_DAO);
			bloodSystemDAO.save(currentBloodSystem);
		}
    }
}
