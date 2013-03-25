package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationEntryType;

/**
 * Запись осмотра - объективные данные
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_examination_entries")
public class ExaminationEntry extends IdentifiedEntity {

	public ExaminationEntryType getType() {
		return type;
	}

	public void setType(ExaminationEntryType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setValues(Set<Classifier> values) {
		this.values = values;
	}
	
	public Set<Classifier> getValues() {
		return values;
	}
	
	@Transient
	public List<Classifier> getValuesList() {
		List<Classifier> result = new ArrayList<Classifier>();
		if (values != null) {
			result.addAll(values);
		}
		Collections.sort(result, new Comparator<Classifier>() {
			public int compare(Classifier o1, Classifier o2) {
				return o1.getId() - o2.getId();
			}
		});
		return result;
	}

	public void setParentEntry(ExaminationEntry parentEntry) {
		this.parentEntry = parentEntry;
	}

	public ExaminationEntry getParentEntry() {
		return parentEntry;
	}

	public void setExaminationRequest(ExaminationRequest examinationRequest) {
		this.examinationRequest = examinationRequest;
	}

	public ExaminationRequest getExaminationRequest() {
		return examinationRequest;
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
	private ExaminationEntryType type;
	
	/**
	 * Значение
	 */
	private String value;
	
	/**
	 * Значения (для полей со множественным выбором)
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "trfu_examination_entry_values", 
			joinColumns = { @JoinColumn(name = "entry_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "value_id") })
	private Set<Classifier> values;
	
	/**
	 * Базовая запись осмотра
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private ExaminationEntry parentEntry;
	
	/**
	 * Обращение на обследование
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name="trfu_examination_request_entries", 
		joinColumns = { @JoinColumn(name = "entry_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "request_id") })
	private ExaminationRequest examinationRequest;
	
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