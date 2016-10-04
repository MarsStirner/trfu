package ru.efive.medicine.niidg.trfu.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentOrdersFilter;

import java.util.Date;
import java.util.List;
@org.springframework.transaction.annotation.Transactional
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
		return (List<BloodComponentOrderRequest>) getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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
	        
	        criteria.createAlias("componentType", "componentType", JoinType.LEFT_OUTER_JOIN);
	        disjunction.add(Restrictions.ilike("componentType.value", filter, MatchMode.ANYWHERE));
	        criteria.createAlias("staffNurse", "staffNurse", JoinType.LEFT_OUTER_JOIN);
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
        return (List<BloodComponentOrderRequest>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
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
		
		return (List<BloodComponentOrderRequest>) getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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

	@SuppressWarnings("unchecked")
	public List<BloodComponentOrderRequest> findDocuments(
			BloodComponentOrdersFilter filter, int offset, int count,
			String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		return (List<BloodComponentOrderRequest>) getHibernateTemplate().findByCriteria(
				getSearchCriteria(detachedCriteria, filter), offset, count
		);
	}

	public long countDocument(BloodComponentOrdersFilter filter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addNotDeletedCriteria(detachedCriteria);
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}

	protected DetachedCriteria getSearchCriteria(
			DetachedCriteria criteria, BloodComponentOrdersFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			String ibNumber = filter.getIbNumber();
			Date createdFrom = filter.getCreatedFrom();
			Date createdTo = filter.getCreatedTo();
			Date factDateFrom = filter.getFactDateFrom();
			Date factDateTo = filter.getFactDateTo();
			Date recipientBirthFrom = filter.getRecipientBirthFrom();
			Date recipientBirthTo = filter.getRecipientBirthTo();
			String recipientFirstName = filter.getRecipientFirstName();
			String recipient = filter.getRecipient();
			String recipientMiddleName = filter.getRecipientMiddleName();
			int bloodGroupId = filter.getBloodGroupId();
			int rhesusFactorId = filter.getRhesusFactorId();
			int bloodComponentTypeId = filter.getBloodComponentTypeId();
			String division = filter.getDivision();
			int transfusionTypeId = filter.getTransfusionTypeId();
			
			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(ibNumber)) {
				conjunction.add(Restrictions.ilike("ibNumber", ibNumber, MatchMode.ANYWHERE));
			}
			if (createdFrom != null || createdTo != null) {
				addDateSearchCriteria(conjunction, createdFrom, createdTo, "created");
			}
			if (factDateFrom != null || factDateTo != null) {
				addDateSearchCriteria(conjunction, factDateFrom, factDateTo, "factDate");
			}
			if (recipientBirthFrom != null || recipientBirthTo != null) {
				addDateSearchCriteria(conjunction, recipientBirthFrom, recipientBirthTo, "recipientBirth");
			}
			if (StringUtils.isNotEmpty(recipient)) {
				conjunction.add(Restrictions.ilike("recipient", recipient, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(recipientFirstName)) {
				conjunction.add(Restrictions.ilike("recipientFirstName", recipientFirstName, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(recipientMiddleName)) {
				conjunction.add(Restrictions.ilike("recipientMiddleName", recipientMiddleName, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(division)) {
				conjunction.add(Restrictions.ilike("division", division, MatchMode.ANYWHERE));
			}
			if (bloodGroupId != AbstractFilter.BLOOD_GROUP_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("bloodGroup.id", bloodGroupId));
			}
			if (rhesusFactorId != AbstractFilter.RHESUS_FACTOR_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("rhesusFactor.id", rhesusFactorId));
			}
			if (bloodComponentTypeId != AbstractFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("componentType.id", bloodComponentTypeId));
			}
			if (transfusionTypeId != BloodComponentOrdersFilter.TRANSFUSION_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("transfusionType", transfusionTypeId));
			}
			
			criteria.add(conjunction);
		}
        return criteria;
	}

	public BloodComponentOrderRequest findComponentOrderRequestByExternalNumber(final String externalNumber) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.add(Restrictions.eq("deleted", false));
		detachedCriteria.add(Restrictions.eq("fromMIS", true));
		detachedCriteria.add(Restrictions.eq("externalNumber", externalNumber));
		final List list = getHibernateTemplate().findByCriteria(detachedCriteria);
		if(!list.isEmpty()){
			return (BloodComponentOrderRequest) list.get(0);
		} else {
			return null;
		}

	}
}