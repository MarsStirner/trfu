package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;

public class IntegerPropertyConverter extends PropertyConverter {
    @Override
    public Object getAsObject(String s) {
        return Integer.parseInt(s);
    }
}
