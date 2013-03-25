package ru.efive.medicine.niidg.trfu.uifaces;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;

public class SessionListener implements HttpSessionListener {
	
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}
	
	public void sessionDestroyed(HttpSessionEvent arg0) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null && context.getApplication() != null) {
			try {
				((HttpSession) context.getExternalContext().getSession(false)).invalidate();
			}
			catch (Exception e) {
				System.out.println("Invalidate session issue: " + e.getMessage());
			}
			SessionManagementBean sessionManagement = (SessionManagementBean) 
			context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			if (sessionManagement != null && sessionManagement.isLoggedIn()) {
				sessionManagement.logOut();
			}
		}
	}
	
}