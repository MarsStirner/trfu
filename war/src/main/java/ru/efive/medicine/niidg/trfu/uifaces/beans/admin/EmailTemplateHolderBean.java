package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.medicine.niidg.trfu.dao.EmailTemplateDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.TextTemplateDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;
import ru.efive.medicine.niidg.trfu.data.entity.TextTemplate;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.EMAIL_TEMPLATE_DAO;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.TEXT_TEMPLATE_DAO;

@Named("emailTemplate")
@ConversationScoped
public class EmailTemplateHolderBean extends AbstractDocumentHolderBean<EmailTemplate, Integer> implements Serializable {
    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject @Named("emailTemplateList")
    EmailTemplateListHolderBean listHolderBean;

    @Override
    protected boolean deleteDocument() {
        boolean result = false;
        try {
            sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).delete(getDocument());
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
        setDocument(new EmailTemplate());
    }

    @Override
    protected void initDocument(Integer id) {
        setDocument(sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).get(id));
        if (getDocument() == null) {
            setState(STATE_NOT_FOUND);
        }
    }

    @Override
    protected boolean saveNewDocument() {
        boolean result = false;
        try {
            TextTemplateDAOImpl textTemplateDAO = sessionManagement.getDAO(TextTemplateDAOImpl.class, TEXT_TEMPLATE_DAO);
            if(getDocument().getSendTo()!=null)
                for(TextTemplate t:getDocument().getSendTo()){
                    textTemplateDAO.save(t);
                }
            if(getDocument().getCopyTo()!=null)
                for(TextTemplate t:getDocument().getCopyTo()){
                    textTemplateDAO.save(t);
                }
            if(getDocument().getBlindCopyTo()!=null)
                for(TextTemplate t:getDocument().getBlindCopyTo()){
                    textTemplateDAO.save(t);
                }

            EmailTemplate emailTemplate = sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).save(getDocument());
            if (emailTemplate == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            }
            else {
                setDocument(emailTemplate);
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
            TextTemplateDAOImpl textTemplateDAO = sessionManagement.getDAO(TextTemplateDAOImpl.class, TEXT_TEMPLATE_DAO);
            for(TextTemplate t:getDocument().getSendTo()){
                textTemplateDAO.save(t);
            }
            for(TextTemplate t:getDocument().getCopyTo()){
                textTemplateDAO.save(t);
            }
            for(TextTemplate t:getDocument().getBlindCopyTo()){
                textTemplateDAO.save(t);
            }

            EmailTemplate emailTemplate = sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).update(getDocument());
            if (emailTemplate == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
            }
            else {
                setDocument(emailTemplate);
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

    public void sendToAdd(){
        if(getDocument().getSendTo()==null)
            getDocument().setSendTo(new ArrayList<TextTemplate>());
        getDocument().getSendTo().add(new TextTemplate());
    }

    public void copyToAdd(){
        if(getDocument().getCopyTo()==null)
            getDocument().setCopyTo(new ArrayList<TextTemplate>());
        getDocument().getCopyTo().add(new TextTemplate());
    }

    public void blindCopyToAdd(){
        if(getDocument().getBlindCopyTo()==null)
            getDocument().setBlindCopyTo(new ArrayList<TextTemplate>());
        getDocument().getBlindCopyTo().add(new TextTemplate());

    }
}