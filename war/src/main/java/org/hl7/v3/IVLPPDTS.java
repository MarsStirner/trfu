
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IVL_PPD_TS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVL_PPD_TS">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_PPD_TS">
 *       &lt;choice>
 *         &lt;element name="low" type="{urn:hl7-org:v3}IVXB_PPD_TS" minOccurs="0"/>
 *         &lt;element name="width" type="{urn:hl7-org:v3}PPD_PQ" minOccurs="0"/>
 *         &lt;element name="high" type="{urn:hl7-org:v3}IVXB_PPD_TS" minOccurs="0"/>
 *         &lt;element name="center" type="{urn:hl7-org:v3}PPD_TS" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVL_PPD_TS", propOrder = {
    "low",
    "width",
    "high",
    "center"
})
public class IVLPPDTS
    extends SXCMPPDTS
{

    protected IVXBPPDTS low;
    protected PPDPQ width;
    protected IVXBPPDTS high;
    protected PPDTS center;

    /**
     * Gets the value of the low property.
     * 
     * @return
     *     possible object is
     *     {@link IVXBPPDTS }
     *     
     */
    public IVXBPPDTS getLow() {
        return low;
    }

    /**
     * Sets the value of the low property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVXBPPDTS }
     *     
     */
    public void setLow(IVXBPPDTS value) {
        this.low = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link PPDPQ }
     *     
     */
    public PPDPQ getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link PPDPQ }
     *     
     */
    public void setWidth(PPDPQ value) {
        this.width = value;
    }

    /**
     * Gets the value of the high property.
     * 
     * @return
     *     possible object is
     *     {@link IVXBPPDTS }
     *     
     */
    public IVXBPPDTS getHigh() {
        return high;
    }

    /**
     * Sets the value of the high property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVXBPPDTS }
     *     
     */
    public void setHigh(IVXBPPDTS value) {
        this.high = value;
    }

    /**
     * Gets the value of the center property.
     * 
     * @return
     *     possible object is
     *     {@link PPDTS }
     *     
     */
    public PPDTS getCenter() {
        return center;
    }

    /**
     * Sets the value of the center property.
     * 
     * @param value
     *     allowed object is
     *     {@link PPDTS }
     *     
     */
    public void setCenter(PPDTS value) {
        this.center = value;
    }

}
