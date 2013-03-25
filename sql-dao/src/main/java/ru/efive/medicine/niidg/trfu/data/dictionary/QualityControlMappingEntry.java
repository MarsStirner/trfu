package ru.efive.medicine.niidg.trfu.data.dictionary;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Результат анализа
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_quality_control_mapping_entries")
public class QualityControlMappingEntry extends DictionaryEntity {
	
	public void setAnalysisTypes(List<AnalysisType> analysisTypes) {
		this.analysisTypes = analysisTypes;
	}
	
	public List<AnalysisType> getAnalysisTypes() {
		return analysisTypes;
	}
	
	public void setComponentTypes(List<BloodComponentType> componentTypes) {
		this.componentTypes = componentTypes;
	}
	
	public List<BloodComponentType> getComponentTypes() {
		return componentTypes;
	}
	
	
	/**
	 * Типы анализов для контроля качества
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_quality_control_mapping_entry_tests", 
			joinColumns = { @JoinColumn(name = "entry_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "test_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<AnalysisType> analysisTypes;
	
	/**
	 * Тип компонента
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_quality_control_mapping_entry_types", 
			joinColumns = { @JoinColumn(name = "entry_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "component_type_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<BloodComponentType> componentTypes;
	
	
	private static final long serialVersionUID = 7686356279007461779L;
}