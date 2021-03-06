
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassAgent.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassAgent">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="AGNT"/>
 *     &lt;enumeration value="ASSIGNED"/>
 *     &lt;enumeration value="COMPAR"/>
 *     &lt;enumeration value="CON"/>
 *     &lt;enumeration value="ECON"/>
 *     &lt;enumeration value="GUARD"/>
 *     &lt;enumeration value="NOK"/>
 *     &lt;enumeration value="SGNOFF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassAgent")
@XmlEnum
public enum RoleClassAgent {

    AGNT,
    ASSIGNED,
    COMPAR,
    CON,
    ECON,
    GUARD,
    NOK,
    SGNOFF;

    public String value() {
        return name();
    }

    public static RoleClassAgent fromValue(String v) {
        return valueOf(v);
    }

}
