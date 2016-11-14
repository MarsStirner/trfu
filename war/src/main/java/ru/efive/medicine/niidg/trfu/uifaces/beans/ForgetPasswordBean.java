package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.axis.utils.StringUtils;
import ru.efive.dao.sql.dao.GenericDAO;
import ru.efive.dao.sql.dao.user.UserDAO;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.wf.core.activity.MailMessage;
import ru.efive.wf.core.activity.SendMailActivity;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.USER_DAO;
@ManagedBean( name = "forgetPass")
@ViewScoped
public class ForgetPasswordBean implements Serializable {
	private static final long serialVersionUID = 381779623328037514L;
	private static final String MAIL_ACCOUNT_USERNAME = "mail.account.username";
	private static final String MAIL_ACCOUNT_PASSWORD = "mail.account.password";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_SMTP_STARTTLS_ENABLED = "mail.smtp.starttls.enable";
	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
	private static final String MAIL_SMTP_PORT = "mail.smtp.port";

	//Формат почтового сообщения
	private static final String FORGET_MESSAGE_BODY =  "Здравствуйте, %s. Ваш пароль - \"%s\"";
	private static final String FORGET_MESSAGE_SUBJECT = "Восстановление пароля в системе ТРФУ";
	private static final String FORGET_MESSAGE_CONTENT_TYPE = "text/html";

	//Сообщения
	private static final FacesMessage FORGET_MAIL_MAIL_NOT_SET = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Введите e-mail", null);
	private static final FacesMessage FORGET_MAIL_USER_NOT_FOUND = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Письмо не отправлено", null);
	private static final FacesMessage FORGET_MAIL_MAIL_SEND_FAILURE = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ошибка при отправлении почты. Обратитесь к администратору", null);
	private static final FacesMessage FORGET_MAIL_MAIL_SENDED = new FacesMessage(FacesMessage.SEVERITY_INFO, "Письмо с паролем было отправлено. Проверьте почту", null);



	@Inject
	private transient IndexManagementBean indexManagement;
	@Inject
	private transient ApplicationPropertiesHolder propertiesHolder = new ApplicationPropertiesHolder();

	private String email = "";

	public void sendMail() {
		if (!StringUtils.isEmpty(email)) {
			UserDAO dao = getDAO(UserDAO.class, USER_DAO);
			User user = dao.getByEmail(email);
			if (user != null) {
				MailMessage message = setupMessage(user);
				SendMailActivity sendMail = new SendMailActivity();
				sendMail.setMessage(message);
				try {
					Session session = getMailSession();
					sendMail.executeBySession(session);
					FacesContext.getCurrentInstance().addMessage("forget_mail", FORGET_MAIL_MAIL_SENDED);
				} catch (MessagingException e) {
					FacesContext.getCurrentInstance().addMessage("forget_mail", FORGET_MAIL_MAIL_SEND_FAILURE);
					e.printStackTrace();
				}
			} else {
				FacesContext.getCurrentInstance().addMessage("forget_mail", FORGET_MAIL_USER_NOT_FOUND);
			}
		} else {
			FacesContext.getCurrentInstance().addMessage("forget_mail", FORGET_MAIL_MAIL_NOT_SET);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends GenericDAO> T getDAO(Class<T> persistentClass, String beanName) {
    	return (T) indexManagement.getContext().getBean(beanName);
    }
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private MailMessage setupMessage(User user) {
		MailMessage message = new MailMessage(new ArrayList<String>(), new ArrayList<String>(), FORGET_MESSAGE_SUBJECT, null);
        message.getSendTo().add(email);
        message.setBody(String.format(FORGET_MESSAGE_BODY, user.getFirstName(), user.getPassword()));
        message.setContentType(FORGET_MESSAGE_CONTENT_TYPE);
		return message;
	}
	
	private Session getMailSession() {
		final String username = (String)getAppProperty(MAIL_ACCOUNT_USERNAME);
		final String password = (String)getAppProperty(MAIL_ACCOUNT_PASSWORD);
 
		Properties props = getMailSMPTProperties();

		return Session.getInstance(props,
		  new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	private Object getAppProperty(String key) {
		return propertiesHolder.getProperty("application", key);
	}
	
	private Properties getMailSMPTProperties() {
		Properties props = new Properties();
		props.put(MAIL_SMTP_AUTH, getAppProperty(MAIL_SMTP_AUTH));
		props.put(MAIL_SMTP_STARTTLS_ENABLED, getAppProperty(MAIL_SMTP_STARTTLS_ENABLED));
		props.put(MAIL_SMTP_HOST, getAppProperty(MAIL_SMTP_HOST));
		props.put(MAIL_SMTP_SSL_TRUST, getAppProperty(MAIL_SMTP_SSL_TRUST));
		props.put(MAIL_SMTP_PORT, getAppProperty(MAIL_SMTP_PORT));
		return props;
	}
}
