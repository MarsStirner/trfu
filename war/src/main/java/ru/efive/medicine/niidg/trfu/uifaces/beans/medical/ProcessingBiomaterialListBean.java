package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("biomaterialProcessingList")
@SessionScoped
public class ProcessingBiomaterialListBean extends AbstractDocumentListHolderBean<Biomaterial> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	public Sorting initSorting() {
		return new Sorting("operation.number,number", true);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).countBiomaterials(filter, 3, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Biomaterial> loadDocuments() {
		List<Biomaterial> result = new ArrayList<Biomaterial>();
		try {
			result = sessionManagement.getDAO(MedicalOperationDAOImpl.class, ApplicationHelper.MEDICAL_DAO).findBiomaterials(filter, 3, false,
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Biomaterial> getDocuments() {
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