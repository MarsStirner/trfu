package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named("donorByBloodGroupList")
@SessionScoped
public class DonorByBloodGroupListHolderBean extends AbstractDocumentListHolderBean<Donor> {

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").countDocument(filter, false);
			return new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<Donor> loadDocuments() {
		List<Donor> result = new ArrayList<>();
		try {
			List<Donor> list = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").findDocuments(filter, false,
				getPagination().getOffset(), getPagination().getPageSize(), null, true);
			Collections.sort(list, new Comparator<Donor>() {
				@Override
				public int compare(Donor o1, Donor o2) {
					int result = o1.getBloodGroup().getId() - o2.getBloodGroup().getId();
					return result == 0? o1.getRhesusFactor().getId() - o2.getRhesusFactor().getId(): result;
				}
			});
	        BloodGroup bloodGroup = null;
	        Classifier rhesus = null;
	        for (Donor donor:list) {
	        	if (!donor.getBloodGroup().equals(bloodGroup)) {
	        		bloodGroup = donor.getBloodGroup();
	        		rhesus = null;
	            	Donor group = new Donor();
	            	group.setId(0);
	            	group.setGrouping(0);
	            	group.setBloodGroup(bloodGroup);
	            	result.add(group);
	            }
	        	if (!donor.getRhesusFactor().equals(rhesus)) {
	        		rhesus = donor.getRhesusFactor();
	        		Donor group = new Donor();
	            	group.setId(0);
	            	group.setGrouping(1);
	            	group.setBloodGroup(bloodGroup);
	            	group.setRhesusFactor(rhesus);
	            	result.add(group);
	        	}
	        	result.add(donor);
	        }
		}
		catch (Exception e) {
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
	

	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private String filter;
	
	private static final long serialVersionUID = 6546450615334686914L;
}