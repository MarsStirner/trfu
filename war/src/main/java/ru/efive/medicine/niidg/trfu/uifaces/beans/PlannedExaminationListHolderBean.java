package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("plannedExaminationList")
@SessionScoped
public class PlannedExaminationListHolderBean extends AbstractDocumentListHolderBean<ExaminationRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("planDate", false);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			return new Long(sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).countPlannedDocument(filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<ExaminationRequest> loadDocuments() {
		List<ExaminationRequest> result = new ArrayList<ExaminationRequest>();
		try {
			result = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).findPlannedDocuments(filter, false,
				getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ExaminationRequest> getDocuments() {
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
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 6546450615334686914L;
}