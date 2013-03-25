package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * микроорганизмы
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MicroOrganismResult")
@XmlType(name = "MicroOrganismResult", propOrder = {"organismLisId", "organismName", "organismConcetration"} )
public class MicroOrganismResult implements java.io.Serializable {
	
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
	@XmlElement(name = "organismLisId")
	private String organismLisId;
	
	/**
	 * название организма
	 */
	@XmlElement(name = "organismName")
	private String organismName;
	
	/**
	 * описание концентрации в произвольном виде
	 */
	@XmlElement(name = "organismConcetration")
	private String organismConcetration;
	
	
	private static final long serialVersionUID = 1L;
}