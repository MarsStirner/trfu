
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityClassLivingSubject.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityClassLivingSubject">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ANM"/>
 *     &lt;enumeration value="LIV"/>
 *     &lt;enumeration value="MIC"/>
 *     &lt;enumeration value="NLIV"/>
 *     &lt;enumeration value="PLNT"/>
 *     &lt;enumeration value="PSN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityClassLivingSubject")
@XmlEnum
public enum EntityClassLivingSubject {

    ANM,
    LIV,
    MIC,
    NLIV,
    PLNT,
    PSN;

    public String value() {
        return name();
    }

    public static EntityClassLivingSubject fromValue(String v) {
        return valueOf(v);
    }

}
