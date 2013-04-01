package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.DictionaryDAOHibernate;
import ru.efive.dao.sql.entity.DictionaryEntity;
import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.*;

public class DictionaryDAOImpl extends DictionaryDAOHibernate<DictionaryEntity> {
	
	public <T extends IdentifiedEntity> T get(Class<T> persistentClass, Serializable id) {
		return (T) getHibernateTemplate().get(persistentClass, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked") @Override
	public List<DictionaryEntity> findByValue(String value) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (StringUtils.isNotEmpty(value)) {
			detachedCriteria.add(Restrictions.eq("value", value));
        }
		
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponentType> findComponentTypeByValue(String value) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponentType.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (StringUtils.isNotEmpty(value)) {
			int pos = value.indexOf(" ");
			//System.out.println("DictionaryDAO: поиск по типу компонента крови, позиция первого разделителя " + pos);
			String code = StringUtils.left(value, pos);
			String val = StringUtils.right(value, StringUtils.length(value) - pos - 1);
			//System.out.println("DictionaryDAO: поиск по типу компонента крови, code = " + code + ", value = " + val);
			detachedCriteria.add(Restrictions.eq("code", code));
			detachedCriteria.add(Restrictions.eq("value", val));
        }
		
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
     * Поиск справочных данных по значению
     *
     * @param value        значение из справочника
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public <T extends DictionaryEntity> List<T> findByValue(Class<T> persistentClass, String value) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(persistentClass);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (StringUtils.isNotEmpty(value)) {
			detachedCriteria.add(Restrictions.eq("value", value));
        }
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
     * Поиск справочных данных по категории
     *
     * @param category        категория справочника
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public <T extends DictionaryEntity> List<T> findByCategory(Class<T> persistentClass, String category, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(persistentClass);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		
		if (StringUtils.isNotEmpty(category)) {
			detachedCriteria.add(Restrictions.eq("category", category));
        }
		
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findClassifierCategories() {
		Query query = getSession().createQuery("select category from Classifier as classifier group by classifier.category");
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked") @Override
	public List<DictionaryEntity> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		
		return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countDocument(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(detachedCriteria);
	}
	
	
	/**
     * Поиск антикоагулянтов
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список антикоагулянтов
     */
	@SuppressWarnings("unchecked")
	public List<Anticoagulant> findAnticoagulants(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Anticoagulant.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество антикоагулянтов
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countAnticoagulants(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Anticoagulant.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	
	/**
     * Поиск типов компонентов крови
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов компонентов крови
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponentType> findBloodComponentTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponentType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	public List<BloodComponentType> findBloodComponentTypes(String filter, boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponentType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (StringUtils.isNotEmpty(filter)) {
        	detachedCriteria.add(Restrictions.ilike("value", filter, MatchMode.ANYWHERE));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов компонентов крови
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countBloodComponentTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponentType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	public List<BloodComponentType> findUsedBloodComponentTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodComponentType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("used", true));
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	
	/**
     * Поиск типов донации
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов донации
     */
	@SuppressWarnings("unchecked")
	public List<BloodDonationType> findBloodDonationTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodDonationType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов донации
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countBloodDonationTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodDonationType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	
	/**
     * Поиск групп крови
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список групп крови
     */
	@SuppressWarnings("unchecked")
	public List<BloodGroup> findBloodGroups(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodGroup.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество групп крови
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countBloodGroups(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodGroup.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск группы крови по номеру
     *
     * @param number          номер группы крови
     * @return группа крови
     */
	@SuppressWarnings("unchecked")
	public BloodGroup findBloodGroupByNumber(int number) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodGroup.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        detachedCriteria.add(Restrictions.eq("number", number));
        List<BloodGroup> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list != null && list.size() > 0) {
        	return list.get(0);
        }
        else {
        	return null;
        }
	}
	
	/**
     * Поиск типов доноров
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов доноров
     */
	@SuppressWarnings("unchecked")
	public List<DonorType> findDonorTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов доноров
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countDonorTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	
	/**
     * Поиск типов обработки
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов обработки
     */
	@SuppressWarnings("unchecked")
	public List<ProcessingType> findProcessingTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessingType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов обработки
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countProcessingTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessingType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск типов анализов
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов анализов
     */
	@SuppressWarnings("unchecked")
	public List<AnalysisType> findAnalysisTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AnalysisType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов анализов
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countAnalysisTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AnalysisType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск типов анализов по категории
     * @param category        категория анализов
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return список типов анализов
     */
	@SuppressWarnings("unchecked")
	public List<AnalysisType> findAnalysisTypes(String category, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AnalysisType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (StringUtils.isNotEmpty(category)) {
            detachedCriteria.add(Restrictions.eq("category", category));
        }
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	public List<AnalysisType> findAnalysisTypes(String filter, String category, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AnalysisType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (StringUtils.isNotEmpty(filter)) {
        	detachedCriteria.add(Restrictions.ilike("value", filter, MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(category)) {
            detachedCriteria.add(Restrictions.eq("category", category));
        }
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
     * Поиск типов анализов по категории
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return список типов анализов
     */
	@SuppressWarnings("unchecked")
	public List<AnalysisType> findSecondaryAnalysisTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AnalysisType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.ne("category", "Первичный"));
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
     * Поиск типов записей осмотра
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов записей осмотра
     */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntryType> findExaminationEntryTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationEntryType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов записей осмотра
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countExaminationEntryTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationEntryType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск типов записей осмотра верхнего уровня (базовых)
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return список типов записей осмотра верхнего уровня (базовых)
     */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntryType> findBaseExaminationEntryTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationEntryType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.isNull("parentEntry.id"));
        addOrder(detachedCriteria, "id", true);
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Поиск дочерних типов записей осмотра
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @param parentId - идентификатор базовой записи
	 * @return - список дочерних типов записей осмотра
	 */
	@SuppressWarnings("unchecked")
	public List<ExaminationEntryType> findChildExaminationEntryTypes(boolean showDeleted, int parentId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationEntryType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (parentId > 0) {
            detachedCriteria.add(Restrictions.eq("parentEntry.id", parentId));
        }
        addOrder(detachedCriteria, "id", true);
        
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	
	/**
     * Поиск типов отвода
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов отвода
     */
	@SuppressWarnings("unchecked")
	public List<DonorRejectionType> findDonorRejectionTypes(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorRejectionType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов отвода
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countDonorRejectionTypes(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorRejectionType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск типов отвода
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов отвода
     */
	@SuppressWarnings("unchecked")
	public List<DonorRejectionType> findDonorRejectionTypes(String filter, boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorRejectionType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter));
	}
	
	/**
	 * Количество типов отвода
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countDonorRejectionTypes(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DonorRejectionType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	
	/**
     * Поиск типов Классификаторов
     *
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов отвода
     */
	@SuppressWarnings("unchecked")
	public List<Classifier> findClassifier(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов классификаторов
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countClassifier(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	/**
     * Поиск типов классификаторов
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список типов отвода
     */
	@SuppressWarnings("unchecked")
	public List<Classifier> findClassifier(String filter, boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (StringUtils.isNotEmpty(filter)) {
	        Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("value", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("category", filter, MatchMode.ANYWHERE));
	        detachedCriteria.add(disjunction);
        }
        
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * Количество типов классификаторов
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return - количество
	 */
	public long countClassifier(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Classifier.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (StringUtils.isNotEmpty(filter)) {
	        Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("value", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("category", filter, MatchMode.ANYWHERE));
	        detachedCriteria.add(disjunction);
        }
        
		return getCountOf(detachedCriteria);
	}
	
	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter) {
		if (StringUtils.isNotEmpty(filter)) {
			Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("value", filter, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("code", filter, MatchMode.ANYWHERE));
	        criteria.add(disjunction);
		}
        return criteria;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<QualityControlMappingEntry> findQualityControlMappingEntries(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(QualityControlMappingEntry.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	public long countQualityControlMappingEntries(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(QualityControlMappingEntry.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	@SuppressWarnings("unchecked")
	public QualityControlMappingEntry findQualityControlMappingEntries(BloodComponentType componentType, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(QualityControlMappingEntry.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.createAlias("componentTypes", "componentTypes", CriteriaSpecification.LEFT_JOIN);
        detachedCriteria.add(Restrictions.eq("componentTypes.id", componentType.getId()));
        List<QualityControlMappingEntry> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if (list != null && list.size() > 0) {
        	return list.get(0);
        }
        else {
        	return null;
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TimeTableSpecialDay> findSpecialDays(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TimeTableSpecialDay.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	public long countSpecialDays(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TimeTableSpecialDay.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Recommendation> findRecommendations(boolean showDeleted, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Recommendation.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
        return getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	public long countRecommendations(boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Recommendation.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
        	detachedCriteria.add(Restrictions.eq("deleted", false));
        }
		return getCountOf(detachedCriteria);
	}

    public List<BloodSystemType> findBloodSystemTypes(boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BloodSystemType.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        addOrder(detachedCriteria, "id", true);
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }

	/**
	 * Список полов.
	 */
	protected List<Gender> genders = null;
	/**
	 * Список статусов доноров. На самом деле список статусов доноров является справочной информацией 
	 * и такую информацию желательно хранить в базе данных. Как альтернатива для работы можно завести подобный 
	 * список и вести с ним работу через методы {@link #getDonorStatuses()} и {@link #getDonorStatus(int)} или их аналоги. 
	 * Делать это было необходимо с самого начала реализации приложения, а не использовать код наподобии 
	 * {@link ru.efive.medicine.niidg.trfu.util.ApplicationHelper#getStatusName(String, int)}. 
	 * Данный список и методы {@link #getDonorStatuses()} и {@link #getDonorStatus(int)}
	 * были введены для удобства работы во время реализации фильтра для представления "Доноры".
	 */
	protected List<DonorStatus> donorStatuses = null;
	/**
	 * Список статусов компонентов крови. Со списком этих статусов в приложении такая же ситуация, 
	 * как и со списком статусов доноров.
	 */
	protected List<BloodComponentStatus> bloodComponentStatuses = null;
	/**
	 * Список статусов обращений на обследование. Со списком этих статусов в приложении такая же ситуация, 
	 * как и со списком статусов доноров.
	 */
	protected List<ExaminationStatus> examinationStatuses = null;
	/**
	 * Список статусов донаций крови. Со списком этих статусов в приложении такая же ситуация, 
	 * как и со списком статусов доноров.
	 */
	protected List<BloodDonationStatus> bloodDonationStatuses = null;
	/**
	 * Список типов обращений на обследование. 
	 */
	protected List<ExaminationType> examinationTypes = null;
	/**
	 * Список видов трансфузии. 
	 */
	protected List<TransfusionType> transfusionTypes = null;

	/**
	 * Получение списка полов (женский, мужской).
	 * @return список полов.
	 */
	public List<Gender> getGenders() {
		if (genders == null) {
			genders = new ArrayList<Gender>();
			genders.add(new Gender(0, "Ж"));
			genders.add(new Gender(1, "М"));
		}		
		return genders;
	}

	/**
	 * Получение списка статусов доноров.
	 * @return список статусов доноров.
	 */
	public List<DonorStatus> getDonorStatuses() {
		if (donorStatuses == null) {
			donorStatuses = new ArrayList<DonorStatus>();
			donorStatuses.add(new DonorStatus(1, "Кандидат"));
			donorStatuses.add(new DonorStatus(2, "Донор"));
			donorStatuses.add(new DonorStatus(-1, "Временный отвод"));
			donorStatuses.add(new DonorStatus(-2, "Абсолютный отвод"));
		}		
		return donorStatuses;
	}
	/**
	 * Получение списка статусов компонентов крови.
	 * @return список статусов компонентов крови.
	 */
	public List<BloodComponentStatus> getBloodComponentStatuses() {
		if (bloodComponentStatuses == null) {
			bloodComponentStatuses = new ArrayList<BloodComponentStatus>();
			bloodComponentStatuses.add(new BloodComponentStatus(1, "Зарегистрирован"));
			bloodComponentStatuses.add(new BloodComponentStatus(2, "В карантине"));
			bloodComponentStatuses.add(new BloodComponentStatus(3, "Готов к выдаче"));
			bloodComponentStatuses.add(new BloodComponentStatus(4, "Задержка"));
			bloodComponentStatuses.add(new BloodComponentStatus(5, "Готов к выдаче из карантина"));
			bloodComponentStatuses.add(new BloodComponentStatus(6, "Брак из карантина"));
			bloodComponentStatuses.add(new BloodComponentStatus(10, "Выдан"));
			bloodComponentStatuses.add(new BloodComponentStatus(11, "Забронирован"));
			bloodComponentStatuses.add(new BloodComponentStatus(-1, "Брак"));
			bloodComponentStatuses.add(new BloodComponentStatus(-2, "Списан"));
			bloodComponentStatuses.add(new BloodComponentStatus(-10, "Утилизирован"));
			bloodComponentStatuses.add(new BloodComponentStatus(100, "Разделен"));
		}		
		return bloodComponentStatuses;
	}
	/**
	 * Получение списка статусов обращений на обследование.
	 * @return список статусов обращений на обследование.
	 */
	public List<ExaminationStatus> getExaminationStatuses() {
		if (examinationStatuses == null) {
			examinationStatuses = new ArrayList<ExaminationStatus>();
			examinationStatuses.add(new ExaminationStatus(1, "Заполнение"));
			examinationStatuses.add(new ExaminationStatus(2, "Осмотр"));
			examinationStatuses.add(new ExaminationStatus(3, "Получение результатов анализов"));
			examinationStatuses.add(new ExaminationStatus(4, "Определение допуска к донорству"));
			examinationStatuses.add(new ExaminationStatus(5, "Допущен"));
			examinationStatuses.add(new ExaminationStatus(6, "Направлен на дообследование"));
			examinationStatuses.add(new ExaminationStatus(7, "Первичное исследование"));
			examinationStatuses.add(new ExaminationStatus(9, "Запланировано"));
			examinationStatuses.add(new ExaminationStatus(-1, "Отвод от донорства"));
			examinationStatuses.add(new ExaminationStatus(-2, "Отменено"));
		}		
		return examinationStatuses;
	}

	/**
	 * Получение списка статусов донаций крови.
	 * @return список статусов донаций крови.
	 */
	public List<BloodDonationStatus> getBloodDonationStatuses() {
		if (bloodDonationStatuses == null) {
			bloodDonationStatuses = new ArrayList<BloodDonationStatus>();
			bloodDonationStatuses.add(new BloodDonationStatus(1, "Заполнение"));
			bloodDonationStatuses.add(new BloodDonationStatus(2, "Донация"));
			bloodDonationStatuses.add(new BloodDonationStatus(3, "Получение результатов анализов"));
			bloodDonationStatuses.add(new BloodDonationStatus(4, "Паспортизация"));
			bloodDonationStatuses.add(new BloodDonationStatus(9, "Запланировано"));
			bloodDonationStatuses.add(new BloodDonationStatus(21, "Фракционирование"));
			bloodDonationStatuses.add(new BloodDonationStatus(-1, "Отвод от донорства"));
			bloodDonationStatuses.add(new BloodDonationStatus(-2, "Донация не состоялась"));
			bloodDonationStatuses.add(new BloodDonationStatus(-3, "Донация отменена"));
		}		
		return bloodDonationStatuses;
	}

	/**
	 * Получение списка типов обращений на обследование.
	 * @return список типов обращений на обследование.
	 */
	public List<ExaminationType> getExaminationTypes() {
		if (examinationTypes == null) {
			examinationTypes = new ArrayList<ExaminationType>();
			examinationTypes.add(new ExaminationType(0, "Первичное"));
			examinationTypes.add(new ExaminationType(1, "Повторное"));
		}		
		return examinationTypes;
	}

	/**
	 * Получение списка видов трансфузии.
	 * @return список видов трансфузии.
	 */
	public List<TransfusionType> getTransfusionTypes() {
		if (transfusionTypes == null) {
			transfusionTypes = new ArrayList<TransfusionType>();
			transfusionTypes.add(new TransfusionType(0, "Плановая"));
			transfusionTypes.add(new TransfusionType(1, "Экстренная"));
		}		
		return transfusionTypes;
	}
	 
	/**
	 * Получение пола по идентификатору.
	 * 
	 * @param id идентификатор пола.
	 * @return найденный по идентификатору объект.
	 */
	public Gender getGender(int id) {
		return findObject(id, getGenders());
	}

	/**
	 * Получение статуса донора по идентификатору.
	 * 
	 * @param id идентификатор статуса донора.
	 * @return найденный по идентификатору статус донора.
	 */
	public DonorStatus getDonorStatus(int id) {
		return findObject(id, getDonorStatuses());
	}
	
	/**
	 * Получение статуса компонента крови по идентификатору.
	 * 
	 * @param id идентификатор статуса компонента крови.
	 * @return найденный по идентификатору статус донора.
	 */
	public BloodComponentStatus getBloodComponentStatus(int id) {
		return findObject(id, getBloodComponentStatuses());
	}

	/**
	 * Получение статуса обращения на обследование по идентификатору.
	 * 
	 * @param id идентификатор статуса обращения на обследование.
	 * @return найденный по идентификатору статус обращения на обследование.
	 */
	public ExaminationStatus getExaminationStatus(int id) {
		return findObject(id, getExaminationStatuses());
	}

	/**
	 * Получение статуса донации крови по идентификатору.
	 * 
	 * @param id идентификатор статуса донации крови.
	 * @return найденный по идентификатору статус донации крови.
	 */
	public BloodDonationStatus getBloodDonationStatus(int id) {
		return findObject(id, getBloodDonationStatuses());
	}

	/**
	 * Получение типа обращения на обследование по идентификатору.
	 * 
	 * @param id идентификатор типа обращения на обследование.
	 * @return найденный по идентификатору тип обращения на обследование.
	 */
	public ExaminationType getExaminationType(int id) {
		return findObject(id, getExaminationTypes());
	}
	
	/**
	 * Получение вида трансфузии по идентификатору.
	 * 
	 * @param id идентификатор вида трансфузии.
	 * @return найденный по идентификатору вид трансфузии.
	 */
	public TransfusionType getTransfusionType(int id) {
		return findObject(id, getTransfusionTypes());
	}

	/**
	 * Generic метод для поиска объекта по его идентификатору в коллекции объектов.
	 * @param id идентификатор объекта.
	 * @param objects коллекция объектов в которой осуществляется поиск.
	 * @return найденный объект.
	 */
	public static <T extends DictionaryEntity> T findObject(int id, List<T> objects) {
		for (T object : objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}
}