package ru.efive.medicine.niidg.trfu.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;

import java.util.List;

public class EmailTemplateDAOImpl extends GenericDAOHibernate<EmailTemplate> {

    @Override
    protected Class<EmailTemplate> getPersistentClass() {
        return EmailTemplate.class;
    }

    public EmailTemplate findByName(String name){
        if (StringUtils.isNotEmpty(name)){
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
            detachedCriteria.add(Restrictions.eq("name", name));
            List<EmailTemplate> list = getHibernateTemplate().findByCriteria(detachedCriteria);
            if(list.size()>0)
                return list.get(0);
        }
        return null;
    }
}
