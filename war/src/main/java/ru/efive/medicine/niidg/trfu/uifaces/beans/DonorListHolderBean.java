package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.*;
@Named("donorList")
@SessionScoped
public class DonorListHolderBean extends AbstractDocumentListHolderBean<Donor> {

    @Override
    public Pagination initPagination() {
        return new Pagination(0, getTotalCount(), 100);
    }

    @Override
    protected Sorting initSorting() {
        return new Sorting("lastName,firstName,middleName", true);
    }

    @Override
    protected int getTotalCount() {
        int result = 0;
        try {
            long count = sessionManagement.getDAO(DonorDAOImpl.class, DONOR_DAO).countDocument(filter, false);
            return new Long(count).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected List<Donor> loadDocuments() {
        List<Donor> result = new ArrayList<>();
        try {
            result = sessionManagement.getDAO(DonorDAOImpl.class, DONOR_DAO).findDocuments(filter,
					false, getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(),
					getSorting().isAsc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Donor> getDocuments() {
        return super.getDocuments();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Donor> getDocumentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String
			bloodGroup, boolean searchRhesus, String rhesusFactor) {
        List<Donor> result = new ArrayList<>();
        try {
            if (phenotypes != null && !phenotypes.isEmpty()) {
                result = sessionManagement.getDAO(DonorDAOImpl.class, DONOR_DAO)
						.findComponentsByPhenotypes(phenotypes, searchBloodGroup, bloodGroup, searchRhesus,
                                rhesusFactor, "lastName,firstName,middleName", true);
            } else {
                result = sessionManagement.getDAO(DonorDAOImpl.class, DONOR_DAO)
						.findDonorsByBloodGroupAndRh(
                                searchBloodGroup, bloodGroup, searchRhesus, rhesusFactor, "lastName,firstName,middleName", true
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } return result;
    }


    private String filter;

    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement = new SessionManagementBean();


    private static final long serialVersionUID = 6546450615334686914L;
}