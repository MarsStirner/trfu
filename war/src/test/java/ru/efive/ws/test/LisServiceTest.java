package ru.efive.ws.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper;
import ru.efive.wf.core.ActionResult;

public class LisServiceTest {
	
	public LisServiceTest() {
		
	}
	
	@Test
	public void test() throws Exception {
		/*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ExaminationRequestDAOImpl dao = (ExaminationRequestDAOImpl) ctx.getBean(ApplicationHelper.EXAMINATION_DAO);
		ExaminationRequest request = dao.get(1290);
		ActionResult result = IntegrationHelper.queryAppointment(request);
		if (result.isProcessed()) {
			System.out.println("Success");
		}
		else {
			System.out.println(result.getDescription());
		}
		ctx.close();*/
	}
	
}