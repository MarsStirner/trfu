package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Фильтр для представления "Доноры".
 * 
 * @author Siarhei Ushanau
 */
public class DonorsFilter extends  AppendSRPDFilter<DonorsFilter> {
	private static final long serialVersionUID = -3981143263084944918L;

	/**
	 * "Нулевое" значение для пола донора.
	 */
	public static final int GENDER_NULL_VALUE = 2;
	/**
	 * "Нулевое" значение для типа документа.
	 */
	public static final int DOCUMENT_TYPE_NULL_VALUE = 0;
	/**
	 * Тип документа "паспорт".
	 */
	public static final int PASSPORT_DOCUMENT_TYPE = 1;
	/**
	 * Тип документа "полис".
	 */
	public static final int POLICY_DOCUMENT_TYPE = 2;
	/**
	 * "Нулевое" значение для статуса донора.
	 */
	public static final int DONOR_STATUS_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для признака "прошедние карантинизацию".
	 */
	public static final int PAST_QUARANTINE_NULL_VALUE = 0;
	/**
	 * Значение "да" для признака "прошедние карантинизацию".
	 */
	public static final int PAST_QUARANTINE_YES_VALUE = 1;
	/**
	 * Значение "нет" для признака "прошедние карантинизацию".
	 */
	public static final int PAST_QUARANTINE_NO_VALUE = 2;
	/**
	 * "Нулевое" значение для категории донора.
	 */
	public static final int DONOR_CATEGORY_NULL_VALUE = -1;

	/**
	 * Номер донора.
	 */
	private String number;
	/**
	 * Имя донора.
	 */
	private String firstName;
	/**
	 * Фамилия донора.
	 */
	private String lastName;
	/**
	 * Отчетсво донора.
	 */
	private String middleName;
	/**
	 * Дата рождения донора.
	 */
	private Date birth;
	/**
	 * Дата создания записи о доноре.
	 */
	private Date created;
	/**
	 * Пол (0 - женский; 1 - мужской; 
	 * 2 - любой, значение по умолчанию, прописано в константе GENDER_NULL_VALUE).
	 */
    private int genderId;
	/**
	 * Тип документа (0 - паспорт, значение по умолчанию, прописано в константе PASSPORT_DOCUMENT_TYPE; 1 - полис, значение прописано в константе POLICY_DOCUMENT_TYPE).
	 */
    private int documentTypeId;
	/**
	 * Серия документа.
	 */
	private String documentSeries;
	/**
	 * Номер документа.
	 */
	private String documentNumber;
	/**
	 * Серия страховки ОМС, если нужно добавить.
	 */
	private String insuranceSeries;
	/**
	 * Номер страховки ОМС, если нужно добавить.
	 */
	private String insuranceNumber;
    /**
     * Номер донора в ЕДЦ.
     */
    private String externalNumber;
	/**
	 * Фактический адрес проживания донора.
	 */
	private String factAddress;
	/**
	 * Статус донора (1 - Кандидат; 2 - Донор; -1 - Временный отвод; -2 - Абсолютный отвод; 
	 * 0 - любой, значение по умолчанию, прописано в константе DONOR_STATUS_NULL_VALUE).
	 */
	private List<Integer> listStatusId;
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
	 * Признак, прошел ли донор карантинизацию (1 - прошел; 2 - не прошел; 
	 * 0 - любые, значение по умолчанию, прописано в константе PAST_QUARANTINE_NULL_VALUE).
	 */
	private int pastQuarantineId;
	/**
	 * Категория донора (0 - Первичный; 1 - Кадровый; 2 - Повторный;
	 * -1 - любой, значение по умолчанию, прописано в константе DONOR_CATEGORY_NULL_VALUE).
	 */
	private int categoryId;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Отображение DonorType.value
	 */
	private String donorTypeValue;
	/**
	 * Список stogate_id
	 */
	private List<Integer> listStorageIds;
	/**
	 * Список id-ов для ограничения условий выборки
	 */
	private List listIds;
	/**
	 * Флаг для определения использовать conjunction или disjunction
	 */
	private boolean conjunction;
	/**
	 * Логин для авторизации, по умолчанию должен быть null
	 */
	private String mail;
	/**
	 * Пароль для авторизации, по умолчанию должен быть null
	 */
	private String password;
	
    /**
     * Конструктор по умолчанию.
     */
	public DonorsFilter() {
		super();
		setDefaultValues();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getGenderId() {
		return genderId;
	}

	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
		if (documentTypeId == DOCUMENT_TYPE_NULL_VALUE) {
			setDocumentSeries(null);
			setDocumentNumber(null);
		}
	}

	public String getDocumentSeries() {
		if (documentTypeId == DOCUMENT_TYPE_NULL_VALUE) {
			return null;
		}
		return documentSeries;
	}

	public void setDocumentSeries(String documentSeries) {
		if (documentTypeId == DOCUMENT_TYPE_NULL_VALUE) {
			this.documentSeries = null;
		} else {
			this.documentSeries = documentSeries;
		}
	}

	public String getDocumentNumber() {
		if (documentTypeId == DOCUMENT_TYPE_NULL_VALUE) {
			return null;
		}
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		if (documentTypeId == DOCUMENT_TYPE_NULL_VALUE) {
			this.documentNumber = null;
		} else {
			this.documentNumber = documentNumber;
		}
	}

	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public String getFactAddress() {
		return factAddress;
	}

	public void setFactAddress(String factAddress) {
		this.factAddress = factAddress;
	}

