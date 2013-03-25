/**
 * DiagnosticRequestInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.korusconsulting.laboratory.www;

public class DiagnosticRequestInfo  implements java.io.Serializable {
    private int orderId;

    private java.util.Calendar orderDate;

    private java.lang.String orderPhysicianLastName;

    private java.lang.String orderPhysicianFirstName;

    private java.lang.String orderPhysicianMiddleName;

    private java.lang.String orderPhysicianId;

    public DiagnosticRequestInfo() {
    }

    public DiagnosticRequestInfo(
           int orderId,
           java.util.Calendar orderDate,
           java.lang.String orderPhysicianLastName,
           java.lang.String orderPhysicianFirstName,
           java.lang.String orderPhysicianMiddleName,
           java.lang.String orderPhysicianId) {
           this.orderId = orderId;
           this.orderDate = orderDate;
           this.orderPhysicianLastName = orderPhysicianLastName;
           this.orderPhysicianFirstName = orderPhysicianFirstName;
           this.orderPhysicianMiddleName = orderPhysicianMiddleName;
           this.orderPhysicianId = orderPhysicianId;
    }


    /**
     * Gets the orderId value for this DiagnosticRequestInfo.
     * 
     * @return orderId
     */
    public int getOrderId() {
        return orderId;
    }


    /**
     * Sets the orderId value for this DiagnosticRequestInfo.
     * 
     * @param orderId
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    /**
     * Gets the orderDate value for this DiagnosticRequestInfo.
     * 
     * @return orderDate
     */
    public java.util.Calendar getOrderDate() {
        return orderDate;
    }


    /**
     * Sets the orderDate value for this DiagnosticRequestInfo.
     * 
     * @param orderDate
     */
    public void setOrderDate(java.util.Calendar orderDate) {
        this.orderDate = orderDate;
    }


    /**
     * Gets the orderPhysicianLastName value for this DiagnosticRequestInfo.
     * 
     * @return orderPhysicianLastName
     */
    public java.lang.String getOrderPhysicianLastName() {
        return orderPhysicianLastName;
    }


    /**
     * Sets the orderPhysicianLastName value for this DiagnosticRequestInfo.
     * 
     * @param orderPhysicianLastName
     */
    public void setOrderPhysicianLastName(java.lang.String orderPhysicianLastName) {
        this.orderPhysicianLastName = orderPhysicianLastName;
    }


    /**
     * Gets the orderPhysicianFirstName value for this DiagnosticRequestInfo.
     * 
     * @return orderPhysicianFirstName
     */
    public java.lang.String getOrderPhysicianFirstName() {
        return orderPhysicianFirstName;
    }


    /**
     * Sets the orderPhysicianFirstName value for this DiagnosticRequestInfo.
     * 
     * @param orderPhysicianFirstName
     */
    public void setOrderPhysicianFirstName(java.lang.String orderPhysicianFirstName) {
        this.orderPhysicianFirstName = orderPhysicianFirstName;
    }


    /**
     * Gets the orderPhysicianMiddleName value for this DiagnosticRequestInfo.
     * 
     * @return orderPhysicianMiddleName
     */
    public java.lang.String getOrderPhysicianMiddleName() {
        return orderPhysicianMiddleName;
    }


    /**
     * Sets the orderPhysicianMiddleName value for this DiagnosticRequestInfo.
     * 
     * @param orderPhysicianMiddleName
     */
    public void setOrderPhysicianMiddleName(java.lang.String orderPhysicianMiddleName) {
        this.orderPhysicianMiddleName = orderPhysicianMiddleName;
    }


    /**
     * Gets the orderPhysicianId value for this DiagnosticRequestInfo.
     * 
     * @return orderPhysicianId
     */
    public java.lang.String getOrderPhysicianId() {
        return orderPhysicianId;
    }


    /**
     * Sets the orderPhysicianId value for this DiagnosticRequestInfo.
     * 
     * @param orderPhysicianId
     */
    public void setOrderPhysicianId(java.lang.String orderPhysicianId) {
        this.orderPhysicianId = orderPhysicianId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DiagnosticRequestInfo)) return false;
        DiagnosticRequestInfo other = (DiagnosticRequestInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.orderId == other.getOrderId() &&
            ((this.orderDate==null && other.getOrderDate()==null) || 
             (this.orderDate!=null &&
              this.orderDate.equals(other.getOrderDate()))) &&
            ((this.orderPhysicianLastName==null && other.getOrderPhysicianLastName()==null) || 
             (this.orderPhysicianLastName!=null &&
              this.orderPhysicianLastName.equals(other.getOrderPhysicianLastName()))) &&
            ((this.orderPhysicianFirstName==null && other.getOrderPhysicianFirstName()==null) || 
             (this.orderPhysicianFirstName!=null &&
              this.orderPhysicianFirstName.equals(other.getOrderPhysicianFirstName()))) &&
            ((this.orderPhysicianMiddleName==null && other.getOrderPhysicianMiddleName()==null) || 
             (this.orderPhysicianMiddleName!=null &&
              this.orderPhysicianMiddleName.equals(other.getOrderPhysicianMiddleName()))) &&
            ((this.orderPhysicianId==null && other.getOrderPhysicianId()==null) || 
             (this.orderPhysicianId!=null &&
              this.orderPhysicianId.equals(other.getOrderPhysicianId())));
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
        _hashCode += getOrderId();
        if (getOrderDate() != null) {
            _hashCode += getOrderDate().hashCode();
        }
        if (getOrderPhysicianLastName() != null) {
            _hashCode += getOrderPhysicianLastName().hashCode();
        }
        if (getOrderPhysicianFirstName() != null) {
            _hashCode += getOrderPhysicianFirstName().hashCode();
        }
        if (getOrderPhysicianMiddleName() != null) {
            _hashCode += getOrderPhysicianMiddleName().hashCode();
        }
        if (getOrderPhysicianId() != null) {
            _hashCode += getOrderPhysicianId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DiagnosticRequestInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.korusconsulting.ru", "DiagnosticRequestInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderPhysicianLastName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderPhysicianLastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderPhysicianFirstName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderPhysicianFirstName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderPhysicianMiddleName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderPhysicianMiddleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderPhysicianId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderPhysicianId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
