package ru.efive.medicine.niidg.trfu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class DonorRejectionDAOImpl extends GenericDAOHibernate<DonorRejection> {
	
	@Override
	protected Class<DonorRejection> getPersistentClass() {
		return DonorRejection.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<DonorRejection> findDocumentsByRequestId(String requestId) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		if (StringUtils.isNotEmpty(requestId)) {
			detachedCriteria.add(Restrictions.eq("request", requestId));
		}
		addOrder(detachedCriteria, "created", false);
		List<DonorRejection> rejections =  getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
		if (DonorHelper.USE_SRPD) {
			DonorHelper donorHelper = new DonorHelper();
			Map<FieldsInMap, Object> paramMap = new HashMap<FieldsInMap, Object>();
			paramMap.put(FieldsInMap.LIST_STORAGE_IDS, donorHelper.listIdsSRPDFromDonorRejectionRequest(rejections));
			Map<String, Map<FieldsInMap, Object>> resMap = new SRPDDao().get(paramMap);
			rejections = donorHelper.mergeDonorRejectionsAndMap(rejections, resMap);
		}
		return rejections;
	}
	
}