package ru.efive.medicine.niidg.trfu.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

public class SplitComponentFix {
	
	public SplitComponentFix() {
		
	}
	
	@Test
	public void init() throws Exception {
		/*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		BloodComponentDAOImpl dao = (BloodComponentDAOImpl) ctx.getBean(ApplicationHelper.BLOOD_COMPONENT_DAO);
		List<BloodComponent> splitList = dao.findUniquieSplitComponents();
		
		System.out.println(splitList.size());
		for (BloodComponent splitComponent: splitList) {
			System.out.println("Processing component: " + splitComponent.getId() + ", donation id: " + splitComponent.getDonationId());
			BloodComponent parentComponent = dao.findParentComponent(splitComponent);
			parentComponent.setSplitDate(splitComponent.getSplitDate());
			System.out.println("Parent: " + parentComponent.getId());
			dao.save(parentComponent);
		}
		
		ctx.close();*/
	}
	
}