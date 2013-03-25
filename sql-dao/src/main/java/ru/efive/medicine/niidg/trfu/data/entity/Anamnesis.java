package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Анамнез - результаты первичного обследования
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_anamnesis")
public class Anamnesis extends IdentifiedEntity {
	
	public String getHeredity() {
		return heredity;
	}

	public void setHeredity(String heredity) {
		this.heredity = heredity;
	}

	public String getSicknesses() {
		return sicknesses;
	}

	public void setSicknesses(String sicknesses) {
		this.sicknesses = sicknesses;
	}

	public String getTransfusions() {
		return transfusions;
	}

	public void setTransfusions(String transfusions) {
		this.transfusions = transfusions;
	}

	public String getVaccinations() {
		return vaccinations;
	}

	public void setVaccinations(String vaccinations) {
		this.vaccinations = vaccinations;
	}
	
	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	
	
	/**
	 * Наследственность
	 */
	@Column(columnDefinition="text")
	private String heredity;
	
	/**
	 * Перенесенные заболевания (в том числе операции) и их давность
	 */
	@Column(columnDefinition="text")
	private String sicknesses;
	
	/**
	 * Трансфузии крови
	 */
	@Column(columnDefinition="text")
	private String transfusions;
	
	/**
	 * Прививки, вакцинации и их давность
	 */
	@Column(columnDefinition="text")
	private String vaccinations;
	
	/**
	 * Заключение о годности к донорству
	 */
	@Column(columnDefinition="text")
	private String conclusion;
	
	private static final long serialVersionUID = 5363513342141407932L;
}