	public int getStatusId() {
		if (listStatusId.size() == 0) {
			return DONOR_STATUS_NULL_VALUE;
		}
		return listStatusId.get(0);
	}

	public void setStatusId(int statusId) {
		this.listStatusId.set(0, statusId);
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

	public int getPastQuarantineId() {
		return pastQuarantineId;
	}

	public void setPastQuarantineId(int pastQuarantineId) {
		this.pastQuarantineId = pastQuarantineId;
	}

	public int getGenderNullValue() {
		return GENDER_NULL_VALUE;
	}

	public int getDocumentTypeNullValue() {
		return DOCUMENT_TYPE_NULL_VALUE;
	}

	public int getStatusNullValue() {
		return DONOR_STATUS_NULL_VALUE;
	}

	public int getCategoryNullValue() {
		return DONOR_CATEGORY_NULL_VALUE;
	}

	public int getPastQuarantineNullValue() {
		return PAST_QUARANTINE_NULL_VALUE;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the donorTypeValue
	 */
	public String getDonorTypeValue() {
		return donorTypeValue;
	}

	/**
	 * @param donorTypeValue the donorTypeValue to set
	 */
	public void setDonorTypeValue(String donorTypeValue) {
		this.donorTypeValue = donorTypeValue;
	}

	/**
	 * @return the listStogateIds
	 */
	public List<Integer> getListStorageIds() {
		return listStorageIds;
	}

	/**
	 * @param listStogateIds the listStogateIds to set
	 */
	public void setListStorageIds(List<Integer> listStorageIds) {
		this.listStorageIds = listStorageIds;
	}


	/**
	 * @return the listStatusId
	 */
	public List<Integer> getLisStatusId() {
		return listStatusId;
	}

	/**
	 * @param listStatusId the listStatusId to set
	 */
	public void setListStatusId(List<Integer> listStatusId) {
		this.listStatusId = listStatusId;
	}

	/**
	 * @return the listIds
	 */
	public List getListIds() {
		return listIds;
	}

	/**
	 * @param listIds the listIds to set
	 */
	public void setListIds(List listIds) {
		this.listIds = listIds;
	}

	/**
	 * @return the useConjunction
	 */
	public boolean isConjunction() {
		return conjunction;
	}

	/**
	 * @param useConjunction the useConjunction to set
	 */
	public void setConjunction(boolean conjunction) {
		this.conjunction = conjunction;
	}

	/**
	 * @return the insuranceSeries
	 */
	public String getInsuranceSeries() {
		return insuranceSeries;
	}

	/**
	 * @param insuranceSeries the insuranceSeries1 to set
	 */
	public void setInsuranceSeries(String insuranceSeries) {
		this.insuranceSeries = insuranceSeries;
	}

	/**
	 * @return the insuranceNumber
	 */
	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	/**
	 * @param insuranceNumber the insuranceNumber to set
	 */
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	/**
	 * @return the listStatusId
	 */
	public List<Integer> getListStatusId() {
		return listStatusId;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void clear() {
		setDefaultValues();
	}

	protected void setDefaultValues() {
		number = null;
		firstName = null;
		lastName = null;
		middleName = null;
		birth = null;
		created = null;
		genderId = GENDER_NULL_VALUE;
		documentTypeId = DOCUMENT_TYPE_NULL_VALUE;
		documentSeries = null;
		documentNumber = null;
		insuranceSeries = null;
		insuranceNumber = null;
		externalNumber = null;
		factAddress = null;
		bloodGroupId = BLOOD_GROUP_NULL_VALUE;
		rhesusFactorId = RHESUS_FACTOR_NULL_VALUE;
		pastQuarantineId = PAST_QUARANTINE_NULL_VALUE;
		categoryId = DONOR_CATEGORY_NULL_VALUE;
		/////////////////////////////////////////////////////////
		donorTypeValue = null;
		listStorageIds = new ArrayList<Integer>();
		listStatusId = new ArrayList<Integer>();
		listIds = new ArrayList();
		conjunction = true;
		listStatusId.add(DONOR_STATUS_NULL_VALUE);
		mail = null;
		password = null;
	}

	public void fillFrom(DonorsFilter source) {
		super.fillFrom(source);
		setNumber(source.getNumber());
		setFirstName(source.getFirstName());
		setLastName(source.getLastName());
		setMiddleName(source.getMiddleName());
		setCreated(source.getCreated());
		setBirth(source.getBirth());
		setGenderId(source.getGenderId());
		setDocumentTypeId(source.getDocumentTypeId());
		setDocumentSeries(source.getDocumentSeries());
		setDocumentNumber(source.getDocumentNumber());
		setInsuranceSeries(source.getInsuranceSeries());
		setInsuranceNumber(source.getInsuranceNumber());
		setExternalNumber(source.getExternalNumber());
		setFactAddress(source.getFactAddress());
		setStatusId(source.getStatusId());
		setBloodGroupId(source.getBloodGroupId());
		setRhesusFactorId(source.getRhesusFactorId());
		setPastQuarantineId(source.getPastQuarantineId());
		setCategoryId(source.getCategoryId());
		///////////////////////////////////////////////////////
		setDonorTypeValue(source.getDonorTypeValue());
		setListStorageIds(source.getListStorageIds());
		setListStatusId(source.getListStatusId());
		setListIds(source.getListIds());
		setConjunction(source.isConjunction());
		setMail(source.getMail());
		setPassword(source.getPassword());
	}
}