package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.QuarantineType;

import java.util.Collections;
import java.util.List;

@org.springframework.transaction.annotation.Transactional
public class QuarantineTypeDAOImpl extends GenericDAOHibernate<QuarantineType> {
	
	@Override
	protected Class<QuarantineType> getPersistentClass() {
		return QuarantineType.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<QuarantineType> findDocumentsByComponentType(int componentTypeId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (componentTypeId != 0) {
        	detachedCriteria.createAlias("componentType", "componentType", JoinType.LEFT_OUTER_JOIN);
        	detachedCriteria.add(Restrictions.eq("componentType.id", componentTypeId));
        	return (List<QuarantineType>) getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
}