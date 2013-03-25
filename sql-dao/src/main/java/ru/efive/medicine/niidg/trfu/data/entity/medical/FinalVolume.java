package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Финальные объемы (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_final_volume")
public class FinalVolume extends IdentifiedEntity {
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getAnticoagulantVolume() {
		return anticoagulantVolume;
	}

	public void setAnticoagulantVolume(double anticoagulantVolume) {
		this.anticoagulantVolume = anticoagulantVolume;
	}

	public double getInletVolume() {
		return inletVolume;
	}

	public void setInletVolume(double inletVolume) {
		this.inletVolume = inletVolume;
	}

	public double getPlasmaVolume() {
		return plasmaVolume;
	}

	public void setPlasmaVolume(double plasmaVolume) {
		this.plasmaVolume = plasmaVolume;
	}

	public double getCollectVolume() {
		return collectVolume;
	}

	public void setCollectVolume(double collectVolume) {
		this.collectVolume = collectVolume;
	}

	public double getAnticoagulantInCollect() {
		return anticoagulantInCollect;
	}

	public void setAnticoagulantInCollect(double anticoagulantInCollect) {
		this.anticoagulantInCollect = anticoagulantInCollect;
	}

	public double getAnticoagulantInPlasma() {
		return anticoagulantInPlasma;
	}

	public void setAnticoagulantInPlasma(double anticoagulantInPlasma) {
		this.anticoagulantInPlasma = anticoagulantInPlasma;
	}
	
	
	/**
	 * Время
	 */
	private String time;
	
	/**
	 * Финальный объем антикоагулянта
	 */
	private double anticoagulantVolume;
	
	/**
	 * Финальный объем inlet
	 */
	private double inletVolume;

	/**
	 * Финальный объем plasma
	 */
	private double plasmaVolume;

	/**
	 * Финальный объем collect
	 */
	private double collectVolume;

	/**
	 * AC в мешке collect
	 */
	private double anticoagulantInCollect;

	/**
	 * AC в мешке plasma
	 */
	private double anticoagulantInPlasma;
	
	
	private static final long serialVersionUID = -4914963501888261622L;
}