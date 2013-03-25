
package ru.korusconsulting.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AntibioticSensitivity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AntibioticSensitivity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="antibioticLisId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="antibioticName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="antibioticActivityValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AntibioticSensitivity", propOrder = {
    "antibioticLisId",
    "antibioticName",
    "mic",
    "antibioticActivityValue"
})
public class AntibioticSensitivity {

    protected String antibioticLisId;
    protected String antibioticName;
    protected String mic;
    protected String antibioticActivityValue;

    /**
     * Gets the value of the antibioticLisId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntibioticLisId() {
        return antibioticLisId;
    }

    /**
     * Sets the value of the antibioticLisId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntibioticLisId(String value) {
        this.antibioticLisId = value;
    }

    /**
     * Gets the value of the antibioticName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntibioticName() {
        return antibioticName;
    }

    /**
     * Sets the value of the antibioticName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntibioticName(String value) {
        this.antibioticName = value;
    }

    /**
     * Gets the value of the mic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMic() {
        return mic;
    }

    /**
     * Sets the value of the mic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMic(String value) {
        this.mic = value;
    }

    /**
     * Gets the value of the antibioticActivityValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntibioticActivityValue() {
        return antibioticActivityValue;
    }

    /**
     * Sets the value of the antibioticActivityValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntibioticActivityValue(String value) {
        this.antibioticActivityValue = value;
    }

}
