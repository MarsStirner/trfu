package ru.efive.ws.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.efive.medicine.niidg.trfu.context.NoPropertyStoragePathException;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyTypeNotSupported;
import ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper;
import ru.efive.wf.core.ActionResult;
import ru.korusconsulting.external.DivisionInfo;
import ru.korusconsulting.external.TransfusionServiceImpl;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.COMPONENT_ORDER_DAO;
import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.DIVISION_DAO;

public class ExtrernalMedicalServiceTest {
	ClassPathXmlApplicationContext ctx;
	TransfusionServiceImpl medicalService;
	
    //@Before
    public void init() throws IOException, PropertyTypeNotSupported, ParseException, NoPropertyStoragePathException, JAXBException {
    	ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationPropertiesHolder propertiesHolder = new ApplicationPropertiesHolder();

        Object serviceAddress = propertiesHolder.getProperty("application", "mis.integration.address");
        System.out.println(serviceAddress);
        
        medicalService = new TransfusionServiceImpl(new java.net.URL(serviceAddress.toString()));
    }
    
    //@Test
    public void divisionListTest() {
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	DivisionDAOImpl dao = (DivisionDAOImpl) ctx.getBean(DIVISION_DAO);
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
    public void processComponentRequestTest() throws Exception {
    	BloodComponentOrderRequest request = ((BloodComponentOrderRequestDAOImpl) ctx.getBean(COMPONENT_ORDER_DAO)).get(1452);
    	ActionResult result = IntegrationHelper.processComponentRequest(request);
    	System.out.println(result.getDescription());
    }
    
}