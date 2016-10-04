package ru.efive.medicine.niidg.trfu.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;

import java.util.ArrayList;
import java.util.List;

@org.springframework.transaction.annotation.Transactional
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
			List<ExternalAppointment> list = (List<ExternalAppointment>) getHibernateTemplate().findByCriteria(criteria, -1, -1);
			if (list != null && !list.isEmpty()) {
				result = list.get(0);
			}
		}
		return result;
	}

	public ExternalAppointment getWithHistory(int orderId){
		final DetachedCriteria criteria = DetachedCriteria.forClass(ExternalAppointment.class)
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.idEq(orderId)).createAlias("historyEntries", "history", JoinType.LEFT_OUTER_JOIN);
		final List result = getHibernateTemplate().findByCriteria(criteria, -1, 1);
		return result.iterator().hasNext() ? (ExternalAppointment) result.iterator().next() : null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ExternalAppointment> getAppoitments(String orderBarCode) {
		List<ExternalAppointment> result = new ArrayList<>();
		if (StringUtils.isNotEmpty(orderBarCode)) {
			DetachedCriteria criteria = DetachedCriteria.forClass(ExaminationRequest.class).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
			criteria.add(Restrictions.eq("number", orderBarCode));
			List<ExaminationRequest> list = (List<ExaminationRequest>) getHibernateTemplate().findByCriteria(criteria, -1, -1);
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
			List<BloodDonationRequest> list2 = (List<BloodDonationRequest>) getHibernateTemplate().findByCriteria(criteria, -1, -1);
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