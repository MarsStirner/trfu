package ru.efive.dao.sql.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import ru.efive.dao.sql.entity.AbstractEntity;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Имплементация базового интерфейса для управления сущностями при помощи Hibernate с использованием generics.
 * Также содержит методы помощи при работе с hibernate и получением результатов запросов.
 */
public class GenericDAOHibernate<T extends AbstractEntity> extends HibernateDaoSupport implements GenericDAO<T> {
	
	/**
	 * Возвращает элемент по идентификатору.
	 * 
	 * @param id
	 *            идентификатор сущности (обычно числовое значение)
	 * @return элемент сохраненный в БД.
	 */
	@Override
	public T get(Serializable id) {
		return (T) getHibernateTemplate().get(getPersistentClass(), id);
	}
	
	/**
	 * Сохраняет элемент сущности и возвращает сохраненный элемент.
	 * 
	 * @param entity
	 *            сущность для сохранения в БД
	 * @return сохраненный элемент сущности в БД
	 */
	@Override
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * Обновляет элемент сущности в БД.
	 * 
	 * @param entity
	 *            сущность для обновления
	 * @return обновленный элемент сущности
	 */
	@Override
	public T update(T entity) {
		getHibernateTemplate().update(entity);
		return entity;
	}

	/**
	 * Удаляет элемент сущности из БД.
	 * 
	 * @param entity
	 *            сущность для удаления
	 */
	@Override
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	/**
	 * Удаляет элемент сущности из БД.
	 * 
	 * @param id
	 *            идентификатор сущности для удаления.
	 * @return true если удаление прошло успешно, иначе false
	 */
	@Override
	public boolean delete(Serializable id) {
		AbstractEntity ent = get(id);
		if (ent == null) {
			return false;
		}
		getHibernateTemplate().delete(ent);
		return true;
	}

	/**
	 * Удаляет список элементов из БД.
	 * 
	 * @param entities
	 *            список элементов для удаления
	 */
	@Override
	public void delete(Collection<? extends T> entities) {
		getHibernateTemplate().deleteAll(entities);
	}

	/**
	 * Сохраняет коллекцию элементов в БД
	 * 
	 * @param entities
	 *            коллекция элементов
	 */
	@Override
	public void save(Collection<? extends T> entities) {
		getHibernateTemplate().saveOrUpdateAll(entities);
	}

