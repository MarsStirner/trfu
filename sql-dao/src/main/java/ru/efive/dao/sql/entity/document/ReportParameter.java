package ru.efive.dao.sql.entity.document;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import ru.efive.dao.sql.entity.IdentifiedEntity;

@Entity
@Table(name = "report_parameters")
public class ReportParameter extends IdentifiedEntity {
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public void setClassfierName(String classfierName) {
		this.classfierName = classfierName;
	}

	public String getClassfierName() {
		return classfierName;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	
	/**
	 * Описание поля (для UI)
	 */
	private String description;
	
	/**
	 * Алиас
	 */
	private String alias;
	
	/**
	 * Имя, по которому приозводится поиск возможных значений
	 */
	private String classfierName;
	
	/**
	 * Значение
	 */
	@Transient
	private String value;
	
	
	private static final long serialVersionUID = -3808620131311502998L;
}