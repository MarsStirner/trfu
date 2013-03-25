package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;



public class PropertyConverterFactory {
    public static PropertyConverter getConverter(String type) throws PropertyTypeNotSupported {
        if (type.equals("String"))
            return new StringPropertyConverter();
        if (type.equals("Integer"))
            return new IntegerPropertyConverter();
        if (type.equals("Long"))
            return new LongPropertyConverter();
        if (type.equals("Boolean"))
            return new BooleanPropertyConverter();
        if (type.equals("Double"))
            return new DoublePropertyConverter();
        if (type.equals("Date"))
            return new DatePropertyConverter();
        if (type.equals("Time"))
            return new TimePropertyConverter();
        if (type.equals("DateTime"))
            return new DateTimePropertyConverter();
        if (type.equals("Password"))
            return new PasswordPropertyConverter();

        throw new PropertyTypeNotSupported();
    }
}
