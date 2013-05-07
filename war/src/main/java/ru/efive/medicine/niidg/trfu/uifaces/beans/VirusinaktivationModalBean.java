package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.efive.uifaces.bean.ModalWindowHolderBean;

/**
 * 
 * Класс предназаначен для работы с модальным окном "Вирусинактивация"
 * @author Artsiom Shmaenkov
 *
 */
public class VirusinaktivationModalBean extends ModalWindowHolderBean {
	
	private static final long serialVersionUID = -266520779235745847L;
	
	private String volume = "0";

	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
			System.out.println("Запись");
			this.volume = volume;	
	}	
	
	public boolean isValid(){
		System.out.println("Проверка");
		Pattern p1 = Pattern.compile("^(0)$|^([1-9][0-9]*)$");
    	Matcher m1 = p1.matcher(volume);
    	System.out.println(m1.matches());
		return m1.matches();		
	}

	public void perfomVirusinaktivation() {
				
	}
}
