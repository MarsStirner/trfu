package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.medicine.niidg.trfu.dao.ExternalAnalysisResultDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("externalAnalysisResultList")
@SessionScoped
public class ExternalAnalysisResultListHolderBean extends AbstractDocumentListHolderBean<ExternalAnalysisResult> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("id", false);
    }
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(ExternalAnalysisResultDAOImpl.class, "externalAnalysisResultDao").countDocument()).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<ExternalAnalysisResult> loadDocuments() {
		List<ExternalAnalysisResult> result = new ArrayList<ExternalAnalysisResult>();
		try {
			result = sessionManagement.getDAO(ExternalAnalysisResultDAOImpl.class, "externalAnalysisResultDao").findDocuments(getPagination().getOffset(), 
					getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 1L;
}