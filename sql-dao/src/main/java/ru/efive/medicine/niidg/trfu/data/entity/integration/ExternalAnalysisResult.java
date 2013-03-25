package ru.efive.medicine.niidg.trfu.data.entity.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * результаты анализов
 */
@Entity
@Table(name = "trfu_external_analysis_results")
public class ExternalAnalysisResult extends IdentifiedEntity {
	
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
	
	public List<ExternalImageValue> getImageValues() {
		return imageValues;
	}
	
	public void setImageValues(List<ExternalImageValue> imageValues) {
		this.imageValues = imageValues;
	}
	
	public void addImageValue(ExternalImageValue imageValue) {
		if (imageValues == null) {
			imageValues = new ArrayList<ExternalImageValue>();
		}
		imageValues.add(imageValue);
	}
	
	public List<ExternalMicroOrganismResult> getMicroValues() {
		return microValues;
	}
	
	public void setMicroValues(List<ExternalMicroOrganismResult> microValues) {
		this.microValues = microValues;
	}
	
	public void addMicroValue(ExternalMicroOrganismResult microValue) {
		if (microValues == null) {
			microValues = new ArrayList<ExternalMicroOrganismResult>();
		}
		microValues.add(microValue);
	}
	
	public List<ExternalAntibioticSensitivity> getMicroSensitivity() {
		return microSensitivity;
	}
	
	public void setMicroSensitivity(List<ExternalAntibioticSensitivity> microSensitivity) {
		this.microSensitivity = microSensitivity;
	}
	
	public void addMicroSensitivity(ExternalAntibioticSensitivity microSensitivityEntry) {
		if (microSensitivity == null) {
			microSensitivity = new ArrayList<ExternalAntibioticSensitivity>();
		}
		microSensitivity.add(microSensitivityEntry);
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
	private String indicatorName;
	
	/**
	 * код методики/показателя
	 */
	private String indicatorCode;
	
	/**
	 * название прибора
	 */
	private String deviceName;
	
	/**
	 * тип значения
	 * 1 – строковое значение или большой текст, 2 – изображение, 3 – концентрация микроорг., 4 –чувствительность микроорг.
	 */
	private int valueType;
	
	/**
	 * значение (значение в виде строки или большого текста)
	 */
	@Column(columnDefinition="text")
	private String resultValueText;
	
	/**
	 * список изображений
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_external_analysis_result_images", 
			joinColumns = { @JoinColumn(name = "analysis_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "image_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExternalImageValue> imageValues;
	
	/**
	 * набор значений
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_external_analysis_result_micro_values", 
			joinColumns = { @JoinColumn(name = "analysis_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "micro_value_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExternalMicroOrganismResult> microValues;
	
	/**
	 * набор значений
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_external_analysis_result_micro_sensivity", 
			joinColumns = { @JoinColumn(name = "analysis_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "micro_sensivity_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExternalAntibioticSensitivity> microSensitivity;
	
	/**
	 * диапазон допустимых значений
	 */
	private String resultNormString;
	
	/**
	 * значение результата относительно нормы
	 */
	private float resultNormalityIndex;
	
	/**
	 * единица измерения
	 */
	private String resultUnit;
	
	/**
	 * дата выполнения/утверждения результата
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date resultSignDate;
	
	/**
	 * если результата нет здесь указана причина
	 */
	private String resultStatus;
	
	/**
	 * произвольный текстовый комментарий
	 */
	@Column(columnDefinition="text")
	private String resultComment;
	
	
	private static final long serialVersionUID = 1L;
}