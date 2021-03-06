/**
 * OrderInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.korusconsulting.laboratory.www;

public class OrderInfo  implements java.io.Serializable {
    private int orderPriority;

    private Tindicator[] indicators;

    public OrderInfo() {
    }

    public OrderInfo(
           int orderPriority,
           Tindicator[] indicators) {
           this.orderPriority = orderPriority;
           this.indicators = indicators;
    }


    /**
     * Gets the orderPriority value for this OrderInfo.
     * 
     * @return orderPriority
     */
    public int getOrderPriority() {
        return orderPriority;
    }


    /**
     * Sets the orderPriority value for this OrderInfo.
     * 
     * @param orderPriority
     */
    public void setOrderPriority(int orderPriority) {
        this.orderPriority = orderPriority;
    }


    /**
     * Gets the indicators value for this OrderInfo.
     * 
     * @return indicators
     */
    public Tindicator[] getIndicators() {
        return indicators;
    }


    /**
     * Sets the indicators value for this OrderInfo.
     * 
     * @param indicators
     */
    public void setIndicators(Tindicator[] indicators) {
        this.indicators = indicators;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderInfo)) return false;
        OrderInfo other = (OrderInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.orderPriority == other.getOrderPriority() &&
            ((this.indicators==null && other.getIndicators()==null) || 
             (this.indicators!=null &&
              java.util.Arrays.equals(this.indicators, other.getIndicators())));
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
        _hashCode += getOrderPriority();
        if (getIndicators() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndicators());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndicators(), i);
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
        new org.apache.axis.description.TypeDesc(OrderInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.korusconsulting.ru", "OrderInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderPriority");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderPriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indicators");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indicators"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.korusconsulting.ru", "Tindicator"));
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
