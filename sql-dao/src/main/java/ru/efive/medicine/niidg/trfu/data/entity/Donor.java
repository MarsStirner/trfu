package ru.efive.medicine.niidg.trfu.data.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorType;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardFilterFactory;
import org.apache.solr.analysis.StopFilterFactory;
//import org.apache.solr.analysis.NGramFilterFactory;*/
/*import org.hibernate.search.annotations.Analyzer;
//import org.hibernate.search.annotations.AnalyzerDef;
//import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
//import org.hibernate.search.annotations.Indexed;
//import org.hibernate.search.annotations.Parameter;
//import org.hibernate.search.annotations.TokenFilterDef;
//import org.hibernate.search.annotations.TokenizerDef;*/

/**
 * Документ донора
 */
@Entity
@Table(name = "trfu_donors")
public class Donor extends IdentifiedEntity implements ProcessedData {

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public String getExternalNumber() {
		return externalNumber;
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

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setDonorType(DonorType donorType) {
		this.donorType = donorType;
	}

	public DonorType getDonorType() {
		return donorType;
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
		if (birth != null) {
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
		else {
			return 0;
		}
	}
	
	@Transient
	public String getAgeFull() {
		int age = getAge();
		if (age == 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
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

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getEmployment() {
		return employment;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getWorkPhone() {
		return workPhone;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRejection(DonorRejection rejection) {
		this.rejection = rejection;
	}
	
	public DonorRejection getRejection() {
		return rejection;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	@Transient
	public String getType() {
		return "Donor";
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getStatusName() {
		String result = ApplicationHelper.getStatusName("Donor", getStatusId());
		if (getStatusId() == -1) {
			if (getRejection() != null && getRejection().getRejectionType() != null) {
				if (getRejection().getRejectionType().getType() == 1) {
					result += " до выяснения";
				}
				else if (getRejection().getRejectionType().getType() == 2) {
					result += " до заключения";
				}
				else {
					result += " до " + new SimpleDateFormat("dd.MM.yy").format(getRejection().getExpiration());
				}
			}
		}
		else if (getStatusId() == -2) {
			if (getRejection() != null && getRejection().getRejectionType() != null) {
				if (getRejection().getRejectionType().getType() == 3) {
					result += " до заключения";
				}
			}
		}
		return result;
	}

	public void setGrouping(int grouping) {
		this.grouping = grouping;
	}

	public int getGrouping() {
		return grouping;
	}
	
	/*@Field(name="content", index=Index.TOKENIZED)
    @Analyzer(definition="donor_content")
    @Transient
    public String getContent() {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append(getCategory() == 0? "Первичный ": "Кадровый ").append("донор ").append(getDescription()).append(" ");
			buffer.append("Группа крови ").append(getBloodGroup().getValue()).append(" резус-фактор ").append(getRhesusFactor().getValue()).append(" ");
			buffer.append("Номер паспорта ").append(getPassportSeries()).append(" ").append(getPassportNumber()).append(" ");
			buffer.append("Номер страхового полиса ОМС ").append(getInsuranceSeries()).append(" ").append(getInsuranceNumber()).append(" ");
			buffer.append("Место работы: ").append(getEmployment()).append(" ");
			buffer.append("Адрес регистрации: ").append(getRegistrationAddress()).append(" ");
			buffer.append("Фактический адрес: ").append(getFactAddress()).append(" ");
			buffer.append("Комментарий: ").append(getCommentary());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}*/
	
	public void setAvailableDonationTypes(List<BloodDonationType> availableDonationTypes) {
		this.availableDonationTypes = availableDonationTypes;
	}
	
	public List<BloodDonationType> getAvailableDonationTypes() {
		return availableDonationTypes;
	}

	public void setLastDonationType(BloodDonationType lastDonationType) {
		this.lastDonationType = lastDonationType;
	}

	public BloodDonationType getLastDonationType() {
		return lastDonationType;
	}

	public void setLastDonationDate(Date lastDonationDate) {
		this.lastDonationDate = lastDonationDate;
	}

	public Date getLastDonationDate() {
		return lastDonationDate;
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
		Collections.sort(result);
		return result;
	}

    /**
     * Добавление в историю компонента еще одной записи, если история пуста, то она создается
     * @param historyEntry Запись в истории компонента, которую надо добавить
     * @return  статус добавления (true - успех)
     */
    public boolean addToHistory(final HistoryEntry historyEntry) {
        if(history == null){
            this.history = new HashSet<HistoryEntry>(1);
        }
        return this.history.add(historyEntry);
    }

	public String getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(String extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getExtensionUuid() {
		return extensionUuid;
	}

	public void setExtensionUuid(String extensionUuid) {
		this.extensionUuid = extensionUuid;
	}

	public boolean isSendNews() {
		return sendNews;
	}

	public void setSendNews(boolean sendNews) {
		this.sendNews = sendNews;
	}

	/**
	 * дата создания документа
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
     * Автор
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
    /**
     * Номер донора
     */
    private String number;
    
    /**
     * Номер донора в ЕДЦ
     */
    private String externalNumber;
    
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
	 * Категория (0 - первичный, 1 - кадровый, 2 - повторный)
	 */
	private int category;
	
	/**
	 * Тип донора
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private DonorType donorType;
	
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
	 * Почтовый адрес
	 */
	private String mail;
	
	/**
	 * Комментарий
	 */
	@Column(columnDefinition="text")
	private String commentary;
	
	/**
	 * Пароль для доступа через внешний портал
	 */
	private String password;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	/**
	 * Текущий статус документа в процессе
	 */
	@Column(name="status_id")
	private int statusId;
	
	/**
	 * Для группировок в представлениях
	 */
	@Transient
	private int grouping = 100;
	
	/**
	 * Запись отвода
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private DonorRejection rejection;
	
	@Transient
	private List<BloodDonationType> availableDonationTypes;
	
	@Transient
	private BloodDonationType lastDonationType;
	
	@Transient
	private Date lastDonationDate;
	
	/**
	 * История
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_donor_history", 
			joinColumns = { @JoinColumn(name = "donor_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	
	@Transient
	public static final String DEFAULT_SEARCH_FIELD = "content";
	
	@Column(name="extension_number")
	private String extensionNumber;
	
	@Column(name="extension_uuid")
	private String extensionUuid;
	
	@Column(name="send_news")
	private boolean sendNews;
	
	private static final long serialVersionUID = 1L;
}