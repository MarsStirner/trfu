package ru.efive.medicine.niidg.trfu.filters;

/**
 * @author Viktar Kastsiuchenka
 *
 */
public class OperationsFilter extends AppendSRPDFilter<OperationsFilter> {

	private static final long serialVersionUID = 4798587246022366606L;
	private String number;
	private String recepient;
	private String idNumber;
	private Integer donorId;
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
	 * @return the recepient
	 */
	public String getRecepient() {
		return recepient;
	}
	/**
	 * @param recepient the recepient to set
	 */
	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}
	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return the donorId
	 */
	public Integer getDonorId() {
		return donorId;
	}
	/**
	 * @param donorId the donorId to set
	 */
	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#fillFrom(java.lang.Object)
	 */
	@Override
	public void fillFrom(OperationsFilter source) {
		super.fillFrom(source);
		setNumber(source.getNumber());
		setRecepient(source.getRecepient());
		setIdNumber(source.getIdNumber());
		setDonorId(source.getDonorId());
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		number = null;
		recepient = null;
		idNumber = null;
		donorId = null;
	}
	

}
