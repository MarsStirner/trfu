
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassCareProvision.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassCareProvision">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PCPR"/>
 *     &lt;enumeration value="ENC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassCareProvision")
@XmlEnum
public enum ActClassCareProvision {

    PCPR,
    ENC;

    public String value() {
        return name();
    }

    public static ActClassCareProvision fromValue(String v) {
        return valueOf(v);
    }

}
