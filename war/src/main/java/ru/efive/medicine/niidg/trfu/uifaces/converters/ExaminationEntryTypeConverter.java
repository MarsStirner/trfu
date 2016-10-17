package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationEntryType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;

@FacesConverter("ExaminationEntryTypeConverter")
public class ExaminationEntryTypeConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (value == null || value.equals("")) {
				return null;
			}
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			List<ExaminationEntryType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, DICTIONARY_DAO).findByValue(ExaminationEntryType.class, value);
			if (list.size() > 0) {
				result = list.get(0);
			}
			else {
				System.out.println("ExaminationEntryTypeConverter - Не найдены значения");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value == null? "": ((ExaminationEntryType) value).getValue();
	}
	
}