package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;

/**
 * Фильтр для представления "Компоненты крови".
 * 
 * @author Siarhei Ushanau
 */
public class BloodComponentsFilter extends AppendSRPDFilter<BloodComponentsFilter> {
	private static final long serialVersionUID = -7622975649986835130L;

	/**
	 * "Нулевое" значение для статуса компонента крови.
	 */
	public static final int BLOOD_COMPONENT_STATUS_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для изготовителя.
	 */
	public static final int MAKER_NULL_VALUE = 0;
	/**
	 * Тип параметра для поиска satus_id: искать на "равно"
	 */
	public static final int STATUS_ID_EQ = 0;
	/**
	 * Тип параметра для поиска satus_id: искать на "не равно"
	 */
	public static final int STATUS_ID_NE = -1;
	/**
	 * Тип параметра для поиска satus_id: null-значение
	 */
	public static final int STATUS_ID_NULL = -10;
	public enum InControl {
		/* Тип параметра для поиска in_control: искать на "равно" */
		IN_CONTROL_EQ,
		/* Тип параметра для поиска in_control: искать на "не равно" */
		IN_CONTROL_NE,
		/* Тип параметра для поиска in_control: не искать */
		IN_CONTROL_NULL
	}
	/**
	 * Номер компонента крови.
	 */
	private String number;
	/**
	 * Тип компонента крови.
	 */
	private int bloodComponentTypeId;
	/**
	 * Код донора крови.
	 */
	private String donorCode;
	/**
	 * Изготовитель компонента крови.
	 */
	private int makerId;
    /**
     * Группа крови. Используются значения из таблицы trfu_blood_groups (BloodGroup тип),
     * к которым добавляется значение 0 - любой, значение по умолчанию, прописано в константе BLOOD_GROUP_NULL_VALUE).
     */
	private int bloodGroupId;
    /**
     * Резус-фактор. Используются значения из таблицы trfu_classifiers, категория "Резус-фактор" (Classifier тип),
     * к которым добавляется значение 0 - любой, значение по умолчанию, прописано в константе RHESUS_FACTOR_NULL_VALUE).
     */
	private int rhesusFactorId;
	/**
	 * Дата донации.
	 */
	private Date donationDate;
	/**
	 * Дата окончания срока хранения.
	 */
	private Date expirationDate;
	/**
	 * Статус компонента крови (1 - Зарегистрирован; 2 - В карантине; 3 - Готов к выдаче; 
	 * 4 - Задержка; 5 - Готов к выдаче из карантина; 6 - Брак из карантина;
	 * 10 - Выдан; 11 - Забронирован; -1 - Брак; -2 - Списан; -10 - Утилизирован; 100 - Разделен;
	 * 0 - любой, значение по умолчанию, прописано в константе BLOOD_COMPONENT_STATUS_NULL_VALUE).
	 */
	private int statusId;
	private Boolean inControl;
	private Boolean split;
	/**
	 * ФИО донора.
	 */
	//private String fio;
	/**
	 * Тип поиска:
	 * STATUS_ID_EQ - поиск по соответствию
	 * STATUS_ID_NE - поиск на несоответствие
	 * 
	 */
	private int statusIdCompareFlag;
	/**
	 * Тип поиска:
	 * IN_CONTROL_EQ - поиск по соответствию
	 * IN_CONTROL_NE - поиск на несоответствие
	 * IN_CONTROL_NULL_VALUE - не осуществлять поиск; значение по умолчанию
	 */
	private InControl inControlCompareFlag;
	
	private Integer donationId;
	
	private Integer orderId;
	
	private Boolean purchased;
	
	private String parentNumber;
	
	private Date quarantineFinishDate;
	
	private List<Integer> listIds;
		
	private boolean expirationDateNull;
	
	private Date expirationDateGe;
	private Date expirationDatelt;
	
	/* temporal */
	private List<Junction> listJunctions;
	private List<String> listAlias;
	private Map<String, Object> map;
	
    public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getBloodComponentTypeId() {
		return bloodComponentTypeId;
	}

	public void setBloodComponentTypeId(int bloodComponentTypeId) {
		this.bloodComponentTypeId = bloodComponentTypeId;
	}

	public String getDonorCode() {
		return donorCode;
	}

	public void setDonorCode(String donorCode) {
		this.donorCode = donorCode;
	}

	public int getMakerId() {
		return makerId;
	}

	public void setMakerId(int makerId) {
		this.makerId = makerId;
	}

