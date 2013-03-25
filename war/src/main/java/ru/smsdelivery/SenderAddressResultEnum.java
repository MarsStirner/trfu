/**
 * SenderAddressResultEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public class SenderAddressResultEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SenderAddressResultEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OK = "OK";
    public static final java.lang.String _InvalidCredentials = "InvalidCredentials";
    public static final java.lang.String _UserDisabled = "UserDisabled";
    public static final java.lang.String _DuplicateSenderAddress = "DuplicateSenderAddress";
    public static final java.lang.String _WrongSenderAddress = "WrongSenderAddress";
    public static final java.lang.String _DatabaseOffline = "DatabaseOffline";
    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _Error = "Error";
    public static final SenderAddressResultEnum OK = new SenderAddressResultEnum(_OK);
    public static final SenderAddressResultEnum InvalidCredentials = new SenderAddressResultEnum(_InvalidCredentials);
    public static final SenderAddressResultEnum UserDisabled = new SenderAddressResultEnum(_UserDisabled);
    public static final SenderAddressResultEnum DuplicateSenderAddress = new SenderAddressResultEnum(_DuplicateSenderAddress);
    public static final SenderAddressResultEnum WrongSenderAddress = new SenderAddressResultEnum(_WrongSenderAddress);
    public static final SenderAddressResultEnum DatabaseOffline = new SenderAddressResultEnum(_DatabaseOffline);
    public static final SenderAddressResultEnum Unknown = new SenderAddressResultEnum(_Unknown);
    public static final SenderAddressResultEnum Error = new SenderAddressResultEnum(_Error);
    public java.lang.String getValue() { return _value_;}
    public static SenderAddressResultEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        SenderAddressResultEnum enumeration = (SenderAddressResultEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static SenderAddressResultEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(SenderAddressResultEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://smsdelivery.ru/", "SenderAddressResultEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
