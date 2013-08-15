
package org.hl7.v3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="deliveryInstallationQualifier" type="{urn:hl7-org:v3}adxp.deliveryInstallationQualifier"/>
 *           &lt;element name="streetName" type="{urn:hl7-org:v3}adxp.streetName"/>
 *           &lt;element name="state" type="{urn:hl7-org:v3}adxp.state"/>
 *           &lt;element name="deliveryInstallationType" type="{urn:hl7-org:v3}adxp.deliveryInstallationType"/>
 *           &lt;element name="unitType" type="{urn:hl7-org:v3}adxp.unitType"/>
 *           &lt;element name="postBox" type="{urn:hl7-org:v3}adxp.postBox"/>
 *           &lt;element name="country" type="{urn:hl7-org:v3}adxp.country"/>
 *           &lt;element name="unitID" type="{urn:hl7-org:v3}adxp.unitID"/>
 *           &lt;element name="precinct" type="{urn:hl7-org:v3}adxp.precinct"/>
 *           &lt;element name="houseNumber" type="{urn:hl7-org:v3}adxp.houseNumber"/>
 *           &lt;element name="city" type="{urn:hl7-org:v3}adxp.city"/>
 *           &lt;element name="delimiter" type="{urn:hl7-org:v3}adxp.delimiter"/>
 *           &lt;element name="useablePeriod" type="{urn:hl7-org:v3}SXCM_TS"/>
 *           &lt;element name="buildingNumberSuffix" type="{urn:hl7-org:v3}adxp.buildingNumberSuffix"/>
 *           &lt;element name="streetNameType" type="{urn:hl7-org:v3}adxp.streetNameType"/>
 *           &lt;element name="careOf" type="{urn:hl7-org:v3}adxp.careOf"/>
 *           &lt;element name="censusTract" type="{urn:hl7-org:v3}adxp.censusTract"/>
 *           &lt;element name="deliveryMode" type="{urn:hl7-org:v3}adxp.deliveryMode"/>
 *           &lt;element name="houseNumberNumeric" type="{urn:hl7-org:v3}adxp.houseNumberNumeric"/>
 *           &lt;element name="additionalLocator" type="{urn:hl7-org:v3}adxp.additionalLocator"/>
 *           &lt;element name="deliveryAddressLine" type="{urn:hl7-org:v3}adxp.deliveryAddressLine"/>
 *           &lt;element name="deliveryInstallationArea" type="{urn:hl7-org:v3}adxp.deliveryInstallationArea"/>
 *           &lt;element name="postalCode" type="{urn:hl7-org:v3}adxp.postalCode"/>
 *           &lt;element name="direction" type="{urn:hl7-org:v3}adxp.direction"/>
 *           &lt;element name="streetAddressLine" type="{urn:hl7-org:v3}adxp.streetAddressLine"/>
 *           &lt;element name="county" type="{urn:hl7-org:v3}adxp.county"/>
 *           &lt;element name="streetNameBase" type="{urn:hl7-org:v3}adxp.streetNameBase"/>
 *           &lt;element name="deliveryModeIdentifier" type="{urn:hl7-org:v3}adxp.deliveryModeIdentifier"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="use">
 *         &lt;simpleType>
 *           &lt;list itemType="{urn:hl7-org:v3}PostalAddressUse" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="isNotOrdered" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AD", propOrder = {
    "content"
})
public class AD {

    @XmlElementRefs({
        @XmlElementRef(name = "deliveryAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "country", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "precinct", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "careOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "deliveryInstallationArea", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "deliveryModeIdentifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "additionalLocator", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "censusTract", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "streetNameType", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "streetAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "delimiter", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "county", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "state", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "unitID", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "postBox", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "useablePeriod", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "deliveryInstallationType", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "city", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "postalCode", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "streetName", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "unitType", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "buildingNumberSuffix", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "streetNameBase", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "houseNumberNumeric", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "deliveryMode", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "houseNumber", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "direction", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "deliveryInstallationQualifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
    })
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute
    protected List<PostalAddressUse> use;
    @XmlAttribute
    protected Boolean isNotOrdered;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryAddressLine }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCountry }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPrecinct }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCareOf }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationArea }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryModeIdentifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpAdditionalLocator }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCensusTract }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetNameType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetAddressLine }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDelimiter }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCounty }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpState }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpUnitID }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPostBox }{@code >}
     * {@link JAXBElement }{@code <}{@link SXCMTS }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCity }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPostalCode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetName }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpUnitType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpBuildingNumberSuffix }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetNameBase }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpHouseNumberNumeric }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryMode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpHouseNumber }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationQualifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDirection }{@code >}
     * {@link String }
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
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
     * {@link PostalAddressUse }
     * 
     * 
     */
    public List<PostalAddressUse> getUse() {
        if (use == null) {
            use = new ArrayList<PostalAddressUse>();
        }
        return this.use;
    }

    /**
     * Gets the value of the isNotOrdered property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNotOrdered() {
        return isNotOrdered;
    }

    /**
     * Sets the value of the isNotOrdered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNotOrdered(Boolean value) {
        this.isNotOrdered = value;
    }

}
