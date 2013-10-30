package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.RequestFilter;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.SRPDDao;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;

public class RequestDAOImpl extends GenericDAOHibernate<AbstractRequest> {
	private static SRPDDao srpdDAO;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDAO = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked") @Override
	public List<AbstractRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		RequestFilter filter = createRequestFilter(showDeleted);
		List<AbstractRequest> result = findDocuments(filter, ExaminationRequest.class, offset, count, orderBy, orderAsc);
		result.addAll(findDocuments(filter, BloodDonationRequest.class, offset, count, orderBy, orderAsc));
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countDocument(boolean showDeleted) {
		RequestFilter filter = createRequestFilter(showDeleted);
		long result = countDocuments(filter, ExaminationRequest.class);
    	result += countDocuments(filter, BloodDonationRequest.class);
        
		return result;
	}
	
	/**
	 * Поиск обращений
	 * @param donorId - идентификатор донора
	 * @return - список обращений
	 */
	@SuppressWarnings("unchecked")
	public List<AbstractRequest> findRequestsByDonor(boolean showDeleted, int donorId, String orderBy, boolean orderAsc) {
		RequestFilter filter = createRequestFilter(showDeleted);
		if (donorId > 0) {
			filter.getListFields().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
        }
		List<AbstractRequest> result = findDocuments(filter, ExaminationRequest.class, -1, -1, orderBy, orderAsc);
		result.addAll(findDocuments(filter, BloodDonationRequest.class, -1, -1, orderBy, orderAsc));
		
		return result;
	}

	/**
	 * Количество обращений по донору
	 * @param donorId - идентификатор донора
	 * @return - количество
	 */
	public long countRequestsByDonor(boolean showDeleted, int donorId) {
		RequestFilter filter = createRequestFilter(showDeleted);
		if (donorId > 0) {
			filter.getListFields().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
        }
		long result = countDocuments(filter, ExaminationRequest.class);
		result += countDocuments(filter, BloodDonationRequest.class);
		
		return result;
	}
	
	private long countDocuments(RequestFilter filter, Class persistenceClass) {
		DetachedCriteria criteria = createDetachedCriteria(persistenceClass);
		return getCountOf(criteria);
	}
	private List<AbstractRequest> findDocuments(RequestFilter filter, Class persistenceClass, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria criteria = createDetachedCriteria(persistenceClass);
		addOrder(criteria, orderBy, orderAsc);
		List<AbstractRequest> examinationRequests = getHibernateTemplate().findByCriteria(criteria, offset, count);
		if (DonorHelper.USE_SRPD) {
			filter.setListSRPDIds(donorHelper.listIdsSRPDFromRequest(examinationRequests));
			Map<FieldsInMap, Object> paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			Map<String, Map<FieldsInMap, Object>> resMap = srpdDAO.get(paramMap);
			examinationRequests = donorHelper.mergeRequestsAndMap(examinationRequests, resMap);
		}
		return examinationRequests;
	}
	private DetachedCriteria getSearchCriteria(RequestFilter filter, DetachedCriteria criteria) {
		for(FieldFilterBean i: filter.getListFields()) {
			switch (i.getCompareType()) {
			case EQ:
				criteria.add(Restrictions.eq(i.getFieldName(), i.getValue()));
				break;
			}
		}
		if (!filter.isShowDeleted()){
			criteria.add(Restrictions.eq("deleted", false));
		}
		return criteria;
	}
	private RequestFilter createRequestFilter(Boolean showDeleted) {
		RequestFilter filter = new RequestFilter();
		if (showDeleted != null) {
			filter.setShowDeleted(showDeleted);
		}
		return filter;
	}
	
}