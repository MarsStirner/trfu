package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Division;

public class DivisionDAOImpl extends GenericDAOHibernate<Division> {
	
	@Override
	protected Class<Division> getPersistentClass() {
		return Division.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Division> findByName(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(name)) {
            detachedCriteria.add(Restrictions.ilike("name", name));
        }
        
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public Division findByExternalId(Serializable id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
		detachedCriteria.add(Restrictions.eq("externalId", id));
		List<Division> list = getHibernateTemplate().findByCriteria(detachedCriteria);

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		else {
			return null;
		}

	}
	
}