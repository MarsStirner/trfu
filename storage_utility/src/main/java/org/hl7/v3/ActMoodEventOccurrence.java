
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActMoodEventOccurrence.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActMoodEventOccurrence">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EVN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActMoodEventOccurrence")
@XmlEnum
public enum ActMoodEventOccurrence {

    EVN;

    public String value() {
        return name();
    }

    public static ActMoodEventOccurrence fromValue(String v) {
        return valueOf(v);
    }

}
