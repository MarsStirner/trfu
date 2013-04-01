package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

/**
 * Параметр фильтра. 
 * 
 * @author Siarhei Ushanau
 */
public class FilterParameter {
	protected String title;
	protected String value;

	public FilterParameter(String title, String value) {
		super();
		this.title = title;
		this.value = value;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}