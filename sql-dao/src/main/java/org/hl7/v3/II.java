
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for II complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="II">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ANY">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="root" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="extension" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="assigningAuthorityName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="displayable" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "II")
public class II
    extends ANY
{

    @XmlAttribute
    protected String root;
    @XmlAttribute
    protected String extension;
    @XmlAttribute
    protected String assigningAuthorityName;
    @XmlAttribute
    protected Boolean displayable;

    /**
     * Gets the value of the root property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoot() {
        return root;
    }

    /**
     * Sets the value of the root property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoot(String value) {
        this.root = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension(String value) {
        this.extension = value;
    }

    /**
     * Gets the value of the assigningAuthorityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssigningAuthorityName() {
        return assigningAuthorityName;
    }

    /**
     * Sets the value of the assigningAuthorityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssigningAuthorityName(String value) {
        this.assigningAuthorityName = value;
    }

    /**
     * Gets the value of the displayable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisplayable() {
        return displayable;
    }

    /**
     * Sets the value of the displayable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisplayable(Boolean value) {
        this.displayable = value;
    }

}
