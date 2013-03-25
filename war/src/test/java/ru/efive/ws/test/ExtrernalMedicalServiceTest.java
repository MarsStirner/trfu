package ru.efive.ws.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper;
import ru.efive.wf.core.ActionResult;
import ru.korusconsulting.external.DivisionInfo;
import ru.korusconsulting.external.TransfusionServiceImpl;

import java.util.Date;
import java.util.List;

public class ExtrernalMedicalServiceTest {
	TransfusionServiceImpl medicalService;
	
    //@Before
    public void init(){
         medicalService = new TransfusionServiceImpl();
    }
    
    //@Test
    public void divisionListTest() {
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	DivisionDAOImpl dao = (DivisionDAOImpl) ctx.getBean(ApplicationHelper.DIVISION_DAO);
        List<DivisionInfo> divisionsInfo = medicalService.getPortTransfusion().getDivisions();
        for (DivisionInfo divisionInfo: divisionsInfo) {
        	Division division = dao.findByExternalId(divisionInfo.getId());
        	if (division == null) {
        		division = new Division();
        		division.setCreated(new Date());
        		division.setDeleted(false);
        		division.setEdited(new Date());
        		division.setName(divisionInfo.getName());
        		division.setExternalId(divisionInfo.getId());
        		
        		dao.save(division);
        	}
        }
    }
    
    //@Test
    public void processComponentRequestTest() {
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	BloodComponentOrderRequest request = ((BloodComponentOrderRequestDAOImpl) ctx.getBean(ApplicationHelper.COMPONENT_ORDER_DAO)).get(1452);
    	ActionResult result = IntegrationHelper.processComponentRequest(request);
    	System.out.println(result.getDescription());
    }
    
}