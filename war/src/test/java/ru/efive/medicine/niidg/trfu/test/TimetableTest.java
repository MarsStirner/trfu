package ru.efive.medicine.niidg.trfu.test;

import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dictionary.impl.OneDayTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.dictionary.impl.OnePlanTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.dictionary.impl.PeriodTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

public class TimetableTest {
	
	public TimetableTest() {
		
	}
	/*
	@Before
	public void init() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContextHelper.setApplicationContext(context);
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		user = ((UserDAOHibernate) context.getBean(ApplicationHelper.USER_DAO)).get(1);
	}
	
	@Test
	public void testTimesheetTemplates() throws Exception {
		System.out.println("Testing one plan timesheet");
		OnePlanTimesheetTemplate onePlanTimesheet = new OnePlanTimesheetTemplate();
		onePlanTimesheet.setUser(user);
		onePlanTimesheet.setPeriodStartDate(dateFormat.parse("01.06.2012"));
		onePlanTimesheet.setPeriodEndDate(dateFormat.parse("08.06.2012"));
		onePlanTimesheet.setHolidayFill(false);
		onePlanTimesheet.setPossibleStartTime(timeFormat.parse("09:00:00"));
		onePlanTimesheet.setEndTime(timeFormat.parse("18:00:00"));
		
		onePlanTimesheet.compose();
		
		System.out.println("Testing periodic timesheet");
		PeriodTimesheetTemplate periodTemplate = new PeriodTimesheetTemplate();
		periodTemplate.setUser(user);
		periodTemplate.setPeriodStartDate(dateFormat.parse("12.06.2012"));
		periodTemplate.setPeriodEndDate(dateFormat.parse("20.06.2012"));
		periodTemplate.setWorkdayCount(1);
		periodTemplate.setRestdayCount(4);
		periodTemplate.setPossibleStartTime(timeFormat.parse("10:00:00"));
		periodTemplate.setEndTime(timeFormat.parse("17:00:00"));
		periodTemplate.compose();
		
		System.out.println("Testing one day timesheet");
		OneDayTimesheetTemplate oneDayTemplate = new OneDayTimesheetTemplate();
		oneDayTemplate.setUser(user);
		oneDayTemplate.setPeriodStartDate(dateFormat.parse("25.06.2012"));
		oneDayTemplate.setPossibleStartTime(timeFormat.parse("11:00:00"));
		oneDayTemplate.setEndTime(timeFormat.parse("19:00:00"));
		oneDayTemplate.compose();
	}
	
	@After
	public void dispose() {
		context.close();
	}
	*/
	private ClassPathXmlApplicationContext context;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;
	private User user;
}