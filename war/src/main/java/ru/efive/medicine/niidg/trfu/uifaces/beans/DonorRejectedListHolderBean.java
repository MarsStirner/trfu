package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("donorRejectedList")
@SessionScoped
public class DonorRejectedListHolderBean extends AbstractDocumentListHolderBean<Donor> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("rejection.created", true);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").countRejectedDocument(filter, false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Donor> loadDocuments() {
		List<Donor> result = new ArrayList<Donor>();
		try {
			result = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").findRejectedDocuments(filter, false,
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Donor> getDocuments() {
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