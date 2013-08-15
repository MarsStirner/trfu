
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassPositionAccuracy.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassPositionAccuracy">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POSACC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassPositionAccuracy")
@XmlEnum
public enum ActClassPositionAccuracy {

    POSACC;

    public String value() {
        return name();
    }

    public static ActClassPositionAccuracy fromValue(String v) {
        return valueOf(v);
    }

}
