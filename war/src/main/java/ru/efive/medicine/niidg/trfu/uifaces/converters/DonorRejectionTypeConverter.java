package ru.efive.medicine.niidg.trfu.uifaces.converters;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorRejectionType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;

@FacesConverter("DonorRejectionTypeConverter")
public class DonorRejectionTypeConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			List<DonorRejectionType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, DICTIONARY_DAO).findByValue(DonorRejectionType.class, value);
			if (list.size() > 0) {
				result = list.get(0);
			}
			else {
				System.out.println("DonorTypeConverter - Не найдены значения");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((DonorRejectionType) value).getValue();
	}
	
}