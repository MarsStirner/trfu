package ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util;

/**
 * Created with IntelliJ IDEA.
 * User: Valiev
 * Date: 19.09.12
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class StringPropertyConverter extends PropertyConverter {
    @Override
    public Object getAsObject(String s) {
        return s;
    }
}
