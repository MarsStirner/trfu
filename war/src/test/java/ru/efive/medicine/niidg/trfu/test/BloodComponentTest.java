package ru.efive.medicine.niidg.trfu.test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;

public class BloodComponentTest {
	
	public BloodComponentTest() {
		
	}
	
	@Test
	public void test() throws Exception {
		/*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		UserDAOHibernate udao = (UserDAOHibernate) ctx.getBean(ApplicationHelper.USER_DAO);
		User user = udao.findByLoginAndPassword("Administrator", "Qaz12345");
		BloodComponentDAOImpl dao = (BloodComponentDAOImpl) ctx.getBean(ApplicationHelper.BLOOD_COMPONENT_DAO);
		BloodComponentOrderRequestDAOImpl rdao = (BloodComponentOrderRequestDAOImpl) ctx.getBean(ApplicationHelper.COMPONENT_ORDER_DAO);
		List<BloodComponent> list = dao.findComponentsWrongHistory2();
		System.out.println("Components with wrong history: " + list.size());
		for (BloodComponent component: list) {
			System.out.println("Processing component " + component.getId());
			
			//if (component.getOrderId() > 0) {
				//BloodComponentOrderRequest request = rdao.get(component.getOrderId());
				
				//if (request != null && !request.getHistoryList().isEmpty()) {
					//System.out.println("Component order " + request.getId());
					
					//HistoryEntry entry = request.getHistoryList().get(request.getHistoryList().size() - 1);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(component.getCreated());
					calendar.add(Calendar.HOUR_OF_DAY, 1);
					java.util.Date date = calendar.getTime();
					
					HistoryEntry newEntry = new HistoryEntry();
					newEntry.setActionId(3);
					newEntry.setFromStatusId(1);
					newEntry.setToStatusId(3);
					//newEntry.setCreated(entry.getCreated());
					newEntry.setCreated(date);
					//newEntry.setStartDate(entry.getStartDate());
					newEntry.setStartDate(date);
					newEntry.setOwner(user);
					newEntry.setDocType(component.getType());
					newEntry.setParentId(component.getId());
					//newEntry.setEndDate(entry.getEndDate());
					newEntry.setEndDate(date);
					newEntry.setProcessed(true);
					
					Set<HistoryEntry> history = component.getHistory();
					if (history == null) {
						history = new HashSet<HistoryEntry>();
					}
					history.add(newEntry);
					component.setHistory(history);
					
					dao.save(component);
				//}
			//}
		}
		
		ctx.close();*/
	}
	
}