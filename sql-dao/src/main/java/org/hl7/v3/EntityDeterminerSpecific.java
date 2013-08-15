
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityDeterminerSpecific.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityDeterminerSpecific">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INSTANCE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityDeterminerSpecific")
@XmlEnum
public enum EntityDeterminerSpecific {

    INSTANCE;

    public String value() {
        return name();
    }

    public static EntityDeterminerSpecific fromValue(String v) {
        return valueOf(v);
    }

}
