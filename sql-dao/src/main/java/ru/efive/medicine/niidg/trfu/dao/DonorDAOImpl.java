package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class DonorDAOImpl extends GenericDAOHibernate<Donor> {
	private static SRPDDao srpdDao;
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
		}
	}
	
	@Override
	protected Class<Donor> getPersistentClass() {
		return Donor.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Donor> findDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		return findDocuments(createDonorsFilterFromPattern(pattern, showDeleted, false), offset, count, orderBy, orderAsc);
	}
	
	public long countDocument(String pattern, boolean showDeleted) {        
        return countDocument(createDonorsFilterFromPattern(pattern, showDeleted, false));
	}
	private DonorsFilter createDonorsFilterFromPattern(String pattern, boolean showDeleted, boolean isConjunction) {
		if (StringUtils.isNotEmpty(pattern)) {
			DonorsFilter donorsFilter = new DonorsFilter();
			donorsFilter.setNumber(pattern);
			donorsFilter.setExternalNumber(pattern);
			donorsFilter.setLastName(pattern);
			donorsFilter.setMiddleName(pattern);
			donorsFilter.setFirstName(pattern);
			donorsFilter.setDonorTypeValue(pattern);
			donorsFilter.setDocumentTypeId(DonorsFilter.PASSPORT_DOCUMENT_TYPE);
			donorsFilter.setDocumentSeries(pattern);//------------------------- for Passport
			donorsFilter.setDocumentNumber(pattern);
			donorsFilter.setInsuranceSeries(pattern);//------------------------- for OMC
			donorsFilter.setInsuranceNumber(pattern);
			donorsFilter.setShowDeleted(showDeleted);   
			donorsFilter.setConjunction(isConjunction);
			return donorsFilter;
		}
		return null;
	}
		
	public List<Donor> findRejectedDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
	    DonorsFilter donorsFilter = createRejectedDonorsFilter(pattern,	showDeleted);
        return findDocuments(donorsFilter, offset, count, orderBy, orderAsc);
	}
	public long countRejectedDocument(String pattern, boolean showDeleted) {
        DonorsFilter donorsFilter = createRejectedDonorsFilter(pattern, showDeleted);
        
        return countDocument(donorsFilter);
	}
	
	private DonorsFilter createRejectedDonorsFilter(String pattern,	boolean showDeleted) {
		DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.getListStatusId().add(-1);
        donorsFilter.getListStatusId().add(-2);
		return donorsFilter;
	}
	
	@SuppressWarnings("unchecked")
	public List<Donor> findAvailableDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
		
		List list = createListIdsforFindAvaliableDocuments();
        donorsFilter.setListIds(list);
        
        return findDocuments(donorsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countAvailableDocument(String pattern, boolean showDeleted) {
		DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
		
		List list = createListIdsforFindAvaliableDocuments();
        donorsFilter.setListIds(list);
      
        return countDocument(donorsFilter);
	}
	private List createListIdsforFindAvaliableDocuments() {
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
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Donor> findQuarantineFinishInviteDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		String query = "select donor.id from trfu_donors donor "
        	+ "inner join trfu_blood_donation_requests requests on requests.donor_id = donor.id "
        	+ "inner join trfu_blood_components components on components.donationId = requests.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate()";
        List list = getSession().createSQLQuery(query).list();
        
        DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.setListIds(list);
        return findDocuments(donorsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countQuarantineFinishInviteDocument(String pattern, boolean showDeleted) {
		String query = "select donor.id from trfu_donors donor "
        	+ "inner join trfu_blood_donation_requests requests on requests.donor_id = donor.id "
        	+ "inner join trfu_blood_components components on components.donationId = requests.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate()";
        List list = getSession().createSQLQuery(query).list();
        
        DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.setListIds(list);
        return countDocument(donorsFilter);
	}
	
	
	/**
	 * Подбор доноров по фенотипам
	 * @return - список доноров
	 */
	@SuppressWarnings("unchecked")
	public List<Donor> findComponentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        detachedCriteria.add(Restrictions.eq("deleted", false));
        
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
        	
        	DonorsFilter donorsFilter = new DonorsFilter();
        	donorsFilter.setListIds(list);
        	return findDocuments(donorsFilter, -1, -1, orderBy, orderAsc);
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
        	DonorsFilter donorsFilter = new DonorsFilter();
        	donorsFilter.setMail(login);
        	donorsFilter.setPassword(password);
        	List<Donor> donors = findDocuments(donorsFilter, -1, 1, null, true);
            if ((donors != null) && !donors.isEmpty()) {
                // don't modify this routine work! (for proxy caching such objects as Personage, Location)
                return donors.get(0);
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
    	if (!DonorHelper.USE_SRPD) {
    		String query = "select mail FROM trfu_donors where deleted = false and send_news = true and mail <> '' and mail is not null";
    		return getSession().createSQLQuery(query).list();
    	} else {
    		String query = "select temp_storage_id FROM trfu_donors where deleted = false and send_news = true";
    		List listIds = getSession().createSQLQuery(query).list();
    		DonorHelper donorHelper = new DonorHelper();
    		Map<FieldsInMap, Object> paramsMap = donorHelper.makeParametersforSearchMails(listIds);
    		Map<String, Map<FieldsInMap,Object>> resMap = srpdDao.get(paramsMap);
    		return  donorHelper.getMailsFromMap(resMap);
    	}
	}
		   
	public long countDocument(DonorsFilter donorsFilter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addNotDeletedCriteria(detachedCriteria);
        donorsFilter.setConjunction(true);
        if (DonorHelper.USE_SRPD) {
        	DonorHelper donorHelper = new DonorHelper();
        	/* map with paramters for counting right donors */
        	Map<FieldsInMap, Object> paramMap = donorHelper.listIdsDonorsForFilter(donorsFilter);
        	/* map with result from SRPD */
        	Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
        	if (resMap != null && resMap.size() > 0) {
        		donorsFilter.setListSRPDIds(resMap.keySet());
        	}
        }
		return getCountOf(getSearchCriteria(detachedCriteria, donorsFilter));
	}

	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria,
			DonorsFilter donorsFilter) {
		if (donorsFilter != null) {
			Junction junction;
			if (donorsFilter.isConjunction()) {
				junction = Restrictions.conjunction();
			} else {
				junction = Restrictions.disjunction();
			}
			String number = donorsFilter.getNumber();
			Date created = donorsFilter.getCreated();
			Date birth = donorsFilter.getBirth();
			int genderId = donorsFilter.getGenderId();
			String externalNumber = donorsFilter.getExternalNumber();
			String factAddress = donorsFilter.getFactAddress();
			int statusId = donorsFilter.getStatusId();
			int bloodGroupId = donorsFilter.getBloodGroupId();
			int rhesusFactorId = donorsFilter.getRhesusFactorId();
			int pastQuarantineId = donorsFilter.getPastQuarantineId();
			int categoryId = donorsFilter.getCategoryId();
			///////////////////////////////////////////////////////
			String donorTypeValue = donorsFilter.getDonorTypeValue();
			List<Integer> listIds = donorsFilter.getListIds();
			List<Integer> listStatusesId = donorsFilter.getLisStatusId();
			Collection<String> listSRPDIds = donorsFilter.getListSRPDIds();
			String mail = donorsFilter.getMail();
			String password = donorsFilter.getPassword();
			
			if (StringUtils.isNotEmpty(number)) {
				junction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (created != null) {
				addDateSearchCriteria(junction, created, "created");
			}
			if (birth != null) {
				addDateSearchCriteria(junction, birth, "birth");
			}
	        if (genderId != DonorsFilter.GENDER_NULL_VALUE) {
	        	junction.add(Restrictions.eq("gender", genderId));
	        }
			if (StringUtils.isNotEmpty(externalNumber)) {
				junction.add(Restrictions.ilike("externalNumber", externalNumber, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(factAddress)) {
				junction.add(Restrictions.ilike("factAddress", factAddress, MatchMode.ANYWHERE));
			}
	        if (categoryId != DonorsFilter.DONOR_CATEGORY_NULL_VALUE) {
	        	junction.add(Restrictions.eq("category", categoryId));
	        }
	        if (statusId != DonorsFilter.DONOR_STATUS_NULL_VALUE) {
	        	junction.add(Restrictions.eq("statusId", statusId));
	        }
	        if (bloodGroupId != DonorsFilter.BLOOD_GROUP_NULL_VALUE) {
	        	junction.add(Restrictions.eq("bloodGroup.id", bloodGroupId));
	        }
	        if (rhesusFactorId != DonorsFilter.RHESUS_FACTOR_NULL_VALUE) {
	        	junction.add(Restrictions.eq("rhesusFactor.id", rhesusFactorId));
	        }
	        if (pastQuarantineId != DonorsFilter.PAST_QUARANTINE_NULL_VALUE) {
		        criteria.createAlias("rejection", "rejection", CriteriaSpecification.INNER_JOIN);
	        	if (pastQuarantineId == DonorsFilter.PAST_QUARANTINE_YES_VALUE) {
	        		junction.add(Restrictions.le("rejection.expiration", new Date()));
	        	} else {
	    			Disjunction disjunction = Restrictions.disjunction();
	    			disjunction.add(Restrictions.ge("rejection.expiration", new Date()));
	    			disjunction.add(Restrictions.isNull("rejection.expiration"));
	    			junction.add(disjunction);
	        	}
	        }
	        ////////////////////////////// for SRPD ////////////////////////////////////////////////
	        if (!DonorHelper.USE_SRPD) {
	        	String lastName = donorsFilter.getLastName();
	        	String firstName = donorsFilter.getFirstName();
	        	String middleName = donorsFilter.getMiddleName();
	        	int documentTypeId = donorsFilter.getDocumentTypeId();
				String documentNumber = donorsFilter.getDocumentNumber();
				String documentSeries = donorsFilter.getDocumentSeries();
				String insuranceNumber = donorsFilter.getInsuranceNumber();
				String insuranceSeries = donorsFilter.getInsuranceSeries();
				if (StringUtils.isNotEmpty(lastName)) {
					junction.add(Restrictions.ilike("lastName", lastName, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(firstName)) {
					junction.add(Restrictions.ilike("firstName", firstName, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(middleName)) {
					junction.add(Restrictions.ilike("middleName", middleName, MatchMode.ANYWHERE));
				}
				if (documentTypeId != DonorsFilter.DOCUMENT_TYPE_NULL_VALUE) {
			        if (StringUtils.isNotEmpty(documentNumber)) {
			        	if (documentTypeId == DonorsFilter.PASSPORT_DOCUMENT_TYPE) {
		    				junction.add(Restrictions.ilike("passportNumber", documentNumber, MatchMode.ANYWHERE));
		    				if(StringUtils.isNotEmpty(insuranceNumber)) {
		    					junction.add(Restrictions.ilike("insuranceNumber", documentNumber, MatchMode.ANYWHERE));
		    				}
			        	} else {
		    				junction.add(Restrictions.ilike("insuranceNumber", documentNumber, MatchMode.ANYWHERE));
			        	}
			        }
			        if (StringUtils.isNotEmpty(documentSeries)) {
			        	if (documentTypeId == DonorsFilter.PASSPORT_DOCUMENT_TYPE) {
		    				junction.add(Restrictions.ilike("passportSeries", documentSeries, MatchMode.ANYWHERE));
		    				if (StringUtils.isNotEmpty(insuranceSeries)) {
		    					junction.add(Restrictions.ilike("insuranceSeries", documentSeries, MatchMode.ANYWHERE));
		    				}
			        	} else {
		    				junction.add(Restrictions.ilike("insuranceSeries", documentSeries, MatchMode.ANYWHERE));
			        	}
			        }
		        }
				if (mail != null && StringUtils.isNotEmpty(mail) && password != null && StringUtils.isNotEmpty(password)) {
		        	criteria.add(Restrictions.eq("mail", mail));
		        }
	        } 
	        if (listSRPDIds != null && listSRPDIds.size() > 0) {
	        	junction.add(Restrictions.in("tempStorageId", listSRPDIds));
	        }
	        //////////////////////////// refactoring /////////////////////////////////////////////
	        if (!donorsFilter.isConjunction()) {
	        	criteria.createAlias("donorType", "donorType", CriteriaSpecification.LEFT_JOIN);
	        }
	        if (donorTypeValue != null) {
	        	junction.add(Restrictions.ilike("donorType.value", donorTypeValue, MatchMode.ANYWHERE));
	        }
	        if (listStatusesId.size() > 1) {
	        	Disjunction disjun = Restrictions.disjunction();
	        	for (int i = 1; i < listStatusesId.size(); i ++) {
	        		disjun.add(Restrictions.eq("statusId", i));
	        	}
	        	criteria.add(disjun);
	        }
	        
	        criteria.add(junction);
	        if (!donorsFilter.isShowDeleted()) {
				criteria.add(Restrictions.eq("deleted", false));
			}
	        if (listIds != null && listIds.size() > 0) {
	        	criteria.add(Restrictions.in("id", listIds));
	        }
	        if (mail != null && StringUtils.isNotEmpty(mail) && password != null && StringUtils.isNotEmpty(password)) {
	        	criteria.add(Restrictions.eq("password", password));
	        }
		}
        return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Donor> findDocuments(DonorsFilter donorsFilter, int offset,
			int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		Map<FieldsInMap, Object> paramMap = null;
		Map<String, Map<FieldsInMap, Object>> resMap = null;
		DonorHelper donorHelper = new DonorHelper();
        addOrderCriteria(orderBy, orderAsc, detachedCriteria);
        /* Здесь должен быть функционал для поиска по ЗХПД по условию "или" */
        /*if (DonorHelper.USE_SRPD && donorsFilter.isQueryToSRPD()) {
        	donorHelper = new DonorHelper();
        	/* карта для предварительного поиска по данным в ЗХПД */
       /* 	paramMap = donorHelper.listIdsDonorsForFilter(donorsFilter);
        	/* карта с response от ЗХПД */
       /* 	resMap = srpdDAO.get(paramMap);
        	/* занесение в фильтр id-шников от ЗХПД, для дальнешего поиска в базе ТРФУ;
        	 * доноры с данными id-шниками будут обязательно добавлены в результат выборки из базы */
        /*	donorsFilter.setListSRPDIds(resMap.keySet());
        }*/
        List<Donor> donors = getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, donorsFilter), offset, count);
        if (DonorHelper.USE_SRPD) {
        	if (resMap == null) {
        		/* новый запрос к ЗХПД, т.к. колличество доноров для которых понадобятся данные может увеличится */
        		donorsFilter.setListSRPDIds(donorHelper.listIdsSRPDFromDonors(donors));
        		paramMap = donorHelper.listIdsDonorsForFilter(donorsFilter);
        		resMap = srpdDao.get(paramMap);
        	}
        	donors = donorHelper.mergeDonorsAndMap(donors, resMap);
        }
		return donors;
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
				donor = new DonorHelper().mergeDonorAndMap(donor, (Map<FieldsInMap, Object>)map.values().toArray()[0]);
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
}