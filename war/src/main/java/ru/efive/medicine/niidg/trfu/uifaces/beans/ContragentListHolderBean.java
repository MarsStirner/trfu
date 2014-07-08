package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("contragentList")
@SessionScoped
public class ContragentListHolderBean extends AbstractDocumentListHolderBean<Contragent> {

    @Override
    public Pagination initPagination() {
        return new Pagination(0, getTotalCount(), 100);
    }

    @Override
    protected int getTotalCount() {
        int result = 0;
        try {
            result = new Long(sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO).countDocument(filter, false)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected List<Contragent> loadDocuments() {
        List<Contragent> result = new ArrayList<Contragent>();
        try {
            result = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO).
                    findDocuments(filter, false, getPagination().getOffset(), getPagination().getPageSize(), "fullName", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Contragent> getContragentsWithEmpty() {
        List<Contragent> result = new ArrayList<Contragent>();
        Contragent contragent = new Contragent();
        contragent.setFullName("");
        contragent.setShortName("");
        result.add(contragent);
        try {
            result.addAll(sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO).findDocuments(false, -1, -1, "fullName", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveDeletions() {
        final ContragentDAOHibernate contragentDAO = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO);
        if (contragentDAO != null) {
            for (Contragent currentContragent : getDocuments()) {
                if (currentContragent.isDeleted()) {
                    contragentDAO.save(currentContragent);
                }
            }
        }
        refresh();
    }


    private String filter;

    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();

    private static final long serialVersionUID = -1748241340948003433L;
}