package ru.korusconsulting.laboratory.www;

public class IAcrossIntf_FNKCProxy implements IAcrossIntf_FNKC {
  private String _endpoint = null;
  private IAcrossIntf_FNKC iAcrossIntf_FNKC = null;
  
  public IAcrossIntf_FNKCProxy() {
    _initIAcrossIntf_FNKCProxy();
  }
  
  public IAcrossIntf_FNKCProxy(String endpoint) {
    _endpoint = endpoint;
    _initIAcrossIntf_FNKCProxy();
  }
  
  private void _initIAcrossIntf_FNKCProxy() {
    try {
      iAcrossIntf_FNKC = (new IAcrossIntf_FNKCserviceLocator()).getIAcrossIntf_FNKCPort();
      if (iAcrossIntf_FNKC != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iAcrossIntf_FNKC)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iAcrossIntf_FNKC)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iAcrossIntf_FNKC != null)
      ((javax.xml.rpc.Stub)iAcrossIntf_FNKC)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public IAcrossIntf_FNKC getIAcrossIntf_FNKC() {
    if (iAcrossIntf_FNKC == null)
      _initIAcrossIntf_FNKCProxy();
    return iAcrossIntf_FNKC;
  }
  
  public int queryAnalysis(DonorInfo donorInfo, DiagnosticRequestInfo diagnosticRequestInfo, BiomaterialInfo biomaterialInfo, OrderInfo orderInfo) throws java.rmi.RemoteException{
    if (iAcrossIntf_FNKC == null)
      _initIAcrossIntf_FNKCProxy();
    return iAcrossIntf_FNKC.queryAnalysis(donorInfo, diagnosticRequestInfo, biomaterialInfo, orderInfo);
  }
  
  
}