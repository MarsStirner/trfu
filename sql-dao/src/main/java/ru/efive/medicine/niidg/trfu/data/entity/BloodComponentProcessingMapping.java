package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;

/**
 * Настройка связей типов компонентов крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_component_processing_mappings")
public class BloodComponentProcessingMapping extends IdentifiedEntity {
	
	public void setProcessingType(int processingType) {
		this.processingType = processingType;
	}
	
	public int getProcessingType() {
		return processingType;
	}
	
	public void setSourceTypes(List<BloodComponentType> sourceTypes) {
		this.sourceTypes = sourceTypes;
	}
	
	public List<BloodComponentType> getSourceTypes() {
		return sourceTypes;
	}
	
	public void setDestinationType(BloodComponentType destinationType) {
		this.destinationType = destinationType;
	}
	
	public BloodComponentType getDestinationType() {
		return destinationType;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	
	/**
	 * Тип обработки
	 * 0 - карантинизация
	 */
	private int processingType;
	
	/**
	 * Исходные типы компонентов крови
	 */
	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "trfu_blood_component_processing_mapping_source_types", 
			joinColumns = { @JoinColumn(name = "mapping_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "type_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<BloodComponentType> sourceTypes;
	
	/**
	 * Получаемый тип компонента крови
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodComponentType destinationType;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1723150169183900148L;
}