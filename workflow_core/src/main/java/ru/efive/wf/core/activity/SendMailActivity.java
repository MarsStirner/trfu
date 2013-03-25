package ru.efive.wf.core.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.PropertyUtils;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;
import ru.efive.medicine.niidg.trfu.data.entity.TextTemplate;
import ru.efive.wf.core.IActivity;
import ru.efive.wf.core.ProcessedData;
import ru.efive.wf.core.Settings;
import ru.efive.wf.core.util.GroovyProcessor;

public class SendMailActivity implements IActivity {

    private Map<String, Object> localContext;
    private EmailTemplate emailTemplate;
    private ProcessedData processedData;
    private MailMessage message;

    private String resultMessage;

    public SendMailActivity() {
		
	}
	
	@Override
	public <T extends ProcessedData> boolean initialize(T t) {
		boolean result = false;
		try {
			processedData = t;
            localContext.put("data", t);
            List<String> sendTo = new ArrayList<String>();
            for(TextTemplate textTemplate:emailTemplate.getSendTo())
                sendTo.add(GroovyProcessor.processTemplate(textTemplate.getValue(), localContext));

            List<String> blindCopyTo = new ArrayList<String>();
            for(TextTemplate textTemplate:emailTemplate.getBlindCopyTo())
                blindCopyTo.add(GroovyProcessor.processTemplate(textTemplate.getValue(), localContext));

            List<String> copyTo = new ArrayList<String>();
            for(TextTemplate textTemplate:emailTemplate.getCopyTo())
                copyTo.add(GroovyProcessor.processTemplate(textTemplate.getValue(), localContext));

            Date planDate = (Date) PropertyUtils.getProperty(t, "planDate");
            if (planDate != null) {
                localContext.put("planDate", planDate);
                localContext.put("dateFormater", new java.text.SimpleDateFormat("dd.MM.yyyy"));
                localContext.put("timeFormater", new java.text.SimpleDateFormat("HH:mm"));
            }

            message = new MailMessage(sendTo, copyTo, GroovyProcessor.processTemplate(emailTemplate.getSubject(), localContext), GroovyProcessor.processTemplate(emailTemplate.getBody(), localContext));
            message.setBlindCopyTo(blindCopyTo);
            message.setContentType("text/html");


            result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

        return result;
	}
	
	//TODO: for testing
	public void setMessage(MailMessage message) {
		this.message = message;
	}
	
	@Override
	public boolean execute() {
		boolean result = false;
		try {
			Session session = (Session) new InitialContext().lookup(Settings.getMessagingService());
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSubject(message.getSubject());
			
			mimeMessage.setFrom();
			
			StringBuilder sb = new StringBuilder();
			for (String address : message.getSendTo()) {
				sb.append(address).append(" ");
			}
			if (!sb.toString().trim().equals(""))
				mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(sb.toString().trim(), false));
			sb = new StringBuilder();
			for (String address : message.getCopyTo()) {
				sb.append(address).append(" ");
			}
			if (!sb.toString().trim().equals(""))
				mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(sb.toString().trim(), false));
			sb = new StringBuilder();
			for (String address : message.getBlindCopyTo()) {
				sb.append(address).append(" ");
			}
			if (!sb.toString().trim().equals(""))
				mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(sb.toString().trim(), false));
			
			mimeMessage.setSubject(message.getSubject());
			mimeMessage.setSentDate(new Date());
			
			MimeBodyPart mbp = new MimeBodyPart();
			if (message.getContentType().equals("text/html")) {				
				mbp.setHeader("Content-Type","text/html; charset=\"utf-8\"");
				mbp.setContent(message.getBody(), "text/html; charset=utf-8");
				mbp.setHeader("Content-Transfer-Encoding", "quoted-printable");
			}
			else {
				mbp.setText(message.getBody());
			}
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
			mimeMessage.setContent(mp);

			Transport.send(mimeMessage);
			result = true;
		}
		catch (NamingException e) {
			result = false;
			e.printStackTrace();
		}
		catch (MessagingException e) {
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public boolean dispose() {
		return true;
	}
	
	public String getResult() {
		return resultMessage;
	}


    public void setLocalContext(Map<String, Object> localContext) {
        this.localContext = localContext;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}