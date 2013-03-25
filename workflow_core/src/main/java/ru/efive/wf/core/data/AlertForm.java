package ru.efive.wf.core.data;

import java.util.ArrayList;
import java.util.List;

import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.activity.LocalBackingBean;

public class AlertForm implements LocalBackingBean {

	@Override
	public String getForm() {
		return "<?xml version='1.0' encoding='UTF-8' ?>\n"
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:h=\"http://java.sun.com/jsf/html\" xmlns:ui=\"http://java.sun.com/jsf/facelets\" "
        + "xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:e5ui=\"http://efive.ru/uitemplates\">\n"
        + "<div id=\"title\">Внимание</div>\n"
        + "<div style=\"margin-top:0;\">\n"
        + "  <ui:repeat value=\"#{" + getBeanName() + ".processorModal.processedActivity.document.alert.split('@')}\" var=\"element\">\n"
        + "    <h:outputText value=\"#{element}\" /><br />\n"
		+ "  </ui:repeat>\n"
		+ "</div>\n"
        + "</html>";
	}
	
	public void setAlert(String alert) {
		this.alert = alert;
	}
	
	public String getAlert() {
		return alert;
	}
	
	@Override
	public List<EditableProperty> getProperties() {
		return new ArrayList<EditableProperty>();
	}
	
	@Override
	public ActionResult initialize() {
		ActionResult result = new ActionResult();
		result.setProcessed(true);
		return result;
	}
	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanName() {
		return beanName;
	}
	
	
	private String beanName;
	
	private String alert;
}