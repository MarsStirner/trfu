package ru.efive.crm.dao;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.crm.data.Contragent;

public class ContragentDAOHibernate extends GenericDAOHibernate<Contragent> {
	
	@Override
	protected Class<Contragent> getPersistentClass() {
		return Contragent.class;
	}
	
	public List<Contragent> findDocuments(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            detachedCriteria.add(Restrictions.ilike("shortName", pattern + "%"));
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
		return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
	}
	
	public long countDocument(String pattern, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            detachedCriteria.add(Restrictions.ilike("fullName", pattern + "%"));
        }
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(detachedCriteria);
	}
	
	public Contragent getByFullName(String fullname)
	  {
	    if (StringUtils.isNotEmpty(fullname)) {
	      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Contragent.class);
	      detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

	      detachedCriteria.add(Restrictions.eq("fullName", fullname));

	      List contragents = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
	      if ((contragents != null) && (!contragents.isEmpty())) {
	        return (Contragent)contragents.get(0);
	      }
	      return null;
	    }

	    return null;
	  }
	
	public List<Contragent> getByStateCode(int stateCode) {
	    if (stateCode > 0) {
	      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Contragent.class);
	      detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

	      detachedCriteria.add(Restrictions.eq("stateCode", stateCode));

	      return getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
	    }
	    return Collections.emptyList();
	  }
	
	public Contragent getByOrganizationCode(int organizationCode) {
	    if (organizationCode > 0) {
	      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Contragent.class);
	      detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

	      detachedCriteria.add(Restrictions.eq("organizationCode", organizationCode));

	      List contragents = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
	      if ((contragents != null) && (!contragents.isEmpty())) {
	      	return (Contragent)contragents.get(0);
	      }
	      return null;
	    }
	    return null;
	  }
	
	public Contragent getByStateAndOrganizationCode(int stateCode, int organizationCode) {
	    if (organizationCode > 0 && stateCode > 0) {
	      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Contragent.class);
	      detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
	      
	      detachedCriteria.add(Restrictions.eq("stateCode", stateCode));
	      detachedCriteria.add(Restrictions.eq("organizationCode", organizationCode));
	      
	      List contragents = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
	      if ((contragents != null) && (!contragents.isEmpty())) {
	    	  return (Contragent)contragents.get(0);
	      }
	      return null;
	    }
	    return null;
	  }
	
	@SuppressWarnings("unchecked")
	public List<Contragent> getContragents() {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
}