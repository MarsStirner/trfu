package ru.efive.medicine.niidg.trfu.uifaces.beans.print;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ru.efive.medicine.niidg.trfu.util.DateHelper;

/**
 * Класс предназначен для получения текущей даты при печати.
 * 
 * @author artsiom, Siarhei Ushanau
 */
@ManagedBean(name="printDate")
@SessionScoped
public class PrintDateBean implements Serializable{
	private static final long serialVersionUID = 2016202001335193864L;

	/**
	 * Метод используется для вывода текущей даты при печати.
	 * 
	 * @return текущую дату в формате "dd.MM.yyyy HH:mm".
	 */
	public String getCurrentDate() {
		return DateHelper.getCurrentDate();
	}
}