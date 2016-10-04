/**
 * GetStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class GetStatusResponse  implements java.io.Serializable {
    private ru.smsdelivery.ValidateEnum result;

    private ru.smsdelivery.StatusEnum messageStatus;

    public GetStatusResponse() {
    }

    public GetStatusResponse(
           ru.smsdelivery.ValidateEnum result,
           ru.smsdelivery.StatusEnum messageStatus) {
           this.result = result;
           this.messageStatus = messageStatus;
    }


    /**
     * Gets the result value for this GetStatusResponse.
     * 
     * @return result
     */
    public ru.smsdelivery.ValidateEnum getResult() {
        return result;
    }


    /**
     * Sets the result value for this GetStatusResponse.
     * 
     * @param result
     */
    public void setResult(ru.smsdelivery.ValidateEnum result) {
        this.result = result;
    }


    /**
     * Gets the messageStatus value for this GetStatusResponse.
     * 
     * @return messageStatus
     */
    public ru.smsdelivery.StatusEnum getMessageStatus() {
        return messageStatus;
    }


    /**
     * Sets the messageStatus value for this GetStatusResponse.
     * 
     * @param messageStatus
     */
    public void setMessageStatus(ru.smsdelivery.StatusEnum messageStatus) {
        this.messageStatus = messageStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetStatusResponse)) return false;
        GetStatusResponse other = (GetStatusResponse) obj;
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
            ((this.messageStatus==null && other.getMessageStatus()==null) || 
             (this.messageStatus!=null &&
              this.messageStatus.equals(other.getMessageStatus())));
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
        if (getMessageStatus() != null) {
            _hashCode += getMessageStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "GetStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "ValidateEnum"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "MessageStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "StatusEnum"));
        elemField.setNillable(false);
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
