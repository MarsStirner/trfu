package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("medicalOperationList")
@SessionScoped
public class MedicalOperationListBean extends AbstractDocumentListHolderBean<Operation> {

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
			return new Long(sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).countOperations(filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Operation> loadDocuments() {
		List<Operation> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).findOperations(filter, false,
				getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Operation> getDocuments() {
		return super.getDocuments();
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	
	public List<Operation> getOperationsByDonor(BiomaterialDonor donor) {
		List<Operation> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).findOperationsByDonor(donor, "", false,
					-1, -1, "number", false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	private String filter;
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 6546450615334686914L;
}