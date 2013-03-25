package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;

public class LongPropertyConverter extends PropertyConverter {
    @Override
    public Object getAsObject(String s) {
        return Long.parseLong(s);
    }
}
