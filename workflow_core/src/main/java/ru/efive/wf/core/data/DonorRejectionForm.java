package ru.efive.wf.core.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ru.efive.medicine.niidg.trfu.data.dictionary.DonorRejectionType;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.activity.LocalBackingBean;
import ru.efive.wf.core.activity.enums.EditablePropertyScope;

public class DonorRejectionForm implements LocalBackingBean {
	
	public void setRejectionType(DonorRejectionType rejectionType) {
		this.rejectionType = rejectionType;
	}
	
	public DonorRejectionType getRejectionType() {
		return rejectionType;
	}

	@Override
	public String getForm() {
		return "<?xml version='1.0' encoding='UTF-8' ?>\n"
        + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
        + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:h=\"http://java.sun.com/jsf/html\" "
        + "xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:e5ui=\"http://efive.ru/uitemplates\">\n"
        + "<div id=\"title\">Выбор причины отвода</div>\n"
        + "<div id=\"document_list\" style=\"margin-top:0;\">\n"
        + "  <div class=\"searchbar\">\n"
        + "    <h:inputText id=\"filter_string\" value=\"#{donorRejectionTypeList.filter}\"\n"
		+ "          style=\"display:block; float:left; margin-right:10px;\" title=\"Поиск\" />\n"
		+ "    <h:commandButton value=\" \" action=\"#{donorRejectionTypeList.changePageOffset(0)}\" styleClass=\"searchbutton\">\n"
		+ "      <f:ajax execute=\":main_content_form:actionsModal:filter_string\"\n"
		+ "            render=\":main_content_form:actionsModal:modal_select_table\" />\n"
		+ "    </h:commandButton>\n"
		+ "  </div>\n"
		+ "  <div class=\"wrap\" style=\"height:300px; background-color:#fff; clear:both;\">\n"
		+ "    <div class=\"inner\" style=\"height:300px;\">\n"
		+ "      <e5ui:dataTable id=\"modal_select_table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n"
		+ "            value=\"#{donorRejectionTypeList.documents}\"\n"
		+ "		       var=\"row\" grouping=\"false\" width=\"100%\">\n"
		+ "        <e5ui:row onclick=\"e5ui_util.clickElement(this, 'selectLink$');\"\n"
		+ "              styleClass=\"#{" + getBeanName() + ".processorModal.processedActivity.document.selected(row)? 'grid_row_selected': ''}\" />\n"
		+ "        <e5ui:column>\n"
		+ "          <f:facet name=\"header\">\n"
		+ "            <p>Код ЕДЦ</p>\n"
		+ "          </f:facet>\n"
		+ "          <h:outputText value=\"#{row.code}\" />\n"
		+ "          <h:commandLink id=\"selectLink\" style=\"display: none;\""
		+ "                rendered=\"#{not " + getBeanName() + ".processorModal.processedActivity.document.selected(row)}\"\n"
		+ "                action=\"#{" + getBeanName() + ".processorModal.processedActivity.document.select(row)}\"/>\n"
		+ "        </e5ui:column>\n"
		+ "        <e5ui:column>\n"
		+ "          <f:facet name=\"header\">\n"
		+ "            <p>Причина отвода</p>\n"
		+ "          </f:facet>\n"
		+ "          <h:outputText value=\"#{row}\" converter=\"DonorRejectionTypeConverter\" />\n"
		+ "        </e5ui:column>\n"
		+ "        <e5ui:column>\n"
		+ "          <f:facet name=\"header\">\n"
		+ "            <p>Тип отвода</p>\n"
		+ "          </f:facet>\n"
		+ "          <h:outputText value=\"#{row.displayedType}\" />\n"
		+ "        </e5ui:column>\n"
		+ "      </e5ui:dataTable>\n"
		+ "    </div>\n"
		+ "  </div>\n"
		+ "</div>\n"
        + "</html>";
	}
	
	@Override
	public List<EditableProperty> getProperties() {
		return properties == null? new ArrayList<EditableProperty>(): properties;
	}
	
	@Override
	public ActionResult initialize() {
		ActionResult result = new ActionResult();
		try {
			if (rejectionType == null) {
				result.setProcessed(false);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Не выбрана причина отвода", ""));
			}
			properties = new ArrayList<EditableProperty>();
			DonorRejection rejection = new DonorRejection();
			rejection.setRejectionType(rejectionType);
			Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
			rejection.setCreated(calendar.getTime());
			if (rejectionType.getType() == 0) {
				if (rejectionType.getYears() != 0) calendar.add(Calendar.YEAR, rejectionType.getYears());
				if (rejectionType.getMonths() != 0) calendar.add(Calendar.MONTH, rejectionType.getMonths());
				if (rejectionType.getDays() != 0) calendar.add(Calendar.DATE, rejectionType.getDays());
				rejection.setExpiration(calendar.getTime());
			}
			properties.add(new EditableProperty("rejection", rejection, EditablePropertyScope.GLOBAL));
			result.setProcessed(true);
		}
		catch (Exception e) {
			result.setProcessed(false);
			result.setDescription("Exception in initialize");
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean selected(DonorRejectionType rejectionType) {
		return this.rejectionType != null && this.rejectionType.equals(rejectionType);
	}
	
	public void select(DonorRejectionType rejectionType) {
		this.rejectionType = rejectionType;
	}
	
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanName() {
		return beanName;
	}
	
	
	private DonorRejectionType rejectionType;
	
	private List<EditableProperty> properties;
	
	private String beanName;
}