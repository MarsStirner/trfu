package ru.efive.medicine.niidg.trfu.uifaces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang.StringUtils;

@FacesConverter("FieldTypeConverter")
public class FieldTypeConverter implements Converter {
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (StringUtils.isEmpty(value)) {
				result = null;
			}
			else if (value.equals("Text")) {
				result = 0;
			}
			else if (value.equals("Textarea")) {
				result = 1;
			}
			else if (value.equals("Drop-down list")) {
				result = 3;
			}
			else if (value.equals("Radio button")) {
				result = 4;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String result = "";
		if (value != null && value instanceof Integer) {
			Integer intval = (Integer) value;
			switch (intval) {
				case 0:
					result = "Text";
					break;
				case 1:
					result = "Textarea";
					break;
				case 3:
					result = "Drop-down list";
					break;
				case 4:
					result = "Radio button";
					break;
			}
		}
		return result;
	}
	
}