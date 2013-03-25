package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Результат подбора
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_component_match")
public class BloodComponentMatch extends IdentifiedEntity {
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Set<BloodComponent> getComponents() {
		return components;
	}
	
	public void setComponents(Set<BloodComponent> components) {
		this.components = components;
	}
	
	public void setCriteriaList(List<BloodComponentMatchCriteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	public List<BloodComponentMatchCriteria> getCriteriaList() {
		return criteriaList;
	}
	
	public List<Analysis> getPhenotypeList() {
		List<Analysis> result = new ArrayList<Analysis>();
		for (BloodComponentMatchCriteria criteria: criteriaList) {
			if (criteria.isNecessary()) result.add(criteria.getPhenotype());
		}
		return result;
	}
	
	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setSearchBloodGroup(boolean searchBloodGroup) {
		this.searchBloodGroup = searchBloodGroup;
	}

	public boolean isSearchBloodGroup() {
		return searchBloodGroup;
	}

	public void setRhesusFactor(Classifier rhesusFactor) {
		this.rhesusFactor = rhesusFactor;
	}

	public Classifier getRhesusFactor() {
		return rhesusFactor;
	}

	public void setSearchRhesus(boolean searchRhesus) {
		this.searchRhesus = searchRhesus;
	}

	public boolean isSearchRhesus() {
		return searchRhesus;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isUsed() {
		return used;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * Дата создания
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * Автор подбора
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * Описание подбора
	 */
	private String description;
	
	/**
	 * Подходящие компоненты
	 */
	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "trfu_blood_component_match_results", 
			joinColumns = { @JoinColumn(name = "match_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "component_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<BloodComponent> components;
	
	/**
	 * параметры
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_blood_component_match_criteria_list", 
			joinColumns = { @JoinColumn(name = "match_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "criteria_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<BloodComponentMatchCriteria> criteriaList;
	
	/**
	 * Группа крови
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodGroup bloodGroup;
	
	/**
	 * Необходим ли подбор по группе крови
	 */
	private boolean searchBloodGroup;
	
	/**
	 * Резус-фактор
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier rhesusFactor;
	
	/**
	 * Необходим ли подбор по резус-фактору
	 */
	private boolean searchRhesus;
	
	/**
	 * Был ли использован поиск в заявке на выдачу КК
	 */
	private boolean used;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1085673922582263235L;
}