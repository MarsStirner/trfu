package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;

/**
 * Результат анализа
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_tests")
public class Analysis extends IdentifiedEntity {

	public AnalysisType getType() {
		return type;
	}

	public void setType(AnalysisType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValueShort() {
		String result = "...";
		if (getExternalValues() != null && !getExternalValues().isEmpty()) {
			StringBuffer sb = new StringBuffer(getExternalValues().get(0).getIndicator().getName()).append(" - ").append(getExternalValues().get(0).getValue()).
					append(" ").append(getExternalValues().get(0).getUnit());
			result = sb.toString();
		}
		return result;
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

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public List<AnalysisExternalValue> getExternalValues() {
		return externalValues;
	}

	public void setExternalValues(List<AnalysisExternalValue> externalValues) {
		this.externalValues = externalValues;
	}
	
	public void addExternalValue() {
		if (externalValues == null) {
			externalValues = new ArrayList<AnalysisExternalValue>();
		}
		externalValues.add(new AnalysisExternalValue());
	}
	
	public void addExternalValue(AnalysisExternalValue externalValue) {
		if (externalValues == null) {
			externalValues = new ArrayList<AnalysisExternalValue>();
		}
		boolean found = false;
		for (AnalysisExternalValue value: externalValues) {
			if (value.getIndicator() != null && StringUtils.isNotEmpty(value.getIndicator().getCode()) && value.getIndicator().getCode().equals(externalValue.getIndicator().getCode())) {
				value.setValue(externalValue.getValue());
				found = true;
			}
		}
		if (!found) {
			externalValues.add(externalValue);
		}
	}

	public void setParentEntry(Analysis parentEntry) {
		this.parentEntry = parentEntry;
	}
	
	public Analysis getParentEntry() {
		return parentEntry;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public void setGrouping(int grouping) {
		this.grouping = grouping;
	}
	
	public int getGrouping() {
		return grouping;
	}
	
	public void setParent(boolean parent) {
		this.parent = parent;
	}
	
	public boolean isParent() {
		return parent;
	}


	/**
	 * Тип анализа
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private AnalysisType type;
	
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
	 * Тип результата для этого анализа
	 * 0 - введенное вручную значение
	 * 1 - единичный результат исследования в лаборатории
	 * 2 - множественный результат исследования в лаборатории
	 */
	private int valueType;
	
	/**
	 * Внешнее значение
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_tests_values", 
			joinColumns = { @JoinColumn(name = "analysis_type_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "indicator_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<AnalysisExternalValue> externalValues;
	
	/**
	 * Базовый анализ
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Analysis parentEntry;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	/**
	 * Техническое значение для группировки
	 */
	@Transient
	private int grouping;
	
	/**
	 * Есть ли документы - потомки
	 */
	@Transient
	private boolean parent = false;
	
	private static final long serialVersionUID = -1265430116892447289L;
}