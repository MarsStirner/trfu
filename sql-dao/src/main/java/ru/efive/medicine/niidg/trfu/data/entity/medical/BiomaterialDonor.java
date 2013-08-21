package ru.efive.medicine.niidg.trfu.data.entity.medical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.document.Document;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

/**
 * Донор (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_donors")
public class BiomaterialDonor extends Document implements ProcessedData {
	
	public int getExternalId() {
		return externalId;
	}

	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	
	@Transient
	public int getAge() {
		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();
		
		int age = 0;
		
		birthDate.setTime(birth);
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}
		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
			age--;
		}
		else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) && (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
			age--;
		}

		return age;
	}
	
	@Transient
	public String getAgeFull() {
		int age = getAge();
		StringBuffer buffer = new StringBuffer();
		buffer.append(age).append(" ").append(age%10==1 && age%100!=11 ? "год" : age%10>=2 && age%10<=4 && (age%100<10 || age%100>=20) ? "года" : "лет");
		return buffer.toString();
	}

	public String getPassportSeries() {
		return passportSeries;
	}

	public void setPassportSeries(String passportSeries) {
		this.passportSeries = passportSeries;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getInsuranceSeries() {
		return insuranceSeries;
	}

	public void setInsuranceSeries(String insuranceSeries) {
		this.insuranceSeries = insuranceSeries;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getEmployment() {
		return employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getInfectiousStatus() {
		return infectiousStatus;
	}

	public void setInfectiousStatus(String infectiousStatus) {
		this.infectiousStatus = infectiousStatus;
	}

	public String getPregnancy() {
		return pregnancy;
	}

	public void setPregnancy(String pregnancy) {
		this.pregnancy = pregnancy;
	}
	
	@Transient
	public String getType() {
		return "BiomaterialDonor";
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getStatusName() {
		return ApplicationHelper.getStatusName("BiomaterialDonor", getStatusId());
	}

	public int getGrouping() {
		return grouping;
	}

	public void setGrouping(int grouping) {
		this.grouping = grouping;
	}
	
	public void setHistory(Set<HistoryEntry> history) {
		this.history = history;
	}
	
	public Set<HistoryEntry> getHistory() {
		return history;
	}
	
	@Transient
	public List<HistoryEntry> getHistoryList() {
		List<HistoryEntry> result = new ArrayList<HistoryEntry>();
		if (history != null) {
			result.addAll(history);
		}
		Collections.sort(result, new Comparator<HistoryEntry>() {
			public int compare(HistoryEntry o1, HistoryEntry o2) {
				Calendar c1 = Calendar.getInstance(ApplicationHelper.getLocale());
				c1.setTime(o1.getCreated());
				Calendar c2 = Calendar.getInstance(ApplicationHelper.getLocale());
				c2.setTime(o2.getCreated());
				return c1.compareTo(c2);
			}
		});
		return result;
	}
	

	public boolean isFromMIS() {
		return fromMIS;
	}

	public void setFromMIS(boolean fromMIS) {
		this.fromMIS = fromMIS;
	}


	/**
	 * @return the tempStogateId
	 */
	public Integer getTempStorageId() {
		return tempStorageId;
	}

	/**
	 * @param tempStogateId the tempStogateId to set
	 */
	public void setTempStorageId(Integer tempStorageId) {
		this.tempStorageId = tempStorageId;
	}


	/**
	 * Идентификатор донора в МИС
	 */
	private int externalId;
	
	/**
     * Номер донора
     */
    private String number;
    
    /**
	 * фамилия
	 */
	private String lastName;

	/**
	 * отчество
	 */
	private String middleName;

	/**
	 * имя
	 */
	private String firstName;
	
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
	 * Серия паспорта
	 */
	private String passportSeries;
	
	/**
	 * Номер паспорта
	 */
	private String passportNumber;
	
	/**
	 * Серия страхового полиса ОМС
	 */
	private String insuranceSeries;
	
	/**
	 * Номер страхового полиса ОМС
	 */
	private String insuranceNumber;
	
	/**
	 * Место работы
	 */
	@Column(columnDefinition="text")
	private String employment;
	
	/**
	 * Рабочий телефон
	 */
	private String workPhone;
	
	/**
	 * Адрес регистрации
	 */
	private String registrationAddress;
	
	/**
	 * Фактический адрес
	 */
	private String factAddress;
	
	/**
	 * Телефон
	 */
	private String phone;
	
	/**
	 * Комментарий
	 */
	@Column(columnDefinition="text")
	private String commentary;
	
	/**
	 * Инфекционный статус
	 */
	private String infectiousStatus;
	
	/**
	 * Статус беремености
	 */
    private String pregnancy;
	
    /**
	 * Текущий статус документа в процессе
	 */
	@Column(name="status_id")
	private int statusId;
	
	@Column(name="temp_stogate_id")
	private Integer tempStorageId;
    
	/**
	 * Для группировок в представлениях
	 */
	@Transient
	private int grouping = 100;
	
	/**
	 * История
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_medical_donor_history", 
			joinColumns = { @JoinColumn(name = "donor_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	
	private boolean fromMIS;
	
	private static final long serialVersionUID = -2649469860128460148L;
}