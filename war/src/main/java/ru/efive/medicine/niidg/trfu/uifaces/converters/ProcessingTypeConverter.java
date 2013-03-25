package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.ProcessingType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@FacesConverter("ProcessingTypeConverter")
public class ProcessingTypeConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			List<ProcessingType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByValue(ProcessingType.class, value);
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
		return ((ProcessingType) value).getValue();
	}
	
}