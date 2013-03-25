package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;


public class DoublePropertyConverter extends PropertyConverter {
    @Override
    public Object getAsObject(String s) {
        return Double.parseDouble(s);
    }
}
