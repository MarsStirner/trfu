package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("requests_personal")
@SessionScoped
public class PersonalRequestsListHolderBean extends AbstractDocumentListHolderBean<AbstractRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count;
			if (filterExaminationRequests) {
				count = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).countDocumentsByPerson(
						false, sessionManagement.getLoggedUser().getId());
			}
			else {
				count = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).countDocumentsByPerson(
						false, sessionManagement.getLoggedUser().getId());
			}
			result = new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<AbstractRequest> loadDocuments() {
		List<AbstractRequest> result = new ArrayList<>();
		try {
			if (filterExaminationRequests) {
				List<ExaminationRequest> list = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).findDocumentsByPerson(false,
						sessionManagement.getLoggedUser().getId(), getPagination().getOffset(), getPagination().getPageSize(), "number", false);
				for (ExaminationRequest request: list) {
					result.add(request);
				}
			}
			else {
				List<BloodDonationRequest> list = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).findDocumentsByPerson(false,
						sessionManagement.getLoggedUser().getId(), getPagination().getOffset(), getPagination().getPageSize(), "number", false);
				for (BloodDonationRequest request: list) {
					result.add(request);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<AbstractRequest> getDocuments() {
		return super.getDocuments();
	}
	
	public boolean isFilterExaminationRequests() {
		return filterExaminationRequests;
	}
	
	public boolean isFilterBloodDonationRequests() {
		return filterBloodDonationRequests;
	}
	
	public void doFilterExaminationRequests() {
		filterExaminationRequests = true;
		filterBloodDonationRequests = false;
		refresh();
	}
	
	public void doFilterBloodDonationRequests() {
		filterExaminationRequests = false;
		filterBloodDonationRequests = true;
		refresh();
	}
	
	public String getFilterName() {
		String result = "";
		if (filterExaminationRequests) {
			result = "на обследование";
		}
		else {
			result = "на донацию";
		}
		return result;
	}
	
	
	private boolean filterExaminationRequests = true;
    private boolean filterBloodDonationRequests = false;
    
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private static final long serialVersionUID = 6546450615334686914L;
}