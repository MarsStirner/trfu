package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Весовой коэффициент при поиске для фенотипа
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_component_match_criteria")
public class BloodComponentMatchCriteria extends IdentifiedEntity {
	
	public boolean isNecessary() {
		return necessary;
	}
	
	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}
	
	public Analysis getPhenotype() {
		return phenotype;
	}
	
	public void setPhenotype(Analysis phenotype) {
		this.phenotype = phenotype;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * Обязательное совпадение
	 */
	private boolean necessary;
	
	/**
	 * Тип анализа
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Analysis phenotype;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1446955294955802492L;
}