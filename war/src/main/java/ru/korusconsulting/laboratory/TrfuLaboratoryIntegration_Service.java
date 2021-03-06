
package ru.korusconsulting.laboratory;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "trfu-laboratory-integration", targetNamespace = "http://www.korusconsulting.ru", wsdlLocation = "http://test3.gfish.fccho-moscow.ru/trfu-laboratory-integration?wsdl")
public class TrfuLaboratoryIntegration_Service
    extends Service
{

    private final static URL TRFULABORATORYINTEGRATION_WSDL_LOCATION;
    private final static WebServiceException TRFULABORATORYINTEGRATION_EXCEPTION;
    private final static QName TRFULABORATORYINTEGRATION_QNAME = new QName("http://www.korusconsulting.ru", "trfu-laboratory-integration");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://test3.gfish.fccho-moscow.ru/trfu-laboratory-integration?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TRFULABORATORYINTEGRATION_WSDL_LOCATION = url;
        TRFULABORATORYINTEGRATION_EXCEPTION = e;
    }

    public TrfuLaboratoryIntegration_Service() {
        super(__getWsdlLocation(), TRFULABORATORYINTEGRATION_QNAME);
    }

    public TrfuLaboratoryIntegration_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), TRFULABORATORYINTEGRATION_QNAME, features);
    }

    public TrfuLaboratoryIntegration_Service(URL wsdlLocation) {
        super(wsdlLocation, TRFULABORATORYINTEGRATION_QNAME);
    }

    public TrfuLaboratoryIntegration_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TRFULABORATORYINTEGRATION_QNAME, features);
    }

    public TrfuLaboratoryIntegration_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TrfuLaboratoryIntegration_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TrfuLaboratoryIntegration
     */
    @WebEndpoint(name = "trfu-laboratory-integration")
    public TrfuLaboratoryIntegration getTrfuLaboratoryIntegration() {
        return super.getPort(new QName("http://www.korusconsulting.ru", "trfu-laboratory-integration"), TrfuLaboratoryIntegration.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TrfuLaboratoryIntegration
     */
    @WebEndpoint(name = "trfu-laboratory-integration")
    public TrfuLaboratoryIntegration getTrfuLaboratoryIntegration(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.korusconsulting.ru", "trfu-laboratory-integration"), TrfuLaboratoryIntegration.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TRFULABORATORYINTEGRATION_EXCEPTION!= null) {
            throw TRFULABORATORYINTEGRATION_EXCEPTION;
        }
        return TRFULABORATORYINTEGRATION_WSDL_LOCATION;
    }

}
