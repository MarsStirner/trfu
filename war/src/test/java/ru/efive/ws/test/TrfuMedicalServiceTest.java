package ru.efive.ws.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import ru.korusconsulting.medical.OrderInformation;
import ru.korusconsulting.medical.OrderResult;
import ru.korusconsulting.medical.PatientCredentials;
import ru.korusconsulting.medical.TransfusionMedicalService_Service;

public class TrfuMedicalServiceTest {
	
	public TrfuMedicalServiceTest() {
		
	}
	
	//@Test
	public void bloodComponentOrderTest() {
		TransfusionMedicalService_Service tService = new TransfusionMedicalService_Service();
		
		PatientCredentials patientCredentials = new PatientCredentials();
		patientCredentials.setFirstName("Тест");
		patientCredentials.setMiddleName("Вебсервисович");
		patientCredentials.setLastName("Вебсервисов");
		patientCredentials.setBloodGroupId(1);
		patientCredentials.setRhesusFactorId(1);
		GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, 30);
        XMLGregorianCalendar date = new XMLGregorianCalendarImpl(calendar);
		patientCredentials.setBirth(date);
		
		OrderInformation orderInformation = new OrderInformation();
		orderInformation.setComponentTypeId(6);
		orderInformation.setDoseCount(1.0);
		orderInformation.setVolume(250);
		orderInformation.setDivisionId(12);
		orderInformation.setId(1);
		orderInformation.setTransfusionType(0);
		
		OrderResult result = tService.getTransfusionMedicalService().orderBloodComponents(patientCredentials, orderInformation);
		System.out.println("Result: " + result.isResult() + (result.isResult()? "": ", description: '" + result.getDescription() + "'"));
	}
	
}