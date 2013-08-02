package ru.korusconsulting.migration.bean;

import java.util.Date;

import javax.persistence.*;

@MappedSuperclass
public class CommonDonor {
	private int id;
	private String lastName;
	private String middleName;
	private String firstName;
	/* Gender (0 - female, 1 - male) */
    private int gender;
	/* Date of birth */
	private Date birth;
	private String passportSeries;
	private String passportNumber;
	private String insuranceSeries;
	private String insuranceNumber;
	private String employment;
	private String workPhone;
	private String registrationAddress;
	private String phone;
	/* id from Save Repository of personal data */
	private Integer temp_stogate_id;
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
	@Column
	public Integer getTemp_stogate_id() {
		return temp_stogate_id;
	}

	public void setTemp_stogate_id(Integer temp_stogate_id) {
		this.temp_stogate_id = temp_stogate_id;
	}

	@Override
	public String toString() {
		return " id=" + id + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", firstName=" + firstName
				+ ", gender=" + gender + ", birth=" + birth
				+ ", passportSeries=" + passportSeries + ", passportNumber="
				+ passportNumber + ", insuranceSeries=" + insuranceSeries
				+ ", insuranceNumber=" + insuranceNumber + ", employment="
				+ employment + ", workPhone=" + workPhone
				+ ", registrationAddress=" + registrationAddress + ", phone="
				+ phone + ", temp_stogate_id=" + temp_stogate_id;
	}
}
