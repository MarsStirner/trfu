package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import java.util.ArrayList;
import java.util.List;

public class UserListByAppointmentHolderBean extends AbstractDocumentListHolderBean<User> {
	private static final long serialVersionUID = -746492376641890611L;
	private String appointment;
	private String filter;
	private UserDAOHibernate dao;
	
	public UserListByAppointmentHolderBean(String appointment, UserDAOHibernate dao) {
		super();
		this.appointment = appointment;
		this.dao = dao;
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
			result = new Long(dao.countUsersByAppointment(appointment, filter, false)).intValue();
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
			result = dao.getUsersByAppointment(appointment, filter, false,
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc()); 
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
}
