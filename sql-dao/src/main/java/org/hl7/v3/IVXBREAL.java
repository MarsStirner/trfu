
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IVXB_REAL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVXB_REAL">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}REAL">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="inclusive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVXB_REAL")
public class IVXBREAL
    extends REAL
{

    @XmlAttribute
    protected Boolean inclusive;

    /**
     * Gets the value of the inclusive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInclusive() {
        return inclusive;
    }

    /**
     * Sets the value of the inclusive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInclusive(Boolean value) {
        this.inclusive = value;
    }

}
