
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ADXP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ADXP">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ST">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}AddressPartType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADXP")
@XmlSeeAlso({
    AdxpDeliveryMode.class,
    AdxpDeliveryInstallationType.class,
    AdxpDelimiter.class,
    AdxpPrecinct.class,
    AdxpStreetAddressLine.class,
    AdxpUnitType.class,
    AdxpCountry.class,
    AdxpHouseNumberNumeric.class,
    AdxpUnitID.class,
    AdxpCareOf.class,
    AdxpDeliveryInstallationQualifier.class,
    AdxpCounty.class,
    AdxpHouseNumber.class,
    AdxpBuildingNumberSuffix.class,
    AdxpCensusTract.class,
    AdxpDeliveryAddressLine.class,
    AdxpDeliveryModeIdentifier.class,
    AdxpStreetName.class,
    AdxpStreetNameType.class,
    AdxpDirection.class,
    AdxpState.class,
    AdxpCity.class,
    AdxpPostalCode.class,
    AdxpStreetNameBase.class,
    AdxpAdditionalLocator.class,
    AdxpPostBox.class,
    AdxpDeliveryInstallationArea.class
})
public class ADXP
    extends ST
{

    @XmlAttribute
    protected AddressPartType partType;

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
