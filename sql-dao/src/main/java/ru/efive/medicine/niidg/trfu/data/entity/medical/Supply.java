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
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Расходный материал (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_supplies")
public class Supply extends IdentifiedEntity {
	
	public Supply() {
		
	}
	
	public Supply(Classifier type) {
		super();
		setType(type);
	}
	
	public void setType(Classifier type) {
		this.type = type;
	}

	public Classifier getType() {
		return type;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
	/**
	 * Тип расходного материала
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier type;
	
	/**
	 * Тип расходного материала (строковый)
	 */
	private String typeName;
	
	/**
	 * Производитель системы
	 */
	private String maker;
	
	/**
	 * Лот
	 */
	private String lot;
	
	/**
	 * дата окончания срока хранения
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date expirationDate;
	
	private static final long serialVersionUID = -5404523934731856382L;
}