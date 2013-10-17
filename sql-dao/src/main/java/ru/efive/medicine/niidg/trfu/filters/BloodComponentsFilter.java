package ru.efive.medicine.niidg.trfu.filters;

import java.util.Date;

/**
 * Фильтр для представления "Компоненты крови".
 * 
 * @author Siarhei Ushanau
 */
public class BloodComponentsFilter extends AbstractFilter<BloodComponentsFilter> {
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
	 * Дата донации с.
	 */
	private Date donationDateFrom;
	/**
	 * Дата донации по.
	 */
	private Date donationDateTo;
	/**
	 * Дата окончания срока хранения с.
	 */
	private Date expirationDateFrom;
	/**
	 * Дата окончания срока хранения по.
	 */
	private Date expirationDateTo;
	/**
	 * Признак закупленный.
	 */
	private boolean purchased;
	/**
	 * Дата создания записи с.
	 */
	private Date historyCreatedDateFrom;
	/**
	 * Дата создания записи по.
	 */
	private Date historyCreatedDateTo;

	/**
	 * Статус компонента крови (1 - Зарегистрирован; 2 - В карантине; 3 - Готов к выдаче; 
	 * 4 - Задержка; 5 - Готов к выдаче из карантина; 6 - Брак из карантина;
	 * 10 - Выдан; 11 - Забронирован; -1 - Брак; -2 - Списан; -10 - Утилизирован; 100 - Разделен;
	 * 0 - любой, значение по умолчанию, прописано в константе BLOOD_COMPONENT_STATUS_NULL_VALUE).
	 */
	private int statusId;
	/**
	 * ФИО донора.
	 */
	private String fio;
	
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

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public Date getHistoryCreatedDateFrom() {
		return historyCreatedDateFrom;
	}

	public void setHistoryCreatedDateFrom(Date historyCreatedDateFrom) {
		this.historyCreatedDateFrom = historyCreatedDateFrom;
	}

	public Date getHistoryCreatedDateTo() {
		return historyCreatedDateTo;
	}

	public void setHistoryCreatedDateTo(Date historyCreatedDateTo) {
		this.historyCreatedDateTo = historyCreatedDateTo;
	}

	public int getMakerNullValue() {
		return MAKER_NULL_VALUE;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}
	
	public Date getDonationDateFrom() {
		return donationDateFrom;
	}

	public void setDonationDateFrom(Date donationDateFrom) {
		this.donationDateFrom = donationDateFrom;
	}

	public Date getDonationDateTo() {
		return donationDateTo;
	}

	public void setDonationDateTo(Date donationDateTo) {
		this.donationDateTo = donationDateTo;
	}

	public Date getExpirationDateFrom() {
		return expirationDateFrom;
	}

	public void setExpirationDateFrom(Date expirationDateFrom) {
		this.expirationDateFrom = expirationDateFrom;
	}

	public Date getExpirationDateTo() {
		return expirationDateTo;
	}

	public void setExpirationDateTo(Date expirationDateTo) {
		this.expirationDateTo = expirationDateTo;
	}

	public boolean isPurchased() {
		return purchased;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
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
	
	@Override
	public void clear() {
		setDefaultValues();
	}

	protected void setDefaultValues() {
		number = null;
		donationDateFrom = null;
		donationDateTo = null;
		expirationDateFrom = null;
		expirationDateTo = null;
		makerId = MAKER_NULL_VALUE;
		bloodComponentTypeId = BLOOD_COMPONENT_TYPE_NULL_VALUE;
		donorCode = null;
		statusId = BLOOD_COMPONENT_STATUS_NULL_VALUE;
		bloodGroupId = BLOOD_GROUP_NULL_VALUE;
		rhesusFactorId = RHESUS_FACTOR_NULL_VALUE;
		fio = null;
		purchased = false;
		historyCreatedDateFrom = null;
		historyCreatedDateTo = null;
	}

	@Override
	public void fillFrom(BloodComponentsFilter source) {
		setNumber(source.getNumber());
		setDonationDateFrom(source.getDonationDateFrom());
		setDonationDateTo(source.getDonationDateTo());
		setExpirationDateFrom(source.getExpirationDateFrom());
		setExpirationDateTo(source.getExpirationDateTo());
		setMakerId(source.getMakerId());
		setBloodComponentTypeId(source.getBloodComponentTypeId());
		setDonorCode(source.getDonorCode());
		setStatusId(source.getStatusId());
		setBloodGroupId(source.getBloodGroupId());
		setRhesusFactorId(source.getRhesusFactorId());
		setFio(source.getFio());
		setPurchased(source.isPurchased());
		setHistoryCreatedDateFrom(source.getHistoryCreatedDateFrom());
		setHistoryCreatedDateTo(source.getHistoryCreatedDateTo());
	}
}