
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassEmployee.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassEmployee">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EMP"/>
 *     &lt;enumeration value="MIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassEmployee")
@XmlEnum
public enum RoleClassEmployee {

    EMP,
    MIL;

    public String value() {
        return name();
    }

    public static RoleClassEmployee fromValue(String v) {
        return valueOf(v);
    }

}
