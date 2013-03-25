/**
 * GetSenderAddressesResp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class GetSenderAddressesResp  implements java.io.Serializable {
    private ru.smsdelivery.SenderAddressResultEnum result;

    private java.lang.String[] addresses;

    public GetSenderAddressesResp() {
    }

    public GetSenderAddressesResp(
           ru.smsdelivery.SenderAddressResultEnum result,
           java.lang.String[] addresses) {
           this.result = result;
           this.addresses = addresses;
    }


    /**
     * Gets the result value for this GetSenderAddressesResp.
     * 
     * @return result
     */
    public ru.smsdelivery.SenderAddressResultEnum getResult() {
        return result;
    }


    /**
     * Sets the result value for this GetSenderAddressesResp.
     * 
     * @param result
     */
    public void setResult(ru.smsdelivery.SenderAddressResultEnum result) {
        this.result = result;
    }


    /**
     * Gets the addresses value for this GetSenderAddressesResp.
     * 
     * @return addresses
     */
    public java.lang.String[] getAddresses() {
        return addresses;
    }


    /**
     * Sets the addresses value for this GetSenderAddressesResp.
     * 
     * @param addresses
     */
    public void setAddresses(java.lang.String[] addresses) {
        this.addresses = addresses;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSenderAddressesResp)) return false;
        GetSenderAddressesResp other = (GetSenderAddressesResp) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              this.result.equals(other.getResult()))) &&
            ((this.addresses==null && other.getAddresses()==null) || 
             (this.addresses!=null &&
              java.util.Arrays.equals(this.addresses, other.getAddresses())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getResult() != null) {
            _hashCode += getResult().hashCode();
        }
        if (getAddresses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAddresses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAddresses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSenderAddressesResp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "GetSenderAddressesResp"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "SenderAddressResultEnum"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addresses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "Addresses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "string"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
