package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named("versionInfo")
@ConversationScoped
public class VersionInfo implements Serializable {
	private static final long serialVersionUID = 6929693412341369918L;
	private String versionAssembly = "0.3.51";
	private String dateAssembly = "13.09.2013 12:25:00";
	
	public String getVersionAssembly() {
		return versionAssembly;
	}
	public void setVersionAssembly(String versionAssembly) {
		this.versionAssembly = versionAssembly;
	}
	public String getDateAssembly() {
		return dateAssembly;
	}
	public void setDateAssembly(String dateAssembly) {
		this.dateAssembly = dateAssembly;
	}
}
