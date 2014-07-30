package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.DirAdditionalLiquor;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

/**
 * Компонент крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_components")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BloodComponent extends IdentifiedEntity implements ProcessedData, Cloneable {

    public void setAuthor(User author) {
		this.author = author;
	}
	
	public User getAuthor() {
		return author;
	}

	@Transient
	public String getFullNumber() {
		return purchased? (getParentNumber() == null || getParentNumber().equals("")? getNumber(): getParentNumber() + "-" + getNumber()): getParentNumber() + "-" + getNumber();
	}
	
	public void setParentNumber(String parentNumber) {
		this.parentNumber = parentNumber;
	}
	
	public String getParentNumber() {
		return parentNumber;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public Contragent getMaker() {
		return maker;
	}

	public void setMaker(Contragent maker) {
		this.maker = maker;
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

	public Date getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Anticoagulant getAnticoagulant() {
		return anticoagulant;
	}

	public void setAnticoagulant(Anticoagulant anticoagulant) {
		this.anticoagulant = anticoagulant;
	}
	
	public void setAnticoagulantVolume(double anticoagulantVolume) {
		this.anticoagulantVolume = anticoagulantVolume;
	}
	
	public double getAnticoagulantVolume() {
		return anticoagulantVolume;
	}

	public BloodComponentType getComponentType() {
		return componentType;
	}

	public void setComponentType(BloodComponentType componentType) {
		this.componentType = componentType;
	}

	public void setEmVolume(int emVolume) {
		this.emVolume = emVolume;
	}

	public int getEmVolume() {
		return emVolume;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getPreInactivatedVolume() {
		return preInactivatedVolume;
	}

	public void setPreInactivatedVolume(int preInactivatedVolume) {
		this.preInactivatedVolume = preInactivatedVolume;
	}
	
	public byte getLowerStorageTemperature() {
		return lowerStorageTemperature;
	}

	public void setLowerStorageTemperature(byte lowerStorageTemperature) {
		this.lowerStorageTemperature = lowerStorageTemperature;
	}

	public byte getHigherStorageTemperature() {
		return higherStorageTemperature;
	}

	public void setHigherStorageTemperature(byte higherStorageTemperature) {
		this.higherStorageTemperature = higherStorageTemperature;
	}
	
	public void setDoseCount(double doseCount) {
		this.doseCount = doseCount;
	}
	
	public double getDoseCount() {
		return doseCount;
	}
	
	public void setCellCount(double cellCount) {
		this.cellCount = cellCount;
	}
	
	public double getCellCount() {
		return cellCount;
	}

	public void setDosePerKg(double dosePerKg) {
		this.dosePerKg = dosePerKg;
	}

	public double getDosePerKg() {
		return dosePerKg;
	}

	public void setPreservative(Classifier preservative) {
		this.preservative = preservative;
	}
	
	public Classifier getPreservative() {
		return preservative;
	}
	
	public void setPreservativeVolume(int preservativeVolume) {
		this.preservativeVolume = preservativeVolume;
	}
	
	public int getPreservativeVolume() {
		return preservativeVolume;
	}

	public void setDonationId(int donationId) {
		this.donationId = donationId;
	}

	public int getDonationId() {
		return donationId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderId() {
		return orderId;
	}
	
	public boolean isAutologous() {
		return autologous;
	}
	
	public void setAutologous(boolean autologous) {
		this.autologous = autologous;
	}

	public void setInactivated(boolean inactivated) {
		this.inactivated = inactivated;
	}

	public boolean isInactivated() {
		return inactivated;
	}
	
	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}
	
	public boolean isFiltered() {
		return filtered;
	}
	
	public void setIrradiated(boolean irradiated) {
		this.irradiated = irradiated;
	}

	public boolean isIrradiated() {
		return irradiated;
	}
	
	public void setLeukoFiltered(boolean leukoFiltered) {
		this.leukoFiltered = leukoFiltered;
	}
	
	public boolean isLeukoFiltered() {
		return leukoFiltered;
	}

	public void setPooling(boolean pooling) {
		this.pooling = pooling;
	}

	public boolean isPooling() {
		return pooling;
	}

	public void setWashing(boolean washing) {
		this.washing = washing;
	}

	public boolean isWashing() {
		return washing;
	}

	public void setAutoApheresis(boolean autoApheresis) {
		this.autoApheresis = autoApheresis;
	}

	public boolean isAutoApheresis() {
		return autoApheresis;
	}
	
	public void setDiscreteApheresis(boolean discreteApheresis) {
		this.discreteApheresis = discreteApheresis;
	}
	
	public boolean isDiscreteApheresis() {
		return discreteApheresis;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}
	
	public boolean isSplit() {
		return split;
	}

	public void setSplitVolume(int splitVolume) {
		this.splitVolume = splitVolume;
	}

	public int getSplitVolume() {
		return splitVolume;
	}

	public void setSplitDate(Date splitDate) {
		this.splitDate = splitDate;
	}

	public Date getSplitDate() {
		return splitDate;
	}
	
	public void setQuarantineStartDate(Date quarantineStartDate) {
		this.quarantineStartDate = quarantineStartDate;
	}
	
	public Date getQuarantineStartDate() {
		return quarantineStartDate;
	}
	
	public void setQuarantineFinishDate(Date quarantineFinishDate) {
		this.quarantineFinishDate = quarantineFinishDate;
	}
	
	public Date getQuarantineFinishDate() {
		return quarantineFinishDate;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}
	
	public boolean isPurchased() {
		return purchased;
	}
	
	public void setDonorCode(String donorCode) {
		this.donorCode = donorCode;
	}
	
	public String getDonorCode() {
		return donorCode;
	}
	
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	@Transient
	public String getType() {
		return "BloodComponent";
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getStatusName() {
		return ApplicationHelper.getStatusName("BloodComponent", getStatusId());
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

	public BloodComponent cloneComponent() {
		try {
			return (BloodComponent) super.clone();
		}
		catch(CloneNotSupportedException e) {
			System.out.println("Cloning not allowed."); 
			return this;
		}
	}
	
	@Transient
	public String getDefectReason() {
		String result = "";
		if (statusId == -1) {
			for (HistoryEntry entry: history) {
				if (entry.getActionId() == -1 || entry.getActionId() == -2 || entry.getActionId() == -3) {
					return entry.getCommentary();
				}
			}
		}
		return result;
	}
	
	public void setInControl(boolean inControl) {
		this.inControl = inControl;
	}

	public boolean isInControl() {
		return inControl;
	}
	
	public void setQualityControlList(List<Analysis> qualityControlList) {
		//this.qualityControlList = qualityControlList;
		appointment.setTests(qualityControlList);
	}
	
	public List<Analysis> getQualityControlList() {
		return qualityControlList == null? (appointment != null? appointment.getTests(): null): qualityControlList;
	}
	
	public ExternalAppointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(ExternalAppointment appointment) {
		this.appointment = appointment;
	}
	
	public DirAdditionalLiquor getDirAdditionalLiquor() {
		return dirAdditionalLiquor;
	}
	
	public void setDirAdditionalLiquor(DirAdditionalLiquor dirAdditionalLiquor) {
		this.dirAdditionalLiquor = dirAdditionalLiquor;
	}
	
	public BloodDonationRequest getDonation() {
		return donation;
	}

	public void setDonation(BloodDonationRequest donation) {
		this.donation = donation;
	}
	
	public BloodSystem getBloodSystem() {
		return bloodSystem;
	}

	public void setBloodSystem(BloodSystem bloodSystem) {
		this.bloodSystem = bloodSystem;
	}
	
	public Classifier getAdditionalLiquor() {
		return additionalLiquor;
	}

	public void setAdditionalLiquor(Classifier additionalLiquor) {
		this.additionalLiquor = additionalLiquor;
	}

	public int getAdditionalVolume() {
		return additionalVolume;
	}

	public void setAdditionalVolume(int additionalVolume) {
		this.additionalVolume = additionalVolume;
	}
	
	/**
	 * Автор документа
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * Номер родительского документа
	 */
	private String parentNumber;
	
	/**
	 * Номер КК в рамках донации
	 */
	private String number;

	/**
	 * Изготовитель
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Contragent maker;
	
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
	 * дата донации
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date donationDate;
	
    /**
	 * дата производства
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date productionDate;
    
    /**
	 * дата окончания срока хранения
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date expirationDate;
    
    /**
     * Антикоагулянт
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Anticoagulant anticoagulant;
    
    /**
     * Объем антикоагулянта
     */
    private double anticoagulantVolume;
    
    /**
     * Тип компонента крови
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private BloodComponentType componentType;
    
    /**
     * Вес ЭМ до фильтрации
     */
    private int emVolume;
    
    /**
     * Объем компонента
     */
    private int volume;
    
    /**
     * Объем до вирусинактивации, мл
     */
    private int preInactivatedVolume;
    
    /**
     * Низшая температура хранения
     */
    private byte lowerStorageTemperature;
    
    /**
     * Высшая температура хранения
     */
    private byte higherStorageTemperature;
    
    /**
     * Количество доз
     */
    private double doseCount;
    
    /**
     * Количество клеток, х1011/x109
     */
    private double cellCount;
    
    /**
     * Доза на кг массы тела (x10 в восьмой степени/кг)
     */
    private double dosePerKg;
    
    /**
     * Ресуспендирующий/Седиментирующий раствор
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Classifier preservative;
    
    /**
     * Объем ресуспендирующего/седиментирующего раствора
     */
    private int preservativeVolume;
    
    /**
     * идентификатор донации
     */
    private int donationId;
    
    /**
     * идентификатор заказа на донацию
     */
    private int orderId;
    
    /**
	 * Аутологичный компонент
	 */
	private boolean autologous;
    
	/**
	 * Вирусинактивация
	 */
	private boolean inactivated;
	
	/**
	 * Фильтрация
	 */
	private boolean filtered;
	
	/**
	 * Облучение
	 */
	private boolean irradiated;
	
	/**
	 * Лейкофильтрация
	 */
	private boolean leukoFiltered;
	
	/**
	 * Пулирование
	 */
	private boolean pooling;
	
	/**
	 * Отмывание
	 */
	private boolean washing;
	
	/**
	 * Автоматический аферез
	 */
	private boolean autoApheresis;
	
	/**
	 * Дискретный аферез
	 */
	private boolean discreteApheresis;
	
	/**
	 * Получен путем разделения
	 */
	private boolean split;
	
	/**
	 * Объем компонента, из которого был получен
	 */
	private int splitVolume;
	
	/**
	 * Дата разделения
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date splitDate;
	
	/**
	 * Дата начала карантина
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date quarantineStartDate;
	
	/**
	 * Дата завершения карантина
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date quarantineFinishDate;
	
	/**
	 * Закупленный компонент
	 */
	private boolean purchased;
	
	/**
	 * Код донора (для закупленных компонентов)
	 */
	private String donorCode;
	
	/**
	 * Дата поступления (закупленных компонентов)
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date receivedDate;
	
	/**
	 * Номер накладной (для закупленных компонентов)
	 */
	private String invoiceNumber;
	
	/**
	 * Персонифицированный КК
	 */
	private boolean personal;
	
	/**
	 * ФИО реципиента
	 */
	@Column(nullable=true)
	private String recipient;
	
	/**
	 * Текущий статус документа в процессе
	 */
    @Column(name="status_id")
	private int statusId;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	/**
	 * История
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_blood_component_history", 
			joinColumns = { @JoinColumn(name = "component_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	
	/**
	 * На контроле
	 */
	private boolean inControl;
	
	/**
	 * Анализы
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_blood_component_quality_control", 
			joinColumns = { @JoinColumn(name = "component_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Analysis> qualityControlList;
	
	/**
	 * Направление на анализы
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ExternalAppointment appointment;
	
	/**
     * Донация
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "donationId", insertable = false, updatable = false)
    private BloodDonationRequest donation;
    
    /**
	 * Значения по умолчанию для ресуспендирующего раствора
	 */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "componentType_id", referencedColumnName = "componentType_id", insertable=false, updatable=false)
	private DirAdditionalLiquor dirAdditionalLiquor;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodSystem bloodSystem;
    
    /**
     * Тип добавочного раствора
     */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Classifier additionalLiquor;
	private int additionalVolume ;

    /**
     * Путь к месту хранения финальной клинической этикетки
     */
    @Column(name = "bigLabelPath", nullable = true)
    private String bigLabelPath;

    public String getBigLabelPath() {
        return bigLabelPath;
    }

    public void setBigLabelPath(String bigLabelPath) {
        this.bigLabelPath = bigLabelPath;
    }

    /**
     * Дата проведения вирусинактивации КК
     */
    @Column(name="virusInactivationDate", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date virusInactivationDate;

    public void setVirusInactivationDate(Date virusInactivationDate) {
        this.virusInactivationDate = virusInactivationDate;
    }

    public Date getVirusInactivationDate() {
        return virusInactivationDate;
    }

    private static final long serialVersionUID = -7114694453722100437L;
}