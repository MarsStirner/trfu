package ru.efive.ws.test;

import org.junit.Test;

import ru.smsdelivery.MessageResponse;
import ru.smsdelivery.SMSWebService;
import ru.smsdelivery.SMSWebServiceLocator;

public class SmsServiceTest {
	
	public SmsServiceTest() {
		
	}
	
	@Test
	public void sendSms() throws Exception {
		//ru.sms.ShortMessagingService.testSmsService();
		
		//SMSWebService service = new SMSWebServiceLocator();
		//java.lang.String userName, java.lang.String password, boolean isFlash, int lifeTime, java.lang.String destNumber, java.lang.String senderAddr, java.lang.String text
		//MessageResponse response = service.getSMSWebServiceSoap().sendMessage("smsdelivery", "1", false, 24, "79265594249", "", "Test");
		//System.out.println("Результат отправки: " + response.getResult() + "; Номер сообщения: " + response.getMessageID() + "; Число сегментов: " + response.getSegmentsNumber());

	}
	
}