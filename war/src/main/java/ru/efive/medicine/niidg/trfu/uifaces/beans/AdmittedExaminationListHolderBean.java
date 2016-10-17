package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("admittedExaminationList")
@SessionScoped
public class AdmittedExaminationListHolderBean extends AbstractDocumentListHolderBean<ExaminationRequest> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("number", false);
    }

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			return new Long(sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).countDocumentByStatus(5,
					filterNumberExamination, filterDonorLastName, filterDonorFirstName, filterDonorMiddleName, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<ExaminationRequest> loadDocuments() {
		List<ExaminationRequest> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).findDocumentsByStatus(5,
					filterNumberExamination, filterDonorLastName, filterDonorFirstName, filterDonorMiddleName, false,
				getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ExaminationRequest> getDocuments() {
		return super.getDocuments();
	}
	
	public String getFilterNumberExamination() {
		return filterNumberExamination;
	}

	public void setFilterNumberExamination(String filterNumberExamination) {
		this.filterNumberExamination = filterNumberExamination;
	}

	public String getFilterDonorLastName() {
		return filterDonorLastName;
	}

	public void setFilterDonorLastName(String filterDonorLastName) {
		this.filterDonorLastName = filterDonorLastName;
	}

	public String getFilterDonorFirstName() {
		return filterDonorFirstName;
	}

	public void setFilterDonorFirstName(String filterDonorFirstName) {
		this.filterDonorFirstName = filterDonorFirstName;
	}

	public String getFilterDonorMiddleName() {
		return filterDonorMiddleName;
	}

	public void setFilterDonorMiddleName(String filterDonorMiddleName) {
		this.filterDonorMiddleName = filterDonorMiddleName;
	}

	private String filterNumberExamination;
	private String filterDonorLastName;
	private String filterDonorFirstName;
	private String filterDonorMiddleName;
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 6546450615334686914L;
}