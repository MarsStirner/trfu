package ru.efive.dao.sql.dao.user;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Permission;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;

public class UserDAOHibernate extends GenericDAOHibernate<User> implements UserDAO {
	
	public UserDAOHibernate() {
		
	}
	
	@Override
	protected Class<User> getPersistentClass() {
		return User.class;
	}
	
    /**
     * Возвращает пользователя по логину и паролю. 
     *
     * @param login    логин
     * @param password пароль
     * @return пользователь или null, если такового не существует
     */
	@Override
    public User findByLoginAndPassword(String login, String password) throws JDBCConnectionException, DataAccessException {
        if (StringUtils.isNotEmpty(login) && StringUtils.isNotEmpty(password)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.add(Restrictions.eq("login", login));
            detachedCriteria.add(Restrictions.eq("password", password));

            List<User> users = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            if ((users != null) && !users.isEmpty()) {
                // don't modify this routine work! (for proxy caching such objects as Personage, Location)
                User user = users.get(0);
                if (user != null) {
                    // todo caching proxy objects
                }

                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Возвращает пользователя по логину
     *
     * @param login логин
     * @return пользователь или null, если такового не существует
     */
    @Override
    public User getByLogin(String login) {
        if (StringUtils.isNotEmpty(login)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.add(Restrictions.eq("login", login));

            List<User> users = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            if ((users != null) && !users.isEmpty()) {
                // don't modify this routine work! (for proxy caching such objects as Role, etc.)
                User user = users.get(0);
                if (user != null) {
                	
                }
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Override
    public User getByLogin(String login, Integer excludeUserId) {
        if (StringUtils.isNotEmpty(login)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.add(Restrictions.eq("login", login));
            detachedCriteria.add(Restrictions.ne("id", excludeUserId));

            List<User> users = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            if ((users != null) && !users.isEmpty()) {
                // don't modify this routine work! (for proxy caching such objects as Role, etc.)
                User user = users.get(0);
                if (user != null) {
                	
                }
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * Возвращает пользователя по email
     *
     * @param email адрес электронной почты
     * @return пользователь или null, если такового не существует
     */
    public User getByEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.add(Restrictions.eq("email", email));

            List<User> users = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            if ((users != null) && !users.isEmpty()) {
                return users.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User getByEmail(String email, Integer excludeUserId) {
        if (StringUtils.isNotEmpty(email)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.add(Restrictions.eq("email", email));
            detachedCriteria.add(Restrictions.ne("id", excludeUserId));

            List<User> users = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            if ((users != null) && !users.isEmpty()) {
                return users.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private DetachedCriteria createCriteriaForRoles(String name) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (StringUtils.isNotEmpty(name)) {
            detachedCriteria.add(Restrictions.eq("name", name));
        }

        return detachedCriteria;
    }
    
    /**
     * Находит роли пользователей
     *
     * @param name        название роли
     * @param offset      номер начального элемента списка
     * @param count       количество возвращаемых элементов
     * @param orderBy     поле для сортировки списка
     * @param asc         указывает направление сортировки. true = по возрастанию
     * @return коллекция ролей
     */
    public List<Role> findRoles(String name, int offset, int count, String orderBy, boolean asc) {
        DetachedCriteria detachedCriteria = createCriteriaForRoles(name);

        if (StringUtils.isNotEmpty(orderBy)) {
            detachedCriteria.addOrder(asc ? Order.asc(orderBy) : Order.desc(orderBy));
        }

        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
    }

    /**
     * Считает количество ролей пользователей, зарегистрированных в системе
     *
     * @param name        название роли
     * @return количество ролей пользователей, зарегистрированных в системе
     */
    public long countRoles(String name) {
        DetachedCriteria detachedCriteria = createCriteriaForRoles(name);
        return getCountOf(detachedCriteria);
    }

    private DetachedCriteria createCriteriaForUsers(String login, String firstname, String lastname, String middlename, String email, Role role, boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (StringUtils.isNotEmpty(login)) {
            detachedCriteria.add(Restrictions.ilike("login", login + "%"));
        }

        if (StringUtils.isNotEmpty(lastname)) {
            detachedCriteria.add(Restrictions.ilike("lastname", lastname + "%"));
        }

        if (StringUtils.isNotEmpty(firstname)) {
            detachedCriteria.add(Restrictions.ilike("firstname", firstname + "%"));
        }

        if (StringUtils.isNotEmpty(middlename)) {
            detachedCriteria.add(Restrictions.ilike("middlename", middlename + "%"));
        }

        if (StringUtils.isNotEmpty(email)) {
            detachedCriteria.add(Restrictions.ilike("email", email + "%"));
        }

        if (role != null) {
            detachedCriteria.add(Restrictions.eq("role", role));
        }

        if (!showDeleted)
            detachedCriteria.add(Restrictions.or(Restrictions.isNull("deleted"),Restrictions.eq("deleted",false)));

        return detachedCriteria;
    }

    /**
     * Есть ли у пользователя user право permissionType
     * @param user пользователь
     * @param permissionType право
     * @return true - есть; false - доступ запрещен
     */
    @Override
    public boolean hasPermission(User user, RoleType permissionType) {
        if ((user != null) && (permissionType != null)) {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
            detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

            detachedCriteria.createCriteria("users", DetachedCriteria.LEFT_JOIN).add(Restrictions.idEq(user.getId()));

            detachedCriteria.createCriteria("permissions", DetachedCriteria.LEFT_JOIN).add(Restrictions.eq("permissionType", permissionType));

            List<Role> roles = getHibernateTemplate().findByCriteria(detachedCriteria, -1, 1);
            return ((roles != null) && !roles.isEmpty());
        } else {
            return false;
        }
    }

    /**
     * Находит права(разрешения) в справочнике прав по типу и по названию
     *
     * @param name           название
     * @param permissionType тип права доступа
     * @return право доступа
     */
    @Override
    public List<Permission> getPermission(String name, RoleType permissionType) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Permission.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if ((StringUtils.isNotEmpty(name)) || (permissionType != null)) {
            if (StringUtils.isNotEmpty(name)) {
                detachedCriteria.add(Restrictions.eq("name", name));
            }
            if ((permissionType != null)) {
                detachedCriteria.add(Restrictions.eq("permissionType", permissionType));
            }
        } else {
            return null;
        }
        return getHibernateTemplate().findByCriteria(detachedCriteria);
    }


    /**
     * Находит всех пользователей, удовлетворяющих условиям
     *
     * @param login            логин пользователя
     * @param firstname        имя пользователя
     * @param lastname         фамилия пользователя
     * @param middlename       отчество пользователя
     * @param email            адрес электронной почты пользователя
     * @param role             роль пользователя
     * @param showDeleted      включает в выборку удалённых пользователей
     * @param offset           номер начального элемента списка
     * @param count            количество возвращаемых элементов
     * @param orderBy          поле для сортировки списка
     * @param orderAsc         указывает направление сортировки. true = по возрастанию
     * @return список пользователей удовлетворяющих условию поиска
     */
    @Override
    public List<User> findUsers(String login, String firstname, String lastname, String middlename, String email, Role role, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        DetachedCriteria detachedCriteria = createCriteriaForUsers(login, firstname, lastname, middlename, email, role, showDeleted);
        addOrder(detachedCriteria, orderBy, orderAsc);
        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
    }

    /**
     * Находит количество пользователей, зарегистрированных в системе, удовлетворяющих условиям
     *
     * @param login            логин пользователя
     * @param firstname        имя пользователя
     * @param lastname         фамилия пользователя
     * @param middlename       отчество пользователя
     * @param email            адрес электронной почты пользователя
     * @param role             роль пользователя
     * @param showDeleted      включает в выборку удалённых пользователей
     * @return количество зарегистрированных пользователей удовлетворяющих условию поиска
     */
    @Override
    public long countUsers(String login, String firstname, String lastname, String middlename, String email, Role role, boolean showDeleted) {
        DetachedCriteria detachedCriteria = createCriteriaForUsers(login, firstname, lastname, middlename, email, role, showDeleted);
        return getCountOf(detachedCriteria);
    }
    
    /**
	 * {@inheritDoc}
	 */
    @Override
    public List<User> findUsers(String pattern, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            LogicalExpression orExp = Restrictions.or(Restrictions.ilike("lastName", pattern + "%"), 
            		Restrictions.ilike("middleName", pattern + "%"));
            orExp = Restrictions.or(orExp, Restrictions.ilike("firstName", pattern + "%"));
            detachedCriteria.add(orExp);
        }
        
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
	public long countUsers(String pattern, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (StringUtils.isNotEmpty(pattern)) {
            LogicalExpression orExp = Restrictions.or(Restrictions.ilike("lastName", pattern + "%"), 
            		Restrictions.ilike("middleName", pattern + "%"));
            orExp = Restrictions.or(orExp, Restrictions.ilike("firstName", pattern + "%"));
            detachedCriteria.add(orExp);
        }
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(detachedCriteria);
	}
}