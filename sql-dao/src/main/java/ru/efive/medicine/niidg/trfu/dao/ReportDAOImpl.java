package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.dao.sql.entity.document.ReportTemplate;
import ru.efive.dao.sql.entity.user.Role;

public class ReportDAOImpl extends GenericDAOHibernate<ReportTemplate> {
	
	@Override
	protected Class<ReportTemplate> getPersistentClass() {
		return ReportTemplate.class;
	}
	
	@SuppressWarnings("unchecked")
	public ReportTemplate findTemplateByName(String templateName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        detachedCriteria.add(Restrictions.eq("templateName", templateName));
        
		List<ReportTemplate> list = getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
        } else {
            return null;
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReportTemplate> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        detachedCriteria.add(Restrictions.eq("displayInFolders", true));
        
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
        
        detachedCriteria.add(Restrictions.eq("displayInFolders", true));
        
		return getCountOf(detachedCriteria);
	}
	
	
	public List<ReportTemplate> findDocuments(Role currentRole, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        detachedCriteria.add(Restrictions.eq("displayInFolders", true));
        
        detachedCriteria.createAlias("roles", "roles");
        detachedCriteria.add(Restrictions.eq("roles.id", currentRole.getId()));
        
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
	
	public long countDocument(Role currentRole, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        detachedCriteria.add(Restrictions.eq("displayInFolders", true));
        
        detachedCriteria.createAlias("roles", "roles");
        detachedCriteria.add(Restrictions.eq("roles.id", currentRole.getId()));
        
		return getCountOf(detachedCriteria);
	}
	
}