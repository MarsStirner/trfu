package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("roleList")
@SessionScoped
public class RoleListHolderBean extends AbstractDocumentListHolderBean<Role> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("name", true);
    }
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(RoleDAOHibernate.class, ApplicationHelper.ROLE_DAO).countRoles()).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Role> loadDocuments() {
		List<Role> result = new ArrayList<Role>();
		try {
			result = sessionManagement.getDAO(RoleDAOHibernate.class, ApplicationHelper.ROLE_DAO).findRoles(getPagination().getOffset(), 
					getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Role> getAvailableRoles() {
		List<Role> result = new ArrayList<Role>();
		try {
			result = sessionManagement.getDAO(RoleDAOHibernate.class, ApplicationHelper.ROLE_DAO).findRoles(-1, -1, "name", true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 1023130636261147049L;
}