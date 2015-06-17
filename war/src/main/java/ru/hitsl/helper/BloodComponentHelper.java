package ru.hitsl.helper;

import org.apache.commons.lang.StringUtils;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Author: Upatov Egor <br>
 * Date: 16.06.2015, 20:31 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
public class BloodComponentHelper {
    private static final String PRINT_MESSAGE_TAG = "print";
    private static final String PRINT_MESSAGE_TITLE = "Ошибка при печати";

    public static void addMessage(final String tag, final FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(tag, message);
    }

    public static boolean validateBeforePrint(final BloodComponent document) {
        boolean result = true;
        if(document.isDeleted()){
            addMessage(
                    PRINT_MESSAGE_TAG, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, PRINT_MESSAGE_TITLE, "Нельзя печатать этикетку удаленного компонента крови"
                    )
            );
            result = false;
        }
        //Номер донации (parentNumber) должен содержать от 5 до 6 символов включительно
        if (StringUtils.isEmpty(document.getParentNumber()) || document.getParentNumber().length() < 5 || document.getParentNumber().length() > 6) {
            addMessage(
                    PRINT_MESSAGE_TAG, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,  PRINT_MESSAGE_TITLE,
                            String.format(
                                    "Номер донации должен содержать 5 либо 6 символов, у этого компонента %d символ(-а\\ов) \'%s\'",
                                    StringUtils.length(document.getParentNumber()),
                                    document.getParentNumber()
                            )
                    )
            );
            result = false;
        }
        //Номер КК (number) должен содержать 2 символа
        if (StringUtils.isEmpty(document.getNumber()) || document.getNumber().length() != 2) {
            addMessage(
                    PRINT_MESSAGE_TAG, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,  PRINT_MESSAGE_TITLE,
                            String.format(
                                    "Номер компонента должен содержать 2 символа, у этого компонента %d символ(-а\\ов) \'%s\'",
                                    StringUtils.length(document.getNumber()),
                                    document.getNumber()
                            )
                    )
            );
            result = false;
        }
        if (document.getBloodGroup() == null || document.getBloodGroup().getNumber() == 0) {
            addMessage(
                    PRINT_MESSAGE_TAG, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,  PRINT_MESSAGE_TITLE,
                            "Группа крови должна быть указана, не допускается использование \"Не определено\" при печати"
                    )
            );
            result = false;
        }
        if (document.getRhesusFactor() == null || StringUtils.containsIgnoreCase(document.getRhesusFactor().getValue(), "Не определено")) {
            addMessage(
                    PRINT_MESSAGE_TAG, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            PRINT_MESSAGE_TITLE,
                            "Резус-фактор должен быть указан, не допускается использование \"Не определено\" при печати"
                    )
            );
            result = false;
        }
        return result;
    }
}
