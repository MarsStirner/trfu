package ru.efive.medicine.niidg.trfu.filters;

import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;

/**
 * @author Viktar Kastsiuchenka
 *
 */
public class OperationsFilter extends MedicalOperationFilter<OperationsFilter> {

	private static final long serialVersionUID = 4798587246022366606L;
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#fillFrom(java.lang.Object)
	 */
	@Override
	public void fillFrom(OperationsFilter source) {
		super.fillFrom(source);
		setCurrentEntity(source.getCurrentEntity());
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		setCurrentEntity(Operation.class);
	}
}
