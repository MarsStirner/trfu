package ru.efive.medicine.niidg.trfu.uifaces.beans.integration;

import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ru.efive.medicine.niidg.trfu.dao.InformationEntryDaoImpl;
import ru.efive.medicine.niidg.trfu.data.entity.integration.InformationEntry;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

@Named("information")
@ConversationScoped
public class InformationEntryHolderBean extends AbstractDocumentHolderBean<InformationEntry, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).delete(getDocument());
			result = true;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при удалении документа", ""));
			logger.error("Ошибка при удалении документа", e);
		}
		return result;
	}
	
	@Override
	protected Integer getDocumentId() {
		return getDocument() == null? 0: getDocument().getId();
	}
	
	@Override
	protected FromStringConverter<Integer> getIdConverter() {
		return FromStringConverter.INTEGER_CONVERTER;
	}
	
	@Override
	protected void initDocument(Integer id) {
		try {
			InformationEntry entry = sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).get(id);
			if (entry == null) {
				setState(STATE_NOT_FOUND);
			}
			else {
				setDocument(entry);
			}
		}
		catch (Exception e) {
			setState(STATE_INTERNAL_ERROR);
			logger.error("Ошибка при инициализации документа", e);
		}
	}
	
	@Override
	protected void initNewDocument() {
		try {
			InformationEntry entry = new InformationEntry();
			Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
			entry.setCreated(created);
			entry.setAuthor(sessionManagement.getLoggedUser());
			entry.setDeleted(false);
			
			setDocument(entry);
		}
		catch (Exception e) {
			setState(STATE_INTERNAL_ERROR);
			logger.error("Ошибка при инициализации нового документа", e);
		}
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			InformationEntry entry = sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).save(getDocument());
			if (entry == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			}
			else {
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			logger.error("Ошибка при сохранении документа", e);
		}
		return result;
	}
	
	@Override
	protected boolean saveNewDocument() {
		boolean result = false;
		try {
			InformationEntry entry = sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).save(getDocument());
			if (entry == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			}
			else {
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			logger.error("Ошибка при сохранении документа", e);
		}
		return result;
	}
	
	public boolean publish() {
		boolean result = false;
		try {
			InformationEntry entry = getDocument();
			entry.setPublished(true);
			entry.setPublishDate(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
			setDocument(entry);
			save();
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при публикации документа", ""));
			logger.error("Ошибка при сохранении документа", e);
		}
		return result;
	}
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final Logger logger = Logger.getLogger(InformationEntryHolderBean.class);
	
	private static final long serialVersionUID = 1L;
}