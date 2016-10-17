package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.axis.utils.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;

@FacesConverter("BloodGroupConverter")
public class BloodGroupConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			List<BloodGroup> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, DICTIONARY_DAO).findByValue(BloodGroup.class, value);
			if (list.size() > 0) {
				result = list.get(0);
			}
			else {
				System.out.println("Не найдены значения");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value == null? "": ((BloodGroup) value).getValue();
	}
	
}