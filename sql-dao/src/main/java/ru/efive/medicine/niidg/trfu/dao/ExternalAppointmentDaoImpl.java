package ru.efive.medicine.niidg.trfu.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;

public class ExternalAppointmentDaoImpl extends GenericDAOHibernate<ExternalAppointment> {
	
	protected Class<ExternalAppointment> getPersistentClass() {
		return ExternalAppointment.class;
	}
	
	@SuppressWarnings("unchecked")
	public ExternalAppointment getAppoitment(int orderId) {
		ExternalAppointment result = null;
		if (orderId != 0) {
			DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("id", orderId));
			List<ExternalAppointment> list = getHibernateTemplate().findByCriteria(criteria, -1, -1);
			if (list != null && !list.isEmpty()) {
				result = list.get(0);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ExternalAppointment> getAppoitments(String orderBarCode) {
		List<ExternalAppointment> result = new ArrayList<ExternalAppointment>();
		if (StringUtils.isNotEmpty(orderBarCode)) {
			DetachedCriteria criteria = DetachedCriteria.forClass(ExaminationRequest.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("number", orderBarCode));
			List<ExaminationRequest> list = getHibernateTemplate().findByCriteria(criteria, -1, -1);
			Session session = getSessionFactory().openSession();
			try {
				if (list != null && !list.isEmpty()) {
					ExaminationRequest request = list.get(0);
					request = (ExaminationRequest) session.merge(request);
					if (request.getAppointment() != null) {
						Hibernate.initialize(request.getAppointment().getHistoryEntries());
						result.add(request.getAppointment());
					}
				}
			}
			finally {
				session.close();
			}
			
			criteria = DetachedCriteria.forClass(BloodDonationRequest.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("number", orderBarCode));
			List<BloodDonationRequest> list2 = getHibernateTemplate().findByCriteria(criteria, -1, -1);
			session = getSessionFactory().openSession();
			try {
				if (list2 != null && !list2.isEmpty()) {
					BloodDonationRequest request = list2.get(0);
					request = (BloodDonationRequest) session.merge(request);
					if (request.getAppointment() != null) {
						Hibernate.initialize(request.getAppointment().getHistoryEntries());
						result.add(request.getAppointment());
					}
				}
			}
			finally {
				session.close();
			}
		}
		return result;
	}
	
}