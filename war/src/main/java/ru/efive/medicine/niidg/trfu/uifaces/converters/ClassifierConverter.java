package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.axis.utils.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@FacesConverter("ClassifierConverter")
public class ClassifierConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			List<Classifier> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByValue(Classifier.class, value);
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
		return value == null? "": ((Classifier) value).getValue();
	}
	
}