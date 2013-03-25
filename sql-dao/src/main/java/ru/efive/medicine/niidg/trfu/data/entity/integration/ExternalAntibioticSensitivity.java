package ru.efive.medicine.niidg.trfu.data.entity.integration;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * антибиотики
 */
@Entity
@Table(name = "trfu_external_antibiotic_sensivity")
public class ExternalAntibioticSensitivity extends IdentifiedEntity {
	
	public String getAntibioticLisId() {
		return antibioticLisId;
	}
	
	public void setAntibioticLisId(String antibioticLisId) {
		this.antibioticLisId = antibioticLisId;
	}
	
	public String getAntibioticName() {
		return antibioticName;
	}
	
	public void setAntibioticName(String antibioticName) {
		this.antibioticName = antibioticName;
	}
	
	public String getMic() {
		return mic;
	}
	
	public void setMic(String mic) {
		this.mic = mic;
	}
	
	public String getAntibioticActivityValue() {
		return antibioticActivityValue;
	}
	
	public void setAntibioticActivityValue(String antibioticActivityValue) {
		this.antibioticActivityValue = antibioticActivityValue;
	}
	
	
	/**
	 * идентификатор антибиотика БД ЛИС
	 */
	private String antibioticLisId;
	
	/**
	 * название антибиотика
	 */
	private String antibioticName;
	
	/**
	 * величина концентрации
	 */
	private String mic;
	
	/**
	 * описание чувствительности в произвольном виде: R,S,I
	 */
	private String antibioticActivityValue;
	
	
	private static final long serialVersionUID = 1L;
}