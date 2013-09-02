package ru.efive.medicine.niidg.trfu.dao;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.filters.BloodDonationsFilter;

public class BloodDonationRequestDAOImpl extends GenericDAOHibernate<BloodDonationRequest> {
	
	@Override
	protected Class<BloodDonationRequest> getPersistentClass() {
		return BloodDonationRequest.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BloodDonationRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
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
	 * {@inheritDoc}
	 */
	@Override
	public long countDocument(boolean showDeleted) {
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

	/**
     * Кол-во документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(
				DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter) {
		if (StringUtils.isNotEmpty(filter)) {
			Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
	        criteria.createAlias("donorType", "donorType", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("donorType.value", filter, MatchMode.ANYWHERE));
	        criteria.createAlias("donor", "donor", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("donor.lastName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("donor.middleName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("donor.firstName", filter, MatchMode.ANYWHERE));
	        /*criteria.createAlias("transfusiologist", "transfusiologist", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("transfusiologist.lastName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("transfusiologist.middleName", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("transfusiologist.firstName", filter, MatchMode.ANYWHERE));*/
	        
	        /*DetachedCriteria childCriteria = criteria.createAlias("entries", "entries");
	        childCriteria.createAlias("entries.donationType", "donationType");
	        childCriteria.add(Restrictions.ilike("donationType.value", filter, MatchMode.ANYWHERE));
	        childCriteria.setProjection(Projections.property("BloodDonationRequest.id"));
	        disjunction.add(Subqueries.propertyIn("id", childCriteria));*/
	        
	        criteria.createAlias("factEntries", "factEntries", CriteriaSpecification.LEFT_JOIN);
	        criteria.createAlias("factEntries.donationType", "donationType", CriteriaSpecification.LEFT_JOIN);
	        disjunction.add(Restrictions.ilike("donationType.value", filter));
	        
	        criteria.add(disjunction);
		}
        return criteria;
	}
	
	/**
	 * Поиск обращений на донацию
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param donorId - идентификатор донора
	 * @return - список обращений на донацию
	 */
	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDonationRequestsByDonorId(boolean showDeleted, int donorId, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (donorId > 0) {
	        detachedCriteria.add(Restrictions.eq("donor.id", donorId));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (donorId > 0) {
	        detachedCriteria.add(Restrictions.eq("donor.id", donorId));
			return getCountOf(detachedCriteria);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (personId > 0) {
        	detachedCriteria.add(Restrictions.eq("transfusiologist.id", personId));
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (personId > 0) {
        	detachedCriteria.add(Restrictions.eq("transfusiologist.id", personId));
        	return getCountOf(detachedCriteria);
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

    public BloodDonationRequest findDocumentByBarCode(String barCode){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(
                DetachedCriteria.DISTINCT_ROOT_ENTITY);
        detachedCriteria.add(Restrictions.eq("deleted", false));
        detachedCriteria.add(Restrictions.eq("number", barCode));
        List list= getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list!=null && list.size()>0)
            return (BloodDonationRequest) list.get(0);
        return null;
    }

	public long countDocument(BloodDonationsFilter filter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}

	protected DetachedCriteria getSearchCriteria(DetachedCriteria criteria,
			BloodDonationsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			String donor = filter.getDonor();
			Date created = filter.getCreated();
			int statusId = filter.getStatusId();
			int donorTypeId = filter.getDonorTypeId();
			int donationTypeId = filter.getDonationTypeId();

			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number,
						MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(donor)) {
	            criteria.createAlias("donor", "donor", CriteriaSpecification.INNER_JOIN);
	            Disjunction disjunction = Restrictions.disjunction();
	            disjunction.add(Restrictions.ilike("donor.lastName", donor, MatchMode.ANYWHERE));
	            disjunction.add(Restrictions.ilike("donor.middleName", donor, MatchMode.ANYWHERE));
	            disjunction.add(Restrictions.ilike("donor.firstName", donor, MatchMode.ANYWHERE));
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
		        criteria.createAlias("factEntries", "factEntries", CriteriaSpecification.INNER_JOIN);
		        conjunction.add(Restrictions.eq("factEntries.donationType.id", donationTypeId));
	        }

			criteria.add(conjunction);
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<BloodDonationRequest> findDocuments(
			BloodDonationsFilter filter, int offset, int count, String orderBy,
			boolean orderAsc) {
		DetachedCriteria detachedCriteriaSub = createDetachedCriteriaSub();
		addNotDeletedCriteria(detachedCriteriaSub);
		detachedCriteriaSub = getSearchCriteria(detachedCriteriaSub, filter);
		
		DetachedCriteria criteria = createDetachedCriteria();
		addOrderCriteria(orderBy, orderAsc, criteria);
		criteria.add(Restrictions.in("id", getHibernateTemplate().findByCriteria(detachedCriteriaSub, offset, count)));
		
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	protected DetachedCriteria createDetachedCriteriaSub() {
		DetachedCriteria idsOnlyCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
		idsOnlyCriteria.setProjection(Projections.distinct(Projections.id()));
		
		return idsOnlyCriteria;
	}
}