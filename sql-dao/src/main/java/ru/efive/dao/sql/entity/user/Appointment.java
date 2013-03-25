package ru.efive.dao.sql.entity.user;

import ru.efive.dao.sql.entity.IdentifiedEntity;

import javax.persistence.*;

/**
 * Должности
 */
@Entity
@Table(name = "appointments")
public class Appointment extends IdentifiedEntity {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}
	
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	/** название */
	private String name;
	
	/**
	 * true - пользователь удалён, false или null - не удалён
	 */
	private Boolean deleted;
	
	
	private static final long serialVersionUID = -4121985925621903659L;
}