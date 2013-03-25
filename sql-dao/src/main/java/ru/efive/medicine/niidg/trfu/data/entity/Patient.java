package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Пациент (временный тип)
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_external_patients")
public class Patient extends IdentifiedEntity {
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	/**
	 * полное имя
	 */
	@Transient
	public String getDescription() {
		return lastName + " " + (firstName != null && !firstName.equals("")? firstName + " ": "") + 
		(middleName != null && !middleName.equals("")? middleName: "");
	}
	
	/**
	 * краткая форма полного имени
	 */
	@Transient
	public String getDescriptionShort() {
		return lastName + " " + (firstName != null && !firstName.equals("")? firstName.substring(0, 1) + ". ": "") + 
		(middleName != null && !middleName.equals("")? middleName.substring(0, 1) + ".": "");
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Classifier getRhesusFactor() {
		return rhesusFactor;
	}

	public void setRhesusFactor(Classifier rhesusFactor) {
		this.rhesusFactor = rhesusFactor;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getRegistrationAddress() {
		return registrationAddress;
	}

	public void setRegistrationAddress(String registrationAddress) {
		this.registrationAddress = registrationAddress;
	}

	public String getFactAddress() {
		return factAddress;
	}

	public void setFactAddress(String factAddress) {
		this.factAddress = factAddress;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * фамилия
	 */
	private String lastName;

	/**
	 * имя
	 */
	private String firstName;

	/**
	 * отчество
	 */
	private String middleName;

	/**
	 * дата создания документа
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * Группа крови
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodGroup bloodGroup;
	
	/**
	 * Резус-фактор
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier rhesusFactor;
	
	/**
	 * Пол (0 - female, 1 - male)
	 */
    private int gender;
	
	/**
	 * дата рождения
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date birth;
	
    /**
	 * Адрес регистрации
	 */
	private String registrationAddress;
	
	/**
	 * Фактический адрес
	 */
	private String factAddress;
	
	/**
	 * Комментарий
	 */
	@Column(columnDefinition="text")
	private String commentary;
    
	/**
	 * true - пользователь удалён, false или null - не удалён
	 */
	private Boolean deleted;
	
	private static final long serialVersionUID = 8997479278807112160L;
}