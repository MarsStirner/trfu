package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import ru.efive.dao.sql.dao.DictionaryDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;

import java.util.List;

@org.springframework.transaction.annotation.Transactional
public class BloodSystemTypeDAOImpl extends DictionaryDAOHibernate<BloodSystemType> {
    @Override
    protected Class<BloodSystemType> getPersistentClass() {
        return BloodSystemType.class;
    }

    public List<BloodSystemType> getByMnem(String mnem) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(BloodSystemType.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.eq("mnem", mnem));
        return (List<BloodSystemType>) getHibernateTemplate().findByCriteria(criteria);
    }
}
