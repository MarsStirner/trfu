package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Параметры процедуры (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_operation_parameters")
public class OperationParameters extends IdentifiedEntity {
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public String getTbv() {
		return tbv;
	}

	public void setTbv(String tbv) {
		this.tbv = tbv;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getInletAcRatio() {
		return inletAcRatio;
	}

	public void setInletAcRatio(String inletAcRatio) {
		this.inletAcRatio = inletAcRatio;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getProductVolume() {
		return productVolume;
	}

	public void setProductVolume(double productVolume) {
		this.productVolume = productVolume;
	}
	
	
	/**
	 * Объем афереза
	 */
	private double volume;
	
	/**
	 * Обр.TBV
	 */
	private String tbv;
	
	/**
	 * Cкорость забора
	 */
	private String speed;
	
	/**
	 * Inlet:AC ratio
	 */
	private String inletAcRatio;
	
	/**
	 * Время афереза
	 */
	private String time;
	
	/**
	 * Объем продукта афереза
	 */
	private double productVolume;
	
	
	private static final long serialVersionUID = -8531382762850241211L;
}