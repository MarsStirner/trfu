package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DonorListSelectModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.UserListSelectModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.smsdelivery.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.rpc.ServiceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("smsService")
@SessionScoped
public class ShortMessagingServiceBean implements Serializable {
    private String message;
    private List<User> users = new ArrayList<User>();
    private List<Donor> donors = new ArrayList<Donor>();
    private static final Logger logger = LoggerFactory.getLogger(ShortMessagingServiceBean.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Named("propertiesHolder")
    private ApplicationPropertiesHolder propertiesHolder;

    @PostConstruct
    public void init() {
        int i = 0;
        while (i < donorsSelectModal.getDonorList().getDocuments().size()) {
            if (donorsSelectModal.getDonorList().getDocuments().get(i).getPhone() == null)
                donorsSelectModal.getDonorList().getDocuments().remove(i);
            else
                i++;
        }
        i = 0;
        while (i < usersSelectModal.getUserList().getDocuments().size()) {
            if (usersSelectModal.getUserList().getDocuments().get(i).getMobilePhone() == null)
                usersSelectModal.getUserList().getDocuments().remove(i);
            else
                i++;
        }
    }

    private UserListSelectModalBean usersSelectModal = new UserListSelectModalBean() {

        @Override
        protected void doSave() {
            super.doSave();
            users.clear();
            users.addAll(this.getUsers());
        }

        @Override
        protected void doHide() {
            super.doHide();
            getUserList().setFilter("");
            getUserList().markNeedRefresh();
            setUsers(new ArrayList<User>());
        }
    };

    private DonorListSelectModalBean donorsSelectModal = new DonorListSelectModalBean() {

        @Override
        protected void doSave() {
            super.doSave();
            donors.clear();
            donors.addAll(this.getDonors());
        }

        @Override
        protected void doHide() {
            super.doHide();
            getDonorList().setFilter("");
            getDonorList().markNeedRefresh();
            setDonors(new ArrayList<Donor>());
        }
    };

    public List<Donor> getDonors() {
        return donors;
    }

    public void setDonors(List<Donor> donors) {
        this.donors = donors;
    }


    public DonorListSelectModalBean getDonorsSelectModal() {
        return donorsSelectModal;
    }

    public void setDonorsSelectModal(DonorListSelectModalBean donorsSelectModal) {
        this.donorsSelectModal = donorsSelectModal;
    }


    public UserListSelectModalBean getUsersSelectModal() {
        return usersSelectModal;
    }

    public void setUsersSelectModal(UserListSelectModalBean usersSelectModal) {
        this.usersSelectModal = usersSelectModal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public void send() {
        String smsLogin = (String) propertiesHolder.getProperty("application", "smsdelivery.login");
        String smsPwd = (String) propertiesHolder.getProperty("application", "smsdelivery.password");
        SMSWebService service = new SMSWebServiceLocator();
        SMSWebServiceSoap soap  = null;
        try {
            soap = service.getSMSWebServiceSoap();
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при отправке смс-уведомления", ""));
            logger.error("Ошибка при отправке смс-уведомления", e);
            return;
        }

        StringBuffer warnBuffer = new StringBuffer();
        StringBuilder errBuffer = new StringBuilder();
        for (Donor donor : donors) {
            try {
                MessageResponse response = soap.sendMessage(smsLogin, smsPwd, false, 24, donor.getPhone(), "", message);
                warnBuffer.append("Результат отправки на номер: ").append(donor.getPhone()).append(response.getResult());
                if (response.getResult().toString().equals(ValidateEnum._OK)) {
                	warnBuffer.append("; Номер сообщения: ").append(response.getMessageID()).append("; Число сегментов: ").append(response.getSegmentsNumber());
                }
                warnBuffer.append("\n");

            } catch (Exception e) {
                errBuffer.append("Ошибка при отправке смс-уведомления на номер").append(donor.getPhone()).append("\n");
                logger.error("Ошибка при отправке смс-уведомления", e);
            }
        }

        for (User user : users) {
            try {
                MessageResponse response = soap.sendMessage(smsLogin, smsPwd, false, 24, user.getMobilePhone(), "", message);
                warnBuffer.append("Результат отправки на номер: ").append(user.getMobilePhone()).append(response.getResult());
                if (response.getResult().toString().equals(ValidateEnum._OK)) {
                    warnBuffer.append("; Номер сообщения: ").append(response.getMessageID()).append("; Число сегментов: ").append(response.getSegmentsNumber());
                }
                warnBuffer.append("\n");

            } catch (Exception e) {
                errBuffer.append("Ошибка при отправке смс-уведомления на номер").append(user.getMobilePhone()).append("\n");
                logger.error("Ошибка при отправке смс-уведомления", e);
            }
        }

        if(errBuffer.length()>0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errBuffer.toString(), ""));
        }
        if(warnBuffer.length()>0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warnBuffer.toString(), ""));
            logger.warn(warnBuffer.toString());
        }
    }


}