package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.*;
@Named("examinationByStatusList")
@SessionScoped
public class ExaminationByStatusListHolderBean extends AbstractDocumentListHolderBean<ExaminationRequest> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			return new Long(sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).countDocument(filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected List<ExaminationRequest> loadDocuments() {
		List<ExaminationRequest> result = new ArrayList<>();
		try {
			List<ExaminationRequest> list = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).findDocuments(
					filter, false, getPagination().getOffset(), getPagination().getPageSize(), "statusId,number", false);
			int statusId = 0;
	        for (ExaminationRequest request:list) {
	        	if (request.getStatusId() != statusId) {
	        		statusId = request.getStatusId();
	        		ExaminationRequest group = new ExaminationRequest();
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
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 6546450615334686914L;
}