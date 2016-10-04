package ru.efive.medicine.niidg.trfu.uifaces.beans.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.medicine.niidg.trfu.dao.InformationEntryDaoImpl;
import ru.efive.medicine.niidg.trfu.data.entity.integration.InformationEntry;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("informationList")
@SessionScoped
public class InformationEntryListHolderBean extends AbstractDocumentListHolderBean<InformationEntry> {
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("created", false);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).countDocument(false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при загрузке документов.", ""));
			logger.error("Ошибка при загрузке документов", e);
		}
		return result;
	}

	@Override
	protected List<InformationEntry> loadDocuments() {
		List<InformationEntry> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(InformationEntryDaoImpl.class, ApplicationHelper.INFORMATION_DAO).findDocuments(false, -1, -1,
					getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при загрузке документов.", ""));
			logger.error("Ошибка при загрузке документов", e);
		}
		return result;
	}

	@Override
	public List<InformationEntry> getDocuments() {
		return super.getDocuments();
	}
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final Logger logger = LoggerFactory.getLogger(InformationEntryListHolderBean.class);
	
	private static final long serialVersionUID = 6546450615334686914L;
}