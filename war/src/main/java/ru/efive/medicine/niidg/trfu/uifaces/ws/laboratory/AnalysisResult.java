package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * результаты анализов
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnalysisResult", propOrder = {"indicatorName", "indicatorCode", "deviceName", "valueType", "resultValueText", "imageValues", "microValues",
		"microSensitivity", "resultNormString", "resultNormalityIndex", "resultUnit", "resultSignDate", "resultStatus", "resultComment"} )
@XmlRootElement(name = "AnalysisResult")
public class AnalysisResult implements java.io.Serializable {
	
	public String getIndicatorName() {
		return indicatorName;
	}
	
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	
	public String getIndicatorCode() {
		return indicatorCode;
	}
	
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public int getValueType() {
		return valueType;
	}
	
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}
	
	public String getResultValueText() {
		return resultValueText;
	}
	
	public void setResultValueText(String resultValueText) {
		this.resultValueText = resultValueText;
	}
	
	public List<ImageValue> getImageValues() {
		return imageValues;
	}
	
	public void setImageValues(List<ImageValue> imageValues) {
		this.imageValues = imageValues;
	}
	
	public List<MicroOrganismResult> getMicroValues() {
		return microValues;
	}
	
	public void setMicroValues(List<MicroOrganismResult> microValues) {
		this.microValues = microValues;
	}
	
	public List<AntibioticSensitivity> getMicroSensitivity() {
		return microSensitivity;
	}
	
	public void setMicroSensitivity(List<AntibioticSensitivity> microSensitivity) {
		this.microSensitivity = microSensitivity;
	}
	
	public String getResultNormString() {
		return resultNormString;
	}
	
	public void setResultNormString(String resultNormString) {
		this.resultNormString = resultNormString;
	}
	
	public float getResultNormalityIndex() {
		return resultNormalityIndex;
	}
	
	public void setResultNormalityIndex(float resultNormalityIndex) {
		this.resultNormalityIndex = resultNormalityIndex;
	}
	
	public String getResultUnit() {
		return resultUnit;
	}
	
	public void setResultUnit(String resultUnit) {
		this.resultUnit = resultUnit;
	}
	
	public Date getResultSignDate() {
		return resultSignDate;
	}
	
	public void setResultSignDate(Date resultSignDate) {
		this.resultSignDate = resultSignDate;
	}
	
	public String getResultStatus() {
		return resultStatus;
	}
	
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	public String getResultComment() {
		return resultComment;
	}
	
	public void setResultComment(String resultComment) {
		this.resultComment = resultComment;
	}
	
	
	/**
	 * название методики/показателя/микроорганизма
	 */
	@XmlElement(name = "indicatorName")
	private String indicatorName;
	
	/**
	 * код методики/показателя
	 */
	@XmlElement(name = "indicatorCode")
	private String indicatorCode;
	
	/**
	 * название прибора
	 */
	@XmlElement(name = "deviceName")
	private String deviceName;
	
	/**
	 * тип значения
	 * 1 – строковое значение или большой текст, 2 – изображение, 3 – концентрация микроорг., 4 –чувствительность микроорг.
	 */
	@XmlElement(name = "valueType")
	private int valueType;
	
	/**
	 * значение (значение в виде строки или большого текста)
	 */
	@XmlElement(name = "resultValueText")
	private String resultValueText;
	
	/**
	 * список изображений
	 */
	@XmlElement(name = "imageValue")
	@XmlElementWrapper(name = "imageValues")
	private List<ImageValue> imageValues = new ArrayList<ImageValue>();
	
	/**
	 * набор значений
	 */
	@XmlElement(name = "microValue")
	@XmlElementWrapper(name = "microValues")
	private List<MicroOrganismResult> microValues = new ArrayList<MicroOrganismResult>();
	
	/**
	 * набор значений
	 */
	@XmlElement(name = "microSensitivity")
	@XmlElementWrapper(name = "microSensitivityList")
	private List<AntibioticSensitivity> microSensitivity = new ArrayList<AntibioticSensitivity>();
	
	/**
	 * диапазон допустимых значений
	 */
	@XmlElement(name = "resultNormString")
	private String resultNormString;
	
	/**
	 * значение результата относительно нормы
	 */
	@XmlElement(name = "resultNormalityIndex")
	private float resultNormalityIndex;
	
	/**
	 * единица измерения
	 */
	@XmlElement(name = "resultUnit")
	private String resultUnit;
	
	/**
	 * дата выполнения/утверждения результата
	 */
	@XmlElement(name = "resultSignDate")
	private Date resultSignDate;
	
	/**
	 * если результата нет здесь указана причина
	 */
	@XmlElement(name = "resultStatus")
	private String resultStatus;
	
	/**
	 * произвольный текстовый комментарий
	 */
	@XmlElement(name = "resultComment")
	private String resultComment;
	
	
	private static final long serialVersionUID = 1L;
}