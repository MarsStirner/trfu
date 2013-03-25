package ru.efive.medicine.niidg.trfu.uifaces.beans.properties;

import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyConverter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyConverterFactory;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyTypeNotSupported;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.text.ParseException;
import java.util.regex.Pattern;

public class ExtendedProperty {
    private String alias;
    private String name;
    private Object objectValue;
    private String stringValue;
    private String type;
    private String pattern;
    private Pattern p;
    private PropertyConverter converter;
    private Boolean isSimple;


    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (pattern != null  && !pattern.isEmpty()) {
            if(!p.matcher((String)value).matches()){
                ((UIInput)component).setValid(false);
                FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary("Ошибка: значение не удовлетворяет шаблону");
                message.setDetail("Ошибка: значение не удовлетворяет шаблону");
                context.addMessage(component.getClientId(context), message);
                throw new ValidatorException(message);
            }
        }
    }

    public Boolean getSimple() {
        return isSimple;
    }

    public void setSimple(Boolean simple) {
        isSimple = simple;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
        if (converter != null)
            stringValue = converter.getAsString(objectValue);
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) throws ParseException {
        this.stringValue = stringValue;
        if (converter != null)
            objectValue = converter.getAsObject(stringValue);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws PropertyTypeNotSupported, ParseException {
        this.type = type;
        converter = PropertyConverterFactory.getConverter(type);
        if (stringValue!=null)
            objectValue = converter.getAsObject(stringValue);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        p = Pattern.compile(pattern);
    }

    public String getAlias() {
        if (alias!=null)
            return alias;
        return name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
