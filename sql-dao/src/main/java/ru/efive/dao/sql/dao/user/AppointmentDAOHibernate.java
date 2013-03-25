package ru.efive.dao.sql.dao.user;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.dao.sql.entity.user.Appointment;

public class AppointmentDAOHibernate extends GenericDAOHibernate<Appointment> {
	
	@Override
	protected Class<Appointment> getPersistentClass() {
		return Appointment.class;
	}
	
    /**
     * Поиск ролей по названию
     *
     * @param value        название
     * @return список ролей
     */
	@SuppressWarnings("unchecked")
	public List<Appointment> findByName(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (StringUtils.isNotEmpty(name)) {
			detachedCriteria.add(Restrictions.eq("name", name));
        }
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
    
}