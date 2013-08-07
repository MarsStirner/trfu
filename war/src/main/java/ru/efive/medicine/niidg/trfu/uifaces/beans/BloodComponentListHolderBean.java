package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("bloodComponentList")
@SessionScoped
public class BloodComponentListHolderBean extends AbstractDocumentListHolderBean<BloodComponent> {

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
			result = new Long(sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).countDocument(filter, false)).intValue();
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
			result = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findDocuments(filter, false,
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
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
	
	public List<BloodComponent> getDocumentsByDonation(int donationId) {
		List<BloodComponent> result = new ArrayList<BloodComponent>();
		try {
			if (donationId != 0) {
				result = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findComponentsByDonation(donationId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponent> getDocumentsByOrder(int orderId) {
		List<BloodComponent> result = new ArrayList<BloodComponent>();
		try {
			if (orderId != 0) {
				result = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findComponentsByOrder(orderId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponent> getDocumentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor) {
		List<BloodComponent> result = new ArrayList<BloodComponent>();
		try {
			if (phenotypes != null) {
				result = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findComponentsByPhenotypes(
						phenotypes, searchBloodGroup, bloodGroup, searchRhesus, rhesusFactor, "parentNumber,number", true);
			}
			else {
				result = Collections.emptyList();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public void setFullNumber(String fullNumber) {
		this.fullNumber = fullNumber;
	}
	
	public String getFullNumber() {
		return fullNumber;
	}
	
	public void composePurchasedComponent() {
		try {
			String componentNumber = StringUtils.right(fullNumber, 2);
			String parentNumber = StringUtils.substring(fullNumber, fullNumber.length() - 7, fullNumber.length() - 2);
			if (componentNumber != null && !componentNumber.equals("") && parentNumber != null && !parentNumber.equals("")) {
				if (sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).findDocumentsByFullNumber(parentNumber, componentNumber, false).size() > 0) {
					System.out.println("Компонент с таким номером уже зарегистрирован");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Компонент с таким номером уже зарегистрирован", ""));
					return;
				}
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("blood_component.xhtml?docAction=create&purchasedNumber=" + fullNumber);
			fullNumber = "";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String filter;
	
	private String fullNumber;
	

	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private static final long serialVersionUID = 6546450615334686914L;
}