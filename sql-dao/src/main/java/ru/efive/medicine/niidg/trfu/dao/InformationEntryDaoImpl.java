package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.integration.InformationEntry;

public class InformationEntryDaoImpl extends GenericDAOHibernate<InformationEntry> {
	
	@Override
	protected Class<InformationEntry> getPersistentClass() {
		return InformationEntry.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<InformationEntry> findDocuments(boolean showPublished, boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (showPublished) {
        	detachedCriteria.add(Restrictions.eq("published", true));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
	}
	
	public long countDocument(boolean showPublished, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (showPublished) {
        	detachedCriteria.add(Restrictions.eq("published", true));
        }
        
		return getCountOf(detachedCriteria);
	}
	
}