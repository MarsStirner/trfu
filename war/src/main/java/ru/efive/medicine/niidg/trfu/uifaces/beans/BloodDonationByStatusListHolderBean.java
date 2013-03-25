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

@Named("bloodDonationByStatusList")
@SessionScoped
public class BloodDonationByStatusListHolderBean extends AbstractDocumentListHolderBean<BloodDonationRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
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
			List<BloodDonationRequest> list = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).findDocuments(
					filter, false, getPagination().getOffset(), getPagination().getPageSize(), "statusId,number", false);
			int statusId = 0;
	        for (BloodDonationRequest request:list) {
	        	if (request.getStatusId() != statusId) {
	        		statusId = request.getStatusId();
	        		BloodDonationRequest group = new BloodDonationRequest();
	            	group.setId(0);
	            	group.setStatusId(statusId);
	            	result.add(group);
	            }
	        	result.add(request);
	        }
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