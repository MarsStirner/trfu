
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActClassObservation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassObservation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ALRT"/>
 *     &lt;enumeration value="BATTERY"/>
 *     &lt;enumeration value="CASE"/>
 *     &lt;enumeration value="CLNTRL"/>
 *     &lt;enumeration value="CNOD"/>
 *     &lt;enumeration value="CONC"/>
 *     &lt;enumeration value="COND"/>
 *     &lt;enumeration value="DETPOL"/>
 *     &lt;enumeration value="DGIMG"/>
 *     &lt;enumeration value="EXP"/>
 *     &lt;enumeration value="GEN"/>
 *     &lt;enumeration value="INVSTG"/>
 *     &lt;enumeration value="LLD"/>
 *     &lt;enumeration value="LOC"/>
 *     &lt;enumeration value="OBS"/>
 *     &lt;enumeration value="OBSCOR"/>
 *     &lt;enumeration value="OBSSER"/>
 *     &lt;enumeration value="OUTB"/>
 *     &lt;enumeration value="PHN"/>
 *     &lt;enumeration value="POL"/>
 *     &lt;enumeration value="POS"/>
 *     &lt;enumeration value="POSACC"/>
 *     &lt;enumeration value="POSCOORD"/>
 *     &lt;enumeration value="PRN"/>
 *     &lt;enumeration value="RLD"/>
 *     &lt;enumeration value="ROIBND"/>
 *     &lt;enumeration value="ROIOVL"/>
 *     &lt;enumeration value="RTRD"/>
 *     &lt;enumeration value="SEQ"/>
 *     &lt;enumeration value="SEQVAR"/>
 *     &lt;enumeration value="SFWL"/>
 *     &lt;enumeration value="SIT"/>
 *     &lt;enumeration value="SPCOBS"/>
 *     &lt;enumeration value="STN"/>
 *     &lt;enumeration value="SUP"/>
 *     &lt;enumeration value="TRD"/>
 *     &lt;enumeration value="VERIF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassObservation")
@XmlEnum
public enum ActClassObservation {

    ALRT,
    BATTERY,
    CASE,
    CLNTRL,
    CNOD,
    CONC,
    COND,
    DETPOL,
    DGIMG,
    EXP,
    GEN,
    INVSTG,
    LLD,
    LOC,
    OBS,
    OBSCOR,
    OBSSER,
    OUTB,
    PHN,
    POL,
    POS,
    POSACC,
    POSCOORD,
    PRN,
    RLD,
    ROIBND,
    ROIOVL,
    RTRD,
    SEQ,
    SEQVAR,
    SFWL,
    SIT,
    SPCOBS,
    STN,
    SUP,
    TRD,
    VERIF;

    public String value() {
        return name();
    }

    public static ActClassObservation fromValue(String v) {
        return valueOf(v);
    }

}
