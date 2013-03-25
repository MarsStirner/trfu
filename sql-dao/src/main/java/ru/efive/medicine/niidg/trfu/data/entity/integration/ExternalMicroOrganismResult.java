package ru.efive.medicine.niidg.trfu.data.entity.integration;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * микроорганизмы
 */
@Entity
@Table(name = "trfu_external_micro_organism_results")
public class ExternalMicroOrganismResult extends IdentifiedEntity {
	
	public String getOrganismLisId() {
		return organismLisId;
	}
	
	public void setOrganismLisId(String organismLisId) {
		this.organismLisId = organismLisId;
	}
	
	public String getOrganismName() {
		return organismName;
	}
	
	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}
	
	public String getOrganismConcetration() {
		return organismConcetration;
	}
	
	public void setOrganismConcetration(String organismConcetration) {
		this.organismConcetration = organismConcetration;
	}
	
	
	/**
	 * идентификатор организма по БД ЛИС
	 */
	private String organismLisId;
	
	/**
	 * название организма
	 */
	private String organismName;
	
	/**
	 * описание концентрации в произвольном виде
	 */
	private String organismConcetration;
	
	
	private static final long serialVersionUID = 1L;
}