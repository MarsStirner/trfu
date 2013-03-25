/**
 * ValidateEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class ValidateEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ValidateEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OK = "OK";
    public static final java.lang.String _InvalidCredentials = "InvalidCredentials";
    public static final java.lang.String _UserDisabled = "UserDisabled";
    public static final java.lang.String _InvalidFlashMessage = "InvalidFlashMessage";
    public static final java.lang.String _InvalidSenderAddress = "InvalidSenderAddress";
    public static final java.lang.String _InvalidReceiverAddress = "InvalidReceiverAddress";
    public static final java.lang.String _EmptyMessage = "EmptyMessage";
    public static final java.lang.String _TooLongMessage = "TooLongMessage";
    public static final java.lang.String _MessageBlocked = "MessageBlocked";
    public static final java.lang.String _InvalidBalance = "InvalidBalance";
    public static final java.lang.String _DatabaseOffline = "DatabaseOffline";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _Error = "Error";
    public static final ValidateEnum OK = new ValidateEnum(_OK);
    public static final ValidateEnum InvalidCredentials = new ValidateEnum(_InvalidCredentials);
    public static final ValidateEnum UserDisabled = new ValidateEnum(_UserDisabled);
    public static final ValidateEnum InvalidFlashMessage = new ValidateEnum(_InvalidFlashMessage);
    public static final ValidateEnum InvalidSenderAddress = new ValidateEnum(_InvalidSenderAddress);
    public static final ValidateEnum InvalidReceiverAddress = new ValidateEnum(_InvalidReceiverAddress);
    public static final ValidateEnum EmptyMessage = new ValidateEnum(_EmptyMessage);
    public static final ValidateEnum TooLongMessage = new ValidateEnum(_TooLongMessage);
    public static final ValidateEnum MessageBlocked = new ValidateEnum(_MessageBlocked);
    public static final ValidateEnum InvalidBalance = new ValidateEnum(_InvalidBalance);
    public static final ValidateEnum DatabaseOffline = new ValidateEnum(_DatabaseOffline);
    public static final ValidateEnum Unknown = new ValidateEnum(_Unknown);
    public static final ValidateEnum Error = new ValidateEnum(_Error);
    public java.lang.String getValue() { return _value_;}
    public static ValidateEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ValidateEnum enumeration = (ValidateEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ValidateEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ValidateEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "ValidateEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
