
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ENXP_explicit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ENXP_explicit">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}EntityNamePartType" />
 *       &lt;attribute name="qualifier">
 *         &lt;simpleType>
 *           &lt;list itemType="{urn:hl7-org:v3}EntityNamePartQualifier" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ENXP_explicit", propOrder = {
    "value"
})
@XmlSeeAlso({
    EnExplicitPrefix.class,
    EnExplicitSuffix.class,
    EnExplicitGiven.class,
    EnExplicitDelimiter.class,
    EnExplicitFamily.class
})
public class ENXPExplicit {

    @XmlValue
    protected String value;
    @XmlAttribute
    protected EntityNamePartType partType;
    @XmlAttribute
    protected List<EntityNamePartQualifier> qualifier;

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
     *     {@link EntityNamePartType }
     *     
     */
    public EntityNamePartType getPartType() {
        return partType;
    }

    /**
     * Sets the value of the partType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityNamePartType }
     *     
     */
    public void setPartType(EntityNamePartType value) {
        this.partType = value;
    }

    /**
     * Gets the value of the qualifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntityNamePartQualifier }
     * 
     * 
     */
    public List<EntityNamePartQualifier> getQualifier() {
        if (qualifier == null) {
            qualifier = new ArrayList<EntityNamePartQualifier>();
        }
        return this.qualifier;
    }

}
