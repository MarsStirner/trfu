package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.BloodDonationsFilter;
import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class BloodDonationRequestDAOImpl extends GenericDAOHibernate<BloodDonationRequest> {
	private static SRPDDao srpdDao;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}
	@Override
	protected Class<BloodDonationRequest> getPersistentClass() {
		return BloodDonationRequest.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BloodDonationRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		return findDocuments(createBloodDonationsFilter(null, showDeleted), offset, count, orderBy, orderAsc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countDocument(boolean showDeleted) {
		return countDocument(createBloodDonationsFilter(null, showDeleted));
	}
	
	/**
     * Поиск документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		return findDocuments(createBloodDonationsFilter(filter, showDeleted), offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(String filter, boolean showDeleted) {
		return countDocument(createBloodDonationsFilter(filter, showDeleted));
	}
	
	/**
	 * Поиск обращений на донацию
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param donorId - идентификатор донора
	 * @return - список обращений на донацию
	 */
	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDonationRequestsByDonorId(boolean showDeleted, int donorId, int offset, int count, String orderBy, boolean orderAsc) {
		if (donorId > 0) {
        	BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(null, showDeleted);
        	bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
    		return findDocument(bloodDonationsFilter, offset, count, orderBy, orderAsc);	
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	/**
	 * Количество обращений на донацию
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countDonationRequestsByDonorId(boolean showDeleted, int donorId) {
		if (donorId > 0) {
	    	BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(null, showDeleted);
    		bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
    		return countDocument(bloodDonationsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	/**
	 * Поиск обращений на донацию
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param personId - идентификатор пользователя
	 * @return - список обращений на донацию
	 */
	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDocumentsByPerson(boolean showDeleted, int personId, int offset, int count, String orderBy, boolean orderAsc) {
		if (personId > 0) {
        	BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(null, showDeleted);
        	bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("transfusiologist.id", personId, CompareType.EQ));
        	return findDocument(bloodDonationsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.EMPTY_LIST;
        }
	}
	
	/**
	 * Количество обращений на донацию
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param personId - идентификатор пользователя
	 * @return - количество
	 */
	public long countDocumentsByPerson(boolean showDeleted, int personId) {
        if (personId > 0) {
        	BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(null, showDeleted);
        	bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("transfusiologist.id", personId, CompareType.EQ));
        	return countDocuments(bloodDonationsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	/**
     * Поиск документов
     *
     * @param statusId        идентификатор статуса
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDocumentsByStatus(int statusId, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(filter, showDeleted);
		
		if (statusId != 0) {
        	bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("statusId", statusId, CompareType.EQ));
        }
		return findDocument(bloodDonationsFilter, offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param statusId        идентификатор статуса
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocumentByStatus(int statusId, String filter, boolean showDeleted) {
		BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(filter, showDeleted);
        if (statusId != 0) {
        	bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("statusId", statusId, CompareType.EQ));
        }
        return countDocuments(bloodDonationsFilter);
	}

    public BloodDonationRequest findDocumentByBarCode(String barCode){
       BloodDonationsFilter bloodDonationsFilter = createBloodDonationsFilter(null, false);
       bloodDonationsFilter.getListFilds().add(FieldFilterBean.init("number", barCode, CompareType.EQ));
    	List list = findDocument(bloodDonationsFilter, -1, -1, null, false);
        if (list!=null && list.size()>0)
            return (BloodDonationRequest) list.get(0);
        return null;
    }

	public long countDocument(BloodDonationsFilter filter) {
		if (StringUtils.isNotEmpty(filter.getFirstName()) ||
				StringUtils.isNotEmpty(filter.getLastName()) ||
				StringUtils.isNotEmpty(filter.getMiddleName())) {
			filter.getListAlias().add(AliasFilterBean.initAlias("donor", CriteriaSpecification.INNER_JOIN));
		}
		if (filter.getDonationTypeId() > 0) {
			filter.getListAlias().add(AliasFilterBean.initAlias("factEntries", CriteriaSpecification.INNER_JOIN));
		}
		return countDocuments(filter);
	}

	protected DetachedCriteria getSearchCriteria(DetachedCriteria criteria,BloodDonationsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			String firstName = filter.getFirstName();
			String lastName = filter.getLastName();
			String middleName = filter.getMiddleName();
			Date created = filter.getCreated();
			int statusId = filter.getStatusId();
			int donorTypeId = filter.getDonorTypeId();
			int donationTypeId = filter.getDonationTypeId();
			
			for(AliasFilterBean i: filter.getListAlias()) {
				criteria.createAlias(i.getAssociationPath(), i.getAlias(), i.getJoinType());
			}
			for (FieldFilterBean i: filter.getListFilds()) {
				switch (i.getCompareType()) {
				case EQ:
					conjunction.add(Restrictions.eq(i.getFieldName(), i.getValue()));
					break;
				}
			}
			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number,MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(firstName)
					|| StringUtils.isNotEmpty(lastName)
					|| StringUtils.isNotEmpty(middleName)
					|| filter.getListFildsDisjunction().size() > 0) {
	            Disjunction disjunction = Restrictions.disjunction();
	            for(FieldFilterBean i: filter.getListFildsDisjunction()) {
	            	switch (i.getCompareType()) {
					case ILIKE:
						disjunction.add(Restrictions.ilike(i.getFieldName(), i.getValue().toString(), MatchMode.ANYWHERE));
						break;
					}
	            }
	            if (!DonorHelper.USE_SRPD) {
	            	disjunction.add(Restrictions.ilike("donor.lastName", filter.getLastName(), MatchMode.ANYWHERE));
	            	disjunction.add(Restrictions.ilike("donor.middleName", filter.getMiddleName(), MatchMode.ANYWHERE));
	            	disjunction.add(Restrictions.ilike("donor.firstName", filter.getFirstName(), MatchMode.ANYWHERE));
	            }
	            conjunction.add(disjunction);
			}
			if (created != null) {
				addDateSearchCriteria(conjunction, created, "created");
			}
	        if (statusId != BloodDonationsFilter.BLOOD_DONATION_STATUS_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("statusId", statusId));
	        }
	        if (donorTypeId != BloodDonationsFilter.DONOR_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("donorType.id", donorTypeId));
	        }
	        if (donationTypeId != BloodDonationsFilter.BLOOD_DONATION_TYPE_NULL_VALUE) {
		        conjunction.add(Restrictions.eq("factEntries.donationType.id", donationTypeId));
	        }
	       	criteria.add(conjunction);
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDocuments(BloodDonationsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
		if (StringUtils.isNotEmpty(filter.getFirstName()) ||
				StringUtils.isNotEmpty(filter.getLastName()) ||
				StringUtils.isNotEmpty(filter.getMiddleName())) {
			filter.getListAlias().add(AliasFilterBean.initAlias("donor", CriteriaSpecification.INNER_JOIN));
		}
		if (filter.getDonationTypeId() > 0) {
			filter.getListAlias().add(AliasFilterBean.initAlias("factEntries", CriteriaSpecification.INNER_JOIN));
		}
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	private List<BloodDonationRequest> findDocument(BloodDonationsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		List<BloodDonationRequest> list = getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count); 
		if (DonorHelper.USE_SRPD) {
			filter.setListSRPDIds(donorHelper.listIdsSRPDFromBloodDonationRequest(list));
			Map<FieldsInMap, Object> paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
			list = donorHelper.mergeBloodDonationRequestsAndMap(list, resMap);
		}
		return list;
	}
	
	private long countDocuments(BloodDonationsFilter filter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	private BloodDonationsFilter createBloodDonationsFilter(String pattern, Boolean showDeleted) {
		BloodDonationsFilter filter = new BloodDonationsFilter();
		if (StringUtils.isNotEmpty(pattern)) {
			filter.setLastName(pattern);
			filter.setFirstName(pattern);
			filter.setMiddleName(pattern);
		
			filter.getListFildsDisjunction().add(FieldFilterBean.init("number", pattern, CompareType.ILIKE));
			filter.getListFildsDisjunction().add(FieldFilterBean.init("donorType.value", pattern, CompareType.ILIKE));
			filter.getListFildsDisjunction().add(FieldFilterBean.init("donationType.value", pattern, CompareType.ILIKE));
			
			filter.getListAlias().add(AliasFilterBean.initAlias("donorType", CriteriaSpecification.LEFT_JOIN));
			filter.getListAlias().add(AliasFilterBean.initAlias("donor",  CriteriaSpecification.LEFT_JOIN));
			filter.getListAlias().add(AliasFilterBean.initAlias("factEntries", CriteriaSpecification.LEFT_JOIN));
			filter.getListAlias().add(AliasFilterBean.initAlias("factEntries", CriteriaSpecification.LEFT_JOIN));
			filter.getListAlias().add(AliasFilterBean.initAlias("factEntries.donationType", "donationType", CriteriaSpecification.LEFT_JOIN));
		}
		if (showDeleted != null && !showDeleted) {
			filter.getListFildsDisjunction().add(FieldFilterBean.init("deleted", false, CompareType.EQ));
		}
       return filter;
	}

	@Override
	public BloodDonationRequest get(Serializable id) {
		BloodDonationRequest bloodDonationRequest = super.get(id);
		if (DonorHelper.USE_SRPD) {
			Map<FieldsInMap, Object> paramMap = donorHelper.makeMapForGet(bloodDonationRequest.getDonor().getTempStorageId());
			Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
			bloodDonationRequest = donorHelper.mergeBloodDonationRequestAndMap(bloodDonationRequest, resMap);
			
		}
		return bloodDonationRequest;
	}
}