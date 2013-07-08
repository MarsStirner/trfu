package ru.korusconsulting.migration.bean;

import java.util.Date;

import javax.persistence.*;

@MappedSuperclass
public class CommonDonor {
	private static final long serialVersionUID = 1L;
	/* id донора */
	private int id;
	/* фамилия */
	private String lastName;
	/* отчество */
	private String middleName;
	/* имя */
	private String firstName;
	/* Пол (0 - female, 1 - male) */
    private int gender;
	/* дата рождения */
	private Date birth;
	/* Серия паспорта */
	private String passportSeries;
	/* Номер паспорта */
	private String passportNumber;
	/* Серия страхового полиса ОМС */
	private String insuranceSeries;
	/* Номер страхового полиса ОМС */
	private String insuranceNumber;
	/* Место работы */
	private String employment;
	/* Рабочий телефон */
	private String workPhone;
	/* Адрес регистрации */
	private String registrationAddress;
	/* Телефон */
	private String phone;
//----------------------------------------------------------------------------------------
	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Column
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	@Column
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Column
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	@Column
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Column
	public String getPassportSeries() {
		return passportSeries;
	}
	@Column
	public void setPassportSeries(String passportSeries) {
		this.passportSeries = passportSeries;
	}
	@Column
	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	@Column
	public String getInsuranceSeries() {
		return insuranceSeries;
	}

	public void setInsuranceSeries(String insuranceSeries) {
		this.insuranceSeries = insuranceSeries;
	}
	@Column
	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	@Column
	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getEmployment() {
		return employment;
	}
	@Column
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	@Column
	public String getWorkPhone() {
		return workPhone;
	}
	@Column
	public String getRegistrationAddress() {
		return registrationAddress;
	}

	public void setRegistrationAddress(String registrationAddress) {
		this.registrationAddress = registrationAddress;
	}
	@Column
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
