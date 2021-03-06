
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipOutcome.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipOutcome">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="GOAL"/>
 *     &lt;enumeration value="OBJC"/>
 *     &lt;enumeration value="OBJF"/>
 *     &lt;enumeration value="OUTC"/>
 *     &lt;enumeration value="RISK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipOutcome")
@XmlEnum
public enum ActRelationshipOutcome {

    GOAL,
    OBJC,
    OBJF,
    OUTC,
    RISK;

    public String value() {
        return name();
    }

    public static ActRelationshipOutcome fromValue(String v) {
        return valueOf(v);
    }

}
