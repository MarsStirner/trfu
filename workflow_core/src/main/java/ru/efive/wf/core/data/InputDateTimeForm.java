package ru.efive.wf.core.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.activity.LocalBackingBean;
import ru.efive.wf.core.activity.enums.EditablePropertyScope;

public class InputDateTimeForm implements LocalBackingBean {

	@Override
	public String getForm() {
		return "<?xml version='1.0' encoding='UTF-8' ?>\n"
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:h=\"http://java.sun.com/jsf/html\" "
        + "xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:e5ui=\"http://efive.ru/uitemplates\" "
        + "xmlns:e5ui-comp=\"http://efive.ru/uitemplates/composite\">\n"
        + "<div id=\"title\">Дата/время</div>\n"
        + "<div style=\"margin-top:0;\">\n"
        + "  <span class=\"title\">Дата/время:</span>\n"
        + "  <div><e5ui-comp:inputDate id=\"modal_date\" value=\"#{" + getBeanName() 
		+ "    .processorModal.processedActivity.document.actionDate}\" lang=\"ru\" immediate=\"true\" /></div>\n"
		+ "  <div><e5ui-comp:inputTime id=\"modal_time\" value=\"#{" + getBeanName() 
		+ "    .processorModal.processedActivity.document.actionTime}\" lang=\"ru\" immediate=\"true\" minutesInterval=\"20\" /></div>\n"
		+ "</div>\n"
        + "</html>";
	}
	
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	
	public Date getActionDate() {
		return actionDate;
	}
	
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}
	
	public Date getActionTime() {
		return actionTime;
	}
	
	public void setScope(EditablePropertyScope scope) {
		this.scope = scope;
	}
	
	public EditablePropertyScope getScope() {
		return scope;
	}
	
	public void setActionDateField(String actionDateField) {
		this.actionDateField = actionDateField;
	}
	
	public String getActionDateField() {
		return actionDateField;
	}
	
	@Override
	public List<EditableProperty> getProperties() {
		return properties == null? new ArrayList<EditableProperty>(): properties;
	}
	
	@Override
	public ActionResult initialize() {
		ActionResult result = new ActionResult();
		try {
			if (actionDate == null || actionTime == null) {
				result.setProcessed(false);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указаны дата или время", ""));
			}
			else {
				Calendar actionCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
				actionCalendar.setTime(getActionDate());
				Calendar actionTimeCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
				actionTimeCalendar.setTime(getActionTime());
				actionCalendar.set(Calendar.HOUR_OF_DAY, actionTimeCalendar.get(Calendar.HOUR_OF_DAY));
				actionCalendar.set(Calendar.MINUTE, actionTimeCalendar.get(Calendar.MINUTE));
				actionCalendar.set(Calendar.SECOND, actionTimeCalendar.get(Calendar.SECOND));
				properties = new ArrayList<EditableProperty>();
				properties.add(new EditableProperty(getActionDateField(), actionCalendar.getTime(), getScope()));
				if (validate().isProcessed()) {
					result.setProcessed(true);
				}
				else {
					result.setProcessed(validate().isProcessed());
					result.setDescription(validate().getDescription());
				}
			}
		}
		catch (Exception e) {
			result.setProcessed(false);
			result.setDescription("Exception in initialize");
			e.printStackTrace();
		}
		return result;
	}
	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanName() {
		return beanName;
	}
	
	public ActionResult validate() {
		ActionResult result = new ActionResult();
		result.setProcessed(true);
		return result;
	}
	
	
	private List<EditableProperty> properties;
	
	private String beanName;
	
	private Date actionDate;
	private Date actionTime;
	private EditablePropertyScope scope;
	private String actionDateField;
}