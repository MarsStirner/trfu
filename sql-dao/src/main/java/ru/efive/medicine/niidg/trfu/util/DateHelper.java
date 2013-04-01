package ru.efive.medicine.niidg.trfu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Утилитарный класс, необходимый для работы с датами.
 * 
 * @author Siarhei Ushanau
 */
@ManagedBean(name="dateHelper")
@SessionScoped
public class DateHelper {
	/**
	 * Формат даты без времени.
	 */
	public static final String DATE_WITHOUT_TIME_PATTERN = "dd.MM.yyyy";

	/**
	 * Конструктор по умолчанию.
	 */
	public DateHelper() {
		super();
	}

	/**
	 * Возвращает дату с обнуленным временем.
	 * 
	 * @param date исходная дата.
	 * @return дата без времени.
	 */
	public static Date getDateWithoutTime(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}

	/**
	 * Возвращает завтрашнюю дату.
	 * 
	 * @param date исходная дата.
	 * @return завтрашняя дата.
	 */
	public static Date getTomorrowDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, 1);
	    return cal.getTime();
	}
	
	/**
	 * Форматирует дату в соответствии с указанным форматом.
	 * 
	 * @param date дата.
	 * @param pattern формат.
	 * @return отформатированную дату.
	 */
	public static String formatDateByPattern(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
}