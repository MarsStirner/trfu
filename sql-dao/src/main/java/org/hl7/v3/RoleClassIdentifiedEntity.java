
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleClassIdentifiedEntity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleClassIdentifiedEntity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IDENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleClassIdentifiedEntity")
@XmlEnum
public enum RoleClassIdentifiedEntity {

    IDENT;

    public String value() {
        return name();
    }

    public static RoleClassIdentifiedEntity fromValue(String v) {
        return valueOf(v);
    }

}
