package ru.smsdelivery;

public class SMSWebServiceSoapProxy implements ru.smsdelivery.SMSWebServiceSoap {
  private String _endpoint = null;
  private ru.smsdelivery.SMSWebServiceSoap sMSWebServiceSoap = null;
  
  public SMSWebServiceSoapProxy() {
    _initSMSWebServiceSoapProxy();
  }
  
  public SMSWebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSMSWebServiceSoapProxy();
  }
  
  private void _initSMSWebServiceSoapProxy() {
    try {
      sMSWebServiceSoap = (new ru.smsdelivery.SMSWebServiceLocator()).getSMSWebServiceSoap();
      if (sMSWebServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sMSWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sMSWebServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sMSWebServiceSoap != null)
      ((javax.xml.rpc.Stub)sMSWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ru.smsdelivery.SMSWebServiceSoap getSMSWebServiceSoap() {
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap;
  }
  
  public ru.smsdelivery.GetUserBalanceResponse getBalance(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.getBalance(userName, password);
  }
  
  public ru.smsdelivery.GetStatusResponse getMessageStatus(java.lang.String userName, java.lang.String password, int messageID) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.getMessageStatus(userName, password, messageID);
  }
  
  public ru.smsdelivery.GetStatus2Response getMessageStatus2(java.lang.String userName, java.lang.String password, int messageID) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.getMessageStatus2(userName, password, messageID);
  }
  
  public ru.smsdelivery.MessageResponse sendMessage(java.lang.String userName, java.lang.String password, boolean isFlash, int lifeTime, java.lang.String destNumber, java.lang.String senderAddr, java.lang.String text) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.sendMessage(userName, password, isFlash, lifeTime, destNumber, senderAddr, text);
  }
  
  public ru.smsdelivery.GetSenderAddressesResp getSenderAddresses(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.getSenderAddresses(userName, password);
  }
  
  public ru.smsdelivery.AddSenderAddressWithModerationResp addSenderAddressWithModeration(java.lang.String userName, java.lang.String password, java.lang.String address) throws java.rmi.RemoteException{
    if (sMSWebServiceSoap == null)
      _initSMSWebServiceSoapProxy();
    return sMSWebServiceSoap.addSenderAddressWithModeration(userName, password, address);
  }
  
  
}