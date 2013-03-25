package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimePropertyConverter extends PropertyConverter {
    private static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("HH:mm:ss zzz", Locale.ENGLISH);
    }
    @Override
    public Object getAsObject(String s) throws ParseException {
        if (!s.endsWith(" "))
            s += " ";
        return formatter.parse(s);
    }

    @Override
    public String getAsString(Object o) {
        return formatter.format(o);
    }
}
