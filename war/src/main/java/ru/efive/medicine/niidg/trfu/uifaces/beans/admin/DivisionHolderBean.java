package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import org.apache.axis.utils.StringUtils;
import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Calendar;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.DIVISION_DAO;

@Named("division")
@ConversationScoped
public class DivisionHolderBean extends AbstractDocumentHolderBean<Division, Integer> {
	

	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(DivisionDAOImpl.class, DIVISION_DAO).delete(getDocument());
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
		setDocument(sessionManagement.getDAO(DivisionDAOImpl.class, DIVISION_DAO).get(id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		Division division = new Division();
		division.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
		division.setAuthor(sessionManagement.getLoggedUser());
		setDocument(division);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			DivisionDAOImpl dao = sessionManagement.getDAO(DivisionDAOImpl.class, DIVISION_DAO);
			
			if (StringUtils.isEmpty(getDocument().getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указано название отделения", ""));
				return false;
			}
			if (dao.findByName(getDocument().getName(), true).size() > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отделение с таким названием уже зарегистрировано", ""));
				return false;
			}
			
			Division division = dao.update(getDocument());
			if (division == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(division);
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
	protected boolean saveNewDocument() {
		boolean result = false;
		try {
			DivisionDAOImpl dao = sessionManagement.getDAO(DivisionDAOImpl.class, DIVISION_DAO);
			
			if (StringUtils.isEmpty(getDocument().getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указано название отделения", ""));
				return false;
			}
			if (dao.findByName(getDocument().getName(), true).size() > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отделение с таким названием уже зарегистрировано", ""));
				return false;
			}
			
			Division division = dao.save(getDocument());
			if (division == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}

	
    
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();

	
	private static final long serialVersionUID = 1L;
}