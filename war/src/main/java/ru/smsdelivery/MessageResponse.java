/**
 * MessageResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class MessageResponse  implements java.io.Serializable {
    private ru.smsdelivery.ValidateEnum result;

    private int messageID;

    private int segmentsNumber;

    public MessageResponse() {
    }

    public MessageResponse(
           ru.smsdelivery.ValidateEnum result,
           int messageID,
           int segmentsNumber) {
           this.result = result;
           this.messageID = messageID;
           this.segmentsNumber = segmentsNumber;
    }


    /**
     * Gets the result value for this MessageResponse.
     * 
     * @return result
     */
    public ru.smsdelivery.ValidateEnum getResult() {
        return result;
    }


    /**
     * Sets the result value for this MessageResponse.
     * 
     * @param result
     */
    public void setResult(ru.smsdelivery.ValidateEnum result) {
        this.result = result;
    }


    /**
     * Gets the messageID value for this MessageResponse.
     * 
     * @return messageID
     */
    public int getMessageID() {
        return messageID;
    }


    /**
     * Sets the messageID value for this MessageResponse.
     * 
     * @param messageID
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }


    /**
     * Gets the segmentsNumber value for this MessageResponse.
     * 
     * @return segmentsNumber
     */
    public int getSegmentsNumber() {
        return segmentsNumber;
    }


    /**
     * Sets the segmentsNumber value for this MessageResponse.
     * 
     * @param segmentsNumber
     */
    public void setSegmentsNumber(int segmentsNumber) {
        this.segmentsNumber = segmentsNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageResponse)) return false;
        MessageResponse other = (MessageResponse) obj;
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
            this.messageID == other.getMessageID() &&
            this.segmentsNumber == other.getSegmentsNumber();
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
        _hashCode += getMessageID();
        _hashCode += getSegmentsNumber();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MessageResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "MessageResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "ValidateEnum"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "MessageID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segmentsNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://smsdelivery.ru/", "SegmentsNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
