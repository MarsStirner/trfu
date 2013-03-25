
package ru.korusconsulting.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MicroOrganismResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MicroOrganismResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organismLisId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organismName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organismConcetration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MicroOrganismResult", propOrder = {
    "organismLisId",
    "organismName",
    "organismConcetration"
})
public class MicroOrganismResult {

    protected String organismLisId;
    protected String organismName;
    protected String organismConcetration;

    /**
     * Gets the value of the organismLisId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismLisId() {
        return organismLisId;
    }

    /**
     * Sets the value of the organismLisId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismLisId(String value) {
        this.organismLisId = value;
    }

    /**
     * Gets the value of the organismName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismName() {
        return organismName;
    }

    /**
     * Sets the value of the organismName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismName(String value) {
        this.organismName = value;
    }

    /**
     * Gets the value of the organismConcetration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismConcetration() {
        return organismConcetration;
    }

    /**
     * Sets the value of the organismConcetration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismConcetration(String value) {
        this.organismConcetration = value;
    }

}
