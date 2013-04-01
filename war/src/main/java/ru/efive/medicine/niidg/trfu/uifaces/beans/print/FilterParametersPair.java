package ru.efive.medicine.niidg.trfu.uifaces.beans.print;

import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.FilterParameter;

/**
 * Класс введен для удобства реализации печати параметров фильтра в две колонки.
 * 
 * @author Siarhei Ushanau
 */
public class FilterParametersPair {
	protected FilterParameter leftParameter;
	protected FilterParameter rightParameter;
	
	public FilterParametersPair(FilterParameter leftParameter,
			FilterParameter rightParameter) {
		super();
		this.leftParameter = leftParameter;
		this.rightParameter = rightParameter;
	}

	public FilterParameter getLeftParameter() {
		return leftParameter;
	}

	public void setLeftParameter(FilterParameter leftParameter) {
		this.leftParameter = leftParameter;
	}

	public FilterParameter getRightParameter() {
		return rightParameter;
	}

	public void setRightParameter(FilterParameter rightParameter) {
		this.rightParameter = rightParameter;
	}
}