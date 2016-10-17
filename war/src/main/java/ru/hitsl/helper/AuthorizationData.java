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
     * Создаем данные авторизации (авторизованный пользователь и его группы + роли и уровни допуска)
     * @param authorized авторизованный пользователь
     */
    public AuthorizationData(final User authorized){
        this.authorized = authorized;             
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
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Списки идентификаторов
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Флаги ролей
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isAdministrator() {
        return hasRole(RoleType.ENTERPRISE_ADMINISTRATION);
    }

    private boolean hasRole(final RoleType roleType) {
        for (Role role : roles) {
            if (roleType.equals(role.getRoleType())) {
                return true;
            }
        }
        return false;
    }

    public boolean isOperational() {
        return hasRole(RoleType.OPERATIONAL);
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // @Override 's
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthorizationData[");
        sb.append("\n\t AuthorizedUser: [").append(authorized.getId()).append("] ").append(authorized.getDescription());
        sb.append("\n\t Roles: ").append(roles.size());
        for(Role current : roles){
            sb.append("\n\t\t\t").append(current.getRoleType());
        }
        sb.append("\n]");
        return sb.toString();
    }

    public String getDefaultPage() {
        final RoleType currentRoleType = authorized.getSelectedRole().getRoleType();
        switch (currentRoleType){
            case REGISTRATOR:
                return "/component/filter/donors.xhtml";
            case THERAPIST:
                return "/component/personal_requests.xhtml";
            case OPERATIONAL:
                return "/component/blood_donations_operational.xhtml";
            case RECTIFICATION:
                return "/component/blood_donations_rectification.xhtml";
            case DRAFT_OUT:
                return "/component/blood_components.xhtml";
            case LABELING:
                return "/component/blood_components.xhtml";
            case EXPEDITION:
                return "/component/blood_components_ready.xhtml";
            case QUARANTINE:
                return "/component/blood_components_in_quarantine.xhtml";
            case LABORATORY:
                return "/component/laboratory.xhtml";
            case MEDICAL:
                return "/component/medical/donors.xhtml";
            case IN_CONTROL:
                return "/component/blood_components_in_control.xhtml";
            case HEAD_NURSE:
                return "/component/blood_components_quarantined.xhtml";
            case DIVISION_SUPERINTENDENT:
                return "/component/filter/donors.xhtml";
            case ENTERPRISE_ADMINISTRATION:
                return "/component/admin/settings.xhtml";
            case SECONDARY_PROCESSING:
                return "/component/secondary_processing.xhtml";
        }
        return "/component/filter/donors.xhtml";
    }


}
