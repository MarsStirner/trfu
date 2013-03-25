package ru.efive.medicine.niidg.trfu.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationEntry;

public class ExaminationEntryDAOImpl extends GenericDAOHibernate<ExaminationEntry> {
	
	@Override
	protected Class<ExaminationEntry> getPersistentClass() {
		return ExaminationEntry.class;
	}
	
	/**
	 * Поиск записей осмотра
	 * @param examinationRequestId - идентификатор обращения на обследование
	 * @return - список записей осмотра
	 */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntry> findDocuments(int examinationRequestId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (examinationRequestId > 0) {
            detachedCriteria.add(Restrictions.eq("examinationRequest.id", examinationRequestId));
        }
		
        addOrder(detachedCriteria, "parent.id", true);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Поиск записей осмотра верхнего уровня (базовых)
	 * @param examinationRequestId - идентификатор обращения на обследование
	 * @return - список записей осмотра
	 */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntry> findBaseEntries(int examinationRequestId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (examinationRequestId > 0) {
            detachedCriteria.add(Restrictions.eq("examinationRequest.id", examinationRequestId));
        }
        detachedCriteria.add(Restrictions.isNull("parentEntry.id"));
		
        addOrder(detachedCriteria, "id", true);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Поиск дочерних записей осмотра
	 * @param examinationRequestId - идентификатор обращения на обследование
	 * @param parentId - идентификатор базовой записи
	 * @return - список записей осмотра
	 */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntry> findChildEntries(int examinationRequestId, int parentId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (examinationRequestId > 0) {
            detachedCriteria.add(Restrictions.eq("examinationRequest.id", examinationRequestId));
        }
        if (parentId > 0) {
            detachedCriteria.add(Restrictions.eq("parentEntry.id", parentId));
        }
        
        addOrder(detachedCriteria, "id", true);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
}