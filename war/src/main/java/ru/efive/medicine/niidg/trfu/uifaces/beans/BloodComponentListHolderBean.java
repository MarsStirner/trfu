package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		List<BloodComponent> result = new ArrayList<>();
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
		List<BloodComponent> result = new ArrayList<>();
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
		List<BloodComponent> result = new ArrayList<>();
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
		List<BloodComponent> result = new ArrayList<>();
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

	

	
	
	private String filter;



	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private static final long serialVersionUID = 6546450615334686914L;
}