package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Расширенные параметры процедуры (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_extended_procedure_parameters")
public class ExtendedOperationParameters extends IdentifiedEntity {

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public void setAnticoagulantFlowRate(String anticoagulantFlowRate) {
		this.anticoagulantFlowRate = anticoagulantFlowRate;
	}

	public String getAnticoagulantFlowRate() {
		return anticoagulantFlowRate;
	}

	public void setAnticoagulantVolume(double anticoagulantVolume) {
		this.anticoagulantVolume = anticoagulantVolume;
	}

	public double getAnticoagulantVolume() {
		return anticoagulantVolume;
	}

	public void setInletFlowRate(String inletFlowRate) {
		this.inletFlowRate = inletFlowRate;
	}

	public String getInletFlowRate() {
		return inletFlowRate;
	}

	public void setInletVolume(double inletVolume) {
		this.inletVolume = inletVolume;
	}

	public double getInletVolume() {
		return inletVolume;
	}

	public void setPlasmaFlowRate(String plasmaFlowRate) {
		this.plasmaFlowRate = plasmaFlowRate;
	}

	public String getPlasmaFlowRate() {
		return plasmaFlowRate;
	}

	public void setPlasmaVolume(double plasmaVolume) {
		this.plasmaVolume = plasmaVolume;
	}

	public double getPlasmaVolume() {
		return plasmaVolume;
	}

	public void setCollectFlowRate(String collectFlowRate) {
		this.collectFlowRate = collectFlowRate;
	}

	public String getCollectFlowRate() {
		return collectFlowRate;
	}

	public void setCollectVolume(double collectVolume) {
		this.collectVolume = collectVolume;
	}

	public double getCollectVolume() {
		return collectVolume;
	}

	public String getInletAcRatio() {
		return inletAcRatio;
	}

	public void setInletAcRatio(String inletAcRatio) {
		this.inletAcRatio = inletAcRatio;
	}
	
	public void setAcInf(String acInf) {
		this.acInf = acInf;
	}

	public String getAcInf() {
		return acInf;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getCommentary() {
		return commentary;
	}
	
	
	/**
	 * Время афереза
	 */
	private String time;
	
	/**
	 * AC flow rate
	 */
	private String anticoagulantFlowRate;
	
	/**
	 * AC volume
	 */
	private double anticoagulantVolume;
	
	/**
	 * inlet flow rate
	 */
	private String inletFlowRate;
	
	/**
	 * inlet volume
	 */
	private double inletVolume;
	
	/**
	 * plasma flow rate
	 */
	private String plasmaFlowRate;
	
	/**
	 * plasma volume
	 */
	private double plasmaVolume;

	/**
	 * collect flow rate
	 */
	private String collectFlowRate;
	
	/**
	 * collect volume
	 */
	private double collectVolume;
	
	/**
	 * Inlet:AC ratio
	 */
	private String inletAcRatio;
	
	/**
	 * AC inf
	 */
	private String acInf;
	
	/**
	 * Комментарии
	 */
	private String commentary;
	
	
	private static final long serialVersionUID = 4968388177669197561L;
}