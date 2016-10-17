package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.dao.sql.entity.document.ReportTemplate;
import ru.efive.medicine.niidg.trfu.dao.ReportDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.REPORT_DAO;

@Named("reportTemplate")
@ConversationScoped
public class ReportTemplateHolderBean extends AbstractDocumentHolderBean<ReportTemplate, Integer> implements Serializable {

	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).delete(getDocument());
			result = true;
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при удалении документа", ""));
		}
		return result;
	}
	
	@Override
	protected Integer getDocumentId() {
		return getDocument().getId();
	}
	
	@Override
	protected FromStringConverter<Integer> getIdConverter() {
		return FromStringConverter.INTEGER_CONVERTER;
	}
	
	@Override
	protected void initDocument(Integer id) {
		ReportTemplate reportTemplate = sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).get(id);
		reportTemplate.setStartDate(new Date());
		reportTemplate.setEndDate(new Date());
		setDocument(reportTemplate);
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
		else {
			setState(STATE_EDIT);
		}
	}
	
	@Override
	protected void initNewDocument() {
		consumableMaterial = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("consumableMaterial");
		String templateName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("templateName");
		if (templateName != null && !templateName.equals("")) {
			ReportTemplate reportTemplate = sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).findTemplateByName(templateName);
			if (reportTemplate != null) {
				reportTemplate.setStartDate(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
				try {
					setDocument(reportTemplate);
					//reportsManagement.sqlPrintReportByRequestParams(getDocument());
					setState(STATE_EDIT);
				}
				catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при формировании отчета.", ""));
					e.printStackTrace();
				}
			}
			else {
				reportTemplate = new ReportTemplate();
				reportTemplate.setDeleted(false);
				setDocument(reportTemplate);
			}
		}
		else {
			ReportTemplate template = new ReportTemplate();
			template.setDeleted(false);
			setDocument(template);
		}
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			ReportTemplate template = sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).save(getDocument());
			if (template == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			}
			else {
				setDocument(template);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected boolean saveNewDocument() {
		boolean result = false;
		try {
			ReportTemplate template = sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).save(getDocument());
			if (template == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			}
			else {
				setDocument(template);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean saveReport() {
		boolean result = false;
		try {
			ReportTemplate template = sessionManagement.getDAO(ReportDAOImpl.class, REPORT_DAO).save(getDocument());
			if (template == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			}
			else {
				setDocument(template);
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean previewReport() {
		boolean result = false;
		try {
			Map<String, Object> genericProperties = new HashMap<>();
			if (propertiesHolder != null) {
				genericProperties.put("Institution", propertiesHolder.getProperty("application", "reports.institution.name"));
				genericProperties.put("Division", propertiesHolder.getProperty("application", "reports.division.name"));
				genericProperties.put("Address", propertiesHolder.getProperty("application", "reports.institution.address"));
				genericProperties.put("HeadDoctor", propertiesHolder.getProperty("application", "reports.institution.headdoctor"));
				genericProperties.put("BacteriologicalSuperintendent", propertiesHolder.getProperty("application", "reports.bacteriological.superintendent"));
				genericProperties.put("Manager", propertiesHolder.getProperty("application", "reports.institution.manager"));
				genericProperties.put("DeputyChiefAccountant", propertiesHolder.getProperty("application", "reports.institution.deputychiefaccountant"));
				genericProperties.put("Superintendent", propertiesHolder.getProperty("application", "reports.division.superintendent"));
			}
			
			String consumableMaterial = getConsumableMaterial();
			if (consumableMaterial != null) {
				genericProperties.put("ConsumableMaterial", consumableMaterial);
			}
			reportsManagement.sqlPrintReportByRequestParams(getDocument(), genericProperties);
			result = true;
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка при формировании отчета", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	private String getConsumableMaterial() {
    	return consumableMaterial;
    }
	
	
	public class ComponentRequestSelectModalBean extends ModalWindowHolderBean {

		public BloodComponentOrderListHolderBean getBloodComponentOrderList() {
			return bloodComponentOrderList;
		}
		
		public List<BloodComponentOrderRequest> getRequestList() {
			return requestList;
		}
		
		public void select(BloodComponentOrderRequest request) {
			requestList.add(request);		
		}

		public void unselect(BloodComponentOrderRequest request) {
			requestList.remove(request);
		}

		public boolean selected(BloodComponentOrderRequest request) {
			return requestList.contains(request);
		}
		
		@Override
		protected void doSave() {
			super.doSave();
			ReportTemplate reportTemplate = getDocument();
			reportTemplate.setComponentRequestList(requestList);
			setDocument(reportTemplate);
		}
		
		@Override
		protected void doShow() {
			super.doShow();
			requestList = new ArrayList<>();
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			if (bloodComponentOrderList != null) {
				bloodComponentOrderList.setFilter("");
				bloodComponentOrderList.markNeedRefresh();
			}
		}
		
		private List<BloodComponentOrderRequest> requestList;

		private static final long serialVersionUID = -9107594037615723746L;
	}
	
	public ComponentRequestSelectModalBean getComponentRequestSelectModal() {
    	return componentRequestSelectModal;
    }
	
	private String consumableMaterial;
	private ComponentRequestSelectModalBean componentRequestSelectModal = new ComponentRequestSelectModalBean();
    
	
    @Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
    @Inject @Named("reports")
    private transient ReportsManagmentBean reportsManagement = new ReportsManagmentBean();
    @Inject @Named("bloodComponentOrderList")
    private transient BloodComponentOrderListHolderBean bloodComponentOrderList;
    @Inject @Named("propertiesHolder")
    private transient ApplicationPropertiesHolder propertiesHolder;
    
	private static final long serialVersionUID = 1L;
}