package ru.efive.medicine.niidg.trfu.uifaces.beans;

import javax.faces.context.FacesContext;

import ru.efive.uifaces.bean.ModalWindowHolderBean;

public class PurchasedBloodComponentCreationModal extends ModalWindowHolderBean {
	
	public void setFullNumber(String fullNumber) {
		this.fullNumber = fullNumber;
	}
	
	public String getFullNumber() {
		return fullNumber;
	}

	@Override
	protected void doShow() {
		super.doShow();
		fullNumber = "";
	}
	
	@Override
	protected void doSave() {
		super.doSave();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("blood_component.xhtml?docAction=create&purchasedNumber=" + fullNumber);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String fullNumber;
	
	private static final long serialVersionUID = -1617448220359489267L;
}