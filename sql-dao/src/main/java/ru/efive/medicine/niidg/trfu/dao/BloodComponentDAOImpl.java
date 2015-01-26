package ru.efive.medicine.niidg.trfu.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.type.StringType;

import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

public class BloodComponentDAOImpl extends GenericDAOHibernate<BloodComponent> {
	
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.ne("statusId", 100));
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}

	/**
     * Кол-во документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.ne("statusId", 100));
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getConjunctionSearchCriteria(detachedCriteria, in_map), offset, count);
	}

	/**
     * Кол-во документов
     *
     * @param in_map          параметры поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(Map<String, Object> in_map, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getConjunctionSearchCriteria(detachedCriteria, in_map));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        else {
        	detachedCriteria.add(Restrictions.ne("statusId", 100));
        }
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param statusId        статус
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, int statusId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        else {
        	detachedCriteria.add(Restrictions.ne("statusId", 100));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (!showExpired) {
        	Disjunction disjunction = Restrictions.disjunction();
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        	disjunction.add(Restrictions.ge("expirationDate", calendar.getTime()));
        	disjunction.add(Restrictions.isNull("expirationDate"));
        	detachedCriteria.add(disjunction);
        }
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        else {
        	detachedCriteria.add(Restrictions.ne("statusId", 100));
        }
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (!showExpired) {
        	Disjunction disjunction = Restrictions.disjunction();
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        	disjunction.add(Restrictions.ge("expirationDate", calendar.getTime()));
        	disjunction.add(Restrictions.isNull("expirationDate"));
        	detachedCriteria.add(disjunction);
        }
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        else {
        	detachedCriteria.add(Restrictions.ne("statusId", 100));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	/**
     * Поиск просроченных КК
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findExpiredDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        detachedCriteria.add(Restrictions.lt("expirationDate", calendar.getTime()));
        
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(disjunction);
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	public long countExpiredDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        detachedCriteria.add(Restrictions.lt("expirationDate", calendar.getTime()));
        
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(disjunction);
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	
	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter) {
		if (StringUtils.isNotEmpty(filter)) {
			criteria.createAlias("componentType", "componentType", CriteriaSpecification.LEFT_JOIN);
			Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("componentType.value", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("parentNumber", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.sqlRestriction("DATE_FORMAT(this_.donationDate, '%d.%m.%Y') like lower(?)", filter + "%", new StringType()));
	        if (filter.length() > 8) {
	        	disjunction.add(Restrictions.sqlRestriction("CONCAT(this_.parentNumber, this_.number) like RIGHT(?, 8)", filter + "%", new StringType()));
	        }
	        criteria.add(disjunction);
		}
        return criteria;
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (donationId > 0) {
            detachedCriteria.add(Restrictions.eq("donationId", donationId));
        }
        detachedCriteria.add(Restrictions.eq("deleted", false));
	
        addOrder(detachedCriteria, "number", true);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество компонентов по обращению на донацию
	 * @param donationId - идентификатор обращения на донацию
	 * @return - количество компонентов
	 */
	public long countComponentsByDonation(int donationId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (donationId > 0) {
            detachedCriteria.add(Restrictions.eq("donationId", donationId));
        }
        detachedCriteria.add(Restrictions.eq("deleted", false));
		
        return getCountOf(detachedCriteria);
	}
	
	/**
	 * Поиск компонентов по обращению на заказ КК
	 * @param orderId - идентификатор обращения на заказ КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByOrder(int orderId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (orderId > 0) {
            detachedCriteria.add(Restrictions.eq("orderId", orderId));
        }
        detachedCriteria.add(Restrictions.eq("deleted", false));

        addOrder(detachedCriteria, "parentNumber,number".split(","), true);
        detachedCriteria.setFetchMode("donation", FetchMode.JOIN);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Поиск компонентов по статусу
	 * @param statusId - статус КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByStatusId(boolean showDeleted, int statusId, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (statusId > 0) {
            detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        addOrders(detachedCriteria, orderBy, orderAsc);
        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
	}
	
	/**
	 * Количество компонентов по статусу
	 * @param statusId - статус КК
	 * @return - количество компонентов
	 */
	public long countComponentsByStatusId(boolean showDeleted, int statusId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (statusId > 0) {
            detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        return getCountOf(detachedCriteria);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("purchased", true));
        detachedCriteria.add(Restrictions.ne("statusId", 100));
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	/**
	 * Количество закупленных компонентов
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countPurchasedDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("purchased", true));
        detachedCriteria.add(Restrictions.ne("statusId", 100));
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 2));
        detachedCriteria.add(Restrictions.le("quarantineFinishDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime()));
        addOrders(detachedCriteria, orderBy, orderAsc);
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	/**
	 * Количество компонентов с завершенным сроком карантинизации
	 * 
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countQuarantinedDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 2));
        detachedCriteria.add(Restrictions.le("quarantineFinishDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime()));
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            addOrders(detachedCriteria, orderBy, orderAsc);
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            
    		return getCountOf(getSearchCriteria(detachedCriteria, filter));
        }
        else {
        	return 0;
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            addOrders(detachedCriteria, orderBy, orderAsc);
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            
    		return getCountOf(getSearchCriteria(detachedCriteria, filter));
        }
        else {
        	return 0;
        }
	}
	
	
	/**
     * Поиск компонентов по номеру донации и номеру компонента
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return список компонентов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsByFullNumber(String parentNumber, String number, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("purchased", true));
        detachedCriteria.add(Restrictions.eq("parentNumber", parentNumber));
        detachedCriteria.add(Restrictions.eq("number", number));
        
		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
	}
	
	/**
	 * Подбор компонентов по фенотипам
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        detachedCriteria.add(Restrictions.eq("deleted", false));
        
        StringBuilder concat = new StringBuilder();
        StringBuilder testsSubQuery = new StringBuilder();
        boolean hasPhenotypes = phenotypes.size() == 0 ? false : true;
        
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
        	+ (hasPhenotypes? "and tests.type_id in (select id from trfu_analysis_types where " + testsSubQuery + ") " : "")
        	+ "group by components.id "
        	+ (hasPhenotypes? "having group_concat(concat('',tests.value) order by tests.id separator '')='" + concat + "'" : "");
        
        List<Integer> list = getSession().createSQLQuery(query).addScalar("id", Hibernate.INTEGER).list();
        
        if (list != null && list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
    		addOrders(detachedCriteria, orderBy, orderAsc);
            return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInControl(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(Restrictions.eq("inControl", true));
        addOrders(detachedCriteria, orderBy, orderAsc);

		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	public long countDocumentsInControl(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(Restrictions.eq("inControl", true));
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", statusId));
        detachedCriteria.add(Restrictions.eq("inControl", false));
		addOrders(detachedCriteria, orderBy, orderAsc);
        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	public long countControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", statusId));
		// TODO какую роль должно играть это ограничение, почему этого условия нет в основном запросе?
        // detachedCriteria.add(Restrictions.isNotEmpty("qualityControlList"));
        detachedCriteria.add(Restrictions.eq("inControl", false));
        return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", statusId));
        
        detachedCriteria.createAlias("history", "history", CriteriaSpecification.LEFT_JOIN);
        //detachedCriteria.add(Restrictions.eq("history.toStatusId", 2));
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("history.toStatusId", 2));
        disjunction.add(Restrictions.eq("history.fromStatusId", 2));
        detachedCriteria.add(disjunction);
        
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		List<BloodComponent> result = new ArrayList<BloodComponent>(new LinkedHashSet<BloodComponent>(getHibernateTemplate().findByCriteria(
				getSearchCriteria(detachedCriteria, filter), offset, count)));
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public long countQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", statusId));
        
        detachedCriteria.createAlias("history", "history", CriteriaSpecification.LEFT_JOIN);
        //detachedCriteria.add(Restrictions.eq("history.toStatusId", 2));
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("history.toStatusId", 2));
        disjunction.add(Restrictions.eq("history.fromStatusId", 2));
        detachedCriteria.add(disjunction);
        
        List<BloodComponent> result = new ArrayList<BloodComponent>(new LinkedHashSet<BloodComponent>(getHibernateTemplate().findByCriteria(
				getSearchCriteria(detachedCriteria, filter), -1, -1)));
        
        return result.size() * 1l;
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponent.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        String query = "SELECT comp.id FROM trfu_blood_components comp INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE wf.to_status_id = 3 AND wf.startDate = (SELECT max(wf_in.startDate) FROM trfu_blood_component_history hist_in " +
        		"INNER JOIN wf_history wf_in ON hist_in.history_entry_id = wf_in.id WHERE hist_in.component_id = comp.id) " +
        		"AND wf.to_status_id != comp.status_id and comp.orderId=0 and comp.status_id=10";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            
    		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory2() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponent.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
        String query = "SELECT id from(SELECT sum(CASE WHEN wf.to_status_id = 3 THEN 1 ELSE 0 END) AS gotov, " +
        		"sum(CASE WHEN wf.to_status_id = 10 THEN 1 ELSE 0 END) AS vidan, comp.id, comp.volume FROM trfu_blood_components comp " +
        		"INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE comp.status_id = 10 GROUP BY comp.id) as table1 where table1.gotov = 0 AND table1.vidan = 1";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	detachedCriteria.add(Restrictions.in("id", list));
            
    		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findSplitComponents() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponent.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
		detachedCriteria.add(Restrictions.eq("deleted", false));
		
		detachedCriteria.add(Restrictions.eq("split", true));
        
		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findUniquieSplitComponents() {
		//return getSession().createQuery("from BloodComponent c where c.split = 1 and c.donationId <> 0 group by c.donationId").list();
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY).
				add(Restrictions.eq("deleted", false));
		
		List list = getSession().createSQLQuery("select c.id from trfu_blood_components c where c.split = 1 and c.donationId <> 0 group by c.donationId").list();
		
		if (list.size() > 0) {
			criteria.add(Restrictions.in("id", list));
            
    		return getHibernateTemplate().findByCriteria(criteria);
		}
		else {
			return Collections.emptyList();
		}
		/*return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(BloodComponent.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY).
				add(Restrictions.eq("deleted", false)).add(Restrictions.eq("split", true)).add(Restrictions.ne("donationId", 0)).
				setProjection(Projections.groupProperty("donationId")));*/
	}
	
	@SuppressWarnings("unchecked")
	public BloodComponent findParentComponent(BloodComponent splitComponent) {
		/*return (BloodComponent) getSession().
				createQuery("from BloodComponent c where c.statusId = 100 and c.donationId = :donationId and c.parentNumber = :parentNumber and c.number = :number").
				setParameter("donationId", splitComponent.getDonationId()).
				setParameter("parentNumber", splitComponent.getParentNumber()).
				setParameter("number", splitComponent.getNumber()).
				uniqueResult();*/
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponent.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY).
				add(Restrictions.eq("deleted", false)).add(Restrictions.eq("donationId", splitComponent.getDonationId())).
				add(Restrictions.eq("parentNumber", splitComponent.getParentNumber())).add(Restrictions.eq("number", splitComponent.getNumber()));
        
		List<BloodComponent> list = getHibernateTemplate().findByCriteria(detachedCriteria);
		if (list.isEmpty()) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	public long countDocument(BloodComponentsFilter filter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addNotDeletedCriteria(detachedCriteria);
        addNotSplittedCriteria(detachedCriteria);
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	protected DetachedCriteria createDetachedCriteriaSub() {
		DetachedCriteria idsOnlyCriteria = DetachedCriteria.forClass(BloodComponent.class);
		idsOnlyCriteria.setProjection(Projections.distinct(Projections.id()));
		
		return idsOnlyCriteria;
	}

	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(
			BloodComponentsFilter filter, int offset, int count,
			String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteriaSub = createDetachedCriteriaSub();
		addNotDeletedCriteria(detachedCriteriaSub);
        if(filter.getStatusId() != 100) {
            addNotSplittedCriteria(detachedCriteriaSub);
        }
		detachedCriteriaSub = getSearchCriteria(detachedCriteriaSub, filter);
		
		DetachedCriteria criteria = createDetachedCriteria();
		List<?> ids = getHibernateTemplate().findByCriteria(detachedCriteriaSub);
		if (ids.size() == 0) {
			return new ArrayList<BloodComponent>();
		} 
		criteria.add(Restrictions.in("id", ids));
		addOrderCriteria(orderBy, orderAsc, criteria);
		return getHibernateTemplate().findByCriteria(criteria, offset, count);
	}

	private void addNotSplittedCriteria(DetachedCriteria detachedCriteria) {
		detachedCriteria.add(Restrictions.ne("statusId", 100));
	}
	
	protected DetachedCriteria getSearchCriteria(DetachedCriteria criteria,
			BloodComponentsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			int bloodComponentTypeId = filter.getBloodComponentTypeId();
			String donorCode = filter.getDonorCode();
			int makerId = filter.getMakerId();
			int bloodGroupId = filter.getBloodGroupId();
			int rhesusFactorId = filter.getRhesusFactorId();
			Date donationDateFrom = filter.getDonationDateFrom();
			Date donationDateTo = filter.getDonationDateTo();
			Date expirationDateFrom = filter.getExpirationDateFrom();
			Date expirationDateTo = filter.getExpirationDateTo();
			int statusId = filter.getStatusId();
			String fio = filter.getFio();
			boolean purchased = filter.isPurchased();
			Date historyCreatedDateFrom = filter.getHistoryCreatedDateFrom();
			Date historyCreatedDateTo = filter.getHistoryCreatedDateTo();
			
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
			if (donationDateFrom != null || donationDateTo != null) {
				addDateSearchCriteria(conjunction, donationDateFrom, donationDateTo, "donationDate");
			}
			if (expirationDateFrom != null || expirationDateTo != null) {
				addDateSearchCriteria(conjunction, expirationDateFrom, expirationDateTo, "expirationDate");
			}
			
			conjunction.add(Restrictions.eq("purchased", purchased));
			
			if (statusId != BloodComponentsFilter.BLOOD_COMPONENT_STATUS_NULL_VALUE) {
				criteria.createAlias("history", "history", Criteria.INNER_JOIN);
				conjunction.add(Restrictions.eq("history.toStatusId", statusId));
				if (historyCreatedDateFrom != null || historyCreatedDateTo != null) {
					addDateSearchCriteria(conjunction, historyCreatedDateFrom, historyCreatedDateTo, "history.created");
				}
		    }
	        if (bloodComponentTypeId != BloodComponentsFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("componentType.id", bloodComponentTypeId));
	        }
	        if (makerId != BloodComponentsFilter.MAKER_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("maker.id", makerId));
	        }
			if (StringUtils.isNotEmpty(fio)) {
	            criteria.createAlias("donation", "donation", CriteriaSpecification.INNER_JOIN);
	            criteria.createAlias("donation.donor", "donor", CriteriaSpecification.INNER_JOIN);
	            Disjunction disjunction = Restrictions.disjunction();
	            disjunction.add(Restrictions.ilike("donor.lastName", fio, MatchMode.ANYWHERE));
	            disjunction.add(Restrictions.ilike("donor.middleName", fio, MatchMode.ANYWHERE));
	            disjunction.add(Restrictions.ilike("donor.firstName", fio, MatchMode.ANYWHERE));
	            conjunction.add(disjunction);
			}	        
	        
			criteria.add(conjunction);
		}
        return criteria;
	}
	
	/**
     * Поиск документов
     *
     * @param filter          фильтр поиска
     * @param bloodComponentStatusIdList    список статусов КК
     * @param donationStatusIdList    список статусов донаций КК
     * @param showExpired     true - отображать просроченные, false - скрывать просроченные
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, List<Integer> bloodComponentStatusIdList, List<Integer> donationStatusIdList, boolean showExpired, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria = setVirusinaktivationCriteria(detachedCriteria, bloodComponentStatusIdList, donationStatusIdList, showExpired, showDeleted);
        addOrders(detachedCriteria, orderBy, orderAsc);

		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param bloodComponentStatusIdList    список статусов КК
     * @param donationStatusIdList    список статусов донаций КК
	 * @param showExpired     true - отображать просроченные, false - скрывать просроченные
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, List<Integer> bloodComponentStatusIdList, List<Integer> donationStatusIdList, boolean showExpired, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria = setVirusinaktivationCriteria(detachedCriteria, bloodComponentStatusIdList, donationStatusIdList, showExpired, showDeleted);
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	private DetachedCriteria setVirusinaktivationCriteria(DetachedCriteria detachedCriteria, List<Integer> bloodComponentStatusIdList, List<Integer> donationStatusIdList, boolean showExpired, boolean showDeleted) {
		
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        detachedCriteria.add(Restrictions.ne("donationId", 0));
        detachedCriteria.add(Restrictions.eq("inactivated", false));
        
        detachedCriteria.setFetchMode("donation", FetchMode.EAGER);
        detachedCriteria.createAlias("donation", "donation");
        detachedCriteria.setFetchMode("author", FetchMode.LAZY);
        detachedCriteria.setFetchMode("maker", FetchMode.LAZY);
        detachedCriteria.setFetchMode("anticoagulant", FetchMode.LAZY);
        detachedCriteria.setFetchMode("appointment", FetchMode.LAZY);
        detachedCriteria.setFetchMode("bloodGroup", FetchMode.LAZY);
        detachedCriteria.setFetchMode("rhesusFactor", FetchMode.LAZY);
        detachedCriteria.setFetchMode("preservative", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.donor", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.registrator", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.therapist", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.staffNurse", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.appointment", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.bloodSystems", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.donorType", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.examination", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.operationalCrew", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.report", FetchMode.LAZY);
        detachedCriteria.setFetchMode("donation.transfusiologist", FetchMode.LAZY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (!showExpired) {
        	Disjunction disjunction = Restrictions.disjunction();
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        	disjunction.add(Restrictions.ge("expirationDate", calendar.getTime()));
        	disjunction.add(Restrictions.isNull("expirationDate"));
        	detachedCriteria.add(disjunction);
        }
        if (!bloodComponentStatusIdList.isEmpty()) {
        	detachedCriteria.add(Restrictions.in("statusId", bloodComponentStatusIdList));
        }
        if (!donationStatusIdList.isEmpty()) {
        	detachedCriteria.add(Restrictions.in("donation.statusId", donationStatusIdList));
        }
        
        return detachedCriteria;
	}

    /**
     * Поиск КК по ШК (штрихкоду)
     * @param barcodeFilter  декодированный ШК
     * @param bloodComponentStatusIdList   список доступных статусов КК
     * @param donationStatusIdList    список доступных статусов донаций
     * @param showExpired    показывать с истекшим сроком
     * @param showDeleted   показывать удаленные
     * @param offset       смещение
     * @param count       макс количество возвращаемых строк
     * @param orderBy    сортировать по списку полей
     * @param orderAsc  сортировать по возрастанию
     * @return   список КК, удовлетворяющих условиям
     */
    @SuppressWarnings("unchecked")
    public List<BloodComponent> findVirusinactivatedDocumentsByFinalBarcode(
            final String barcodeFilter,
            final List<Integer> bloodComponentStatusIdList,
            final List<Integer> donationStatusIdList,
            final boolean showExpired,
            final boolean showDeleted,
            final int offset,
            final int count,
            final String orderBy,
            final boolean orderAsc) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria = setVirusinaktivationCriteria(detachedCriteria, bloodComponentStatusIdList, donationStatusIdList, showExpired, showDeleted);
        if(barcodeFilter != null && barcodeFilter.length() >=5) {
            //Логика поиска зависит от длины посиковой строки
            String donationNumber="";
            if(barcodeFilter.length() >=7) {
                //Больше или 7 символов - по номеру донации и номеру КК
                final String number = StringUtils.right(barcodeFilter, 2);
                detachedCriteria.add(Restrictions.eq("number", number));
                donationNumber = StringUtils.substring(barcodeFilter, barcodeFilter.length() - 7, barcodeFilter.length() - 2);
            } else {
                //5 символов справа - по номеру донации
                donationNumber = StringUtils.right(barcodeFilter, 5);
            }
            detachedCriteria.add(Restrictions.eq("parentNumber", donationNumber));
        }
        //Сортировка без добавления JOIN-ов, т.к. со вкалдки вирусинактивации сортировки только по тем вещам, которые уже в условии
        //TODO переписать сортировку с проверкой дубликации alias и с фактом наличия их
        if(orderBy.startsWith("componentType")){
            detachedCriteria.createAlias("componentType", "componentType");
        }
        detachedCriteria.addOrder(orderAsc ? Order.asc(orderBy) : Order.desc(orderBy));
        return getHibernateTemplate().findByCriteria(detachedCriteria,offset, count);
    }

    /**
     * Колитчество доступных КК по ШК (штрихкоду)
     * @param barcodeFilter  декодированный ШК
     * @param bloodComponentStatusIdList   список доступных статусов КК
     * @param donationStatusIdList    список доступных статусов донаций
     * @param showExpired    показывать с истекшим сроком
     * @param showDeleted   показывать удаленные
     * @return   общее кол-во КК, удовлетворяющих условиям
     */
    public long countVirusinactivatedDocumentsByFinalBarcode(
            final String barcodeFilter,
            final List<Integer> bloodComponentStatusIdList,
            final List<Integer> donationStatusIdList,
            boolean showExpired,
            boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria = setVirusinaktivationCriteria(detachedCriteria, bloodComponentStatusIdList, donationStatusIdList, showExpired, showDeleted);
        if(barcodeFilter != null && barcodeFilter.length() >= 7) {
            final String number = StringUtils.right(barcodeFilter, 2);
            final String donationNumber = StringUtils.substring(barcodeFilter, barcodeFilter.length() - 7, barcodeFilter.length() - 2);
            detachedCriteria.add(Restrictions.eq("parentNumber", donationNumber));
            detachedCriteria.add(Restrictions.eq("number", number));
        }
        return getCountOf(detachedCriteria);
    }
}