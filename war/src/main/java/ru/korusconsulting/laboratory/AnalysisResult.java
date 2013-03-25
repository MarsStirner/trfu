
package ru.korusconsulting.laboratory;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AnalysisResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnalysisResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indicatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indicatorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deviceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="resultValueText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageValues" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="imageValue" type="{http://www.korusconsulting.ru}ImageValue" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="microValues" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="microValue" type="{http://www.korusconsulting.ru}MicroOrganismResult" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="microSensitivityList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="microSensitivity" type="{http://www.korusconsulting.ru}AntibioticSensitivity" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resultNormString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultNormalityIndex" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="resultUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultSignDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="resultStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnalysisResult", propOrder = {
    "indicatorName",
    "indicatorCode",
    "deviceName",
    "valueType",
    "resultValueText",
    "imageValues",
    "microValues",
    "microSensitivityList",
    "resultNormString",
    "resultNormalityIndex",
    "resultUnit",
    "resultSignDate",
    "resultStatus",
    "resultComment"
})
public class AnalysisResult {

    protected String indicatorName;
    protected String indicatorCode;
    protected String deviceName;
    protected int valueType;
    protected String resultValueText;
    protected ImageValues imageValues;
    protected MicroValues microValues;
    protected MicroSensitivityList microSensitivityList;
    protected String resultNormString;
    protected float resultNormalityIndex;
    protected String resultUnit;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar resultSignDate;
    protected String resultStatus;
    protected String resultComment;

    /**
     * Gets the value of the indicatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicatorName() {
        return indicatorName;
    }

    /**
     * Sets the value of the indicatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicatorName(String value) {
        this.indicatorName = value;
    }

    /**
     * Gets the value of the indicatorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicatorCode() {
        return indicatorCode;
    }

    /**
     * Sets the value of the indicatorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicatorCode(String value) {
        this.indicatorCode = value;
    }

    /**
     * Gets the value of the deviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Sets the value of the deviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceName(String value) {
        this.deviceName = value;
    }

    /**
     * Gets the value of the valueType property.
     * 
     */
    public int getValueType() {
        return valueType;
    }

    /**
     * Sets the value of the valueType property.
     * 
     */
    public void setValueType(int value) {
        this.valueType = value;
    }

    /**
     * Gets the value of the resultValueText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultValueText() {
        return resultValueText;
    }

    /**
     * Sets the value of the resultValueText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultValueText(String value) {
        this.resultValueText = value;
    }

    /**
     * Gets the value of the imageValues property.
     * 
     * @return
     *     possible object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.ImageValues }
     *     
     */
    public ImageValues getImageValues() {
        return imageValues;
    }

    /**
     * Sets the value of the imageValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.ImageValues }
     *     
     */
    public void setImageValues(ImageValues value) {
        this.imageValues = value;
    }

    /**
     * Gets the value of the microValues property.
     * 
     * @return
     *     possible object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.MicroValues }
     *     
     */
    public MicroValues getMicroValues() {
        return microValues;
    }

    /**
     * Sets the value of the microValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.MicroValues }
     *     
     */
    public void setMicroValues(MicroValues value) {
        this.microValues = value;
    }

    /**
     * Gets the value of the microSensitivityList property.
     * 
     * @return
     *     possible object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.MicroSensitivityList }
     *     
     */
    public MicroSensitivityList getMicroSensitivityList() {
        return microSensitivityList;
    }

    /**
     * Sets the value of the microSensitivityList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ru.korusconsulting.laboratory.AnalysisResult.MicroSensitivityList }
     *     
     */
    public void setMicroSensitivityList(MicroSensitivityList value) {
        this.microSensitivityList = value;
    }

    /**
     * Gets the value of the resultNormString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultNormString() {
        return resultNormString;
    }

    /**
     * Sets the value of the resultNormString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultNormString(String value) {
        this.resultNormString = value;
    }

    /**
     * Gets the value of the resultNormalityIndex property.
     * 
     */
    public float getResultNormalityIndex() {
        return resultNormalityIndex;
    }

    /**
     * Sets the value of the resultNormalityIndex property.
     * 
     */
    public void setResultNormalityIndex(float value) {
        this.resultNormalityIndex = value;
    }

    /**
     * Gets the value of the resultUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultUnit() {
        return resultUnit;
    }

    /**
     * Sets the value of the resultUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultUnit(String value) {
        this.resultUnit = value;
    }

    /**
     * Gets the value of the resultSignDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResultSignDate() {
        return resultSignDate;
    }

    /**
     * Sets the value of the resultSignDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setResultSignDate(XMLGregorianCalendar value) {
        this.resultSignDate = value;
    }

    /**
     * Gets the value of the resultStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * Sets the value of the resultStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultStatus(String value) {
        this.resultStatus = value;
    }

    /**
     * Gets the value of the resultComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultComment() {
        return resultComment;
    }

    /**
     * Sets the value of the resultComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultComment(String value) {
        this.resultComment = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="imageValue" type="{http://www.korusconsulting.ru}ImageValue" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "imageValue"
    })
    public static class ImageValues {

        protected List<ImageValue> imageValue;

        /**
         * Gets the value of the imageValue property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the imageValue property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImageValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ru.korusconsulting.laboratory.ImageValue }
         * 
         * 
         */
        public List<ImageValue> getImageValue() {
            if (imageValue == null) {
                imageValue = new ArrayList<ImageValue>();
            }
            return this.imageValue;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="microSensitivity" type="{http://www.korusconsulting.ru}AntibioticSensitivity" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "microSensitivity"
    })
    public static class MicroSensitivityList {

        protected List<AntibioticSensitivity> microSensitivity;

        /**
         * Gets the value of the microSensitivity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the microSensitivity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMicroSensitivity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ru.korusconsulting.laboratory.AntibioticSensitivity }
         * 
         * 
         */
        public List<AntibioticSensitivity> getMicroSensitivity() {
            if (microSensitivity == null) {
                microSensitivity = new ArrayList<AntibioticSensitivity>();
            }
            return this.microSensitivity;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="microValue" type="{http://www.korusconsulting.ru}MicroOrganismResult" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "microValue"
    })
    public static class MicroValues {

        protected List<MicroOrganismResult> microValue;

        /**
         * Gets the value of the microValue property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the microValue property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMicroValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ru.korusconsulting.laboratory.MicroOrganismResult }
         * 
         * 
         */
        public List<MicroOrganismResult> getMicroValue() {
            if (microValue == null) {
                microValue = new ArrayList<MicroOrganismResult>();
            }
            return this.microValue;
        }

    }

}
