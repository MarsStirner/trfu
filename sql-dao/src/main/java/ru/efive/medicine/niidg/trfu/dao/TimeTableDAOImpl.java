package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@org.springframework.transaction.annotation.Transactional
public class TimeTableDAOImpl extends GenericDAOHibernate<TimeTableEntry> {
	
	@Override
	protected Class<TimeTableEntry> getPersistentClass() {
		return TimeTableEntry.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<TimeTableEntry> findUserTimeTable(User user, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0) {
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
            return (List<TimeTableEntry>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
        }
        else {
        	return Collections.EMPTY_LIST;
        }
	}
	
	public long countUserTimeTable(User user, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0) {
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	return getCountOf(detachedCriteria);
        }
        else {
        	return 0l;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<TimeTableEntry> findUserTimeTable(Date date, User user, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0 && date != null) {
        	 Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
             calendar.setTime(date);
             calendar.set(Calendar.DATE, 1);
             String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
             detachedCriteria.add(Restrictions.sqlRestriction("this_.startDate >= '" + formattedDate +"' and this_.startDate < date_add('" + formattedDate + "',interval 1 month)"));
             
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
            return (List<TimeTableEntry>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
        }
        else {
        	return Collections.EMPTY_LIST;
        }
	}
	
	public long countUserTimeTable(Date date, User user, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0 && date != null) {
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.setTime(date);
            calendar.set(Calendar.DATE, 1);
            String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            detachedCriteria.add(Restrictions.sqlRestriction("this_.startDate >= '" + formattedDate +"' and this_.startDate < date_add('" + formattedDate + "',interval 1 month)"));
            
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	return getCountOf(detachedCriteria);
        }
        else {
        	return 0l;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<TimeTableEntry> findUserTimeTable(Date startDate, Date endDate, User user, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0 && startDate != null) {
			Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
			calendar.setTime(startDate);
			String formattedStartDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			if (endDate != null) {
				calendar.setTime(endDate);
			}
			else {
				calendar.add(Calendar.DATE, 1);
			}
			String formattedEndDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			detachedCriteria.add(Restrictions.sqlRestriction("this_.startDate >= '" + formattedStartDate + "' and this_.startDate <= '" + formattedEndDate + "'"));
			
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
            return (List<TimeTableEntry>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
        }
        else {
        	return Collections.EMPTY_LIST;
        }
	}
	
	public long countUserTimeTable(Date startDate, Date endDate, User user, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (user != null && user.getId() > 0 && startDate != null) {
			Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
			calendar.setTime(startDate);
			String formattedStartDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			if (endDate != null) {
				calendar.setTime(endDate);
			}
			else {
				calendar.add(Calendar.DATE, 1);
			}
			String formattedEndDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			detachedCriteria.add(Restrictions.sqlRestriction("this_.startDate >= '" + formattedStartDate + "' and this_.startDate <= '" + formattedEndDate + "'"));
			
        	detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
        	return getCountOf(detachedCriteria);
        }
        else {
        	return 0l;
        }
	}
	
}