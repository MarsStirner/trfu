
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT101302UV02.Student.effectiveTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_MT101302UV02.Student.effectiveTime">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}IVL_TS">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="updateMode" type="{urn:hl7-org:v3}PRPA_MT101302UV02.Student.effectiveTime.updateMode" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT101302UV02.Student.effectiveTime")
public class PRPAMT101302UV02StudentEffectiveTime
    extends IVLTS
{

    @XmlAttribute
    protected PRPAMT101302UV02StudentEffectiveTimeUpdateMode updateMode;

    /**
     * Gets the value of the updateMode property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT101302UV02StudentEffectiveTimeUpdateMode }
     *     
     */
    public PRPAMT101302UV02StudentEffectiveTimeUpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Sets the value of the updateMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT101302UV02StudentEffectiveTimeUpdateMode }
     *     
     */
    public void setUpdateMode(PRPAMT101302UV02StudentEffectiveTimeUpdateMode value) {
        this.updateMode = value;
    }

}
