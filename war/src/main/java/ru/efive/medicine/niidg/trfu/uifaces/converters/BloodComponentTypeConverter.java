package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.axis.utils.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;

@FacesConverter("BloodComponentTypeConverter")
public class BloodComponentTypeConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			SessionManagementBean sessionManagement = (SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			List<BloodComponentType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, DICTIONARY_DAO).findComponentTypeByValue(value);
			if (list.size() > 0) {
				result = list.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value == null? "": ((BloodComponentType) value).getCode() + " " + ((BloodComponentType) value).getValue();
	}
	
}