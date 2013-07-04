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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.document.Document;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

/**
 * Операция (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_operations")
public class Operation extends Document implements ProcessedData {
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public Date getFactDate() {
		return factDate;
	}

	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public void setDonor(BiomaterialDonor donor) {
		this.donor = donor;
	}

	public BiomaterialDonor getDonor() {
		return donor;
	}

	public void setTransfusiologist(User transfusiologist) {
		this.transfusiologist = transfusiologist;
	}

	public User getTransfusiologist() {
		return transfusiologist;
	}

	public Classifier getOperationForm() {
		return operationForm;
	}

	public void setOperationForm(Classifier operationForm) {
		this.operationForm = operationForm;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
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
		return ((StringUtils.isEmpty(attendingDoctorLastName)? "": attendingDoctorLastName + " ") +
				(StringUtils.isEmpty(attendingDoctorFirstName)? "": attendingDoctorFirstName + " ") +
				(StringUtils.isEmpty(attendingDoctorMiddleName)? "": attendingDoctorMiddleName)).trim();
	}

    public int getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public void setAttendingDoctorId(int attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

	public boolean isRelated() {
		return related;
	}

	public void setRelated(boolean related) {
		this.related = related;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Division getDivision() {
		return division;
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

	public String getRecipientLastName() {
		return recipientLastName;
	}

	public void setRecipientLastName(String recipientLastName) {
		this.recipientLastName = recipientLastName;
	}
	
	/**
	 * полное имя реципиента
	 */
	@Transient
	public String getRecipientDescription() {
		return ((StringUtils.isEmpty(recipientLastName)? "": recipientLastName + " ") + (StringUtils.isEmpty(recipient)? "": recipient + " ") + 
		(StringUtils.isEmpty(recipientMiddleName)? "": recipientMiddleName)).trim();
	}
	
	public int getRecipientExternalId() {
		return recipientExternalId;
	}

	public void setRecipientExternalId(int recipientExternalId) {
		this.recipientExternalId = recipientExternalId;
	}

	public String getIbNumber() {
		return ibNumber;
	}

	public void setIbNumber(String ibNumber) {
		this.ibNumber = ibNumber;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeight() {
		return height;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

	public void setTbv(double tbv) {
		this.tbv = tbv;
	}

	public double getTbv() {
		return tbv;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getCellCount() {
		return cellCount;
	}

	public void setCellCount(double cellCount) {
		this.cellCount = cellCount;
	}

	public double getTnc() {
		return tnc;
	}

	public void setTnc(double tnc) {
		this.tnc = tnc;
	}

	public double getNcPerKg() {
		return ncPerKg;
	}

	public void setNcPerKg(double ncPerKg) {
		this.ncPerKg = ncPerKg;
	}

	public double getCd34() {
		return cd34;
	}

	public void setCd34(double cd34) {
		this.cd34 = cd34;
	}

	public double getCd34X6() {
		return cd34X6;
	}

	public void setCd34X6(double cd34x6) {
		cd34X6 = cd34x6;
	}

	public double getCd34X6PerKg() {
		return cd34X6PerKg;
	}

	public void setCd34X6PerKg(double cd34x6PerKg) {
		cd34X6PerKg = cd34x6PerKg;
	}

	public double getCd3() {
		return cd3;
	}

	public void setCd3(double cd3) {
		this.cd3 = cd3;
	}

	public double getCd3X8() {
		return cd3X8;
	}

	public void setCd3X8(double cd3x8) {
		cd3X8 = cd3x8;
	}

	public double getCd3x8PerKg() {
		return cd3x8PerKg;
	}

	public void setCd3x8PerKg(double cd3x8PerKg) {
		this.cd3x8PerKg = cd3x8PerKg;
	}

	public double getMncPercentage() {
		return mncPercentage;
	}

	public void setMncPercentage(double mncPercentage) {
		this.mncPercentage = mncPercentage;
	}

	public double getMncx9() {
		return mncx9;
	}

	public void setMncx9(double mncx9) {
		this.mncx9 = mncx9;
	}

	public void setUsingEr(boolean usingEr) {
		this.usingEr = usingEr;
	}

	public boolean isUsingEr() {
		return usingEr;
	}

	public void setOperationReport(OperationReport operationReport) {
		this.operationReport = operationReport;
	}

	public OperationReport getOperationReport() {
		return operationReport;
	}

	public String getUnp() {
		return unp;
	}

	public void setUnp(String unp) {
		this.unp = unp;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getCommentary() {
		return commentary;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getType() {
		return "Operation";
	}
	
	@Transient
	public String getStatusName() {
		return ApplicationHelper.getStatusName("Operation", getStatusId());
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
	
	
	public int getExternalId() {
		return externalId;
	}

	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}


	/**
	 * Номер
	 */
	private String number;
	
	/**
	 * Фактическая дата
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date factDate;
	
	/**
	 * Планируемая дата
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date planDate;
	
	/**
	 * Дата/время назначения процедуры
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date appointmentDate;
	
	/**
	 * Донор
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BiomaterialDonor donor;
	
	/**
	 * Врач-трансфузиолог
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User transfusiologist;
	
	/**
	 * Вид процедуры
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier operationForm;
	
	/**
	 * Тип процедуры (0 - ауто, 1 - алло)
	 */
	private int operationType;
	
	private String attendingDoctorLastName;
    private String attendingDoctorFirstName;
    private String attendingDoctorMiddleName;
    private int attendingDoctorId;
	
	/**
	 * Родственная/неродственная
	 */
	private boolean related;
	
	/**
	 * Отделение
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Division division;
	
	/**
	 * Реципиент (имя)
	 */
	private String recipient;
	
	/**
	 * Отчество реципиента
	 */
	private String recipientMiddleName;
	
	/**
	 * Фамилия реципиента
	 */
	private String recipientLastName;
	
	private int recipientExternalId;
	
	/**
	 * № ИБ
	 */
	private String ibNumber;
	
	/**
	 * Рост
	 */
	private String height;
	
	/**
	 * Вес
	 */
	private String weight;
	
	/**
	 * TBV
	 */
	private double tbv;
	
	/**
     * Объем биоматериала
     */
    private int volume;
    
    /**
     * Количество клеток, х10^9
     */
    private double cellCount;
    
    /**
     * TNC (x10 в 9 степени)
     */
    private double tnc;
    
    /**
     * NC/кг (x10 в 8 степени)
     */
    private double ncPerKg;
    
    /**
     * CD34
     */
    private double cd34;
    
    /**
     * CD34 (x10 в 6 степени)
     */
    private double cd34X6;
    
    /**
     * CD34 (x10 в 6 степени/кг)
     */
    private double cd34X6PerKg;
    
    /**
     * CD3
     */
    private double cd3;
    
    /**
     * CD3 (x10 в 8 степени)
     */
    private double cd3X8;
    
    /**
     * CD3 (x10 в 8 степени/кг)
     */
    private double cd3x8PerKg;
    
    /**
     * MNC,%
     */
    private double mncPercentage;
    
    /**
     * MNC (x10 в 9 степени)
     */
    private double mncx9;
    
    /**
     * С эр. массой
     */
    private boolean usingEr;
    
    /**
	 * Процедурный лист
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private OperationReport operationReport;
	
	/**
	 * УНП
	 */
	private String unp;
    
    /**
	 * Комментарий
	 */
	@Column(columnDefinition="text")
	private String commentary;
	
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
	@JoinTable(name = "trfu_medical_operation_history", 
			joinColumns = { @JoinColumn(name = "operation_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	
	private boolean fromMIS = false;
	
	private int externalId;
	
	private static final long serialVersionUID = -8145103746578726409L;
}