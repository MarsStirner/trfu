package ru.efive.medicine.niidg.trfu.dictionary.impl;

import java.util.Calendar;

import org.slf4j.Logger;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.TimeTableDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.dictionary.TimesheetTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

/**
 * Один план
 * 
 * @author Alexey Vagizov
 */
public class OnePlanTimesheetTemplate extends TimesheetTemplate {
	
	public void setHolidayFill(boolean holidayFill) {
		this.holidayFill = holidayFill;
	}
	
	public boolean isHolidayFill() {
		return holidayFill;
	}
	
	@Override
	public boolean compose() {
		boolean result = false;
		try {
			TimeTableDAOImpl dao = (TimeTableDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.TIMETABLE_DAO);
			
			Calendar periodCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
			periodCalendar.setTime(getPeriodStartDate());
			Calendar periodEndCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
			periodEndCalendar.setTime(getPeriodEndDate());
			periodEndCalendar.add(Calendar.DATE, 1);
			while (periodCalendar.before(periodEndCalendar)) {
				if (periodCalendar.before(periodEndCalendar)) {
					if (holidayFill || (periodCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && periodCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
						TimeTableEntry entry = composeEntry();
						
						Calendar start = Calendar.getInstance(ApplicationHelper.getLocale());
						start.setTime(getStartTime());
						start.set(Calendar.YEAR, periodCalendar.get(Calendar.YEAR));
						start.set(Calendar.MONTH, periodCalendar.get(Calendar.MONTH));
						start.set(Calendar.DATE, periodCalendar.get(Calendar.DATE));
						
						Calendar end = Calendar.getInstance(ApplicationHelper.getLocale());
						end.setTime(getEndTime());
						end.set(Calendar.YEAR, periodCalendar.get(Calendar.YEAR));
						end.set(Calendar.MONTH, periodCalendar.get(Calendar.MONTH));
						end.set(Calendar.DATE, periodCalendar.get(Calendar.DATE));
						
						entry.setStartDate(start.getTime());
						entry.setEndDate(end.getTime());
						
						dao.save(entry);
					}
				}
				
				periodCalendar.add(Calendar.DATE, 1);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	@Override
	public int getType() {
		return 1;
	}
	
	
	private boolean holidayFill;
}
