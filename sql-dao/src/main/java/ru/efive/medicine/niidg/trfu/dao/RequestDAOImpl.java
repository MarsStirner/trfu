package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;

public class RequestDAOImpl extends GenericDAOHibernate<AbstractRequest> {
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked") @Override
	public List<AbstractRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
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
		List<AbstractRequest> result = getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
		
		detachedCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		result.addAll(getHibernateTemplate().findByCriteria(detachedCriteria, offset, count));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countDocument(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        long result = getCountOf(detachedCriteria);
        
        detachedCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        result = result + getCountOf(detachedCriteria);
        
		return result;
	}
	
	/**
	 * Поиск обращений
	 * @param donorId - идентификатор донора
	 * @return - список обращений
	 */
	@SuppressWarnings("unchecked")
	public List<AbstractRequest> findRequestsByDonor(boolean showDeleted, int donorId, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		if (donorId > 0) {
			detachedCriteria.add(Restrictions.eq("donor.id", donorId));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			}
			else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		List<AbstractRequest> result = getHibernateTemplate().findByCriteria(detachedCriteria);
		
		detachedCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		if (donorId > 0) {
			detachedCriteria.add(Restrictions.eq("donor.id", donorId));
        }
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			}
			else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		result.addAll(getHibernateTemplate().findByCriteria(detachedCriteria));
		
		return result;
	}

	/**
	 * Количество обращений по донору
	 * @param donorId - идентификатор донора
	 * @return - количество
	 */
	public long countRequestsByDonor(boolean showDeleted, int donorId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		if (donorId > 0) {
			detachedCriteria.add(Restrictions.eq("donor.id", donorId));
        }
		long result = getCountOf(detachedCriteria);
		
		detachedCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		if (donorId > 0) {
			detachedCriteria.add(Restrictions.eq("donor.id", donorId));
        }
		result = result + getCountOf(detachedCriteria);
		
		return result;
	}
	
	
}