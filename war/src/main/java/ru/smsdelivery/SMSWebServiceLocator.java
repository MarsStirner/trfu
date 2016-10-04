/**
 * SMSWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class SMSWebServiceLocator extends org.apache.axis.client.Service implements ru.smsdelivery.SMSWebService {

    public SMSWebServiceLocator() {
    }


    public SMSWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SMSWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SMSWebServiceSoap
    private java.lang.String SMSWebServiceSoap_address = "http://ws1.smsdelivery.ru/SMSWebservice.asmx";

    public java.lang.String getSMSWebServiceSoapAddress() {
        return SMSWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SMSWebServiceSoapWSDDServiceName = "SMSWebServiceSoap";

    public java.lang.String getSMSWebServiceSoapWSDDServiceName() {
        return SMSWebServiceSoapWSDDServiceName;
    }

    public void setSMSWebServiceSoapWSDDServiceName(java.lang.String name) {
        SMSWebServiceSoapWSDDServiceName = name;
    }

    public ru.smsdelivery.SMSWebServiceSoap getSMSWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SMSWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSMSWebServiceSoap(endpoint);
    }

    public ru.smsdelivery.SMSWebServiceSoap getSMSWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ru.smsdelivery.SMSWebServiceSoapStub _stub = new ru.smsdelivery.SMSWebServiceSoapStub(portAddress, this);
            _stub.setPortName(getSMSWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSMSWebServiceSoapEndpointAddress(java.lang.String address) {
        SMSWebServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ru.smsdelivery.SMSWebServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                ru.smsdelivery.SMSWebServiceSoapStub _stub = new ru.smsdelivery.SMSWebServiceSoapStub(new java.net.URL(SMSWebServiceSoap_address), this);
                _stub.setPortName(getSMSWebServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SMSWebServiceSoap".equals(inputPortName)) {
            return getSMSWebServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://smsdelivery.ru/", "SMSWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://smsdelivery.ru/", "SMSWebServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SMSWebServiceSoap".equals(portName)) {
            setSMSWebServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
