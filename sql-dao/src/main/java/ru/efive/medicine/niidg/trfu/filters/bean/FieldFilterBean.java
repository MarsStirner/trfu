package ru.efive.medicine.niidg.trfu.filters.bean;

import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;

public class FieldFilterBean {
	private String fieldName;
	private Object value;
	private CompareType compareType;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public CompareType getCompareType() {
		return compareType;
	}
	public void setCompareType(CompareType compareType) {
		this.compareType = compareType;
	}
	public static FieldFilterBean init(String field, Object value, CompareType type) {
		FieldFilterBean bean = new FieldFilterBean();
		bean.setFieldName(field);
		bean.setValue(value);
		bean.setCompareType(type);
		return bean;
		
	}
	/**
	 * Создаёт поле дл сравнения, по умолчанию сравнение: EQ
	 * @param field - имя поля
	 * @param value - значение поля
	 */
	public static FieldFilterBean init(String field, Object value) {
		FieldFilterBean bean = new FieldFilterBean();
		bean.setFieldName(field);
		bean.setValue(value);
		bean.setCompareType(CompareType.EQ);
		return bean;
		
	}

}
