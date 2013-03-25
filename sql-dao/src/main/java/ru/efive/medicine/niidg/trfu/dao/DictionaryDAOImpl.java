package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
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
import ru.efive.medicine.niidg.trfu.data.dictionary.*;

public class DictionaryDAOImpl extends DictionaryDAOHibernate<DictionaryEntity> {
	
	public <T extends DictionaryEntity> T get(Class<T> persistentClass, Serializable id) {
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

}