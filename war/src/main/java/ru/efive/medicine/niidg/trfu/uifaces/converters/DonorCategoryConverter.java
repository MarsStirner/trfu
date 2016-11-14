package ru.efive.medicine.niidg.trfu.uifaces.converters;

/**
 * Author: Upatov Egor <br>
 * Date: 20.10.2016, 18:37 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.medicine.niidg.trfu.uifaces.beans.utils.MessageHolder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Map;

@FacesConverter("DonorCategoryConverter")
public class DonorCategoryConverter implements Converter{

    private static final Logger LOGGER = LoggerFactory.getLogger(DonorCategoryConverter.class);
    private static final Map<String, String> values = ImmutableMap.of("0", "Первичный", "1", "Кадровый", "2", "Повторный");

    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        LOGGER.error("Cannot convert ['{}']. UI[{}]", value, component.getClientId());
        return null;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        final String result = values.get(value.toString());
        if(StringUtils.isEmpty(result)) {
            FacesContext.getCurrentInstance().addMessage(null, MessageHolder.MSG_CONVERTER_ERROR);
            LOGGER.error("Cannot convert ['{}']. UI[{}]", value, component.getClientId());
        }
        return result;
    }
}
