
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IVL_INT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVL_INT">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_INT">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="width" type="{urn:hl7-org:v3}INT"/>
 *           &lt;element name="high" type="{urn:hl7-org:v3}IVXB_INT"/>
 *           &lt;element name="center" type="{urn:hl7-org:v3}INT"/>
 *           &lt;element name="low" type="{urn:hl7-org:v3}IVXB_INT"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVL_INT", propOrder = {
    "widthOrHighOrCenter"
})
public class IVLINT
    extends SXCMINT
{

    @XmlElementRefs({
        @XmlElementRef(name = "center", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "low", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "width", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "high", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends INT>> widthOrHighOrCenter;

    /**
     * Gets the value of the widthOrHighOrCenter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the widthOrHighOrCenter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWidthOrHighOrCenter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link IVXBINT }{@code >}
     * {@link JAXBElement }{@code <}{@link INT }{@code >}
     * {@link JAXBElement }{@code <}{@link INT }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBINT }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends INT>> getWidthOrHighOrCenter() {
        if (widthOrHighOrCenter == null) {
            widthOrHighOrCenter = new ArrayList<JAXBElement<? extends INT>>();
        }
        return this.widthOrHighOrCenter;
    }

}
