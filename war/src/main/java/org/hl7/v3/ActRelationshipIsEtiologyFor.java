
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipIsEtiologyFor.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipIsEtiologyFor">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CAUS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipIsEtiologyFor")
@XmlEnum
public enum ActRelationshipIsEtiologyFor {

    CAUS;

    public String value() {
        return name();
    }

    public static ActRelationshipIsEtiologyFor fromValue(String v) {
        return valueOf(v);
    }

}
