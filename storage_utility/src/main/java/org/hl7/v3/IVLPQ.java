
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IVL_PQ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVL_PQ">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_PQ">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="center" type="{urn:hl7-org:v3}PQ"/>
 *           &lt;element name="low" type="{urn:hl7-org:v3}IVXB_PQ"/>
 *           &lt;element name="high" type="{urn:hl7-org:v3}IVXB_PQ"/>
 *           &lt;element name="width" type="{urn:hl7-org:v3}PQ"/>
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
@XmlType(name = "IVL_PQ", propOrder = {
    "centerOrLowOrHigh"
})
@XmlSeeAlso({
    BXITIVLPQ.class
})
public class IVLPQ
    extends SXCMPQ
{

    @XmlElementRefs({
        @XmlElementRef(name = "low", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "high", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "width", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
        @XmlElementRef(name = "center", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends PQ>> centerOrLowOrHigh;

    /**
     * Gets the value of the centerOrLowOrHigh property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the centerOrLowOrHigh property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCenterOrLowOrHigh().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link IVXBPQ }{@code >}
     * {@link JAXBElement }{@code <}{@link PQ }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBPQ }{@code >}
     * {@link JAXBElement }{@code <}{@link PQ }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends PQ>> getCenterOrLowOrHigh() {
        if (centerOrLowOrHigh == null) {
            centerOrLowOrHigh = new ArrayList<JAXBElement<? extends PQ>>();
        }
        return this.centerOrLowOrHigh;
    }

}