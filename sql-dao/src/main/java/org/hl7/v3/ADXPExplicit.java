
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ADXP_explicit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ADXP_explicit">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}AddressPartType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADXP_explicit", propOrder = {
    "value"
})
@XmlSeeAlso({
    AdxpExplicitAdditionalLocator.class,
    AdxpExplicitCounty.class,
    AdxpExplicitBuildingNumberSuffix.class,
    AdxpExplicitDeliveryInstallationArea.class,
    AdxpExplicitStreetAddressLine.class,
    AdxpExplicitCensusTract.class,
    AdxpExplicitDeliveryInstallationType.class,
    AdxpExplicitCity.class,
    AdxpExplicitDeliveryAddressLine.class,
    AdxpExplicitUnitID.class,
    AdxpExplicitHouseNumberNumeric.class,
    AdxpExplicitStreetNameBase.class,
    AdxpExplicitDelimiter.class,
    AdxpExplicitPrecinct.class,
    AdxpExplicitStreetName.class,
    AdxpExplicitHouseNumber.class,
    AdxpExplicitState.class,
    AdxpExplicitCareOf.class,
    AdxpExplicitStreetNameType1 .class,
    AdxpExplicitDirection.class,
    AdxpExplicitPostalCode.class,
    AdxpExplicitDeliveryInstallationQualifier.class,
    AdxpExplicitDeliveryMode.class,
    AdxpExplicitDeliveryModeIdentifier.class,
    AdxpExplicitCountry.class,
    AdxpExplicitUnitType.class,
    AdxpExplicitPostBox.class
})
public class ADXPExplicit {

    @XmlValue
    protected String value;
    @XmlAttribute
    protected AddressPartType partType;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the partType property.
     * 
     * @return
     *     possible object is
     *     {@link AddressPartType }
     *     
     */
    public AddressPartType getPartType() {
        return partType;
    }

    /**
     * Sets the value of the partType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressPartType }
     *     
     */
    public void setPartType(AddressPartType value) {
        this.partType = value;
    }

}
