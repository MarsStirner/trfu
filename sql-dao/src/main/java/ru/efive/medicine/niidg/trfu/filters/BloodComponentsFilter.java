package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Junction;

import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;

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
	
	private Integer donationId;
	
	private Integer orderId;
	
	
	private String parentNumber;
	
	private List<List<FieldFilterBean>> listFielsDisjunction;
	
	/* temporal */
	private List<Junction> listJunction;
	private List<AliasFilterBean> listAlias;
	private Map<String, Object> map;
	private List<FieldFilterBean> listFields;
	
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

	public String getFio() {
		return getFirstName();
	}

	public void setFio(String fio) {
		setFirstName(fio);
		setLastName(fio);
		setMiddleName(fio);
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

	public List<Junction> getListJunction() {
		return listJunction;
	}

	public void setListJunction(List<Junction> listJunctions) {
		this.listJunction = listJunctions;
	}

	public List<AliasFilterBean> getListAlias() {
		return listAlias;
	}

	public void setListAlias(List<AliasFilterBean> listAlias) {
		this.listAlias = listAlias;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<FieldFilterBean> getListFields() {
		return listFields;
	}

	public void setListFields(List<FieldFilterBean> listFields) {
		this.listFields = listFields;
	}

	public List<List<FieldFilterBean>> getListFielsDisjunction() {
		return listFielsDisjunction;
	}

	public void setListFielDisjunction(
			List<List<FieldFilterBean>> listFielsDisjunction) {
		this.listFielsDisjunction = listFielsDisjunction;
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
		donationId = null;
		orderId = null;
		parentNumber = null;
		listJunction = new ArrayList<Junction>();
		listAlias = new ArrayList<AliasFilterBean>();
		map = null;
		listFields = new ArrayList<FieldFilterBean>();
		listFielsDisjunction = new ArrayList<List<FieldFilterBean>>();
		
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
		setDonationId(source.getDonationId());
		setOrderId(source.getOrderId());
		setParentNumber(source.getParentNumber());
		setListJunction(source.getListJunction());
		setListAlias(source.getListAlias());
		setMap(source.getMap());
		setListFields(source.getListFields());
	}
}