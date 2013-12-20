
package ru.korusconsulting.pdmanager;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN101305UV02;
import org.hl7.v3.PRPAIN101306UV02;
import org.hl7.v3.PRPAIN101307UV02;
import org.hl7.v3.PRPAIN101308UV02;
import org.hl7.v3.PRPAIN101311UV02;
import org.hl7.v3.PRPAIN101312UV02;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "PDManager", targetNamespace = "http://www.korusconsulting.ru/PDManager/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PDManager {


    /**
     * 
     * @param parameters
     * @return
     *     returns org.hl7.v3.PRPAIN101312UV02
     */
    @WebMethod(action = "http://www.korusconsulting.ru/PDManager/new")
    @WebResult(name = "PRPA_IN101312UV02", targetNamespace = "urn:hl7-org:v3", partName = "result")
    public PRPAIN101312UV02 add(
        @WebParam(name = "PRPA_IN101311UV02", targetNamespace = "urn:hl7-org:v3", partName = "parameters")
        PRPAIN101311UV02 parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns org.hl7.v3.PRPAIN101306UV02
     */
    @WebMethod(action = "http://www.korusconsulting.ru/PDManager/findCandidates")
    @WebResult(name = "PRPA_IN101306UV02", targetNamespace = "urn:hl7-org:v3", partName = "result")
    public PRPAIN101306UV02 findCandidates(
        @WebParam(name = "PRPA_IN101305UV02", targetNamespace = "urn:hl7-org:v3", partName = "parameters")
        PRPAIN101305UV02 parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns org.hl7.v3.PRPAIN101308UV02
     */
    @WebMethod(action = "http://www.korusconsulting.ru/PDManager/getDemographics")
    @WebResult(name = "PRPA_IN101308UV02", targetNamespace = "urn:hl7-org:v3", partName = "result")
    public PRPAIN101308UV02 getDemographics(
        @WebParam(name = "PRPA_IN101307UV02", targetNamespace = "urn:hl7-org:v3", partName = "parameters")
        PRPAIN101307UV02 parameters);

}