/**
 * SMSWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public interface SMSWebService extends javax.xml.rpc.Service {
    public java.lang.String getSMSWebServiceSoapAddress();

    public ru.smsdelivery.SMSWebServiceSoap getSMSWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public ru.smsdelivery.SMSWebServiceSoap getSMSWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
