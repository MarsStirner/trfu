
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassDetectedIssue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassDetectedIssue">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALRT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassDetectedIssue")
@XmlEnum
public enum ActClassDetectedIssue {

    ALRT;

    public String value() {
        return name();
    }

    public static ActClassDetectedIssue fromValue(String v) {
        return valueOf(v);
    }

}
