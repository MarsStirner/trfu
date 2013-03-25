package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("bloodDonationList")
@SessionScoped
public class BloodDonationListHolderBean extends AbstractDocumentListHolderBean<BloodDonationRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("number", false);
    }
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			return new Long(sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).countDocument(filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<BloodDonationRequest> loadDocuments() {
		List<BloodDonationRequest> result = new ArrayList<BloodDonationRequest>();
		try {
			result = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).findDocuments(filter, false,
				getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BloodDonationRequest> getDocuments() {
		return super.getDocuments();
	}
	
	public List<BloodDonationRequest> getDocumentsByParent(int parentId) {
		List<BloodDonationRequest> result = new ArrayList<BloodDonationRequest>();
		try {
			if (parentId != 0) {
				result = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).findDonationRequestsByDonorId(false, parentId, -1, -1, "created", false);
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