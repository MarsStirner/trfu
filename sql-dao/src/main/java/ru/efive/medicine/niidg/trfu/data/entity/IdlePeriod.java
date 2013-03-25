package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;

/**
 * Период ожидания до следующей донации
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_idle_periods")
public class IdlePeriod extends IdentifiedEntity {
	
	public void setDonationType(BloodDonationType donationType) {
		this.donationType = donationType;
	}
	
	public BloodDonationType getDonationType() {
		return donationType;
	}
	
	public void setSecondaryDonationType(BloodDonationType secondaryDonationType) {
		this.secondaryDonationType = secondaryDonationType;
	}
	
	public BloodDonationType getSecondaryDonationType() {
		return secondaryDonationType;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	public int getDays() {
		return days;
	}
	
	public void setDose(int dose) {
		this.dose = dose;
	}
	
	public int getDose() {
		return dose;
	}
	
	public void setMaxDose(int maxDose) {
		this.maxDose = maxDose;
	}
	
	public int getMaxDose() {
		return maxDose;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	
	/**
	 * Исходный тип донации
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodDonationType donationType;
	
	/**
	 * Последующий тип донации
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodDonationType secondaryDonationType;
	
	/**
	 * Интервал между донорствами
	 */
	private int days;
	
	/**
	 * Минимальная доза
	 */
	private int dose;
	
	/**
	 * Максимальная доза
	 */
	private int maxDose;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	private static final long serialVersionUID = 2283567903653215768L;
}