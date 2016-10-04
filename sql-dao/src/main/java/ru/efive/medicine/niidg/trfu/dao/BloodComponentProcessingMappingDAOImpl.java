package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentProcessingMapping;

import java.util.List;

@org.springframework.transaction.annotation.Transactional
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
        detachedCriteria.createAlias("sourceTypes", "sourceTypes", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.add(Restrictions.eq("sourceTypes.id", componentType.getId()));
        
		List<BloodComponentProcessingMapping> list = (List<BloodComponentProcessingMapping>) getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
        }
		else {
            return null;
        }
	}
	
}