package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import ru.efive.dao.sql.dao.GenericDAO;
import ru.efive.dao.sql.dao.user.UserDAO;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.wf.core.activity.SendMailActivity;
import ru.efive.wf.core.activity.MailMessage;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;

@Named("forgetPass")
@ConversationScoped
public class ForgetPasswordBean implements Serializable {
	private static final long serialVersionUID = 381779623328037514L;
	private static final String MAIL_ACCOUNT_USERNAME = "mail.account.username";
	private static final String MAIL_ACCOUNT_PASSWORD = "mail.account.password";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_SMTP_STARTTLS_ENABLED = "mail.smtp.starttls.enable";
	private static final String MAIL_SMTP_HOST = "mail.smtp.host";
	private static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
	private static final String MAIL_SMTP_PORT = "mail.smtp.port";
	@Inject
	private transient IndexManagementBean indexManagement;
	@Inject
	private transient ApplicationPropertiesHolder propertiesHolder = new ApplicationPropertiesHolder();
	private String result = "";
	private String email = "";

	public void sendMail() {
		result = "sent";
		if (!email.isEmpty()) {
			UserDAO dao = getDAO(UserDAO.class, "userDao");
			User user = dao.getByEmail(email);
			if (user != null) {
				MailMessage message = setupMessage(user);
				SendMailActivity sendMail = new SendMailActivity();
				sendMail.setMessage(message);
				try {
					Session session = getMailSession();
					sendMail.executeBySession(session);
				} catch (MessagingException e) {
					result = "app_error";
					e.printStackTrace();
				}
			} else {
				result = "not_sent";
			}
		} else {
			result = "enter_email";
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends GenericDAO> T getDAO(Class<T> persistentClass, String beanName) {
    	return (T) indexManagement.getContext().getBean(beanName);
    }
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private MailMessage setupMessage(User user) {
		MailMessage message = new MailMessage(new ArrayList<String>(), new ArrayList<String>(), "Восстановление пароля", null);
        message.getSendTo().add(email);
        message.setBody("Здравствуйте " + user.getFirstName() + ". Ваш пароль - " + user.getPassword());
        message.setContentType("text/html");
		return message;
	}
	
	private Session getMailSession() {
		final String username = (String)getAppProperty(MAIL_ACCOUNT_USERNAME);
		final String password = (String)getAppProperty(MAIL_ACCOUNT_PASSWORD);
 
		Properties props = getMailSMPTProperties();
 
		Session session = Session.getInstance(props,
		  new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		return session;
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
