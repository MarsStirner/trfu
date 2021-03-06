package ru.efive.medicine.niidg.trfu.filters;

import java.util.Date;

/**
 * Фильтр для представления "Заявки на выдачу компонентов крови".
 * 
 * @author Siarhei Ushanau
 */
public class BloodComponentOrdersFilter extends AbstractFilter<BloodComponentOrdersFilter> {
	private static final long serialVersionUID = 1257893669842108860L;

	/**
	 * "Нулевое" значение для вида трансфузии.
	 */
	public static final int TRANSFUSION_TYPE_NULL_VALUE = 2;

	/**
	 * Номер обращения.
	 */
	private String number;
	/**
	 * Дата создания записи об обращении.
	 */
	private Date created;
	/**
	 * Отделение.
	 */
	private String division;
	/**
	 * Фамилия реципиента.
	 */
	private String recipient;
	/**
	 * Имя реципиента.
	 */
	private String recipientFirstName;
	/**
	 * Отчество реципиента.
	 */
	private String recipientMiddleName;
	/**
	 * Дата рождения реципиента.
	 */
	private Date recipientBirth;
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
	 * Тип компонента крови.
	 */
	private int bloodComponentTypeId;
	/**
	 * Вид трансфузии (0 - Плановая; 1 - Экстренная;
	 * 2 - любая, значение по умолчанию, прописано в константе TRANSFUSION_TYPE_NULL_VALUE).
	 */
	private int transfusionTypeId;
	/**
	 * Дата выдачи компонента крови.
	 */
	private Date factDate;
	
	/**
     * Конструктор по умолчанию.
     */
	public BloodComponentOrdersFilter() {
		super();
		setDefaultValues();
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public String getRecipientFirstName() {
		return recipientFirstName;
	}

	public void setRecipientFirstName(String recipientFirstName) {
		this.recipientFirstName = recipientFirstName;
	}

	public String getRecipientMiddleName() {
		return recipientMiddleName;
	}

	public void setRecipientMiddleName(String recipientMiddleName) {
		this.recipientMiddleName = recipientMiddleName;
	}

	public Date getRecipientBirth() {
		return recipientBirth;
	}

	public void setRecipientBirth(Date recipientBirth) {
		this.recipientBirth = recipientBirth;
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

	public int getBloodComponentTypeId() {
		return bloodComponentTypeId;
	}

	public void setBloodComponentTypeId(int bloodComponentTypeId) {
		this.bloodComponentTypeId = bloodComponentTypeId;
	}

	public int getTransfusionTypeId() {
		return transfusionTypeId;
	}

	public void setTransfusionTypeId(int transfusionTypeId) {
		this.transfusionTypeId = transfusionTypeId;
	}

	public Date getFactDate() {
		return factDate;
	}

	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}

	public int getTransfusionTypeNullValue() {
		return TRANSFUSION_TYPE_NULL_VALUE;
	}

	protected void setDefaultValues() {
		number = null;
		created = null;
		division = null;
		recipient = null;
		recipientFirstName = null;
		recipientMiddleName = null;
		recipientBirth = null;
		bloodGroupId = BLOOD_GROUP_NULL_VALUE;
		rhesusFactorId = RHESUS_FACTOR_NULL_VALUE;
		bloodComponentTypeId = BLOOD_COMPONENT_TYPE_NULL_VALUE;
		transfusionTypeId = TRANSFUSION_TYPE_NULL_VALUE;
		factDate = null;
	}

	@Override
	public void clear() {
		setDefaultValues();
	}

	@Override
	public void fillFrom(BloodComponentOrdersFilter source) {
		setNumber(source.getNumber());
		setCreated(source.getCreated());
		setDivision(source.getDivision());
		setRecipient(source.getRecipient());
		setRecipientFirstName(source.getRecipientFirstName());
		setRecipientMiddleName(source.getRecipientMiddleName());
		setRecipientBirth(source.getRecipientBirth());
		setBloodGroupId(source.getBloodGroupId());
		setRhesusFactorId(source.getRhesusFactorId());
		setBloodComponentTypeId(source.getBloodComponentTypeId());
		setTransfusionTypeId(source.getTransfusionTypeId());
		setFactDate(source.getFactDate());
	}
}