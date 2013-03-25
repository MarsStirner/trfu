
package ru.korusconsulting.laboratory;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setAnalysisResults complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setAnalysisResults">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orderBarCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="results" type="{http://www.korusconsulting.ru}AnalysisResult" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="biomaterialDefects" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultDoctorLisId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setAnalysisResults", propOrder = {
    "orderId",
    "orderBarCode",
    "results",
    "biomaterialDefects",
    "resultDoctorLisId"
})
public class SetAnalysisResults {

    protected int orderId;
    protected String orderBarCode;
    protected List<AnalysisResult> results;
    protected String biomaterialDefects;
    protected int resultDoctorLisId;

    /**
     * Gets the value of the orderId property.
     * 
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     */
    public void setOrderId(int value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderBarCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderBarCode() {
        return orderBarCode;
    }

    /**
     * Sets the value of the orderBarCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderBarCode(String value) {
        this.orderBarCode = value;
    }

    /**
     * Gets the value of the results property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the results property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ru.korusconsulting.laboratory.AnalysisResult }
     * 
     * 
     */
    public List<AnalysisResult> getResults() {
        if (results == null) {
            results = new ArrayList<AnalysisResult>();
        }
        return this.results;
    }

    /**
     * Gets the value of the biomaterialDefects property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBiomaterialDefects() {
        return biomaterialDefects;
    }

    /**
     * Sets the value of the biomaterialDefects property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBiomaterialDefects(String value) {
        this.biomaterialDefects = value;
    }

    /**
     * Gets the value of the resultDoctorLisId property.
     * 
     */
    public int getResultDoctorLisId() {
        return resultDoctorLisId;
    }

    /**
     * Sets the value of the resultDoctorLisId property.
     * 
     */
    public void setResultDoctorLisId(int value) {
        this.resultDoctorLisId = value;
    }

}
