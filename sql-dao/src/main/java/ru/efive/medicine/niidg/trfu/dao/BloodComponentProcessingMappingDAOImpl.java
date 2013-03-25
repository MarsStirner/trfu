package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentProcessingMapping;

public class BloodComponentProcessingMappingDAOImpl extends GenericDAOHibernate<BloodComponentProcessingMapping> {
	
	@Override
	protected Class<BloodComponentProcessingMapping> getPersistentClass() {
		return BloodComponentProcessingMapping.class;
	}
	
	@SuppressWarnings("unchecked")
	public BloodComponentProcessingMapping findMapping(BloodComponentType componentType, int processingType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        detachedCriteria.add(Restrictions.eq("processingType", processingType));
        detachedCriteria.createAlias("sourceTypes", "sourceTypes", CriteriaSpecification.LEFT_JOIN);
        detachedCriteria.add(Restrictions.eq("sourceTypes.id", componentType.getId()));
        
		List<BloodComponentProcessingMapping> list = getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
        }
		else {
            return null;
        }
	}
	
}