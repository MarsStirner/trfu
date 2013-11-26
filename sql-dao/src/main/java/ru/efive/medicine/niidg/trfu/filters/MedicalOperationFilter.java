package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;

public abstract class MedicalOperationFilter<T> extends AppendSRPDFilter<T> {
	private static final long serialVersionUID = 1L;
	
	private Collection<FieldFilterBean> listFields;
	private List<List<FieldFilterBean>> listFieldsDisjunction;
	private Collection<AliasFilterBean> listAlias;
	private Class currentEntity;
	
	public Collection<FieldFilterBean> getListFields() {
		return listFields;
	}
	public void setListFields(Collection<FieldFilterBean> listFields) {
		this.listFields = listFields;
	}
	public List<List<FieldFilterBean>> getListFieldsDisjunction() {
		return listFieldsDisjunction;
	}
	public void setListFieldsDisjunction(List<List<FieldFilterBean>> listFieldsDisjunction) {
		this.listFieldsDisjunction = listFieldsDisjunction;
	}
	public Collection<AliasFilterBean> getListAlias() {
		return listAlias;
	}
	public void setListAlias(Collection<AliasFilterBean> listAlias) {
		this.listAlias = listAlias;
	}
	public Class getCurrentEntity() {
		return currentEntity;
	}
	public void setCurrentEntity(Class currentEntity) {
		this.currentEntity = currentEntity;
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		listFields = new ArrayList<FieldFilterBean>();
		listFieldsDisjunction = new ArrayList<List<FieldFilterBean>>();
		currentEntity = null;
	}

	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#fillFrom(java.lang.Object)
	 */
	@Override
	public void fillFrom(T source) {
		super.fillFrom(source);
		setListFields(((MedicalOperationFilter<T>)source).getListFields());
		setListFieldsDisjunction(((MedicalOperationFilter<T>)source).getListFieldsDisjunction());
		setCurrentEntity(((MedicalOperationFilter<T>)source).getCurrentEntity());
	}
}