	public int getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(int bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public int getRhesusFactorId() {
		return rhesusFactorId;
	}

	public void setRhesusFactorId(int rhesusFactorId) {
		this.rhesusFactorId = rhesusFactorId;
	}

	public Date getDonationDate() {
		return donationDate;
	}

	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Boolean getInControl() {
		return inControl;
	}

	public void setInControl(Boolean inControl) {
		this.inControl = inControl;
	}
	

	public Boolean getSplit() {
		return split;
	}

	public void setSplit(Boolean split) {
		this.split = split;
	}

	public int getMakerNullValue() {
		return MAKER_NULL_VALUE;
	}

	public String getFio() {
		return getFirstName();
	}

	public void setFio(String fio) {
		setFirstName(fio);
		setLastName(fio);
		setMiddleName(fio);
	}

	/**
	 * @return the statusIdCompareFlag
	 */
	public int getStatusIdCompareFlag() {
		return statusIdCompareFlag;
	}

	/**
	 * @param statusIdCompareFlag the statusIdCompareFlag to set
	 */
	public void setStatusIdCompareFlag(int statusIdCompareFlag) {
		this.statusIdCompareFlag = statusIdCompareFlag;
	}

	public InControl getInControlCompareFlag() {
		return inControlCompareFlag;
	}

	public void setInControlCompareFlag(InControl inControlCompareFlag) {
		this.inControlCompareFlag = inControlCompareFlag;
	}

	/**
	 * @return the donationId
	 */
	public Integer getDonationId() {
		return donationId;
	}

	/**
	 * @param donationId the donationId to set
	 */
	public void setDonationId(Integer donationId) {
		this.donationId = donationId;
	}

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the purchased
	 */
	public Boolean getPurchased() {
		return purchased;
	}

	/**
	 * @param purchased the purchased to set
	 */
	public void setPurchased(Boolean purchased) {
		this.purchased = purchased;
	}

	/**
	 * @return the quarantineFinishDate
	 */
	public Date getQuarantineFinishDate() {
		return quarantineFinishDate;
	}

	/**
	 * @param quarantineFinishDate the quarantineFinishDate to set
	 */
	public void setQuarantineFinishDate(Date quarantineFinishDate) {
		this.quarantineFinishDate = quarantineFinishDate;
	}

	/**
     * Конструктор по умолчанию.
     */
	public BloodComponentsFilter() {
		super();
		setDefaultValues();
	}
	
	public int getStatusNullValue() {
		return BLOOD_COMPONENT_STATUS_NULL_VALUE;
	}
	
	/**
	 * @return the listIds
	 */
	public List<Integer> getListIds() {
		return listIds;
	}

	/**
	 * @param listIds the listIds to set
	 */
	public void setListIds(List<Integer> listIds) {
		this.listIds = listIds;
	}

	/**
	 * @return the parentNumber
	 */
	public String getParentNumber() {
		return parentNumber;
	}

	/**
	 * @param parentNumber the parentNumber to set
	 */
	public void setParentNumber(String parentNumber) {
		this.parentNumber = parentNumber;
	}

	public Boolean isExpirationDateNull() {
		return expirationDateNull;
	}

	public void setExpirationDateNull(boolean expirationDateNull) {
		this.expirationDateNull = expirationDateNull;
	}

	public Date getExpirationDateGe() {
		return expirationDateGe;
	}

	public void setExpirationDateGe(Date expirationDateGe) {
		this.expirationDateGe = expirationDateGe;
	}

	public Date getExpirationDatelt() {
		return expirationDatelt;
	}

	public void setExpirationDatelt(Date expirationDatelt) {
		this.expirationDatelt = expirationDatelt;
	}

	public List<Junction> getListJunctions() {
		return listJunctions;
	}

	public void setListJunctions(List<Junction> listJunctions) {
		this.listJunctions = listJunctions;
	}

	public List<String> getListAlias() {
		return listAlias;
	}

	public void setListAlias(List<String> listAlias) {
		this.listAlias = listAlias;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	@Override
	public void clear() {
		setDefaultValues();
	}

	protected void setDefaultValues() {
		super.setDefaultValues();
		number = null;
		donationDate = null;
		expirationDate = null;
		makerId = MAKER_NULL_VALUE;
		bloodComponentTypeId = BLOOD_COMPONENT_TYPE_NULL_VALUE;
		donorCode = null;
		statusId = BLOOD_COMPONENT_STATUS_NULL_VALUE;
		bloodGroupId = BLOOD_GROUP_NULL_VALUE;
		rhesusFactorId = RHESUS_FACTOR_NULL_VALUE;
		//fio = null;
		statusIdCompareFlag = STATUS_ID_NULL;
		donationId = null;
		orderId = null;
		purchased = null;
		quarantineFinishDate = null;
		listIds = null;
		parentNumber = null;
		inControlCompareFlag = InControl.IN_CONTROL_NULL;
		split = null;
		expirationDateNull = false;
		expirationDateGe = null;
		expirationDatelt = null;
		listJunctions = new ArrayList<Junction>();
		listAlias = new ArrayList<String>();
		map = null;
		
	}

	@Override
	public void fillFrom(BloodComponentsFilter source) {
		super.fillFrom(source);
		setNumber(source.getNumber());
		setDonationDate(source.getDonationDate());
		setExpirationDate(source.getExpirationDate());
		setMakerId(source.getMakerId());
		setBloodComponentTypeId(source.getBloodComponentTypeId());
		setDonorCode(source.getDonorCode());
		setStatusId(source.getStatusId());
		setBloodGroupId(source.getBloodGroupId());
		setRhesusFactorId(source.getRhesusFactorId());
		setFio(source.getFio());
		setStatusIdCompareFlag(source.getStatusIdCompareFlag());
		setDonationId(source.getDonationId());
		setOrderId(source.getOrderId());
		setPurchased(source.getPurchased());
		setQuarantineFinishDate(source.getQuarantineFinishDate());
		setListIds(source.getListIds());
		setParentNumber(source.getParentNumber());
		setInControl(source.getInControl());
		setInControlCompareFlag(source.getInControlCompareFlag());
		setSplit(source.getSplit());
		setExpirationDateNull(source.isExpirationDateNull());
		setExpirationDateGe(source.getExpirationDateGe());
		setListJunctions(source.getListJunctions());
		setListAlias(source.getListAlias());
		setMap(source.getMap());
	}
}