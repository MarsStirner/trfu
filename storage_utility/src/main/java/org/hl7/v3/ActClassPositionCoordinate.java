
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassPositionCoordinate.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassPositionCoordinate">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POSCOORD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassPositionCoordinate")
@XmlEnum
public enum ActClassPositionCoordinate {

    POSCOORD;

    public String value() {
        return name();
    }

    public static ActClassPositionCoordinate fromValue(String v) {
        return valueOf(v);
    }

}
