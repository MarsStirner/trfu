package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;

/**
 * Фильтр для представления "Обращения на обследование".
 * 
 * @author Siarhei Ushanau
 */
public class ExaminationsFilter extends AppendSRPDFilter<ExaminationsFilter> {
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
	//private String donor;
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
	
	private List<AliasFilterBean> listAlias;
	private List<FieldFilterBean> listFields;
	private List<FieldFilterBean> listFieldsDisjunction;
	private List<String> sqlRestrictions;
	
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
		return getFirstName();
	}

	public void setDonor(String donor) {
		setFirstName(donor);
		setLastName(donor);
		setMiddleName(donor);
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

	public List<AliasFilterBean> getListAlias() {
		return listAlias;
	}

	public void setListAlias(List<AliasFilterBean> listAlias) {
		this.listAlias = listAlias;
	}

	public List<FieldFilterBean> getListFields() {
		return listFields;
	}

	public void setListFields(List<FieldFilterBean> listFilds) {
		this.listFields = listFilds;
	}

	public List<FieldFilterBean> getListFieldsDisjunction() {
		return listFieldsDisjunction;
	}

	public void setListFieldsDisjunction(List<FieldFilterBean> listFieldsDisjunction) {
		this.listFieldsDisjunction = listFieldsDisjunction;
	}

	public List<String> getSqlRestrictions() {
		return sqlRestrictions;
	}

	public void setSqlRestrictions(List<String> sqlRestrictions) {
		this.sqlRestrictions = sqlRestrictions;
	}

	protected void setDefaultValues() {
		number = null;
		//donor = null;
		created = null;
		planDate = null;
		examinationTypeId = EXAMINATION_TYPE_NULL_VALUE;
		statusId = EXAMINATION_STATUS_NULL_VALUE;
		listAlias = new ArrayList<AliasFilterBean>();
		listFields = new ArrayList<FieldFilterBean>();
		listFieldsDisjunction = new ArrayList<FieldFilterBean>();
		sqlRestrictions = new ArrayList<String>();
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
		setListFields(source.getListFields());
		setListFieldsDisjunction(source.getListFieldsDisjunction());
		setSqlRestrictions(source.getSqlRestrictions());
	}
}