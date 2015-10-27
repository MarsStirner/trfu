package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.DonorFullTextEntry;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("donorSearch")
@SessionScoped
public class DonorSearchBean extends AbstractDocumentListHolderBean<DonorFullTextEntry> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected int getTotalCount() {
		return 0;//(filter == null || filter.equals(""))? 0: sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").countSearch(filter);
	}

	@Override
	protected List<DonorFullTextEntry> loadDocuments() {
		List<DonorFullTextEntry> result = new ArrayList<DonorFullTextEntry>();
		try {
			if (filter != null && !filter.equals("")) {
				DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao");
				/*List<Donor> list = dao.search(filter, getPagination().getOffset(), getPagination().getPageSize());
				for (Donor donor: list) {
					result.add(new DonorFullTextEntry(donor.getId(), dao.getHighlightedText(donor.getId())));
				}*/
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<DonorFullTextEntry> getDocuments() {
		return super.getDocuments();
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	

	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private String filter;
	
	private static final long serialVersionUID = 6546450615334686914L;
}