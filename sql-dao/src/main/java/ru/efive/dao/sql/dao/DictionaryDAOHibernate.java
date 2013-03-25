package ru.efive.dao.sql.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Интерфейс для управления справочными записями.
 * 
 * @author Alexey Vagizov
 *
 * @param <T> Класс наследующийся от DictionaryEntity
 */
public class DictionaryDAOHibernate<T extends DictionaryEntity> extends GenericDAOHibernate<T> implements DictionaryDAO<T> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findByValue(String value) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(value)) {
            detachedCriteria.add(Restrictions.ilike("value", value));
        }
        
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findDocuments() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countDocuments() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
		return getCountOf(detachedCriteria);
	}
	
}