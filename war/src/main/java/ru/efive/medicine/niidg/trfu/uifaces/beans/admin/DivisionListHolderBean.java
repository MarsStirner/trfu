package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@ManagedBean(name="divisionList")
@ViewScoped
public class DivisionListHolderBean extends AbstractDocumentListHolderBean<Division> {
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("name", true);
    }

	@Override
	protected int getTotalCount() {
		try {
            return new Long(sessionManagement.getDAO(DivisionDAOImpl.class, ApplicationHelper.DIVISION_DAO).countDocument(false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
            return 0;
		}
	}

	@Override
	protected List<Division> loadDocuments() {
		List<Division> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(DivisionDAOImpl.class, ApplicationHelper.DIVISION_DAO).findByName(filter, false, getSorting().getColumnId(), getSorting().isAsc());
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
	
	public void loadExternalData() {
		if (!IntegrationHelper.updateDivisions()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Ошибка при обновлении списка подразделений. Попробуйте повторить позже", ""));
		}
		refresh();
	}
	
	private String filter;

	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = -4152651239104111469L;
}