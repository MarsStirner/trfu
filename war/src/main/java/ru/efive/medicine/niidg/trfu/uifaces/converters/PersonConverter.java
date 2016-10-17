package ru.efive.medicine.niidg.trfu.uifaces.converters;

import org.apache.commons.lang.StringUtils;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.USER_DAO;

/**
 * Author: Upatov Egor <br>
 * Date: 16.01.2015, 17:46 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
@FacesConverter("PersonConverter")
public class PersonConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            SessionManagementBean sessionManagement = (SessionManagementBean) facesContext.getApplication()
                    .evaluateExpressionGet(facesContext, "#{sessionManagement}", SessionManagementBean.class);
            User result = sessionManagement.getDAO(UserDAOHibernate.class, USER_DAO).get(Integer.valueOf(value));
            if (result != null) {
                return result;
            } else {
                System.out.println("Не найдены значения");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        return String.valueOf(value == null ? "" : ((User) value).getId());
    }
}