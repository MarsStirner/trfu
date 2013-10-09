
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CS">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}CV">
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
@XmlType(name = "CS")
@XmlSeeAlso({
    PRPAMT101302UV02ContactPartyStatusCode.class,
    PRPAMT101302UV02StudentStatusCode.class,
    PRPAMT101302UV02GuardianStatusCode.class,
    PRPAMT101302UV02EmployeeStatusCode.class,
    PRPAMT101302UV02OtherIDsStatusCode.class,
    PRPAMT101302UV02PersonalRelationshipStatusCode.class,
    PRPAMT101302UV02IdentifiedPersonStatusCode.class
})
public class CS
    extends CV
{


}
