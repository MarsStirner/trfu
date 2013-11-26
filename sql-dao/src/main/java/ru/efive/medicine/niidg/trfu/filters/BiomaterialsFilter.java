package ru.efive.medicine.niidg.trfu.filters;

import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;

public class BiomaterialsFilter extends MedicalOperationFilter<BiomaterialsFilter> {
	private static final long serialVersionUID = -3553062023477383249L;
	
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.MedicalOperationFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		setCurrentEntity(Biomaterial.class);
	}

}
