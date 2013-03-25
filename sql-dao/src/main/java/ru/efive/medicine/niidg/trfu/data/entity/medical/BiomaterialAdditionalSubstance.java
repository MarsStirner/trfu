package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Добавленный объём (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_biomaterial_additional_substances")
public class BiomaterialAdditionalSubstance extends IdentifiedEntity {
	
	/**
	 * Тип добавляемого вещества
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier substanceType;
	
	/**
	 * Добавляемый объем
	 */
	private int volume;
	
	private static final long serialVersionUID = 1L;


	public Classifier getSubstanceType() {
		return substanceType;
	}

	public void setSubstanceType(Classifier substanceType) {
		this.substanceType = substanceType;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	
}