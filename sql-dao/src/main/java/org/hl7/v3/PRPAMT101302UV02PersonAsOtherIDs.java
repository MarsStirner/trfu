
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT101302UV02.Person.asOtherIDs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_MT101302UV02.Person.asOtherIDs">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}PRPA_MT101302UV02.OtherIDs">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="updateMode" type="{urn:hl7-org:v3}PRPA_MT101302UV02.Person.asOtherIDs.updateMode" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT101302UV02.Person.asOtherIDs")
public class PRPAMT101302UV02PersonAsOtherIDs
    extends PRPAMT101302UV02OtherIDs
{

    @XmlAttribute
    protected PRPAMT101302UV02PersonAsOtherIDsUpdateMode updateMode;

    /**
     * Gets the value of the updateMode property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT101302UV02PersonAsOtherIDsUpdateMode }
     *     
     */
    public PRPAMT101302UV02PersonAsOtherIDsUpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Sets the value of the updateMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT101302UV02PersonAsOtherIDsUpdateMode }
     *     
     */
    public void setUpdateMode(PRPAMT101302UV02PersonAsOtherIDsUpdateMode value) {
        this.updateMode = value;
    }

}
