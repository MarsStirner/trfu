package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.RequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("requests")
@SessionScoped
public class RequestsListHolderBean extends AbstractDocumentListHolderBean<AbstractRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(RequestDAOImpl.class, ApplicationHelper.REQUEST_DAO).countDocument(false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<AbstractRequest> loadDocuments() {
		List<AbstractRequest> result = new ArrayList<AbstractRequest>();
		try {
			result = sessionManagement.getDAO(RequestDAOImpl.class, ApplicationHelper.REQUEST_DAO).findDocuments(false,
					getPagination().getOffset(), getPagination().getPageSize(), "created", false);
			Collections.sort(result, new Comparator<AbstractRequest>() {
				@Override
				public int compare(AbstractRequest o1, AbstractRequest o2) {
					Calendar c1 = Calendar.getInstance(ApplicationHelper.getLocale());
					c1.setTime(o1.getCreated());
					Calendar c2 = Calendar.getInstance(ApplicationHelper.getLocale());
					c2.setTime(o2.getCreated());
					return c2.compareTo(c1);
				}
			});
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
	
	public List<AbstractRequest> getDocumentsByParent(int parentId) {
		List<AbstractRequest> result = new ArrayList<AbstractRequest>();
		try {
			if (parentId != 0) {
				result = sessionManagement.getDAO(RequestDAOImpl.class, ApplicationHelper.REQUEST_DAO).findRequestsByDonor(false, parentId, "created", false);
				Collections.sort(result, new Comparator<AbstractRequest>() {
					@Override
					public int compare(AbstractRequest o1, AbstractRequest o2) {
						Calendar c1 = Calendar.getInstance(ApplicationHelper.getLocale());
						c1.setTime(o1.getCreated());
						Calendar c2 = Calendar.getInstance(ApplicationHelper.getLocale());
						c2.setTime(o2.getCreated());
						return c2.compareTo(c1);
					}
				});
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
    
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private static final long serialVersionUID = 6546450615334686914L;
}