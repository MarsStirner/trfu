package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.List;

import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;

public class RequestFilter extends AppendSRPDFilter<ExaminationsFilter> {
	private static final long serialVersionUID = -1952114617410828162L;
	private List<AliasFilterBean> listAlias;
	private List<FieldFilterBean> listFields;
	private List<FieldFilterBean> listFieldsDisjunction;
	private List<String> sqlRestrictions;
	public List<AliasFilterBean> getListAlias() {
		return listAlias;
	}
	public void setListAlias(List<AliasFilterBean> listAlias) {
		this.listAlias = listAlias;
	}
	public List<FieldFilterBean> getListFields() {
		return listFields;
	}
	public void setListFields(List<FieldFilterBean> listFields) {
		this.listFields = listFields;
	}
	public List<FieldFilterBean> getListFieldsDisjunction() {
		return listFieldsDisjunction;
	}
	public void setListFieldsDisjunction(List<FieldFilterBean> listFieldsDisjunction) {
		this.listFieldsDisjunction = listFieldsDisjunction;
	}
	public List<String> getSqlRestrictions() {
		return sqlRestrictions;
	}
	public void setSqlRestrictions(List<String> sqlRestrictions) {
		this.sqlRestrictions = sqlRestrictions;
	}
	/**
     * Конструктор по умолчанию.
     */
	public RequestFilter() {
		super();
		setDefaultValues();
	}
	
	protected void setDefaultValues() {
		super.setDefaultValues();
		listAlias = new ArrayList<AliasFilterBean>();
		listFields = new ArrayList<FieldFilterBean>();
		listFieldsDisjunction = new ArrayList<FieldFilterBean>();
	}
	@Override
	public void clear() {
		setDefaultValues();
	}

	@Override
	public void fillFrom(ExaminationsFilter source) {
		super.fillFrom(source);
		setListFields(source.getListFields());
		setListFieldsDisjunction(source.getListFieldsDisjunction());
		setSqlRestrictions(source.getSqlRestrictions());
	}
}
