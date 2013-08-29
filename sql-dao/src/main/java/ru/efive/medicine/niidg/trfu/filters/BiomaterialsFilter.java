package ru.efive.medicine.niidg.trfu.filters;

public class BiomaterialsFilter extends AppendSRPDFilter<BiomaterialsFilter> {
	private static final long serialVersionUID = -3553062023477383249L;
	private String number;
	private String operationNumber;
	private String operationRecepient;
	private Integer statusId;
	private Integer operationId;
	public BiomaterialsFilter() {
		super();
		setDefaultValues();
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
	 * @return the operationNumber
	 */
	public String getOperationNumber() {
		return operationNumber;
	}
	/**
	 * @param operationNumber the operationNumber to set
	 */
	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}
	/**
	 * @return the operationRecepient
	 */
	public String getOperationRecepient() {
		return operationRecepient;
	}
	/**
	 * @param operationRecepient the operationRecepient to set
	 */
	public void setOperationRecepient(String operationRecepient) {
		this.operationRecepient = operationRecepient;
	}
	/**
	 * @return the statusId
	 */
	public Integer getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the operationId
	 */
	public Integer getOperationId() {
		return operationId;
	}
	/**
	 * @param operationId the operationId to set
	 */
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#fillFrom(java.lang.Object)
	 */
	@Override
	public void fillFrom(BiomaterialsFilter source) {
		super.fillFrom(source);
		setNumber(source.getNumber());
		setOperationNumber(source.getOperationNumber());
		setOperationRecepient(source.getOperationRecepient());
		setStatusId(source.getStatusId());
		setOperationId(source.getOperationId());
	}
	/* (non-Javadoc)
	 * @see ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter#setDefaultValues()
	 */
	@Override
	protected void setDefaultValues() {
		super.setDefaultValues();
		number = null;
		operationNumber = null;
		operationRecepient = null;
		statusId = null;
		operationId = null;
	}

}
