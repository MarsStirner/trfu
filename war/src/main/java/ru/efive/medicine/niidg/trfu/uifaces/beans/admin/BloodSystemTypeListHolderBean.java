package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.medicine.niidg.trfu.dao.BloodSystemTypeDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("bloodSystemTypeList")
@SessionScoped
public class BloodSystemTypeListHolderBean extends AbstractDocumentListHolderBean<BloodSystemType> {
    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();

    @Override
    protected int getTotalCount() {
        int result = 0;
        try {
            result = new Long(sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_TYPE_DAO).countDocument(false)).intValue();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected List<BloodSystemType> loadDocuments() {
        List<BloodSystemType> result = new ArrayList<>();
        try {
            result = sessionManagement.getDAO(BloodSystemTypeDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_TYPE_DAO).findDocuments();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
