
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CE">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}CD">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CE")
@XmlSeeAlso({
    PRPAMT101302UV02GuardianCode.class,
    PRPAMT101302UV02EmployeeOccupationCode.class,
    PRPAMT101302UV02PersonAdministrativeGenderCode.class,
    PRPAMT101302UV02EmployeeJobClassCode.class,
    PRPAMT101302UV02IdentifiedPersonConfidentialityCode.class,
    PRPAMT101302UV02ContactPartyCode.class,
    PRPAMT101302UV02PersonalRelationshipCode.class,
    PRPAMT101302UV02EmployeeCode.class,
    PRPAMT101302UV02CitizenCode.class,
    EIVLEvent.class,
    CV.class,
    PRPAMT101302UV02PersonMaritalStatusCode.class,
    HXITCE.class
})
public class CE
    extends CD
{


}
