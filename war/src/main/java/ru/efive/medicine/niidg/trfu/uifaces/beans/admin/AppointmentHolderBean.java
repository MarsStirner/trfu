package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.axis.utils.StringUtils;

import ru.efive.dao.sql.dao.user.AppointmentDAOHibernate;
import ru.efive.dao.sql.entity.user.Appointment;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

@Named("appointment")
@ConversationScoped
public class AppointmentHolderBean extends AbstractDocumentHolderBean<Appointment, Integer> implements Serializable {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(AppointmentDAOHibernate.class, APPOINTMENT_DAO).delete(getDocument());
			result = true;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно удалить документ. Попробуйте повторить позже.", ""));
		}
		return result;
	}
	
	@Override
	protected Integer getDocumentId() {
		return getDocument().getId();
	}
	
	@Override
	protected FromStringConverter<Integer> getIdConverter() {
		return FromStringConverter.INTEGER_CONVERTER;
	}
	
	@Override
	protected void initDocument(Integer id) {
		setDocument(sessionManagement.getDAO(AppointmentDAOHibernate.class, APPOINTMENT_DAO).get(id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		Appointment appointment = new Appointment();
		appointment.setDeleted(false);
		setDocument(appointment);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			AppointmentDAOHibernate dao = sessionManagement.getDAO(AppointmentDAOHibernate.class, APPOINTMENT_DAO);
			if (StringUtils.isEmpty(getDocument().getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указано название должности", ""));
				return false;
			}
			if (dao.findByName(getDocument().getName()).size() > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Должность с таким названием уже зарегистрирована", ""));
				return false;
			}
			
			Appointment appointment = dao.update(getDocument());
			if (appointment == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(appointment);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected boolean saveNewDocument() {
		boolean result = false;
		try {
			AppointmentDAOHibernate dao = sessionManagement.getDAO(AppointmentDAOHibernate.class, APPOINTMENT_DAO);
			if (StringUtils.isEmpty(getDocument().getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указано название должности", ""));
				return false;
			}
			if (dao.findByName(getDocument().getName()).size() > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Должность с таким названием уже зарегистрирована", ""));
				return false;
			}
			
			Appointment appointment = dao.save(getDocument());
			if (appointment == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(appointment);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
    protected String doAfterCreate() {
		appointmentList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	appointmentList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	appointmentList.markNeedRefresh();
        return super.doAfterSave();
    }
	
    
    @Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	@Inject @Named("appointmentList")
	private transient AppointmentListHolderBean appointmentList = new AppointmentListHolderBean();
	
	
	private static final long serialVersionUID = 1L;
}