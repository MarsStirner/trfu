package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.dao.sql.dao.user.AppointmentDAOHibernate;
import ru.efive.dao.sql.entity.user.Appointment;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("appointmentList")
@SessionScoped
public class AppointmentListHolderBean extends AbstractDocumentListHolderBean<Appointment> {
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("name", true);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(AppointmentDAOHibernate.class, "appointmentDao").countDocument(false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Appointment> loadDocuments() {
		List<Appointment> result = new ArrayList<Appointment>();
		try {
			result = sessionManagement.getDAO(AppointmentDAOHibernate.class, "appointmentDao").findDocuments(false, -1, -1, getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Appointment> getDocuments() {
		return super.getDocuments();
	}
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 1023130636261147049L;
}