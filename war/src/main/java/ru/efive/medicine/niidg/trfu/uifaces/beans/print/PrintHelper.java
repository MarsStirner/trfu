package ru.efive.medicine.niidg.trfu.uifaces.beans.print;

import java.util.ArrayList;
import java.util.List;

import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.FilterParameter;

/**
 * Утилитарный класс. Предназначен для подготовки к печати параметров фильтрации.
 * 
 * @author Siarhei Ushanau
 */
public class PrintHelper {
	/**
	 * Подготавливает параметры фильтрации для печати. А именно в случае необходимости
	 * "делит" набор параметров на две колонки.
	 * 
	 * @param parameters исходный набор параметров.
	 * @return набор параметров для удобной печати в две колонки.
	 */
	public static List<FilterParametersPair> prepareParametersForPrinting(
			List<FilterParameter> parameters) {
		int parametersCount = parameters.size();
		if (parametersCount <= MAX_PARAMETERS_COUNT_IN_COLUMN) {
			return putParametersInOneColumn(parameters);
		} else {
			return putParametersInTwoColumns(parameters, parametersCount);
		}
	}
	
	protected static List<FilterParametersPair> putParametersInTwoColumns(
			List<FilterParameter> parameters, int parametersCount) {
		List<FilterParametersPair> parametersForPrinting = new ArrayList<FilterParametersPair>();
		int rowsCount = (parametersCount + 1) / 2;
		for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
			FilterParameter leftParameter = parameters.get(rowIndex);
			FilterParameter rightParameter = null;
			int rightParameterIndex = rowsCount + rowIndex; 
			if (rightParameterIndex < parametersCount) {
				rightParameter = parameters.get(rightParameterIndex);
			}
			parametersForPrinting.add(new FilterParametersPair(leftParameter, rightParameter));
		}
		return parametersForPrinting;
	}

	protected static List<FilterParametersPair> putParametersInOneColumn(
			List<FilterParameter> parameters) {
		List<FilterParametersPair> parametersForPrinting = new ArrayList<FilterParametersPair>();
		for (FilterParameter filterParameter : parameters) {
			parametersForPrinting.add(new FilterParametersPair(filterParameter, null));
		}
		return parametersForPrinting;
	}

	/**
	 * Максимальное количество параметров фильтрации в колонке. Если при печати
	 * количество параметров будет больше значения этой константы, то необходимо
	 * выводить параметры в две колонки.
	 */
	public static final int MAX_PARAMETERS_COUNT_IN_COLUMN = 8;
}