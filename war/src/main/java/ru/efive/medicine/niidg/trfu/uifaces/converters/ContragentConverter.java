package ru.efive.medicine.niidg.trfu.uifaces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.axis.utils.StringUtils;

import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@FacesConverter("ContragentConverter")
public class ContragentConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Object result = null;
		try {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			Contragent contragent = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO).getByFullName(value);
			if (contragent == null) {
				System.out.println("Не найдены значения");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value == null? "": ((Contragent) value).getFullName();
	}
	
}