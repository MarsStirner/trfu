package ru.efive.medicine.niidg.trfu.uifaces.beans.medical;

import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("biomaterialList")
@SessionScoped
public class BiomaterialListBean extends AbstractDocumentListHolderBean<Biomaterial> {

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
			result = new Long(sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).countBiomaterials(filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Biomaterial> loadDocuments() {
		List<Biomaterial> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).findBiomaterials(filter, false,
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
	
	public List<Biomaterial> getBiomaterialsByOperation(Operation operation) {
		List<Biomaterial> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(MedicalOperationDAOImpl.class, MEDICAL_DAO).findBiomaterialsByOperation(operation,
					"", false, -1, -1, "number", false);
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