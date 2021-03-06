
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassConsent.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassConsent">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CONS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassConsent")
@XmlEnum
public enum ActClassConsent {

    CONS;

    public String value() {
        return name();
    }

    public static ActClassConsent fromValue(String v) {
        return valueOf(v);
    }

}
