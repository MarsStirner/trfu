
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassFlavorAdditive.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassFlavorAdditive">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FLVR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassFlavorAdditive")
@XmlEnum
public enum RoleClassFlavorAdditive {

    FLVR;

    public String value() {
        return name();
    }

    public static RoleClassFlavorAdditive fromValue(String v) {
        return valueOf(v);
    }

}
