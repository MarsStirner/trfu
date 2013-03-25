package ru.efive.wf.core.data;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.activity.LocalBackingBean;
import ru.efive.wf.core.activity.enums.EditablePropertyScope;

public class InputReasonForm implements LocalBackingBean {

	@Override
	public String getForm() {
		return "<?xml version='1.0' encoding='UTF-8' ?>\n"
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:h=\"http://java.sun.com/jsf/html\" "
        + "xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:e5ui=\"http://efive.ru/uitemplates\">\n"
        + "<div id=\"title\"><h:outputText value=\"#{" + getBeanName() 
		+ ".processorModal.processedActivity.document.title}\" /></div>\n"
        + "<div style=\"margin-top:10px;\">\n"
		+ "  <h:inputText id=\"action_comment\" styleClass=\"wide\" immediate=\"true\" value=\"#{" + getBeanName() 
		+ ".processorModal.processedActivity.document.actionCommentary}\" />\n"
		+ "</div>\n"
        + "</html>";
	}
	
	public void setActionCommentary(String actionCommentary) {
		this.actionCommentary = actionCommentary;
	}
	
	public String getActionCommentary() {
		return actionCommentary;
	}
	
	public void setScope(EditablePropertyScope scope) {
		this.scope = scope;
	}
	
	public EditablePropertyScope getScope() {
		return scope;
	}
	
	public void setActionCommentaryField(String actionCommentaryField) {
		this.actionCommentaryField = actionCommentaryField;
	}
	
	public String getActionCommentaryField() {
		return actionCommentaryField;
	}
	
	public String getTitle() {
		return StringUtils.isEmpty(title)? "Комментарий": title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public List<EditableProperty> getProperties() {
		return properties == null? new ArrayList<EditableProperty>(): properties;
	}
	
	@Override
	public ActionResult initialize() {
		ActionResult result = new ActionResult();
		try {
			if (actionCommentary == null || actionCommentary.equals("")) {
				result.setProcessed(false);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указан комментарий", ""));
			}
			else {
				properties = new ArrayList<EditableProperty>();
				properties.add(new EditableProperty(getActionCommentaryField(), getActionCommentary(), getScope()));
				result.setProcessed(true);
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
	
	
	private List<EditableProperty> properties;
	
	private String beanName;
	
	private String actionCommentary;
	private EditablePropertyScope scope;
	private String actionCommentaryField;
	private String title;
}