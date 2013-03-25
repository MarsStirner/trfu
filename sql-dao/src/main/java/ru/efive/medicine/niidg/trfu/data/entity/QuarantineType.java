package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;

/**
 * Виды карантинизации
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_quarantine_types")
public class QuarantineType extends IdentifiedEntity {
	
	public BloodComponentType getComponentType() {
		return componentType;
	}

	public void setComponentType(BloodComponentType componentType) {
		this.componentType = componentType;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodComponentType componentType;
	
	/**
	 * срок карантинизации
	 */
	private int years; 
	private int months;
	private int days;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	private static final long serialVersionUID = 334435383569433884L;
}