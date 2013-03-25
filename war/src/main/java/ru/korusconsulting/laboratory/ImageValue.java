
package ru.korusconsulting.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="imageString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageValue", propOrder = {
    "imageString",
    "imageData"
})
public class ImageValue {

    protected String imageString;
    protected String imageData;

    /**
     * Gets the value of the imageString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageString() {
        return imageString;
    }

    /**
     * Sets the value of the imageString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageString(String value) {
        this.imageString = value;
    }

    /**
     * Gets the value of the imageData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageData() {
        return imageData;
    }

    /**
     * Sets the value of the imageData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageData(String value) {
        this.imageData = value;
    }

}
