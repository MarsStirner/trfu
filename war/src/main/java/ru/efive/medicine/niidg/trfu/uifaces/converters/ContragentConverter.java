package ru.efive.medicine.niidg.trfu.uifaces.converters;

import org.apache.axis.utils.StringUtils;
import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.CONTRAGENT_DAO;
@FacesConverter("ContragentConverter")
public class ContragentConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			SessionManagementBean sessionManagement =  context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
						SessionManagementBean.class);
			final Contragent contragent = sessionManagement.getDAO(ContragentDAOHibernate.class, CONTRAGENT_DAO).getByFullName(value);
			if (contragent == null) {
				System.out.println("Не найдены значения");
			}  else {
				return contragent;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value == null? "": ((Contragent) value).getFullName();
	}
	
}