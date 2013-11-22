package ru.efive.medicine.niidg.trfu.uifaces.beans;

import javax.faces.context.FacesContext;

import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListHolderBean;

public class UserSelectModalBean extends AbstractUserSelectModalBean {
	private static final long serialVersionUID = -9107594037615723746L;
	private UserListHolderBean userList;
	
	@Override
	public UserListHolderBean getUserList() {
		if (userList == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			userList = (UserListHolderBean) context.getApplication().evaluateExpressionGet(context, "#{userList}", UserListHolderBean.class);
		}
		return userList;
	}
}