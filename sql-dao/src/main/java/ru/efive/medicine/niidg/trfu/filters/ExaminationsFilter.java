package ru.efive.medicine.niidg.trfu.filters;

import java.util.Date;

/**
 * Фильтр для представления "Обращения на обследование".
 * 
 * @author Siarhei Ushanau
 */
public class ExaminationsFilter extends AbstractFilter<ExaminationsFilter> {
	private static final long serialVersionUID = 8458216636560993764L;
	/**
	 * "Нулевое" значение для статуса обращения на обследование.
	 */
	public static final int EXAMINATION_STATUS_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для типа обращения на обследование.
	 */
	public static final int EXAMINATION_TYPE_NULL_VALUE = 2;

	/**
	 * Номер обращения на обследование.
	 */
	private String number;
	/**
	 * Имя, фамилия или отчетство донора.
	 */
	private String donor;
	/**
	 * Планируемая дата обследования.
	 */
	private Date planDate;
	/**
	 * Дата создания записи об обращении.
	 */
	private Date created;
	/**
	 * Статус обращения на обследование (1 - Заполнение; 2 - Осмотр; 3 - Получение результатов анализов; 
	 * 4 - Определение допуска к донорству; 5 - Допущен; 6 - Направлен на дообследование;
	 * 7 - Первичное исследование; 9 - Запланировано; -1 - Отвод от донорства; -2 - Отменено;
	 * 0 - любой, значение по умолчанию, прописано в константе EXAMINATION_STATUS_NULL_VALUE).
	 */
	private int statusId;
    /**
     * Тип обследования. 0 - первичное, 1 - повторное,
     * к которым добавляется значение 2 - любой, значение по умолчанию, прописано в константе EXAMINATION_TYPE_NULL_VALUE).
     */
	private int examinationTypeId;
	
    /**
     * Конструктор по умолчанию.
     */
	public ExaminationsFilter() {
		super();
		setDefaultValues();
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDonor() {
		return donor;
	}

	public void setDonor(String donor) {
		this.donor = donor;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getExaminationTypeId() {
		return examinationTypeId;
	}

	public void setExaminationTypeId(int examinationTypeId) {
		this.examinationTypeId = examinationTypeId;
	}

	public int getExaminationStatusNullValue() {
		return EXAMINATION_STATUS_NULL_VALUE;
	}

	public int getExaminationTypeNullValue() {
		return EXAMINATION_TYPE_NULL_VALUE;
	}

	protected void setDefaultValues() {
		number = null;
		donor = null;
		created = null;
		planDate = null;
		examinationTypeId = EXAMINATION_TYPE_NULL_VALUE;
		statusId = EXAMINATION_STATUS_NULL_VALUE;
	}

	@Override
	public void clear() {
		setDefaultValues();
	}

	@Override
	public void fillFrom(ExaminationsFilter source) {
		setNumber(source.getNumber());
		setDonor(source.getDonor());
		setCreated(source.getCreated());
		setPlanDate(source.getPlanDate());
		setStatusId(source.getStatusId());
		setExaminationTypeId(source.getExaminationTypeId());
	}
}