package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;

public class DonorRejectionDAOImpl extends GenericDAOHibernate<DonorRejection> {
	
	@Override
	protected Class<DonorRejection> getPersistentClass() {
		return DonorRejection.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<DonorRejection> findDocumentsByRequestId(String requestId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (StringUtils.isNotEmpty(requestId)) {
			detachedCriteria.add(Restrictions.eq("request", requestId));
		}
		addOrder(detachedCriteria, "created", false);
		return getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
	}
	
}