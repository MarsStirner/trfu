package ru.efive.medicine.niidg.trfu.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class BloodComponentDAOImpl extends GenericDAOHibernate<BloodComponent> {
	private static SRPDDao srpdDao;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}
	
	@Override
	protected Class<BloodComponent> getPersistentClass() {
		return BloodComponent.class;
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
	public List<BloodComponent> findDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
		bloodComponentFilter.setStatusId(100);
		return findDocuments(bloodComponentFilter, offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(String filter, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsfilter = createBloodComponentsFilter(filter, showDeleted);
        bloodComponentsfilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
        bloodComponentsfilter.setStatusId(100);
        return countDocument(bloodComponentsfilter);
	}
	
	/**
     * Поиск документов
     *
     * @param in_map          параметры поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(Map<String, Object> in_map, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setMap(in_map);
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param in_map          параметры поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(Map<String, Object> in_map, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setMap(in_map);
		return countDocument(bloodComponentsFilter);
	}
	
	
	/**
     * Поиск документов
     *
     * @param filter          фильтр поиска
     * @param statusId        статус
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (statusId != 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        else {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
        	bloodComponentsFilter.setStatusId(100);
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param statusId        статус
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, int statusId, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (statusId != 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        else {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
        	bloodComponentsFilter.setStatusId(100);
        }
      return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск документов
     *
     * @param filter          фильтр поиска
     * @param statusId        статус
     * @param showExpired     true - отображать просроченные, false - скрывать просроченные
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, int statusId, boolean showExpired, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (!showExpired) {
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        	bloodComponentsFilter.setExpirationDateGe(calendar	.getTime());
        	bloodComponentsFilter.setExpirationDateNull(true);
        }
        if (statusId != 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        else {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
        	bloodComponentsFilter.setStatusId(100);
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param statusId        статус
	 * @param showExpired     true - отображать просроченные, false - скрывать просроченные
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, int statusId, boolean showExpired, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (!showExpired) {
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            bloodComponentsFilter.setExpirationDateGe(calendar.getTime());
            bloodComponentsFilter.setExpirationDateNull(true);
        }
        if (statusId != 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        else {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
        	bloodComponentsFilter.setStatusId(100);
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск просроченных КК
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findExpiredDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, false);
       
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        bloodComponentsFilter.setExpirationDatelt(calendar.getTime());
        
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        bloodComponentsFilter.getListJunctions().add(disjunction);
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countExpiredDocument(String filter, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        bloodComponentsFilter.setExpirationDatelt(calendar.getTime());
        
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        bloodComponentsFilter.getListJunctions().add(disjunction);

        return countDocument(bloodComponentsFilter);
	}
	
	protected DetachedCriteria getConjunctionSearchCriteria(DetachedCriteria criteria, Map<String, Object> in_map) {
		if (in_map != null && in_map.size() > 0) {
			Conjunction conjunction = Restrictions.conjunction();
			
			String in_key = "componentType";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((BloodComponentType) in_map.get(in_key)).getId()));
			}
			
			in_key = "statusId";
			if (in_map.get(in_key) != null) {
				if ((Integer) in_map.get(in_key) != 0) {
					conjunction.add(Restrictions.eq(in_key, in_map.get(in_key)));
				}
			}
			
			in_key = "maker";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "bloodGroup";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((BloodGroup) in_map.get(in_key)).getId()));
			}
			
			in_key = "rhesusFactor";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Classifier) in_map.get(in_key)).getId()));
			}
			/*
			in_key = "startReadyDate";
			if (in_map.get(in_key) != null) {
				conjunction.add(Restrictions.ge(in_key.substring(5, 6).toLowerCase()+in_key.substring(6), in_map.get(in_key)));
			}
			
			in_key = "endReadyDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "startDistributedDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "endDistributedDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			*/
			criteria.add(conjunction);
		}
		return criteria;
	}
	
	
	/**
	 * Поиск компонентов по обращению на донацию
	 * @param donationId - идентификатор обращения на донацию
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByDonation(int donationId) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        
        if (donationId > 0) {
        	bloodComponentsFilter.setDonationId(donationId);
        }
        return findDocument(bloodComponentsFilter, -1, -1, "number", true);
	}
	
	/**
	 * Количество компонентов по обращению на донацию
	 * @param donationId - идентификатор обращения на донацию
	 * @return - количество компонентов
	 */
	public long countComponentsByDonation(int donationId) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        if (donationId > 0) {
        	bloodComponentsFilter.setDonationId(donationId);
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
	 * Поиск компонентов по обращению на заказ КК
	 * @param orderId - идентификатор обращения на заказ КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByOrder(int orderId) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        if (orderId > 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setOrderId(orderId);
        }
        return findDocument(bloodComponentsFilter, -1, -1, "parentNumber,number", true);
	}
	
	/**
	 * Поиск компонентов по статусу
	 * @param statusId - статус КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByStatusId(boolean showDeleted, int statusId, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
        if (statusId > 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов по статусу
	 * @param statusId - статус КК
	 * @return - количество компонентов
	 */
	public long countComponentsByStatusId(boolean showDeleted, int statusId) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
        if (statusId > 0) {
        	bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        	bloodComponentsFilter.setStatusId(statusId);
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск закупленных документов
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findPurchasedDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setPurchased(true);
        bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        bloodComponentsFilter.setStatusId(100);
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество закупленных компонентов
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countPurchasedDocument(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setPurchased(true);
        bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        bloodComponentsFilter.setStatusId(100);
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск компонентов с завершенным сроком карантинизации
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        bloodComponentsFilter.setStatusId(2);
        bloodComponentsFilter.setQuarantineFinishDate(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов с завершенным сроком карантинизации
	 * 
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countQuarantinedDocument(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        bloodComponentsFilter.setStatusId(2);
        bloodComponentsFilter.setQuarantineFinishDate(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск компонентов с завершенным сроком карантинизации по донору
     * 
     * @param donor           донор
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedDocumentsByDonor(Donor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
        	return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	/**
	 * Количество компонентов с завершенным сроком карантинизации по донору
	 * @param donor           донор
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countQuarantinedDocumentByDonor(Donor donor, String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
            return countDocument(bloodComponentsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
        	return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
            return countDocument(bloodComponentsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	/**
     * Поиск компонентов по номеру донации и номеру компонента
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список компонентов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsByFullNumber(String parentNumber, String number, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setPurchased(true);
		bloodComponentsFilter.setParentNumber(parentNumber);
		bloodComponentsFilter.setNumber(number);
		return findDocuments(bloodComponentsFilter, -1, -1, null, false);
	}
	
	/**
	 * Подбор компонентов по фенотипам
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        
        StringBuilder concat = new StringBuilder();
        StringBuilder testsSubQuery = new StringBuilder();
        for (Analysis phenotype: phenotypes) {
        	if (!testsSubQuery.toString().equals("")) {
        		testsSubQuery.append(" or ");
        	}
        	testsSubQuery.append("binary value = '" + phenotype.getType().getValue() + "'");
        	concat.append(phenotype.getValue());
        }
        
        String query = "select components.id as id FROM trfu_blood_donation_request_tests_immuno immuno "
        	+ "inner join trfu_tests tests ON tests.id = immuno.testsImmuno_id "
        	+ "inner join trfu_blood_donation_requests donations on immuno.trfu_blood_donation_requests_id = donations.id "
        	+ "inner join trfu_blood_components components on donations.number = components.parentNumber "
        	+ (searchBloodGroup? "inner join trfu_blood_groups groups on components.bloodGroup_id = groups.id ": "")
        	+ (searchRhesus? "inner join trfu_classifiers classifiers on components.rhesusFactor_id = classifiers.id ": "")
        	+ "where components.status_id = 3 and (components.expirationDate > sysdate() or components.expirationDate is null) "
        	+ (searchBloodGroup? "and groups.value = '" + bloodGroup + "' ": "")
        	+ (searchRhesus? "and classifiers.value = '" + rhesusFactor + "' ": "")
        	+ "and tests.type_id in (select id from trfu_analysis_types where " + testsSubQuery + ") "
        	+ "group by components.id "
        	+ "having group_concat(concat('',tests.value) order by tests.id separator '')='" + concat + "'";
        
        List list = getSession().createSQLQuery(query).addScalar("id").list();
        
        if (list != null && list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInControl(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
		bloodComponentsFilter.setStatusId(3);
		bloodComponentsFilter.setInControlCompareFlag(CompareType.EQ);
		bloodComponentsFilter.setInControl(true);
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
		
	}
	
	public long countDocumentsInControl(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
		bloodComponentsFilter.setStatusId(3);
		bloodComponentsFilter.setInControlCompareFlag(CompareType.EQ);
		bloodComponentsFilter.setInControl(true);
		return countDocument(bloodComponentsFilter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
		bloodComponentsFilter.setStatusId(statusId);
		bloodComponentsFilter.setInControlCompareFlag(CompareType.EQ);
		bloodComponentsFilter.setInControl(false);
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
		bloodComponentsFilter.setStatusId(statusId);
		bloodComponentsFilter.setInControlCompareFlag(CompareType.EQ);
		bloodComponentsFilter.setInControl(false);
		return countDocument(bloodComponentsFilter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, false);
		bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
		bloodComponentsFilter.setStatusId(statusId);
        
		bloodComponentsFilter.getListAlias().add("history");
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("history.toStatusId", 2));
        disjunction.add(Restrictions.eq("history.fromStatusId", 2));
        bloodComponentsFilter.getListJunctions().add(disjunction);
        
        List<BloodComponent> result = new ArrayList<BloodComponent>(
        		new LinkedHashSet<BloodComponent>(findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc)));
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public long countQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        bloodComponentsFilter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_EQ);
        bloodComponentsFilter.setStatusId(statusId);
        
        bloodComponentsFilter.getListAlias().add("history");
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("history.toStatusId", 2));
        disjunction.add(Restrictions.eq("history.fromStatusId", 2));
        bloodComponentsFilter.getListJunctions().add(disjunction);
        List<BloodComponent> result = new ArrayList<BloodComponent>(new LinkedHashSet<BloodComponent>(
        		findDocument(bloodComponentsFilter, -1, -1, null, false)));
        
        return result.size() * 1l;
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory() {  
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, null);
        String query = "SELECT comp.id FROM trfu_blood_components comp INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE wf.to_status_id = 3 AND wf.startDate = (SELECT max(wf_in.startDate) FROM trfu_blood_component_history hist_in " +
        		"INNER JOIN wf_history wf_in ON hist_in.history_entry_id = wf_in.id WHERE hist_in.component_id = comp.id) " +
        		"AND wf.to_status_id != comp.status_id and comp.orderId=0 and comp.status_id=10";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory2() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, null);
        String query = "SELECT id from(SELECT sum(CASE WHEN wf.to_status_id = 3 THEN 1 ELSE 0 END) AS gotov, " +
        		"sum(CASE WHEN wf.to_status_id = 10 THEN 1 ELSE 0 END) AS vidan, comp.id, comp.volume FROM trfu_blood_components comp " +
        		"INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE comp.status_id = 10 GROUP BY comp.id) as table1 where table1.gotov = 0 AND table1.vidan = 1";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.setListIds(list);
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findSplitComponents() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		bloodComponentsFilter.setSplit(true);
		return findDocument(bloodComponentsFilter, -1, -1, null, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findUniquieSplitComponents() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		
		List list = getSession().createSQLQuery("select c.id from trfu_blood_components c where c.split = 1 and c.donationId <> 0 group by c.donationId").list();
		
		if (list.size() > 0) {
			bloodComponentsFilter.setListIds(list);
			return findDocument(bloodComponentsFilter, -1, -1, null, false);
		}
		else {
			return Collections.emptyList();
		}
	}
	
	@SuppressWarnings("unchecked")
	public BloodComponent findParentComponent(BloodComponent splitComponent) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		bloodComponentsFilter.setParentNumber(splitComponent.getParentNumber());
		bloodComponentsFilter.setNumber(splitComponent.getNumber());
		bloodComponentsFilter.setDonationId(splitComponent.getDonationId());
		List<BloodComponent> list = findDocument(bloodComponentsFilter, -1, -1, null, false);
		if (list.isEmpty()) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	public long countDocuments(BloodComponentsFilter filter) {
		filter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
		filter.setStatusId(100);
		return countDocument(filter);
	}

	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(BloodComponentsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
		filter.setStatusIdCompareFlag(BloodComponentsFilter.STATUS_ID_NE);
		filter.setStatusId(100);
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	private long countDocument(BloodComponentsFilter filter){
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		if (!DonorHelper.USE_SRPD && filter.isQueryToSRPD()) {
			Map<FieldsInMap, Object> paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			filter.setListSRPDIds(srpdDao.get(paramMap).keySet());
		}
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
		
	}
	private List<BloodComponent> findDocument(BloodComponentsFilter filter, int offset, int count, String orderBy, boolean orderAsc ) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		Map<String, Map<FieldsInMap,Object>> resMap = null;
		Map<FieldsInMap,Object> paramMap = null;
		List<BloodComponent> bloodComponents = null;
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		/*if (DonorHelper.USE_SRPD && filter.isQueryToSRPD()) {
			donorHelper = new DonorHelper();
			paramMap = donorHelper.listIdsDonorsForFilter(filter);
			resMap = srpdDao.get(paramMap);
			filter.setListSRPDIds(resMap.keySet());
		}*/
		String query = "Select id from trfu.trfu_blood_donation_requests";
        List list = getSession().createSQLQuery(query).list();
		detachedCriteria.setFetchMode("donation", FetchMode.EAGER);
		detachedCriteria.add(Restrictions.in("donationId", list));
		bloodComponents =  getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset,count);
		if (DonorHelper.USE_SRPD && resMap == null) {
			filter.setListSRPDIds(donorHelper.listIdsSRPDFromBloodComponent(bloodComponents));
			paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			resMap = srpdDao.get(paramMap);
			bloodComponents = donorHelper.mergeBloodComponentsAndMap(bloodComponents, resMap);
		}
		return bloodComponents;
	}
	
	protected DetachedCriteria getSearchCriteria(DetachedCriteria criteria, BloodComponentsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			int bloodComponentTypeId = filter.getBloodComponentTypeId();
			String donorCode = filter.getDonorCode();
			int makerId = filter.getMakerId();
			int bloodGroupId = filter.getBloodGroupId();
			int rhesusFactorId = filter.getRhesusFactorId();
			Date donationDate = filter.getDonationDate();
			Date expirationDate = filter.getExpirationDate();
			int statusId = filter.getStatusId();
			//String fio = filter.getFio();
			String firstName = filter.getFirstName();
			String lastName = filter.getLastName();
			String middleName = filter.getMiddleName();
			Boolean purchased = filter.getPurchased();
			Date quarantineFinishDate = filter.getQuarantineFinishDate();
			Collection<String> listSRPDIds = filter.getListSRPDIds();
			List<Integer> listIds = filter.getListIds();
			Boolean inControl = filter.getInControl();
			Boolean split = filter.getSplit();
			/* temporal */
			List<Junction> listJunctions = filter.getListJunctions();
			List<String> listAlias = filter.getListAlias();
			for (String i: listAlias) {
				criteria.createAlias(i, i, CriteriaSpecification.LEFT_JOIN);
			}
			for (Junction i: listJunctions) {
				criteria.add(i);	
			}
			if (filter.getMap() != null) {
				getConjunctionSearchCriteria(criteria, filter.getMap());
			}
			if (StringUtils.isNotEmpty(number)) {
				String[] numbers = number.split("-");
				if (numbers.length == 1) {
					conjunction.add(Restrictions.ilike("number", numbers[0], MatchMode.ANYWHERE));
				} else {
					conjunction.add(Restrictions.ilike("parentNumber", numbers[0], MatchMode.ANYWHERE));
					conjunction.add(Restrictions.ilike("number", numbers[1], MatchMode.ANYWHERE));
				}
			}
			if (StringUtils.isNotEmpty(donorCode)) {
				conjunction.add(Restrictions.ilike("donorCode", donorCode, MatchMode.ANYWHERE));
			}
	        if (bloodGroupId != BloodComponentsFilter.BLOOD_GROUP_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("bloodGroup.id", bloodGroupId));
	        }
	        if (rhesusFactorId != BloodComponentsFilter.RHESUS_FACTOR_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("rhesusFactor.id", rhesusFactorId));
	        }
			if (donationDate != null) {
				addDateSearchCriteria(conjunction, donationDate, "donationDate");
			}
			if (expirationDate != null) {
				addDateSearchCriteria(conjunction, expirationDate, "expirationDate");
			}
			/* Проверка статуса: 1. CompareFlag = null_value или  eq -  Resrtictions.eq 
			 * 					 2. CompareFlag = ne - Restritions.ne */
	        if (statusId != BloodComponentsFilter.BLOOD_COMPONENT_STATUS_NULL_VALUE) {
	        	if (filter.getStatusIdCompareFlag() == BloodComponentsFilter.STATUS_ID_NULL 
	        			|| filter.getStatusIdCompareFlag() == BloodComponentsFilter.STATUS_ID_EQ) {
	        		conjunction.add(Restrictions.eq("statusId", statusId));
	        	} else if(filter.getStatusIdCompareFlag() == BloodComponentsFilter.STATUS_ID_NE) {
	        		conjunction.add(Restrictions.ne("statusId", statusId));
	        	}
	        }
	        if (bloodComponentTypeId != BloodComponentsFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("componentType.id", bloodComponentTypeId));
	        }
	        if (makerId != BloodComponentsFilter.MAKER_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("maker.id", makerId));
	        }
	        //if (StringUtils.isNotEmpty(fio)) {
	        if ((!DonorHelper.USE_SRPD && filter.isQueryToSRPD()) || listSRPDIds != null) {
	        	criteria.createAlias("donation", "donation", CriteriaSpecification.INNER_JOIN);
	        	criteria.createAlias("donation.donor", "donor", CriteriaSpecification.INNER_JOIN);
	        }
	        Disjunction disjunction = Restrictions.disjunction();
	        if (!DonorHelper.USE_SRPD) {
	        	if (StringUtils.isNotEmpty(lastName)) {
	        		disjunction.add(Restrictions.ilike("donor.lastName", lastName, MatchMode.ANYWHERE));
	        	}
	        	if (StringUtils.isNotEmpty(middleName)) {
	        		disjunction.add(Restrictions.ilike("donor.middleName", middleName, MatchMode.ANYWHERE));
	        	}
	        	if (StringUtils.isNotEmpty(firstName)) {
	        		disjunction.add(Restrictions.ilike("donor.firstName", firstName, MatchMode.ANYWHERE));
	        	}
	        	conjunction.add(disjunction);
	        } 
	        if (listSRPDIds != null) {
	        		disjunction.add(Restrictions.in("donor.tempStorageId", listSRPDIds));
	        		conjunction.add(disjunction);
	        }
	        if (!filter.isShowDeleted()) {
				criteria.add(Restrictions.eq("deleted", false));
			}
	        if(purchased != null) {
	        	criteria.add(Restrictions.eq("purchased", purchased));
	        }
	        if (quarantineFinishDate != null) {
	        	criteria.add(Restrictions.le("quarantineFinishDate", quarantineFinishDate));
	        }
	        if (listIds != null && listIds.size() > 0) {
	        	criteria.add(Restrictions.in("id", listIds));
	        }
	        if (inControl != null) {
	        	switch (filter.getInControlCompareFlag()) {
					case EQ:
						criteria.add(Restrictions.eq("inControl", inControl));
						break;
					case NE:
						criteria.add(Restrictions.ne("inControl", inControl));
						break;
					case NULL:
						break;
	        	}
	        }
	        if (split != null) {
	        	criteria.add(Restrictions.eq("split", split));
	        }
	        if (filter.getExpirationDateGe() != null || filter.isExpirationDateNull()) {
	        	Disjunction disj = Restrictions.disjunction();
	        	if (filter.getExpirationDateGe() != null) {
	        		disj.add(Restrictions.ge("expirationDate", filter.getExpirationDateGe()));
	        	}
	        	if (filter.isExpirationDateNull()) {
	        		disj.add(Restrictions.isNull("expirationDate"));
	        	}
	        	criteria.add(disj);
	        }
	        if (filter.getExpirationDatelt() != null) {
	        	criteria.add(Restrictions.lt("expirationDate", filter.getExpirationDatelt()));
	        }
			criteria.add(conjunction);
		}
		return criteria;
	}
	private BloodComponentsFilter createBloodComponentsFilter(String pattern, Boolean showDeleted) {
		BloodComponentsFilter filter = new BloodComponentsFilter();
		if (StringUtils.isNotEmpty(pattern)) {
			filter.getListAlias().add("componentType");
			Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("componentType.value", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("parentNumber", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("number", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.sqlRestriction("DATE_FORMAT(this_.donationDate, '%d.%m.%Y') like lower(?)", filter + "%", new StringType()));
	        if (pattern.length() > 8) {
	        	disjunction.add(Restrictions.sqlRestriction("CONCAT(this_.parentNumber, this_.number) like RIGHT(?, 8)", filter + "%", new StringType()));
	        }
	        filter.getListJunctions().add(disjunction);
		}
		if (showDeleted != null) {
			filter.setShowDeleted(showDeleted);
		}
		return filter;
	}
	private List<BloodComponent> getDataFromSRPD(List<BloodComponent> components) {
		Map<FieldsInMap, Object> paramMap = new HashMap<DonorHelper.FieldsInMap, Object>();
		List<String> listStorageIds = donorHelper.listIdsSRPDFromBloodComponent(components);
		paramMap.put(FieldsInMap.LIST_STORAGE_IDS, listStorageIds);
		Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
		components = donorHelper.mergeBloodComponentsAndMap(components, resMap);
		return components;
	}
}