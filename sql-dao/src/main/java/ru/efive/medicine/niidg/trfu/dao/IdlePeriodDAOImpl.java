package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.entity.IdlePeriod;

import java.util.Collections;
import java.util.List;

public class IdlePeriodDAOImpl extends GenericDAOHibernate<IdlePeriod> {
	
	@Override
	protected Class<IdlePeriod> getPersistentClass() {
		return IdlePeriod.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<IdlePeriod> findDocumentByComponentType(BloodComponentType componentType, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IdlePeriod.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (componentType != null && componentType.getId() != 0) {
        	detachedCriteria.createAlias("componentType", "componentType", JoinType.LEFT_OUTER_JOIN);
        	detachedCriteria.add(Restrictions.eq("componentType.id", componentType.getId()));
        	return (List<IdlePeriod>) getHibernateTemplate().findByCriteria(detachedCriteria);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
}