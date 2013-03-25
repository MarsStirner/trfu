package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Гемодинамика и температура (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_hemodynamics")
public class Hemodynamics extends IdentifiedEntity {
	
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	
	public String getPulse() {
		return pulse;
	}
	
	public void setArterialPressure(String arterialPressure) {
		this.arterialPressure = arterialPressure;
	}
	
	public String getArterialPressure() {
		return arterialPressure;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	
	/**
	 * Пульс
	 */
	private String pulse;
	
	/**
	 * АД
	 */
	private String arterialPressure;
	
	/**
	 * Температура
	 */
	private String temperature;
	
	
	private static final long serialVersionUID = 5971540044291709111L;
}