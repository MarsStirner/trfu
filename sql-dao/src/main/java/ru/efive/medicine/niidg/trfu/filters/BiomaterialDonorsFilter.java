package ru.efive.medicine.niidg.trfu.filters;

import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;

/**
 * @author Viktar Kastsiuchenka
 *
 */
public class BiomaterialDonorsFilter extends MedicalOperationFilter<BiomaterialDonor> {
	private static final long serialVersionUID = -3168001218894316858L;
	
	
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		setCurrentEntity(BiomaterialDonor.class);
		
	}	
}
