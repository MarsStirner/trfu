package ru.efive.medicine.niidg.trfu.data.dictionary;

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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.DictionaryEntity;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;

/**
 * Элемент справочника - виды анализов
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_analysis_types")
public class AnalysisType extends DictionaryEntity {
	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
	
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
	public int getFieldType() {
		return fieldType;
	}
	
	public void setParentEntry(AnalysisType parentEntry) {
		this.parentEntry = parentEntry;
	}
	
	public AnalysisType getParentEntry() {
		return parentEntry;
	}
	
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	
	public boolean isWritable() {
		return writable;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setInfectionTest(boolean infectionTest) {
		this.infectionTest = infectionTest;
	}
	
	public boolean isInfectionTest() {
		return infectionTest;
	}
	
	public List<ExternalIndicator> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<ExternalIndicator> indicators) {
		this.indicators = indicators;
	}
	
	public void addIndicator() {
		if (indicators == null) {
			indicators = new ArrayList<ExternalIndicator>();
		}
		indicators.add(new ExternalIndicator());
	}
	
	public boolean isLaboratoryTest() {
		return laboratoryTest;
	}

	public void setLaboratoryTest(boolean laboratoryTest) {
		this.laboratoryTest = laboratoryTest;
	}

	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof AnalysisType ) ) {
            return false;
        }
        return getValue().equals( ((AnalysisType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	/**
	 * Категория анализов
	 */
	private String category;
	
	/**
	 * Тип поля для ввода:
	 * 0 - inputText, 3 - selectOneMenu
	 */
	private int fieldType;
	
	/**
	 * Базовый анализ
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private AnalysisType parentEntry;
	
	/**
	 * Необходимо ли вносить результат
	 */
	private boolean writable;
	
	/**
	 * Для корректной сортировки, возможно пригодится в будущем
	 */
	private int level;
	
	/**
	 * Анализ для выявления заболевания
	 */
	private boolean infectionTest;
	
	/**
	 * исследования ЛИС
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_analysis_type_external_indicators", 
			joinColumns = { @JoinColumn(name = "analysis_type_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "indicator_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExternalIndicator> indicators;
	
	/**
	 * Внешнее исследование (ЛИС)
	 */
	private boolean laboratoryTest;
	
	
	private static final long serialVersionUID = 1L;
}