package ru.efive.wf.core.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.activity.LocalBackingBean;
import ru.efive.wf.core.activity.enums.EditablePropertyScope;

public class InputDateForm implements LocalBackingBean {

	@Override
	public String getForm() {
		return "<?xml version='1.0' encoding='UTF-8' ?>\n"
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:h=\"http://java.sun.com/jsf/html\" "
        + "xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:e5ui=\"http://efive.ru/uitemplates\" "
        + "xmlns:e5ui-comp=\"http://efive.ru/uitemplates/composite\">\n"
        + "<div id=\"title\">Дата</div>\n"
        + "<div style=\"margin-top:0;\">\n"
        + "  <span class=\"title\">Дата:</span>\n"
        + "  <e5ui-comp:inputDate id=\"modal_date\" value=\"#{" + getBeanName() 
		+ "    .processorModal.processedActivity.document.actionDate}\" lang=\"ru\" immediate=\"true\" />\n"
		+ "</div>\n"
        + "</html>";
	}
	
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	
	public Date getActionDate() {
		return actionDate;
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
			if (actionDate == null) {
				result.setProcessed(false);
				result.setDescription("Не указана дата");
			}
			else {
				if (validate().isProcessed()) {
					properties = new ArrayList<EditableProperty>();
					properties.add(new EditableProperty(getActionDateField(), getActionDate(), getScope()));
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
	private EditablePropertyScope scope;
	private String actionDateField;
}