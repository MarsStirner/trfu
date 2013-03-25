package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.document.Document;

/**
 * Отделение
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "divisions")
public class Division extends Document {
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getExternalId() {
		return externalId;
	}

	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Division other = (Division) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
	private String name;
	
	private int externalId;
	
	private static final long serialVersionUID = -9159327603468702719L;
}