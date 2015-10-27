package ru.efive.medicine.niidg.trfu.dictionary.impl;

import java.util.Calendar;

import org.slf4j.Logger;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.TimeTableDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.dictionary.TimesheetTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

/**
 * Заполнение графика на отдельный день
 * 
 * @author Alexey Vagizov
 */
public class OneDayTimesheetTemplate extends TimesheetTemplate {
	
	@Override
	public boolean compose() {
		boolean result = false;
		try {
			TimeTableDAOImpl dao = (TimeTableDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.TIMETABLE_DAO);
			TimeTableEntry entry = composeEntry();
			
			Calendar oneDayCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
			oneDayCalendar.setTime(getPeriodStartDate());
			
			Calendar start = Calendar.getInstance(ApplicationHelper.getLocale());
			start.setTime(getStartTime());
			start.set(Calendar.YEAR, oneDayCalendar.get(Calendar.YEAR));
			start.set(Calendar.MONTH, oneDayCalendar.get(Calendar.MONTH));
			start.set(Calendar.DATE, oneDayCalendar.get(Calendar.DATE));
			
			Calendar end = Calendar.getInstance(ApplicationHelper.getLocale());
			end.setTime(getEndTime());
			end.set(Calendar.YEAR, oneDayCalendar.get(Calendar.YEAR));
			end.set(Calendar.MONTH, oneDayCalendar.get(Calendar.MONTH));
			end.set(Calendar.DATE, oneDayCalendar.get(Calendar.DATE));
			
			entry.setStartDate(start.getTime());
			entry.setEndDate(end.getTime());
			
			dao.save(entry);
			result = true;
		}
		catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	@Override
	public boolean isPeriodic() {
		return false;
	}
	
	@Override
	public int getType() {
		return 3;
	}
	
}