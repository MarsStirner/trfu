package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class DonorDAOImpl extends GenericDAOHibernate<Donor> {
	private static SRPDDao srpdDao;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}
	
	@Override
	protected Class<Donor> getPersistentClass() {
		return Donor.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Donor> findDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	
	public long countDocument(String pattern, boolean showDeleted) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
		return countDocuments(filter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Donor> findRejectedDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
        List<FieldFilterBean> fields = new ArrayList<FieldFilterBean>();
		fields.add(FieldFilterBean.init("statusId", -1));
		fields.add(FieldFilterBean.init("statusId", -2));
		filter.getListFieldsDisjunction().add(fields);
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	
	public long countRejectedDocument(String pattern, boolean showDeleted) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
		List<FieldFilterBean> fields = new ArrayList<FieldFilterBean>();
		fields.add(FieldFilterBean.init("statusId", -1));
		fields.add(FieldFilterBean.init("statusId", -2));
		filter.getListFieldsDisjunction().add(fields);
		return countDocuments(filter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Donor> findAvailableDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
        String query = "select req.donor_id from trfu_blood_donation_requests req inner join"
        	+ "(select donation.id, donation.donor_id, min(idle.days) "
        	+ "from (select * from trfu_blood_donation_requests group by donor_id desc) donation "
        	+ "inner join trfu_donors donor on donation.donor_id = donor.id "
        	+ "left outer join trfu_blood_donation_request_fact_entries fact on donation.id = fact.donation_id "
        	+ "left outer join trfu_blood_donation_entries entries on fact.entry_id = entries.id "
        	+ "left outer join trfu_blood_donation_types types on entries.donationType_id = types.id "
        	+ "left outer join trfu_idle_periods idle on entries.donationType_id = idle.donationType_id "
        	+ "where (donation.status_id = 4 or donation.id is null) and donor.status_id <> -2 "
        	+ "and TIMESTAMPDIFF(DAY,date(donation.factDate),curdate()) > idle.days "
        	+ "group by donation.donor_id) tmp on req.id = tmp.id";
        List list = getSession().createSQLQuery(query).list();
        
        query = "select donor.id from trfu_donors donor "
        	+ "left outer join trfu_blood_donation_requests donation on donation.donor_id = donor.id "
        	+ "left outer join trfu_examination_requests exam on exam.donor_id = donor.id "
        	+ "where donation.id is null and donor.status_id <> -2 and (exam.status_id <> -1 or exam.id is null)";
        list.addAll(getSession().createSQLQuery(query).list());
        
        filter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
        return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	
	public long countAvailableDocument(String pattern, boolean showDeleted) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
        
        String query = "select req.donor_id from trfu_blood_donation_requests req inner join"
        	+ "(select donation.id, donation.donor_id, min(idle.days) "
        	+ "from (select * from trfu_blood_donation_requests group by donor_id desc) donation "
        	+ "inner join trfu_donors donor on donation.donor_id = donor.id "
        	+ "left outer join trfu_blood_donation_request_fact_entries fact on donation.id = fact.donation_id "
        	+ "left outer join trfu_blood_donation_entries entries on fact.entry_id = entries.id "
        	+ "left outer join trfu_blood_donation_types types on entries.donationType_id = types.id "
        	+ "left outer join trfu_idle_periods idle on entries.donationType_id = idle.donationType_id "
        	+ "where (donation.status_id = 4 or donation.id is null) and donor.status_id <> -2 "
        	+ "and TIMESTAMPDIFF(DAY,date(donation.factDate),curdate()) > idle.days "
        	+ "group by donation.donor_id) tmp on req.id = tmp.id";
        List list = getSession().createSQLQuery(query).list();
        
        query = "select donor.id from trfu_donors donor "
        	+ "left outer join trfu_blood_donation_requests donation on donation.donor_id = donor.id "
        	+ "left outer join trfu_examination_requests exam on exam.donor_id = donor.id "
        	+ "where donation.id is null and donor.status_id <> -2 and (exam.status_id <> -1 or exam.id is null)";
        list.addAll(getSession().createSQLQuery(query).list());
        filter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
        return countDocuments(filter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Donor> findQuarantineFinishInviteDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
        
        String query = "select donor.id from trfu_donors donor "
        	+ "inner join trfu_blood_donation_requests requests on requests.donor_id = donor.id "
        	+ "inner join trfu_blood_components components on components.donationId = requests.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate()";
        List list = getSession().createSQLQuery(query).list();
        
        filter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
        return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	
	public long countQuarantineFinishInviteDocument(String pattern, boolean showDeleted) {
		DonorsFilter filter = createDonorsFilter(pattern, showDeleted);
        String query = "select donor.id from trfu_donors donor "
        	+ "inner join trfu_blood_donation_requests requests on requests.donor_id = donor.id "
        	+ "inner join trfu_blood_components components on components.donationId = requests.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate()";
        List list = getSession().createSQLQuery(query).list();
        filter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
        return countDocuments(filter);
        
	}
	
	
	/**
	 * Подбор доноров по фенотипам
	 * @return - список доноров
	 */
	@SuppressWarnings("unchecked")
	public List<Donor> findComponentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor, String orderBy, boolean orderAsc) {
		DonorsFilter filter = createDonorsFilter(null, false);
        StringBuilder concat = new StringBuilder();
        StringBuilder testsSubQuery = new StringBuilder();
        for (Analysis phenotype: phenotypes) {
        	if (!testsSubQuery.toString().equals("")) {
        		testsSubQuery.append(" or ");
        	}
        	testsSubQuery.append("binary value = '" + phenotype.getType().getValue() + "'");
        	concat.append(phenotype.getValue());
        }
        
        String query = "select donors.id FROM trfu_blood_donation_request_tests_immuno immuno "
        	+ "inner join trfu_tests tests ON tests.id = immuno.testsImmuno_id "
        	+ "inner join trfu_blood_donation_requests donations on immuno.trfu_blood_donation_requests_id = donations.id "
        	+ "inner join trfu_donors donors on donations.donor_id = donors.id "
        	+ (searchBloodGroup? "inner join trfu_blood_groups groups on donors.bloodGroup_id = groups.id ": "")
        	+ (searchRhesus? "inner join trfu_classifiers classifiers on donors.rhesusFactor_id = classifiers.id ": "")
        	+ "where (donors.status_id = 1 or donors.status_id = 2) "
        	+ (searchBloodGroup? "and groups.value = '" + bloodGroup + "' ": "")
        	+ (searchRhesus? "and classifiers.value = '" + rhesusFactor + "' ": "")
        	+ "and tests.type_id in (select id from trfu_analysis_types where " + testsSubQuery + ") "
        	+ "group by donors.id "
        	+ "having group_concat(concat('',tests.value) order by tests.id separator '')='" + concat + "'";
        
        List list = getSession().createSQLQuery(query).list();
        
        if (list != null && list.size() > 0) {
        	filter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
            return findDocument(filter, -1, -1, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	
	/**
     * Возвращает донора по логину и паролю. 
     *
     * @param login    логин (адрес электронной почты)
     * @param password пароль
     * @return донор или null, если такового не существует
     */
    @SuppressWarnings("unchecked")
	public Donor findByLoginAndPassword(String login, String password) throws JDBCConnectionException, DataAccessException {
        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(password)) {
            DonorsFilter filter = createDonorsFilter(null, null);
            filter.setMail(login);
            filter.getListFields().add(FieldFilterBean.init("password", password));
            List<Donor> donors = findDocument(filter, -1, -1, null, false);
            if ((donors != null) && !donors.isEmpty()) {
                // don't modify this routine work! (for proxy caching such objects as Personage, Location)
                Donor donor = donors.get(0);
                if (donor != null) {
                    // todo caching proxy objects
                }
                return donor;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
	
    @SuppressWarnings("unchecked")
	public List<String> findDonorsForNewsletter() {
    	List<String> mails = null;
    	if (!DonorHelper.USE_SRPD) {
    		String query = "select mail FROM trfu_donors where deleted = false and send_news = true and mail <> '' and mail is not null";
    		mails = getSession().createSQLQuery(query).list();
    	} else {
    		String query = "select temp_storage_id FROM trfu_donors where deleted = false and send_news = true";
    		List<String> listSRPDIds = getSession().createSQLQuery(query).list();
    		Map<FieldsInMap, Object> paramMap = new HashMap<FieldsInMap, Object>();
    		paramMap.put(FieldsInMap.LIST_STORAGE_IDS, listSRPDIds);
    		Map<String,Map<FieldsInMap,Object>> resMap = srpdDao.get(paramMap);
    		mails = donorHelper.getMailsFromMap(resMap);
    	}
    	return mails;
	}
    
	public long countDocument(DonorsFilter donorsFilter) {
		donorsFilter.setShowDeleted(false);
		return countDocuments(donorsFilter);
	}

	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria,
			DonorsFilter donorsFilter) {
		if (donorsFilter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = donorsFilter.getNumber();
			String lastName = donorsFilter.getLastName();
			String firstName = donorsFilter.getFirstName();
			String middleName = donorsFilter.getMiddleName();
			Date created = donorsFilter.getCreated();
			Date birth = donorsFilter.getBirth();
			int genderId = donorsFilter.getGenderId();
			int documentTypeId = donorsFilter.getDocumentTypeId();
			String documentNumber = donorsFilter.getDocumentNumber();
			String documentSeries = donorsFilter.getDocumentSeries();
			String externalNumber = donorsFilter.getExternalNumber();
			String factAddress = donorsFilter.getFactAddress();
			int statusId = donorsFilter.getStatusId();
			int bloodGroupId = donorsFilter.getBloodGroupId();
			int rhesusFactorId = donorsFilter.getRhesusFactorId();
			int pastQuarantineId = donorsFilter.getPastQuarantineId();
			int categoryId = donorsFilter.getCategoryId();
			
			for (AliasFilterBean i: donorsFilter.getListAlias()) {
				criteria.createAlias(i.getAssociationPath(), i.getAlias(), i.getJoinType());
			}
			for (FieldFilterBean i: donorsFilter.getListFields()) {
				criteria.add(makeRestriction(i));
				
			}
			for (List<FieldFilterBean> j : donorsFilter.getListFieldsDisjunction()) {
				Disjunction disj  = Restrictions.disjunction();
				for(FieldFilterBean i: j) {
					disj.add(makeRestriction(i));
				}
				criteria.add(disj);
			}
			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(lastName)) {
				conjunction.add(Restrictions.ilike("lastName", lastName, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(firstName)) {
				conjunction.add(Restrictions.ilike("firstName", firstName, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(middleName)) {
				conjunction.add(Restrictions.ilike("middleName", middleName, MatchMode.ANYWHERE));
			}
			if (created != null) {
				addDateSearchCriteria(conjunction, created, "created");
			}
			if (birth != null) {
				addDateSearchCriteria(conjunction, birth, "birth");
			}
	        if (genderId != DonorsFilter.GENDER_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("gender", genderId));
	        }
	        if (documentTypeId != DonorsFilter.DOCUMENT_TYPE_NULL_VALUE) {
		        if (StringUtils.isNotEmpty(documentNumber)) {
		        	if (documentTypeId == DonorsFilter.PASSPORT_DOCUMENT_TYPE) {
	    				conjunction.add(Restrictions.ilike("passportNumber", documentNumber, MatchMode.ANYWHERE));
		        	} else {
	    				conjunction.add(Restrictions.ilike("insuranceNumber", documentNumber, MatchMode.ANYWHERE));
		        	}
		        }
		        if (StringUtils.isNotEmpty(documentSeries)) {
		        	if (documentTypeId == DonorsFilter.PASSPORT_DOCUMENT_TYPE) {
	    				conjunction.add(Restrictions.ilike("passportSeries", documentSeries, MatchMode.ANYWHERE));
		        	} else {
	    				conjunction.add(Restrictions.ilike("insuranceSeries", documentSeries, MatchMode.ANYWHERE));
		        	}
		        }
	        }
			if (StringUtils.isNotEmpty(externalNumber)) {
				conjunction.add(Restrictions.ilike("externalNumber", externalNumber, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(factAddress)) {
				conjunction.add(Restrictions.ilike("factAddress", factAddress, MatchMode.ANYWHERE));
			}
	        if (categoryId != DonorsFilter.DONOR_CATEGORY_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("category", categoryId));
	        }
	        if (statusId != DonorsFilter.DONOR_STATUS_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("statusId", statusId));
	        }
	        if (bloodGroupId != DonorsFilter.BLOOD_GROUP_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("bloodGroup.id", bloodGroupId));
	        }
	        if (rhesusFactorId != DonorsFilter.RHESUS_FACTOR_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("rhesusFactor.id", rhesusFactorId));
	        }
	        if (pastQuarantineId != DonorsFilter.PAST_QUARANTINE_NULL_VALUE) {
		        criteria.createAlias("rejection", "rejection", CriteriaSpecification.INNER_JOIN);
	        	if (pastQuarantineId == DonorsFilter.PAST_QUARANTINE_YES_VALUE) {
		        	conjunction.add(Restrictions.le("rejection.expiration", new Date()));
	        	} else {
	    			Disjunction disjunction = Restrictions.disjunction();
	    			disjunction.add(Restrictions.ge("rejection.expiration", new Date()));
	    			disjunction.add(Restrictions.isNull("rejection.expiration"));
	    			conjunction.add(disjunction);
	        	}
	        }
			criteria.add(conjunction);
		}
        return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Donor> findDocuments(DonorsFilter donorsFilter, int offset,
			int count, String orderBy, boolean orderAsc) {
		donorsFilter.setShowDeleted(false);
		return findDocument(donorsFilter, offset, count, orderBy, orderAsc);
	}
	/* (non-Javadoc)
	 * @see ru.efive.dao.sql.dao.GenericDAOHibernate#get(java.io.Serializable)
	 */
	@Override
	public Donor get(Serializable id) {
		Donor donor = super.get(id);
		if (DonorHelper.USE_SRPD) {
			Map<String,Map<DonorHelper.FieldsInMap, Object>> map = new SRPDDao().get(new DonorHelper().makeMapForGet(donor.getTempStorageId()));
			if (map != null && map.size() > 0) {
				donor = donorHelper.mergeDonorAndMap(donor, (Map<FieldsInMap, Object>)map.values().toArray()[0]);
			} else {
				return null;
			}
		}
		return donor;
	}

	/* (non-Javadoc)
	 * @see ru.efive.dao.sql.dao.GenericDAOHibernate#save(ru.efive.dao.sql.entity.AbstractEntity)
	 */
	@Override
	public Donor save(Donor entity) {
		if (DonorHelper.USE_SRPD) {
			Session session = null;
			DonorHelper helper = new DonorHelper();
			try {
				session = getSession();
				session.beginTransaction();
				session.save(entity);
				Map<DonorHelper.FieldsInMap, Object> query = helper.makeMapFromDonor(entity);
				/* commit for saving information to TRFU, without temp_stogate_id */
				Map<DonorHelper.FieldsInMap, Object> answer = srpdDao.addPDToSRPD(query);
				entity = helper.mergeDonorAndMap(entity, answer);
				/* update for writing temp_stogate_id to DB of TRFU */
				session.update(entity);
				session.getTransaction().commit();
				return entity;
			} catch(Exception e) {
				e.printStackTrace();
				session.getTransaction().rollback();
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return null;
		} else {
			return super.save(entity);
		}
	}

	/* (non-Javadoc)
	 * @see ru.efive.dao.sql.dao.GenericDAOHibernate#update(ru.efive.dao.sql.entity.AbstractEntity)
	 */
	@Override
	public Donor update(Donor entity) {
		if (DonorHelper.USE_SRPD) {
			Session session = null;
			SRPDDao srpdDao = new SRPDDao();
			DonorHelper helper = new DonorHelper();
			try {
				session = getSession();
				session.beginTransaction();
				session.update(entity);
				srpdDao.updateToSRPD(helper.makeMapFromDonor(entity));
				session.getTransaction().commit();
				return entity;
			} catch(Exception e) {
				session.getTransaction().rollback();
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return null;
		} else {
			return super.update(entity);
		}
	}
	private DonorsFilter createDonorsFilter(String pattern, Boolean showDeleted) {
		DonorsFilter filter = new DonorsFilter();
		if (StringUtils.isNotEmpty(pattern)) { 
			List<FieldFilterBean> fields = new ArrayList<FieldFilterBean>();
			fields.add(FieldFilterBean.init("number", filter, CompareType.ILIKE));
			fields.add(FieldFilterBean.init("externalNumber", filter, CompareType.ILIKE));
			fields.add(FieldFilterBean.init("donorType.value", filter, CompareType.ILIKE));
			
			filter.getListAlias().add(AliasFilterBean.initAlias("donorType", CriteriaSpecification.LEFT_JOIN));
			if(!DonorHelper.USE_SRPD) {
				fields.add(FieldFilterBean.init("passportSeries", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("passportNumber", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("insuranceSeries", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("insuranceNumber", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("lastName", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("firstName", filter, CompareType.ILIKE));
				fields.add(FieldFilterBean.init("middleName", filter, CompareType.ILIKE));
				
			} else {
				filter.setLastName(pattern);
				filter.setFirstName(pattern);
				filter.setMiddleName(pattern);
				filter.setPassport(pattern);
				filter.setInsuranceNumber(pattern);
				filter.setInsuranceSeries(pattern);
			}
			filter.getListFieldsDisjunction().add(fields);
		}
		if (showDeleted != null && !showDeleted) {
			filter.getListFields().add(FieldFilterBean.init("deleted", showDeleted));
		}
		return filter;
	}
	private List<Donor> findDocument(DonorsFilter donorsFilter, int offset,
			int count, String orderBy, boolean orderAsc) {
		List<Donor> donors = null;
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		donors = getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, donorsFilter), offset, count);
		if (DonorHelper.USE_SRPD) {
			donorsFilter.setListSRPDIds(donorHelper.listIdsSRPDFromDonors(donors));
			Map<FieldsInMap, Object> map = donorHelper.listIdsSRPDFromFilter(donorsFilter);
			Map<String,Map<FieldsInMap, Object>> resMap = srpdDao.get(map);
			donors = donorHelper.mergeDonorsAndMap(donors, resMap);
		}
		return donors;
	}
	
	private long countDocuments(DonorsFilter donorsFilter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		return getCountOf(getSearchCriteria(detachedCriteria, donorsFilter));
	}
}