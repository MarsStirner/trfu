/**
 * StatusEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class StatusEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StatusEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _EnQueue = "EnQueue";
    public static final java.lang.String _EnRoute = "EnRoute";
    public static final java.lang.String _Delivered = "Delivered";
    public static final java.lang.String _Deleted = "Deleted";
    public static final java.lang.String _Expired = "Expired";
    public static final java.lang.String _Rejected = "Rejected";
    public static final java.lang.String _UnDeliverable = "UnDeliverable";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _Error = "Error";
    public static final java.lang.String _Blocked = "Blocked";
    public static final StatusEnum EnQueue = new StatusEnum(_EnQueue);
    public static final StatusEnum EnRoute = new StatusEnum(_EnRoute);
    public static final StatusEnum Delivered = new StatusEnum(_Delivered);
    public static final StatusEnum Deleted = new StatusEnum(_Deleted);
    public static final StatusEnum Expired = new StatusEnum(_Expired);
    public static final StatusEnum Rejected = new StatusEnum(_Rejected);
    public static final StatusEnum UnDeliverable = new StatusEnum(_UnDeliverable);
    public static final StatusEnum Unknown = new StatusEnum(_Unknown);
    public static final StatusEnum Error = new StatusEnum(_Error);
    public static final StatusEnum Blocked = new StatusEnum(_Blocked);
    public java.lang.String getValue() { return _value_;}
    public static StatusEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StatusEnum enumeration = (StatusEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StatusEnum fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StatusEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "StatusEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
