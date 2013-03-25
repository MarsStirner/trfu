
package misexchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_IN402004UV02 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_IN402004UV02">
 *   &lt;complexContent>
 *     &lt;extension base="{MISExchange}Request">
 *       &lt;sequence>
 *         &lt;element name="Message" type="{urn:hl7-org:v3}PRPA_IN402004UV02"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_IN402004UV02", propOrder = {
    "message"
})
public class PRPAIN402004UV02
    extends Request
{

    @XmlElement(name = "Message", required = true)
    protected org.hl7.v3.PRPAIN402004UV02 message;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link org.hl7.v3.PRPAIN402004UV02 }
     *     
     */
    public org.hl7.v3.PRPAIN402004UV02 getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.hl7.v3.PRPAIN402004UV02 }
     *     
     */
    public void setMessage(org.hl7.v3.PRPAIN402004UV02 value) {
        this.message = value;
    }

}
