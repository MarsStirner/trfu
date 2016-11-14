package ru.efive.medicine.niidg.trfu.uifaces.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.dao.sql.dao.user.RoleDAOHibernate;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.utils.MessageHolder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.ROLE_DAO;

@FacesConverter("RoleConverter")
public class RoleConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleConverter.class);

	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		try {
			final SessionManagementBean sessionManagement = context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}",
                    SessionManagementBean.class);
			final List<Role> list = sessionManagement.getDAO(RoleDAOHibernate.class, ROLE_DAO).findByValue(value);
			if(list.iterator().hasNext()){
				return list.iterator().next();
			} else {
				FacesContext.getCurrentInstance().addMessage(null, MessageHolder.MSG_CONVERTER_ERROR);
				LOGGER.error("Cannot convert ['{}'] to Role. UI[{}]", value, component.getClientId());
			}
		} catch (Exception e) {
			LOGGER.error("Cannot convert ['{}'] to Role. UI[{}]", value, component.getClientId(), e);
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return ((Role) value).getName();
	}
	
}