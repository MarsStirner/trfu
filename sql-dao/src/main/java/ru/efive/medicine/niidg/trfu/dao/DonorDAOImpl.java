package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
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
import ru.efive.medicine.niidg.trfu.util.DateHelper;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.SRPDDao;

public class DonorDAOImpl extends GenericDAOHibernate<Donor> {
	
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
			donorsFilter.setDocumentSeries1(pattern);//------------------------- for OMC
			donorsFilter.setDocumentNumber1(pattern);
			donorsFilter.setShowDeleted(showDeleted);   
			donorsFilter.setConjunction(isConjunction);
			return donorsFilter;
		}
		return null;
	}
		
	public List<Donor> findRejectedDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
	    DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.getListEqStatusId().add(-1);
        donorsFilter.getListEqStatusId().add(-2);

        return findDocuments(donorsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countRejectedDocument(String pattern, boolean showDeleted) {
        DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern,showDeleted, false);
        donorsFilter.getListEqStatusId().add(-1);
        donorsFilter.getListEqStatusId().add(-2);
        
        return countDocument(donorsFilter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Donor> findAvailableDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
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
        
        DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.setListIds(list);
        
        return findDocuments(donorsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countAvailableDocument(String pattern, boolean showDeleted) {
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
        
        DonorsFilter donorsFilter = createDonorsFilterFromPattern(pattern, showDeleted, false);
        donorsFilter.setListIds(list);
      
        return countDocument(donorsFilter);
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
        	detachedCriteria.add(Restrictions.in("id", list));
            
    		String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
            return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
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
        	DetachedCriteria detachedCriteria = createDetachedCriteria();
            
            detachedCriteria.add(Restrictions.eq("mail", login));
            detachedCriteria.add(Restrictions.eq("password", password));
            
            List<Donor> donors = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
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
    	String query = "select mail FROM trfu_donors where deleted = false and send_news = true and mail <> '' and mail is not null";
    	return getSession().createSQLQuery(query).list();
	}
	
	/*
	@SuppressWarnings("unchecked")
	public List<Donor> search(String pattern, int offset, int count) {
		List<Donor> result = new ArrayList<Donor>();
		try {
			if (!StringUtils.isEmpty(pattern)) {
				FullTextSession fullTextSession = Search.getFullTextSession(getSession());
				
				QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_31, new String[] {Donor.DEFAULT_SEARCH_FIELD}, new StandardAnalyzer(Version.LUCENE_31));
				parser.setAllowLeadingWildcard(true);
				
				org.apache.lucene.search.Query luceneQuery = parser.parse(pattern);
				FullTextQuery hibQuery = fullTextSession.createFullTextQuery(luceneQuery, getPersistentClass());
				hibQuery.setFirstResult(offset).setMaxResults(count);
				
				result = hibQuery.list();
				
				Analyzer analyzer = fullTextSession.getSearchFactory().getAnalyzer("donor_content");
				
				QueryScorer scorer = new QueryScorer(luceneQuery, Donor.DEFAULT_SEARCH_FIELD);
				SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class='hl'>", "</span>");
				Highlighter highlighter = new Highlighter(formatter, scorer);
				highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, ApplicationHelper.MAX_FRAGMENT_LEN));
				for (Donor donor : result) {
					String highlightedText = null;
					try {
						highlightedText = highlighter.getBestFragment(analyzer, Donor.DEFAULT_SEARCH_FIELD, donor.getContent());
					}
					catch (Exception e) {
						System.out.println("Failed to highlight text for donor " + donor.getId() + " due to: " + e.toString());
					}
					if (highlightedText != null) {
						highlightedMap.put(donor.getId(), highlightedText);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int countSearch(String pattern) {
		int result = 0;
		try {
			if (!StringUtils.isEmpty(pattern)) {
				FullTextSession fullTextSession = Search.getFullTextSession(getSession());
				
				QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_31, new String[] {Donor.DEFAULT_SEARCH_FIELD}, new StandardAnalyzer(Version.LUCENE_31));
				parser.setAllowLeadingWildcard(true);
				
				FullTextQuery hibQuery = fullTextSession.createFullTextQuery(parser.parse(pattern), getPersistentClass());
				
				hibQuery.list();
				result = hibQuery.getResultSize();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getHighlightedText(int id) {
		return highlightedMap.containsKey(id)? highlightedMap.get(id): "";
	}
	
	@SuppressWarnings("unchecked")
	public void index() {
		try {
			FullTextSession ftSession = Search.getFullTextSession(getSession());
			ftSession.getTransaction().begin();
			List<Donor> list = ftSession.createCriteria(Donor.class).list();
			for (Donor donor: list) {
				ftSession.index(donor);
			}
			ftSession.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<Integer, String> highlightedMap = new HashMap<Integer, String>();*/
    /*private void addDateSearchCriteria(Junction junction, Date created,
			String dateField) {
		Date fromDate = DateHelper.getDateWithoutTime(created);
		Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(created));
		junction.add(Restrictions.ge(dateField, fromDate));
		junction.add(Restrictions.le(dateField, toDate));
	}*/
    
	public long countDocument(DonorsFilter donorsFilter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addNotDeletedCriteria(detachedCriteria);
        donorsFilter.setConjunction(true);
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
			boolean showDeleted = donorsFilter.isShowDeleted();
			List listIds = donorsFilter.getListIds();
			
			if (StringUtils.isNotEmpty(number)) {
				junction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (created != null) {
				addDateSearchCriteria(junction, created, "created");
			}
			if (birth != null && (junction instanceof Conjunction)) {
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
				String documentNumber1 = donorsFilter.getDocumentNumber1();
				String documentSeries1 = donorsFilter.getDocumentSeries1();
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
		    				if(StringUtils.isNotEmpty(documentNumber1)) {
		    					junction.add(Restrictions.ilike("insuranceNumber", documentNumber, MatchMode.ANYWHERE));
		    				}
			        	} else {
		    				junction.add(Restrictions.ilike("insuranceNumber", documentNumber, MatchMode.ANYWHERE));
		    				if(StringUtils.isNotEmpty(documentNumber1)) {
		    					junction.add(Restrictions.ilike("passportNumber", documentNumber, MatchMode.ANYWHERE));
		    				}
			        	}
			        }
			        if (StringUtils.isNotEmpty(documentSeries)) {
			        	if (documentTypeId == DonorsFilter.PASSPORT_DOCUMENT_TYPE) {
		    				junction.add(Restrictions.ilike("passportSeries", documentSeries, MatchMode.ANYWHERE));
		    				if (StringUtils.isNotEmpty(documentSeries1)) {
		    					junction.add(Restrictions.ilike("insuranceSeries", documentSeries, MatchMode.ANYWHERE));
		    				}
			        	} else {
		    				junction.add(Restrictions.ilike("insuranceSeries", documentSeries, MatchMode.ANYWHERE));
		    				if (StringUtils.isNotEmpty(documentSeries1)) {
		    					junction.add(Restrictions.ilike("passportSeries", documentSeries, MatchMode.ANYWHERE));
		    				}
			        	}
			        }
		        }
	        }
	        //////////////////////////// refactoring /////////////////////////////////////////////
	        if (!donorsFilter.isConjunction()) {
	        	criteria.createAlias("donorType", "donorType", CriteriaSpecification.LEFT_JOIN);
	        }
	        if (donorTypeValue != null) {
	        	junction.add(Restrictions.ilike("donorType.value", donorTypeValue, MatchMode.ANYWHERE));
	        }
	        Disjunction disjun = Restrictions.disjunction();
	        for (Integer i : donorsFilter.getListEqStatusId()) {
	        	disjun.add(Restrictions.eq("statusId", i));
	        }
	        criteria.add(disjun);
	        
	        criteria.add(junction);
	        criteria.add(Restrictions.eq("deleted", !showDeleted));
	        criteria.add(Restrictions.in("id", listIds));
		}
        return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Donor> findDocuments(DonorsFilter donorsFilter, int offset,
			int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		List<Donor> donors = getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, donorsFilter), offset, count);
		if (DonorHelper.USE_SRPD) {
			DonorHelper donorHelper = new DonorHelper();
			Map<DonorHelper.FieldsInMap, Object> paramForSearchInSRPD = donorHelper.makeMapFromDonorsFilter(donorsFilter, donors);
			Map<Integer,Map<DonorHelper.FieldsInMap, Object>> resAfterSearchInSRPD = new SRPDDao().getDonors(paramForSearchInSRPD);
			donors =  donorHelper.mergeDonorsAndMap(donors, resAfterSearchInSRPD);
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
			Map<DonorHelper.FieldsInMap, Object> map = new SRPDDao().get(new DonorHelper().makeMapForGet((Integer)id));
			donor = new DonorHelper().mergeDonorAndMap(donor, map);
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
			SRPDDao srpdDao = new SRPDDao();
			DonorHelper helper = new DonorHelper();
			try {
				session = getSession();
				session.beginTransaction();
				session.save(entity);
				Map<DonorHelper.FieldsInMap, Object> query = helper.makeMapFromDonor(entity);
				/* commit for saving information to TRFU, without temp_stogate_id */
				Map<DonorHelper.FieldsInMap, Object> answer = srpdDao.addToSRPD(query);
				Donor newDonor = helper.mergeDonorAndMap(entity, answer);
				/* update for writing temp_stogate_id to DB of TRFU */
				session.update(newDonor);
				session.getTransaction().commit();
				return newDonor;
			} catch(Exception e) {
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
				srpdDao.addToSRPD(helper.makeMapFromDonor(entity));
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