package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;

public class BloodComponentOrderRequestDAOImpl extends GenericDAOHibernate<BloodComponentOrderRequest> {
	
	@Override
	protected Class<BloodComponentOrderRequest> getPersistentClass() {
		return BloodComponentOrderRequest.class;
	}
	
	public List<BloodComponentOrderRequest> findDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
	}
	
	public long countDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter) {
		if (StringUtils.isNotEmpty(filter)) {
			Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("externalNumber", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("division", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("recipient", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("recipientMiddleName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("recipientFirstName", filter, MatchMode.ANYWHERE));
	        //disjunction.add(Restrictions.ilike("recipientAge", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("diagnosis", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("indication", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("attendingDoctor", filter, MatchMode.ANYWHERE));
	        
	        criteria.createAlias("componentType", "componentType", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("componentType.value", filter, MatchMode.ANYWHERE));
	        criteria.createAlias("staffNurse", "staffNurse", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("staffNurse.lastName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("staffNurse.middleName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("staffNurse.firstName", filter, MatchMode.ANYWHERE));
	        
	        criteria.add(disjunction);
		}
        return criteria;
	}
	
	/**
	 * Поиск обращений на заказ КК
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param donorId - идентификатор донора
	 * @return - список обращений на заказ КК
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponentOrderRequest> findComponentOrderRequestsByDonorId(boolean showDeleted, int donorId, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
	}
	
	/**
	 * Количество обращений на заказ КК
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countComponentOrderRequestsByDonorId(boolean showDeleted, int donorId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
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
	public List<BloodComponentOrderRequest> findDocumentsByStatus(int statusId, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		
		if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
		
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(
				DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
}