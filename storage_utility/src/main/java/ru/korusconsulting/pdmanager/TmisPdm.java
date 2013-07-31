
package ru.korusconsulting.pdmanager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "tmis-pdm", targetNamespace = "http://www.korusconsulting.ru/PDManager/", wsdlLocation = "http://84.204.44.36:7009/pdm-war/tmis-pdm?wsdl")
public class TmisPdm
    extends Service
{

    private final static URL TMISPDM_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(ru.korusconsulting.pdmanager.TmisPdm.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = ru.korusconsulting.pdmanager.TmisPdm.class.getResource(".");
            url = new URL(baseUrl, "http://84.204.44.36:7009/pdm-war/tmis-pdm?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://84.204.44.36:7009/pdm-war/tmis-pdm?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        TMISPDM_WSDL_LOCATION = url;
    }

    public TmisPdm(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TmisPdm() {
        super(TMISPDM_WSDL_LOCATION, new QName("http://www.korusconsulting.ru/PDManager/", "tmis-pdm"));
    }

    /**
     * 
     * @return
     *     returns PDManager
     */
    @WebEndpoint(name = "portPdm")
    public PDManager getPortPdm() {
        return super.getPort(new QName("http://www.korusconsulting.ru/PDManager/", "portPdm"), PDManager.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PDManager
     */
    @WebEndpoint(name = "portPdm")
    public PDManager getPortPdm(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.korusconsulting.ru/PDManager/", "portPdm"), PDManager.class, features);
    }

}
