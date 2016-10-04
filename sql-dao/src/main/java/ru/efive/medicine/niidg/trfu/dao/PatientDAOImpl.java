package ru.efive.medicine.niidg.trfu.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Patient;

import java.util.List;

@org.springframework.transaction.annotation.Transactional
public class PatientDAOImpl extends GenericDAOHibernate<Patient> {
	
	@Override
	protected Class<Patient> getPersistentClass() {
		return Patient.class;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Patient> findDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            LogicalExpression orExp = Restrictions.or(Restrictions.ilike("lastName", pattern + "%"), 
            		Restrictions.ilike("middleName", pattern + "%"));
            orExp = Restrictions.or(orExp, Restrictions.ilike("firstName", pattern + "%"));
            detachedCriteria.add(orExp);
        }
        
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
		return (List<Patient>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
	}
	
	public long countDocument(String pattern, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            LogicalExpression orExp = Restrictions.or(Restrictions.ilike("lastName", pattern + "%"), 
            		Restrictions.ilike("middleName", pattern + "%"));
            orExp = Restrictions.or(orExp, Restrictions.ilike("firstName", pattern + "%"));
            detachedCriteria.add(orExp);
        }
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(detachedCriteria);
	}
	
}