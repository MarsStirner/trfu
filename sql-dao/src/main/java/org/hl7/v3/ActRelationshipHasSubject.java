
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActRelationshipHasSubject.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipHasSubject">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUBJ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipHasSubject")
@XmlEnum
public enum ActRelationshipHasSubject {

    SUBJ;

    public String value() {
        return name();
    }

    public static ActRelationshipHasSubject fromValue(String v) {
        return valueOf(v);
    }

}