	/**
	 * Возвращает количество сущностей удовлетворяющих заданному критерию
	 * поиска. Данный метод автоматически устанавливает проекцию для получения
	 * количества строк и привязывает критерий к объекту сессии hibernate.
	 * 
	 * @param criteria
	 *            критерий поиска.
	 * @return количество сущностей, удовлетворяющих критерию.
	 */
	protected long getCountOf(final DetachedCriteria criteria) {
		Assert.notNull(criteria, "DetachedCriteria must not be null");
		Object result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				executableCriteria.setProjection(Projections.countDistinct("id"));
				return executableCriteria.uniqueResult();
			}
		});
		long res = (Long) result;
		return result == null ? 0 : (Long) result;
	}

	/**
	 * Возвращает количество сущностей удовлетворяющих заданному критерию
	 * поиска. Данный метод автоматически устанавливает проекцию для получения
	 * количества строк и привязывает критерий к объекту сессии hibernate.
	 * 
	 * @param executableCriteria
	 *            критерий поиска.
	 * @return количество сущностей, удовлетворяющих критерию.
	 */
	protected long getCountOf(final Criteria executableCriteria) {
		Assert.notNull(executableCriteria, "Criteria must not be null");
		return (Integer) getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						executableCriteria.setProjection(Projections.rowCount());
						executableCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
						return executableCriteria.uniqueResult();
					}
				});
	}

	/**
	 * Возвращает максимальное значение поля среди сущностей, удовлетворяющих
	 * заданному критерию поиска.
	 * 
	 * @param field
	 *            поле по которому надо вычислить максимальное значение.
	 * @param criteria
	 *            критерий поиска.
	 * @return максимальное значение.
	 */
	protected int getMaxValueOf(final String field, final DetachedCriteria criteria) {
		Assert.notNull(criteria, "Criteria must not be null");
		Object obj = getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				executableCriteria.setProjection(Projections.max(field));
				executableCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
				return executableCriteria.uniqueResult();
			}
		});

		return (obj == null) ? 0 : (Integer) obj;
	}

	/**
	 * Возвращает единственный результат для критерия. Возвращается первый
	 * выбранный результат соответствующий критерию
	 * 
	 * @param criteria
	 *            критерий поиска
	 * @return единственный результат поиска по критерию
	 */
	protected Object getUniqueResult(final DetachedCriteria criteria) {
		Assert.notNull(criteria, "DetachedCriteria must not be null");
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				return executableCriteria.uniqueResult();
			}
		});
	}

	/**
	 * Устанавливает сортировку по полю Понимает сортировку по вложенному полю
	 * (вложенность ограничена 2 уровнями)
	 * 
	 * @param detachedCriteria
	 *            критерий для поиска результатов
	 * @param orderBy
	 *            строка содержащая имена полей классов через точку, по которым
	 *            необходимо производить сортировку
	 * @param asc
	 *            направление сортировки
	 */
	protected void addOrder(DetachedCriteria detachedCriteria, String orderBy, Boolean asc) {
		if (StringUtils.isNotEmpty(orderBy)) {
			if (asc == null) {
				asc = true;
			}

			DetachedCriteria orderingCriteria = detachedCriteria;
			if (orderBy.indexOf('.') > 0) {
				String[] parts = StringUtils.split(orderBy, "\\.");
				int length = parts.length;
				if (length == 2) {
					orderingCriteria = detachedCriteria.createCriteria(parts[0], DetachedCriteria.LEFT_JOIN);
					orderBy = parts[1];
				} else if (length > 2) {
					orderingCriteria = detachedCriteria.createCriteria(parts[0], DetachedCriteria.LEFT_JOIN)
							.createCriteria(parts[1], DetachedCriteria.LEFT_JOIN);
					orderBy = parts[2];
				}
			}
			orderingCriteria.addOrder(asc ? Order.asc(orderBy) : Order
					.desc(orderBy));
		}
	}

	/**
	 * Устанавливает сортировку по нескольким полям из !одной таблицы! Понимает
	 * сортировку по вложенному полю (вложенность ограничена 3 уровнями)
	 * Внимание! чтобы избежать повторных JOIN и не словить Exception
	 * предварительно убедитесь, что все ордеры из одной таблицы
	 * 
	 * @param detachedCriteria
	 *            критерий для поиска результатов
	 * @param orders
	 *            массив строк с названиями полей
	 * @param asc
	 *            направление сортировки
	 */
	protected void addOrder(DetachedCriteria detachedCriteria, String[] orders, Boolean asc) {
		if (orders == null || orders.length < 1)
			return;

		if (asc == null) {
			asc = true;
		}
		
		DetachedCriteria orderingCriteria = detachedCriteria;

		if (orders[0].indexOf('.') > 0) {
			String[] parts = StringUtils.split(orders[0], "\\.");
			int length = parts.length;
			if (length == 2) {
				orderingCriteria = detachedCriteria.createCriteria(parts[0], DetachedCriteria.LEFT_JOIN);
			} else if (length == 3) {
				orderingCriteria = detachedCriteria.createCriteria(parts[0], DetachedCriteria.LEFT_JOIN)
						.createCriteria(parts[1], DetachedCriteria.LEFT_JOIN);
			} else if (length == 4) {
				orderingCriteria = detachedCriteria
						.createCriteria(parts[0], DetachedCriteria.LEFT_JOIN)
						.createCriteria(parts[1], DetachedCriteria.LEFT_JOIN)
						.createCriteria(parts[2], DetachedCriteria.LEFT_JOIN);
			}
		}
		for (int i = 0; i < orders.length; i++) {
			String[] parts = StringUtils.split(orders[i], "\\.");
			orderingCriteria.addOrder(asc ? Order.asc(parts[parts.length - 1]) : Order.desc(parts[parts.length - 1]));
		}
		
	}

	/**
	 * Устанавливает сортировку по полю. Понимает сортировку по вложенному полю.
	 * Но вложенность органичена только 1 уровнем - смысла сортировать по еще
	 * более вложенным полям нет.
	 * 
	 * @param criteria
	 *            критерий для поиска результатов
	 * @param orderBy
	 *            строка содержащая имена полей классов через точку, по которым
	 *            необходимо производить сортировку
	 * @param asc
	 *            направление сортировки
	 */
	protected void addOrder(Criteria criteria, String orderBy, Boolean asc) {
		if (StringUtils.isNotEmpty(orderBy)) {
			if (asc == null) {
				asc = true;
			}
			Criteria orderingCriteria = criteria;
			if (orderBy.indexOf('.') > 0) {
				String[] parts = StringUtils.split(orderBy, "\\.");
				int length = parts.length;
				if (length == 2) {
					orderingCriteria = criteria.createCriteria(parts[0], Criteria.LEFT_JOIN);
					orderBy = parts[1];
				} else if (length > 2) {
					orderingCriteria = criteria.createCriteria(parts[0], Criteria.LEFT_JOIN).createCriteria(parts[1], Criteria.LEFT_JOIN);
					orderBy = parts[2];
				}
			}
			orderingCriteria.addOrder(asc ? Order.asc(orderBy) : Order.desc(orderBy));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(detachedCriteria);
	}
	
	
	protected Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	private Class<T> persistentClass;
	
	protected DetachedCriteria createDetachedCriteria() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		return detachedCriteria;
	}

	protected void addNotDeletedCriteria(DetachedCriteria detachedCriteria) {
		detachedCriteria.add(Restrictions.eq("deleted", false));
	}

	protected void addOrderCriteria(String orderBy, boolean orderAsc,
			DetachedCriteria detachedCriteria) {
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
	}
	
	protected void addDateSearchCriteria(Conjunction conjunction, Date created,
			String dateField) {
		Date fromDate = DateHelper.getDateWithoutTime(created);
		Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(created));
		conjunction.add(Restrictions.ge(dateField, fromDate));
		conjunction.add(Restrictions.le(dateField, toDate));
	}
	
	protected void addDateSearchCriteria(Conjunction conjunction, Date from, Date to, String dateField) {
		if (from != null) {
			Date fromDate = DateHelper.getDateWithoutTime(from);
			conjunction.add(Restrictions.ge(dateField, fromDate));
		}
		if (to != null) {
			Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(to));
			conjunction.add(Restrictions.lt(dateField, toDate));
		}
	}
}