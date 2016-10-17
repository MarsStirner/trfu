package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.BLOOD_COMPONENT_DAO;

/**
 * Author: Upatov Egor <br>
 * Date: 03.06.2015, 17:16 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
@Named("purchasedBloodComponentRegistration")
@SessionScoped
public class PurchasedBloodComponentRegistrationBean implements Serializable {


    @Inject
    @Named("sessionManagement")
    SessionManagementBean sessionManagement;


    /**
     * Поле для ввода номера закупленного КК (со штрих-кода)
     */
    private String fullNumber;

    /**
     * Номер накладной для закупленных КК
     */
    private String invoiceNumber;
    /**
     * дата поступления для закупленных КК
     */
    private Date receivedDate = new Date();


    public void composePurchasedComponent() throws IOException {
        if (validate()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("blood_component.xhtml?docAction=create&purchasedNumber=" + fullNumber);
            fullNumber = "";
        } else {
            fullNumber = "";
        }
    }

    public boolean validate() {
        if (StringUtils.isEmpty(invoiceNumber)) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(SEVERITY_WARN, "Поле \'Номер накладной\' должно быть заполнено", null)
            );
            return false;
        }
        if (receivedDate == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(SEVERITY_WARN, "Поле \'Дата поступления\' должно быть заполнено", null)
            );
            return false;
        }
        if (StringUtils.isNotEmpty(fullNumber)) {
            final int fullNumberLength = fullNumber.length();
            if (fullNumberLength < 14 || fullNumberLength > 15) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(SEVERITY_WARN, "Поле \'Код компонента\' должно содержать 14 или 15 символов", null)
                );
                return false;
            }
            final String componentNumber = StringUtils.right(fullNumber, 2);
            final String parentNumber = StringUtils.substring(fullNumber, fullNumber.length() - 8, fullNumber.length() - 2);
            if (!sessionManagement.getDAO(BloodComponentDAOImpl.class, BLOOD_COMPONENT_DAO).findDocumentsByFullNumber(
                    parentNumber, componentNumber, false
            ).isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                SEVERITY_ERROR, "Компонент с таким номером \'" + parentNumber + '-' + componentNumber + "\' уже зарегистрирован", null
                        )
                );
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(SEVERITY_WARN, "Поле \'Код компонента\' не может быть пустым", null));
            return false;
        }
    }

    public String getFullNumber() {
        return fullNumber;
    }

    public void setFullNumber(final String fullNumber) {
        this.fullNumber = fullNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(final String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(final Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}
