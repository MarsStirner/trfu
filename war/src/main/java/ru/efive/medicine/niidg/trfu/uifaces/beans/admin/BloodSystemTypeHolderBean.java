package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;


import ru.efive.medicine.niidg.trfu.dao.BloodSystemTypeDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.BLOOD_SYSTEM_TYPE_DAO;

@Named("bloodSystemType")
@ConversationScoped
public class BloodSystemTypeHolderBean extends AbstractDocumentHolderBean<BloodSystemType, Integer> implements Serializable {
    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject @Named("bloodSystemTypeList")
    BloodSystemTypeListHolderBean listHolderBean;

    @Override
    protected boolean deleteDocument() {
        boolean result = false;
        try {
            sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, BLOOD_SYSTEM_TYPE_DAO).delete(getDocument());
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
    protected void initNewDocument() {
        setDocument(new BloodSystemType());
    }

    @Override
    protected void initDocument(Integer id) {
        setDocument(sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, BLOOD_SYSTEM_TYPE_DAO).get(id));
        if (getDocument() == null) {
            setState(STATE_NOT_FOUND);
        }
    }

    @Override
    protected boolean saveNewDocument() {
        boolean result = false;
        try {
            BloodSystemType systemType = sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, BLOOD_SYSTEM_TYPE_DAO).save(getDocument());
            if (systemType == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            }
            else {
                setDocument(systemType);
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
    protected boolean saveDocument() {
        boolean result = false;
        try {
            BloodSystemType systemType = sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, BLOOD_SYSTEM_TYPE_DAO).update(getDocument());
            if (systemType == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            }
            else {
                setDocument(systemType);
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
    protected FromStringConverter<Integer> getIdConverter() {
        return FromStringConverter.INTEGER_CONVERTER;
    }
}
