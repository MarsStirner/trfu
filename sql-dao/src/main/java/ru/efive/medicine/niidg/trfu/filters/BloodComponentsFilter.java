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

	public int getMakerNullValue() {
		return MAKER_NULL_VALUE;
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
		donationDate = null;
		expirationDate = null;
		makerId = MAKER_NULL_VALUE;
		bloodComponentTypeId = BLOOD_COMPONENT_TYPE_NULL_VALUE;
		donorCode = null;
		statusId = BLOOD_COMPONENT_STATUS_NULL_VALUE;
		bloodGroupId = BLOOD_GROUP_NULL_VALUE;
		rhesusFactorId = RHESUS_FACTOR_NULL_VALUE;
	}

	@Override
	public void fillFrom(BloodComponentsFilter source) {
		setNumber(source.getNumber());
		setDonationDate(source.getDonationDate());
		setExpirationDate(source.getExpirationDate());
		setMakerId(source.getMakerId());
		setBloodComponentTypeId(source.getBloodComponentTypeId());
		setDonorCode(source.getDonorCode());
		setStatusId(source.getStatusId());
		setBloodGroupId(source.getBloodGroupId());
		setRhesusFactorId(source.getRhesusFactorId());
	}
}