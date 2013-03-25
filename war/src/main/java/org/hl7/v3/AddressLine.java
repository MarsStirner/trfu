
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressLine.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AddressLine">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADL"/>
 *     &lt;enumeration value="AL"/>
 *     &lt;enumeration value="BNN"/>
 *     &lt;enumeration value="BNR"/>
 *     &lt;enumeration value="BNS"/>
 *     &lt;enumeration value="DAL"/>
 *     &lt;enumeration value="DINST"/>
 *     &lt;enumeration value="DINSTA"/>
 *     &lt;enumeration value="DINSTQ"/>
 *     &lt;enumeration value="DIR"/>
 *     &lt;enumeration value="DMOD"/>
 *     &lt;enumeration value="DMODID"/>
 *     &lt;enumeration value="INT"/>
 *     &lt;enumeration value="POB"/>
 *     &lt;enumeration value="SAL"/>
 *     &lt;enumeration value="STB"/>
 *     &lt;enumeration value="STR"/>
 *     &lt;enumeration value="STTYP"/>
 *     &lt;enumeration value="UNID"/>
 *     &lt;enumeration value="UNIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AddressLine")
@XmlEnum
public enum AddressLine {

    ADL,
    AL,
    BNN,
    BNR,
    BNS,
    DAL,
    DINST,
    DINSTA,
    DINSTQ,
    DIR,
    DMOD,
    DMODID,
    INT,
    POB,
    SAL,
    STB,
    STR,
    STTYP,
    UNID,
    UNIT;

    public String value() {
        return name();
    }

    public static AddressLine fromValue(String v) {
        return valueOf(v);
    }

}
