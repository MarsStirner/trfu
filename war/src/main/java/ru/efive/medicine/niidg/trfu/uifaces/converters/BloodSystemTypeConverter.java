package ru.efive.medicine.niidg.trfu.uifaces.converters;

import org.apache.axis.utils.StringUtils;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.DICTIONARY_DAO;

/**
 * Author: Upatov Egor <br>
 * Date: 16.01.2015, 15:58 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
@FacesConverter("BloodSystemTypeConverter")
public class BloodSystemTypeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            SessionManagementBean sessionManagement = (SessionManagementBean) facesContext.getApplication()
                    .evaluateExpressionGet(facesContext, "#{sessionManagement}", SessionManagementBean.class);
            List<BloodSystemType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, DICTIONARY_DAO).findByValue(BloodSystemType.class, value);
            if (list.size() > 0) {
                return list.get(0);
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
        return String.valueOf(value == null ? "" : ((BloodSystemType) value).getValue());
    }
}
