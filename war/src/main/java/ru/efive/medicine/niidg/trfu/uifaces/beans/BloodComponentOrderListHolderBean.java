package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("bloodComponentOrderList")
@SessionScoped
public class BloodComponentOrderListHolderBean extends AbstractDocumentListHolderBean<BloodComponentOrderRequest> {

    private static final long serialVersionUID = 6546450615334686914L;
    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();
    private String filter;

    @Override
    protected int getTotalCount() {
        int result = 0;
        try {
            long count = sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO)
                    .countDocument(filter, false);
            return new Long(count).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected List<BloodComponentOrderRequest> loadDocuments() {
        List<BloodComponentOrderRequest> result = new ArrayList<BloodComponentOrderRequest>();
        try {
            result = sessionManagement.getDAO(BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO).findDocuments(
                    filter, false, getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Pagination initPagination() {
        return new Pagination(0, getTotalCount(), 100);
    }

    @Override
    public Sorting initSorting() {
        return new Sorting("number", false);
    }

    @Override
    public List<BloodComponentOrderRequest> getDocuments() {
        return super.getDocuments();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}