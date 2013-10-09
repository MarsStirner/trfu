
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TEL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TEL">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}URL">
 *       &lt;sequence>
 *         &lt;element name="useablePeriod" type="{urn:hl7-org:v3}SXCM_TS" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="use">
 *         &lt;simpleType>
 *           &lt;list itemType="{urn:hl7-org:v3}TelecommunicationAddressUse" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEL", propOrder = {
    "useablePeriod"
})
@XmlSeeAlso({
    PRPAMT101302UV02EmployeeTelecom.class,
    PRPAMT101302UV02ContactPartyTelecom.class,
    PRPAMT101302UV02PersonTelecom.class,
    PRPAMT101302UV02PersonalRelationshipTelecom.class,
    PRPAMT101302UV02GuardianTelecom.class,
    PRPAMT101302UV02StudentTelecom.class,
    PRPAMT101302UV02IdentifiedPersonTelecom.class
})
public class TEL
    extends URL
{

    @XmlElement(nillable = true)
    protected List<SXCMTS> useablePeriod;
    @XmlAttribute
    protected List<TelecommunicationAddressUse> use;

    /**
     * Gets the value of the useablePeriod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the useablePeriod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUseablePeriod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SXCMTS }
     * 
     * 
     */
    public List<SXCMTS> getUseablePeriod() {
        if (useablePeriod == null) {
            useablePeriod = new ArrayList<SXCMTS>();
        }
        return this.useablePeriod;
    }

    /**
     * Gets the value of the use property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the use property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TelecommunicationAddressUse }
     * 
     * 
     */
    public List<TelecommunicationAddressUse> getUse() {
        if (use == null) {
            use = new ArrayList<TelecommunicationAddressUse>();
        }
        return this.use;
    }

}
