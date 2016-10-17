package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("role")
@ConversationScoped
public class RoleHolderBean extends AbstractDocumentHolderBean<Role, Integer> implements Serializable {
    @Inject @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject @Named("roleList")
    RoleListHolderBean roleList = new RoleListHolderBean();

    private static final long serialVersionUID = 5947443099767481905L;

	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO).delete(getDocument());
			result = true;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Невозможно удалить документ. Попробуйте повторить позже.", ""));
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
		setDocument(sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO).get(id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		setDocument(new Role());
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			Role role = sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO).update(getDocument());
			if (role == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(role);
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
			Role role = sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO).save(getDocument());
			if (role == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(role);
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
    protected String doAfterCreate() {
		roleList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterDelete() {
    	roleList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	roleList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    public List<RoleType> getTypes() {
    	List<RoleType> result = new ArrayList<>();
		Collections.addAll(result, RoleType.values());
    	return result;
    }
}