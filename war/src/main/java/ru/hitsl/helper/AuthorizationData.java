package ru.hitsl.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 26.10.2015, 18:27 <br>
 * Company: hitsl (Hi-Tech Solutions) <br>
 * Description: <br>
 */
public class AuthorizationData implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger("AUTH");

    /**
     * Авторизованный пользователь
     */
    private final User authorized;

    /**
     * Суммарный набор ролей
     */
    private final Set<Role> roles;
    private final Set<Integer> roleIds;


    /**
     * Список замещаемых пользователей
     */
    private final Set<User> substitutedUsers = new HashSet<>(0);
    private final Set<Integer> userIds = new HashSet<>(1);

    //Замещает ли текущий пользователь кого-либо
    private boolean isSubstitution = false;

    /**
     * Создаем данные авторизации (авторизованный пользователь и его группы + роли и уровни допуска)
     * @param authorized авторизованный пользователь
     */
    public AuthorizationData(final User authorized){
        this.authorized = authorized;
        this.userIds.add(authorized.getId());
        final Set<Role> authorizedRoles = authorized.getRoles();
        this.roles = new HashSet<>(authorizedRoles.size());
        this.roleIds = new HashSet<>(authorizedRoles.size());
        for(Role current : authorizedRoles){
            roles.add(current);
            roleIds.add(current.getId());
        }
    }


    public User getAuthorized() {
        return authorized;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<User> getSubstitutedUsers() {
        return substitutedUsers;
    }

    public boolean isSubstitution() {
        return isSubstitution;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Списки идентификаторов
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Флаги ролей
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isAdministrator() {
        for (Role role : roles) {
            if (RoleType.ENTERPRISE_ADMINISTRATION.equals(role.getRoleType())) {
                return true;
            }
        }
        return false;
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // @Override 's
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthorizationData[");
        sb.append("\n\t AuthorizedUser: [").append(authorized.getId()).append("] ").append(authorized.getDescription());
        sb.append("\n\t SubstitutedUsers: ");
        if(isSubstitution){
            sb.append(substitutedUsers.size()).append(" substitutions");
            for(User current : substitutedUsers){
                sb.append("\n\t\t\t[").append(current.getId()).append("] ").append(current.getDescription());
            }
        } else {
            sb.append("NO substitutions");
        }
        sb.append("\n\t Roles: ").append(roles.size());
        for(Role current : roles){
            sb.append("\n\t\t\t").append(current.getRoleType());
        }
        sb.append("\n]");
        return sb.toString();
    }
}
