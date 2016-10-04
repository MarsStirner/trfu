package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import java.util.ArrayList;
import java.util.List;

public class UserListByRoleTypeHolderBean  extends AbstractDocumentListHolderBean<User> {
	private static final long serialVersionUID = -6101030623256125020L;
	private Role role;
	private RoleType key;
	private String filter;
	private UserDAOHibernate userDao;
	private RoleDAOHibernate roleDao;
	
	public UserListByRoleTypeHolderBean(RoleType key, UserDAOHibernate userDao, RoleDAOHibernate roleDao) {
		super();
		this.key = key;
		this.userDao = userDao;
		this.roleDao = roleDao;
	}
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("lastName,firstName,middleName", true);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(userDao.countUsersByRole(this.getRole(), false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<User> loadDocuments() {
		List<User> result = new ArrayList<>();
		try {
			result = userDao.findUsersByRole(getRole(), false, 
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void setRoleByType() {
		switch (key) {
			case REGISTRATOR:
				role = roleDao.findByValue("Регистратор").get(0);
				break;
			case THERAPIST:
				role = roleDao.findByValue("Врач-трансфузиолог").get(0);
				break;
		}
	}
	
	private Role getRole() {
		if (role == null) {
			setRoleByType();
		}
			
		return role;
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
