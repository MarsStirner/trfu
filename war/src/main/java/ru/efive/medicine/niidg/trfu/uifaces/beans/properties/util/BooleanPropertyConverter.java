package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;

public class BooleanPropertyConverter extends PropertyConverter {
    @Override
    public Object getAsObject(String s) {
        return Boolean.parseBoolean(s);
    }
}
