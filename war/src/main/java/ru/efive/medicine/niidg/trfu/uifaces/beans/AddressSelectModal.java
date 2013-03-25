package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.uifaces.bean.ModalWindowHolderBean;

public class AddressSelectModal extends ModalWindowHolderBean {
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setHouse(String house) {
		this.house = house;
	}
	
	public String getHouse() {
		return house;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public void setConstruction(String construction) {
		this.construction = construction;
	}
	
	public String getConstruction() {
		return construction;
	}
	
	public void setFlat(String flat) {
		this.flat = flat;
	}
	
	public String getFlat() {
		return flat;
	}
	
	/**
	 * Страна
	 */
	private String country;
	
	/**
	 * Субъект РФ
	 */
	private String state;
	
	/**
	 * Район
	 */
	private String district;
	
	/**
	 * город
	 */
	private String city;
	
	/**
	 * улица
	 */
	private String street;
	
	/**
	 * дом
	 */
	private String house;
	
	/**
	 * корпус
	 */
	private String building;
	
	/**
	 * строение
	 */
	private String construction;
	
	/**
	 * квартира
	 */
	private String flat;
	
	private static final long serialVersionUID = -2520852952686852770L;
}