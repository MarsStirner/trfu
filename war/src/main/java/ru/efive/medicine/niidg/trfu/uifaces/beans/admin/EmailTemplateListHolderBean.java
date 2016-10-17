package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.medicine.niidg.trfu.dao.EmailTemplateDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.EMAIL_TEMPLATE_DAO;

@Named("emailTemplateList")
@SessionScoped
public class EmailTemplateListHolderBean extends AbstractDocumentListHolderBean<EmailTemplate> {
    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();

    @Override
    protected int getTotalCount() {
        int result = 0;
        try {
            result = new Long(sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).countDocument(false)).intValue();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected List<EmailTemplate> loadDocuments() {
        List<EmailTemplate> result = new ArrayList<>();
        try {
            result = sessionManagement.getDAO(EmailTemplateDAOImpl.class, EMAIL_TEMPLATE_DAO).findDocuments(false,-1,-1,"name", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
