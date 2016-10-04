/**
 * GetStatus2Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class GetStatus2Response  implements java.io.Serializable {
    private ru.smsdelivery.ValidateEnum result;

    private ru.smsdelivery.StatusEnum messageStatus;

    private java.util.Calendar messageDeliveryTime;

    public GetStatus2Response() {
    }

    public GetStatus2Response(
           ru.smsdelivery.ValidateEnum result,
           ru.smsdelivery.StatusEnum messageStatus,
           java.util.Calendar messageDeliveryTime) {
           this.result = result;
           this.messageStatus = messageStatus;
           this.messageDeliveryTime = messageDeliveryTime;
    }


    /**
     * Gets the result value for this GetStatus2Response.
     * 
     * @return result
     */
    public ru.smsdelivery.ValidateEnum getResult() {
        return result;
    }


    /**
     * Sets the result value for this GetStatus2Response.
     * 
     * @param result
     */
    public void setResult(ru.smsdelivery.ValidateEnum result) {
        this.result = result;
    }


    /**
     * Gets the messageStatus value for this GetStatus2Response.
     * 
     * @return messageStatus
     */
    public ru.smsdelivery.StatusEnum getMessageStatus() {
        return messageStatus;
    }


    /**
     * Sets the messageStatus value for this GetStatus2Response.
     * 
     * @param messageStatus
     */
    public void setMessageStatus(ru.smsdelivery.StatusEnum messageStatus) {
        this.messageStatus = messageStatus;
    }


    /**
     * Gets the messageDeliveryTime value for this GetStatus2Response.
     * 
     * @return messageDeliveryTime
     */
    public java.util.Calendar getMessageDeliveryTime() {
        return messageDeliveryTime;
    }


    /**
     * Sets the messageDeliveryTime value for this GetStatus2Response.
     * 
     * @param messageDeliveryTime
     */
    public void setMessageDeliveryTime(java.util.Calendar messageDeliveryTime) {
        this.messageDeliveryTime = messageDeliveryTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetStatus2Response)) return false;
        GetStatus2Response other = (GetStatus2Response) obj;
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
              this.messageStatus.equals(other.getMessageStatus()))) &&
            ((this.messageDeliveryTime==null && other.getMessageDeliveryTime()==null) || 
             (this.messageDeliveryTime!=null &&
              this.messageDeliveryTime.equals(other.getMessageDeliveryTime())));
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
        if (getMessageDeliveryTime() != null) {
            _hashCode += getMessageDeliveryTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetStatus2Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "GetStatus2Response"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageDeliveryTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "MessageDeliveryTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
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
