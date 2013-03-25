package ru.efive.medicine.niidg.trfu.uifaces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Valiev
 * Date: 13.09.12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@FacesConverter("examinationDateConverter")
public class ExaminationTimeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return new Date(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return o.toString();
    }
}

