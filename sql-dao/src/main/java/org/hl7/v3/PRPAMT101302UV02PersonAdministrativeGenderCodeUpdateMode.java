
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PRPA_MT101302UV02.Person.administrativeGenderCode.updateMode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PRPA_MT101302UV02.Person.administrativeGenderCode.updateMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PRPA_MT101302UV02.Person.administrativeGenderCode.updateMode")
@XmlEnum
public enum PRPAMT101302UV02PersonAdministrativeGenderCodeUpdateMode {

    A,
    D,
    R,
    N;

    public String value() {
        return name();
    }

    public static PRPAMT101302UV02PersonAdministrativeGenderCodeUpdateMode fromValue(String v) {
        return valueOf(v);
    }

}
