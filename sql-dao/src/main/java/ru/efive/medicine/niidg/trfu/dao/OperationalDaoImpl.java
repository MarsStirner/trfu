package ru.efive.medicine.niidg.trfu.dao;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.operational.OperationalRoom;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 13.01.2015, 18:49 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
public class OperationalDaoImpl extends GenericDAOHibernate<OperationalRoom> {
    @Override
    protected Class<OperationalRoom> getPersistentClass() {
        return OperationalRoom.class;
    }

    public List<OperationalRoom> getRoomList(Date checkDate){
        final DetachedCriteria criteria = DetachedCriteria.forClass(OperationalRoom.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.ge("created", DateUtils.truncate(checkDate, Calendar.DATE)));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
