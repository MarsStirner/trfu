package ru.efive.medicine.niidg.trfu.dictionary.impl;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.TimeTableDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.dictionary.TimesheetTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

import java.util.Calendar;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.TIMETABLE_DAO;

/**
 * График x через y дней
 * 
 * @author Alexey Vagizov
 */
public class PeriodTimesheetTemplate extends TimesheetTemplate {
	
	public void setWorkdayCount(int workdayCount) {
		this.workdayCount = workdayCount;
	}
	
	public int getWorkdayCount() {
		return workdayCount;
	}
	
	public void setRestdayCount(int restdayCount) {
		this.restdayCount = restdayCount;
	}
	
	public int getRestdayCount() {
		return restdayCount;
	}
	
	@Override
	public boolean compose() {
		boolean result = false;
		try {
			TimeTableDAOImpl dao = (TimeTableDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(TIMETABLE_DAO);
			
			Calendar periodCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
			periodCalendar.setTime(getPeriodStartDate());
			Calendar periodEndCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
			periodEndCalendar.setTime(getPeriodEndDate());
			periodEndCalendar.add(Calendar.DATE, 1);
			while (periodCalendar.before(periodEndCalendar)) {
				for (int i = 0; i < getWorkdayCount(); i++) {
					if (periodCalendar.before(periodEndCalendar)) {
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
						
						periodCalendar.add(Calendar.DATE, 1);
					}
				}
				periodCalendar.add(Calendar.DATE, getRestdayCount());
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
		return 2;
	}
	
	
	/**
	 * Количество рабочих дней
	 */
	private int workdayCount;
	
	/**
	 * Количество выходных дней
	 */
	private int restdayCount;
}