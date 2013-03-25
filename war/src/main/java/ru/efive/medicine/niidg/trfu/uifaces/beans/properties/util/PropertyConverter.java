package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;


import java.text.ParseException;

public abstract class PropertyConverter {
    public abstract Object getAsObject(String s) throws ParseException;

    public String getAsString(Object o){
        return o.toString();
    }
}
