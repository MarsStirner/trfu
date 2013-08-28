/**
 * 
 */
package ru.efive.medicine.niidg.trfu.filters;

import java.io.Serializable;

/**
 * @author Viktar Kastsiuchenka
 *
 */
public class BiomaterialDonorsFilter extends AppendSRPDFilter<BiomaterialDonorsFilter> {
	private static final long serialVersionUID = -3168001218894316858L;
	
	private Serializable externalId;
	private String factAdress;
	private String number;
	private String infectiousStatus;
	private String pregnancy;
	private String commentary;

	public BiomaterialDonorsFilter() {
		super();
		setDefaultValues();
	}
	/**
	 * @return the externalId
	 */
	public Serializable getExternalId() {
		return externalId;
	}

	/**
	 * @return the factAdress
	 */
	public String getFactAdress() {
		return factAdress;
	}
	/**
	 * @param factAdress the factAdress to set
	 */
	public void setFactAdress(String factAdress) {
		this.factAdress = factAdress;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the infectiousStatus
	 */
	public String getInfectiousStatus() {
		return infectiousStatus;
	}
	/**
	 * @param infectiousStatus the infectiousStatus to set
	 */
	public void setInfectiousStatus(String infectiousStatus) {
		this.infectiousStatus = infectiousStatus;
	}
	/**
	 * @return the pregnancy
	 */
	public String getPregnancy() {
		return pregnancy;
	}
	/**
	 * @param pregnancy the pregnancy to set
	 */
	public void setPregnancy(String pregnancy) {
		this.pregnancy = pregnancy;
	}
	/**
	 * @return the commentary
	 */
	public String getCommentary() {
		return commentary;
	}
	/**
	 * @param commentary the commentary to set
	 */
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(Serializable externalId) {
		this.externalId = externalId;
	}

	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		externalId = null;
		factAdress = null;
		number = null;
		infectiousStatus = null;
		pregnancy = null;
		commentary = null;
	}

	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#fillFrom(java.lang.Object)
	 */
	@Override
	public void fillFrom(BiomaterialDonorsFilter source) {
		super.fillFrom(source);
		setExternalId(source.getExternalId());
		setFactAdress(source.getFactAdress());
		setNumber(source.getNumber());
		setInfectiousStatus(source.getInfectiousStatus());
		setPregnancy(source.getPregnancy());
		setCommentary(source.getCommentary());
	}
	
}
