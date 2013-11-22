package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.dao.sql.entity.user.User;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

public abstract class AbstractUserSelectModalBean extends ModalWindowHolderBean {
	private static final long serialVersionUID = -5494617618006804930L;
	private User user;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void select(User user) {
		this.user = user;
	}
	
	public boolean selected(User user) {
		return this.user == null? false: this.user.equals(user);
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
	
	public abstract AbstractDocumentListHolderBean<User> getUserList();
}
