package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

/**
 * Обращение на заказ компонента крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_component_order_requests")
public class BloodComponentOrderRequest extends IdentifiedEntity implements ProcessedData {
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public BloodComponentType getComponentType() {
		return componentType;
	}

	public void setComponentType(BloodComponentType componentType) {
		this.componentType = componentType;
	}
	
	public boolean isAutologous() {
		return autologous;
	}
	
	public void setAutologous(boolean autologous) {
		this.autologous = autologous;
	}
	
	public boolean isIndividualSelection() {
		return individualSelection;
	}
	
	public void setIndividualSelection(boolean individualSelection) {
		this.individualSelection = individualSelection;
	}

	public String getAttendingDoctor() {
		return attendingDoctor;
	}

	public void setAttendingDoctor(String attendingDoctor) {
		this.attendingDoctor = attendingDoctor;
	}

    public String getAttendingDoctorLastName() {
        return attendingDoctorLastName;
    }

    public void setAttendingDoctorLastName(String attendingDoctorLastName) {
        this.attendingDoctorLastName = attendingDoctorLastName;
    }

    public String getAttendingDoctorFirstName() {
        return attendingDoctorFirstName;
    }

    public void setAttendingDoctorFirstName(String attendingDoctorFirstName) {
        this.attendingDoctorFirstName = attendingDoctorFirstName;
    }

    public String getAttendingDoctorMiddleName() {
        return attendingDoctorMiddleName;
    }

    public void setAttendingDoctorMiddleName(String attendingDoctorMiddleName) {
        this.attendingDoctorMiddleName = attendingDoctorMiddleName;
    }
    
    /**
	 * полное имя назначившего врача
	 */
	@Transient
	public String getAttendingDoctorDescription() {
		String result = ((StringUtils.isEmpty(attendingDoctorLastName)? "": attendingDoctorLastName + " ") +
				(StringUtils.isEmpty(attendingDoctorFirstName)? "": attendingDoctorFirstName + " ") +
				(StringUtils.isEmpty(attendingDoctorMiddleName)? "": attendingDoctorMiddleName)).trim();
		if (StringUtils.isEmpty(result)) {
			result = attendingDoctor;
		}
		return result;
	}

    public Integer getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public void setAttendingDoctorId(Integer attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

    public User getStaffNurse() {
		return staffNurse;
	}

	public void setStaffNurse(User staffNurse) {
		this.staffNurse = staffNurse;
	}

	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getRecipientMiddleName() {
		return recipientMiddleName;
	}

	public void setRecipientMiddleName(String recipientMiddleName) {
		this.recipientMiddleName = recipientMiddleName;
	}

	public String getRecipientFirstName() {
		return recipientFirstName;
	}

	public void setRecipientFirstName(String recipientFirstName) {
		this.recipientFirstName = recipientFirstName;
	}

	public Date getRecipientBirth() {
		return recipientBirth;
	}

	public void setRecipientBirth(Date recipientBirth) {
		this.recipientBirth = recipientBirth;
	}

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    @Transient
	public int getAge() {
		if (recipientBirth != null) {
			Calendar today = Calendar.getInstance();
			Calendar birthDate = Calendar.getInstance();
			
			int age = 0;
			
			birthDate.setTime(recipientBirth);
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
		StringBuffer buffer = new StringBuffer();
		buffer.append(age).append(" ").append(age%10==1 && age%100!=11 ? "год" : age%10>=2 && age%10<=4 && (age%100<10 || age%100>=20) ? "года" : "лет");
		return buffer.toString();
	}

	public String getIbNumber() {
		return ibNumber;
	}

	public void setIbNumber(String ibNumber) {
		this.ibNumber = ibNumber;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getIndication() {
		return indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getFactDate() {
		return factDate;
	}

	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setDoseCount(double doseCount) {
		this.doseCount = doseCount;
	}
	
	public double getDoseCount() {
		return doseCount;
	}
	
	public void setDoseCountResult(double doseCountResult) {
		this.doseCountResult = doseCountResult;
	}
	
	public double getDoseCountResult() {
		return doseCountResult;
	}

	public void setTransfusionType(int transfusionType) {
		this.transfusionType = transfusionType;
	}
	
	public int getTransfusionType() {
		return transfusionType;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getType() {
		return "BloodComponentOrder";
	}
	
	@Transient
	public String getStatusName() {
		return ApplicationHelper.getStatusName("BloodComponentOrder", getStatusId());
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
	 * Номер
	 */
	private String number;
	
	/**
	 * дата создания документа
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * № требования МИС
	 */
	private String externalNumber;
	
	/**
	 * Отделение
	 */
	private String division;
	
	/**
     * фамилия реципиента
     */
	private String recipient;

	/**
	 * отчество реципиента
	 */
	private String recipientMiddleName;

	/**
	 * имя реципиента
	 */
	private String recipientFirstName;

    private Integer recipientId;

	/**
	 * Дата рождения реципиента
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date recipientBirth;
	
	/**
	 * № ИБ
	 */
	private String ibNumber;
	
	/**
	 * Диагноз
	 */
	private String diagnosis;
	
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
	 * Компонент крови
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodComponentType componentType;
	
	/**
	 * Количество
	 */
	private int count;
	
	/**
	 * Показания к назначению
	 */
	private String indication;
	
	/**
	 * Вид трансфузии
	 */
	private int transfusionType;
	
	/**
	 * Дата плановой трансфузии
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable=true)
	private Date planDate;
	
	/**
     * Медицинская сестра
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User staffNurse;
	
    /**
     * Врач, назначивший трансфузию
     */
    //@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private String attendingDoctor;

    private String attendingDoctorLastName;
    private String attendingDoctorFirstName;
    private String attendingDoctorMiddleName;
    private Integer attendingDoctorId;




    /**
     * Дата/время выдачи компонента
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date factDate;
	
	/**
	 * Аутологичная кровь
	 */
	private boolean autologous;
	
	/**
	 * Подбор крови индивидуальный
	 */
	private boolean individualSelection;
	
	/**
	 * Количество, мл
	 */
	private int volume;
	
	/**
	 * Количество донорских доз
	 */
	private double doseCount;
	
	/**
	 * Количество донорских доз (результат выдачи)
	 */
	private double doseCountResult;
	
    /**
	 * Комментарий
	 */
    @Column(columnDefinition="text")
	private String commentary;
	
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
	 * История
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_blood_component_order_request_history", 
			joinColumns = { @JoinColumn(name = "request_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;


    private boolean fromMIS;
	
	private static final long serialVersionUID = -543434713300187398L;
}