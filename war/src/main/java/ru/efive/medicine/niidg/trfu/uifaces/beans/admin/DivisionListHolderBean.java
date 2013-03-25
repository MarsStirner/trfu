package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("divisionList")
@SessionScoped
public class DivisionListHolderBean extends AbstractDocumentListHolderBean<Division> {
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("name", true);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(DivisionDAOImpl.class, "divisionDao").countDocument(false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Division> loadDocuments() {
		List<Division> result = new ArrayList<Division>();
		try {
			result = sessionManagement.getDAO(DivisionDAOImpl.class, "divisionDao").findDocuments(false, -1, -1, getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Division> getDocuments() {
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
	
	
	private static final long serialVersionUID = -4152651239104111469L;
}