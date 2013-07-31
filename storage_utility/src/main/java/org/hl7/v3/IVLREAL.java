
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
 * <p>Java class for IVL_REAL complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVL_REAL">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_REAL">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="low" type="{urn:hl7-org:v3}IVXB_REAL"/>
 *           &lt;element name="center" type="{urn:hl7-org:v3}REAL"/>
 *           &lt;element name="width" type="{urn:hl7-org:v3}REAL"/>
 *           &lt;element name="high" type="{urn:hl7-org:v3}IVXB_REAL"/>
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
@XmlType(name = "IVL_REAL", propOrder = {
    "lowOrCenterOrWidth"
})
public class IVLREAL
    extends SXCMREAL
{

    @XmlElementRefs({
        @XmlElementRef(name = "center", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "low", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "high", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "width", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends REAL>> lowOrCenterOrWidth;

    /**
     * Gets the value of the lowOrCenterOrWidth property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lowOrCenterOrWidth property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLowOrCenterOrWidth().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link REAL }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBREAL }{@code >}
     * {@link JAXBElement }{@code <}{@link REAL }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBREAL }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends REAL>> getLowOrCenterOrWidth() {
        if (lowOrCenterOrWidth == null) {
            lowOrCenterOrWidth = new ArrayList<JAXBElement<? extends REAL>>();
        }
        return this.lowOrCenterOrWidth;
    }

}
