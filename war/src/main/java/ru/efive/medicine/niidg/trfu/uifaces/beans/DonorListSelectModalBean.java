package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class DonorListSelectModalBean extends ModalWindowHolderBean {

    public DonorListHolderBean getDonorList() {
        if (donorList == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            donorList =  context.getApplication().evaluateExpressionGet(context, "#{donorList}", DonorListHolderBean.class);
        }
        return donorList;
    }

    public List<Donor> getDonors() {
        return donors;
    }

    public void setDonors(List<Donor> donors) {
        if(donors==null){
            this.donors=new ArrayList<Donor>();
        }else{
            this.donors=donors;
        }
    }

    public void select(Donor donor) {
        donors.add(donor);
    }

    public void unselect(Donor donor) {
        donors.remove(donor);
    }

    public boolean selected(Donor donor) {
        return donors.contains(donor);
    }

    @Override
    protected void doSave() {
        super.doSave();
    }

    @Override
    protected void doShow() {
        super.doShow();
    }

    @Override
    protected void doHide() {
        super.doHide();
    }


    private DonorListHolderBean donorList;

    private List<Donor> donors = new ArrayList<Donor>();

    private static final long serialVersionUID = -9107594037615723746L;
}
