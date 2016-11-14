package ru.efive.dao.sql.entity.user;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.enums.RoleType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Роль пользователя системы
 */
@Entity
@Table(name = "roles")
public class Role extends IdentifiedEntity implements Comparable<Role>{

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public RoleType getRoleType() {
		return roleType;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Role && getName().equals(((Role) obj).getName()) && getRoleType().equals(
				((Role) obj).getRoleType());
	}
	
	@Override
    public int hashCode() {
        return getName().hashCode() + getRoleType().ordinal();
    }

	/** название */
	private String name;
	
	/** тип права доступа */
	@Enumerated(value = EnumType.STRING)
	private RoleType roleType;
	
	
	private static final long serialVersionUID = -4121985925621903659L;


	@Override
	public int compareTo(final Role o) {
		return this.getName() == null ? -1 : this.getName().compareToIgnoreCase(o.getName());
	}
}