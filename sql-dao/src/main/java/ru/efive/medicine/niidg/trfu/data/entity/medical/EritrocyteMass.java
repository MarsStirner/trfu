package ru.efive.medicine.niidg.trfu.data.entity.medical;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Эр. масса (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_eritrocyte_mass")
public class EritrocyteMass extends IdentifiedEntity {
	
	public String getErMaker() {
		return erMaker;
	}

	public void setErMaker(String erMaker) {
		this.erMaker = erMaker;
	}

	public String getErNumber() {
		return erNumber;
	}

	public void setErNumber(String erNumber) {
		this.erNumber = erNumber;
	}

	public BloodGroup getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(BloodGroup bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Classifier getRhesusFactor() {
		return rhesusFactor;
	}

	public void setRhesusFactor(Classifier rhesusFactor) {
		this.rhesusFactor = rhesusFactor;
	}

	public double getErVolume() {
		return erVolume;
	}

	public void setErVolume(double erVolume) {
		this.erVolume = erVolume;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public double getErHt() {
		return erHt;
	}

	public void setErHt(double erHt) {
		this.erHt = erHt;
	}

	public double getSalineVolume() {
		return salineVolume;
	}

	public void setSalineVolume(double salineVolume) {
		this.salineVolume = salineVolume;
	}

	public double getFinalHt() {
		return finalHt;
	}

	public void setFinalHt(double finalHt) {
		this.finalHt = finalHt;
	}

	public double getPreFillErVolume() {
		return preFillErVolume;
	}

	public void setPreFillErVolume(double preFillErVolume) {
		this.preFillErVolume = preFillErVolume;
	}
	
	
	/**
	 * Производитель
	 */
	private String erMaker;
	
	/**
	 * Номер
	 */
	private String erNumber;
	
	/**
	 * Группа крови
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodGroup bloodGroup;
	
	/**
	 * Резус
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier rhesusFactor;
	
	/**
	 * Объем
	 */
	private double erVolume;
	
	/**
	 * Дата производства
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date productionDate;
	
	/**
	 * Срок годности
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date expirationDate;
	
	/**
	 * Гематокрит эр. массы
	 */
	private double erHt;
	
	/**
	 * Объем физраствора
	 */
	private double salineVolume;
	
	/**
	 * Финальный гематокрит
	 */
	private double finalHt;
	
	/**
	 * Предварительное заполнение контура аппарата эритроцитарной массой, объём
	 */
	private double preFillErVolume;
	
	
	private static final long serialVersionUID = -8343633698634404728L;
}