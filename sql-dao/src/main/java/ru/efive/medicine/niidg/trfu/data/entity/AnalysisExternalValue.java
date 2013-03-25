package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;

@Entity
@Table(name = "trfu_test_external_values")
public class AnalysisExternalValue extends IdentifiedEntity {
	
	public ExternalIndicator getIndicator() {
		return indicator;
	}
	
	public void setIndicator(ExternalIndicator indicator) {
		this.indicator = indicator;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNormValue() {
		return normValue;
	}

	public void setNormValue(String normValue) {
		this.normValue = normValue;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * Внешнее исследование
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private ExternalIndicator indicator;
	
	/**
	 * Значение
	 */
	private String value;
	
	/**
	 * Единица измерения
	 */
	private String unit;
	
	/**
	 * Норма
	 */
	private String normValue;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1L;
}