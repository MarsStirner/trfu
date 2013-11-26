package ru.efive.medicine.niidg.trfu.filters;

import java.util.Collection;
import java.util.Date;

/**
 * Фильтр для работы с данными, которые используются в ЗХПД и 
 * наиболее повторяющимися, для работы с базой ТРФУ.
 * 
 * @author Viktar Kastsiuchenka
 *
 */
public abstract class AppendSRPDFilter<T> extends AbstractFilter<T> {
	public enum CompareType {
		/* Тип параметра для поиска: искать на "равно" */
		EQ,
		/* Тип параметра для поиска: искать на "не равно" */
		NE,
		/* Тип параметра для поиска: не искать */
		NULL,
		/* Тип параметра для поиска: искать на "подобен" */
		ILIKE,
		/* Тип параметра для поиска: искать на "больше или равно" */
		GE,
		/* Тип параметра для поиска: искать на "меньше или равно" */
		LE,
		/* Тип параметра для поиска: искать "не null" */
		NOT_NULL,
		/* Тип параметра для поиска: искать "входит в список" */
		IN
	}
	private static final long serialVersionUID = 4359117959905220257L;
	/**
	 * Имя донора для поиска в ЗХПД; по-умолчанию = null
	 */
	private String firstName;
	/**
	 * Фамилия донора для поиска в ЗХПД; по-умолчанию = null
	 */
	private String lastName;
	/**
	 * Отчество донора для поиска в ЗХПД; по-умолчанию = null
	 */
	private String middleName;
	/**
	 * Паспорта для поиска в ЗХПД; по-умолчанию = null
	 */
	private String passport;
	/**
	 * Номер полиса ОМС для поиска в ЗХПД; по-умолчанию = null
	 */
	private String insuranceNumber;
	/**
	 * Серия полиса ОМС для поиска в ЗХПД; по-умолчанию = null
	 */
	private String insuranceSeries;
	/**
	 * место работы для поиска в ЗХПД; по-умолчанию = null
	 */
	private String employment;
	/**
	 * рабочий телефон для поиска в ЗХПД; по-умолчанию = null
	 */
	private String workPhone;
	/**
	 * домашний телефон для поиска в ЗХПД; по-умолчанию = null
	 */
	private String phone;
	/**
	 * авдрес прописки для поиска в ЗХПД; по-умолчанию = null
	 */
	private String registrationAdress;
	/**
	 * Логин для авторизации, по умолчанию должен быть null
	 */
	private String mail;
	/**
	 * Дата рождения в строковом формате. По-умолчанию null.
	 */
	private Date birth;
	/**
	 * Список идентификаторов ЗХПД
	 */
	private Collection<String> listSRPDIds;
	/**
	 * Флаг, используемый для отображения или нет удалённых записей 
	 */
	boolean showDeleted;
	/**
	 * Флаг, используемый для определения conjunction или disjunction
	 * по-умолчанию = false
	 */
	boolean conjunction;
	/**
	 * Флаг, который по-умолчанию = false;
	 * Если = true, то в хотябы один из параметров для поиска в ЗХПД инициализирован
	 */
	boolean queryToSRPD;

	public AppendSRPDFilter() {
		super();
		setDefaultValues();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.queryToSRPD = true;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.queryToSRPD = true;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		this.queryToSRPD = true;
	}

	/**
	 * @return the passport
	 */
	public String getPassport() {
		return passport;
	}

	/**
	 * @param passportSeries the passportSeries to set
	 */
	public void setPassport(String passport) {
		this.passport = passport;
		this.queryToSRPD = true;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
		this.queryToSRPD = true;
	}

	public String getInsuranceSeries() {
		return insuranceSeries;
	}

	public void setInsuranceSeries(String insuranceSeries) {
		this.insuranceSeries = insuranceSeries;
		this.queryToSRPD = true;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * @return the employment
	 */
	public String getEmployment() {
		return employment;
	}

	/**
	 * @param employment the employment to set
	 */
	public void setEmployment(String employment) {
		this.employment = employment;
		this.queryToSRPD = true;
	}

	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
		this.queryToSRPD = true;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
		this.queryToSRPD = true;
	}

	/**
	 * @return the registrationAdress
	 */
	public String getRegistrationAdress() {
		return registrationAdress;
	}

	/**
	 * @param registrationAdress the registrationAdress to set
	 */
	public void setRegistrationAdress(String registrationAdress) {
		this.registrationAdress = registrationAdress;
		this.queryToSRPD = true;
	}

	/**
	 * @return the listSRPDIds
	 */
	public Collection<String> getListSRPDIds() {
		return listSRPDIds;
	}

	/**
	 * @param listSRPDIds the listSRPDIds to set
	 */
	public void setListSRPDIds(Collection<String> listSRPDIds) {
		this.listSRPDIds = listSRPDIds;
	}

	/**
	 * @return the showDeleted
	 */
	public boolean isShowDeleted() {
		return showDeleted;
	}

	/**
	 * @param showDeleted the showDeleted to set
	 */
	public void setShowDeleted(boolean showDeleted) {
		this.showDeleted = showDeleted;
	}

	/**
	 * @return the conjunction
	 */
	public boolean isConjunction() {
		return conjunction;
	}

	/**
	 * @param conjunction the conjunction to set
	 */
	public void setConjunction(boolean conjunction) {
		this.conjunction = conjunction;
	}

	/**
	 * @return the queryToSRPD
	 */
	public boolean isQueryToSRPD() {
		return queryToSRPD;
	}

	/**
	 * @param queryToSRPD the queryToSRPD to set
	 */
	public void setQueryToSRPD(boolean queryToSRPD) {
		this.queryToSRPD = queryToSRPD;
	}

	@Override
	public void clear() {
		setDefaultValues();
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void fillFrom(T source) {
		setFirstName(((AppendSRPDFilter<T>)source).getFirstName());
		setLastName(((AppendSRPDFilter<T>)source).getLastName());
		setMiddleName(((AppendSRPDFilter<T>)source).getMiddleName());
		setListSRPDIds(((AppendSRPDFilter<T>)source).getListSRPDIds());
		setShowDeleted(((AppendSRPDFilter<T>)source).isShowDeleted());
		setConjunction(((AppendSRPDFilter<T>)source).isConjunction());
		setPassport(((AppendSRPDFilter<T>)source).getPassport());
		setInsuranceNumber(((AppendSRPDFilter<T>)source).getInsuranceNumber());
		setInsuranceSeries(((AppendSRPDFilter<T>)source).getInsuranceSeries());
		setEmployment(((AppendSRPDFilter<T>)source).getEmployment());
		setWorkPhone(((AppendSRPDFilter<T>)source).getWorkPhone());
		setPhone(((AppendSRPDFilter<T>)source).getPhone());
		setRegistrationAdress(((AppendSRPDFilter<T>)source).getRegistrationAdress());
		setQueryToSRPD(((AppendSRPDFilter<T>)source).isQueryToSRPD());
		
		
	}
	protected void setDefaultValues() {
		firstName = null;
		lastName = null;
		middleName = null;
		passport = null;
		passport = null;
		insuranceNumber = null;
		insuranceSeries = null;
		employment = null;
		workPhone = null;
		phone = null;
		registrationAdress = null;
		listSRPDIds = null;
		showDeleted = true;
		conjunction = false;
		queryToSRPD = false;
	}
}
