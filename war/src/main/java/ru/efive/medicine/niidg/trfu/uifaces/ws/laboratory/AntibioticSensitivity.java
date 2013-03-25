package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * антибиотики
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AntibioticSensitivity")
@XmlType(name = "AntibioticSensitivity", propOrder = {"antibioticLisId", "antibioticName", "mic", "antibioticActivityValue"} )
public class AntibioticSensitivity implements java.io.Serializable {
	
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
	@XmlElement(name = "antibioticLisId")
	private String antibioticLisId;
	
	/**
	 * название антибиотика
	 */
	@XmlElement(name = "antibioticName")
	private String antibioticName;
	
	/**
	 * величина концентрации
	 */
	@XmlElement(name = "mic")
	private String mic;
	
	/**
	 * описание чувствительности в произвольном виде: R,S,I
	 */
	@XmlElement(name = "antibioticActivityValue")
	private String antibioticActivityValue;
	
	
	private static final long serialVersionUID = 1L;
}