package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.crm.data.Contragent;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.BLOOD_COMPONENT_DAO;

@Named("bloodComponenFilterList")
@ViewScoped
public class BloodComponentFilterListHolderBean extends AbstractDocumentListHolderBean<BloodComponent> {
	private static final Logger logger = LoggerFactory.getLogger(BloodComponentFilterListHolderBean.class);

	@PostConstruct
	public void init(){
		logger.info("Init");
	}

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	public Sorting initSorting() {
		return new Sorting("parentNumber,number", true);
	}

	@Override
	protected int getTotalCount() {
		logger.info("get count start");
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(BloodComponentDAOImpl.class, BLOOD_COMPONENT_DAO).countDocument(in_filters, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("get count end");
		return result;
	}

	@Override
	protected List<BloodComponent> loadDocuments() {
		logger.info("load start");
		List<BloodComponent> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(BloodComponentDAOImpl.class, BLOOD_COMPONENT_DAO).findDocuments(in_filters, false,
					getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно осуществить поиск", ""));
			e.printStackTrace();
		}
		logger.info("load end");
		return result;
	}

	@Override
	public List<BloodComponent> getDocuments() {
		return super.getDocuments();
	}
	
	public Map<String, Object> getIn_filters() {
		return in_filters;
	}
	
	public void setIn_filters(Map<String, Object> in_filters) {
		this.in_filters = in_filters;
	}
	
	public BloodComponentType getComponentType() {
		Object result = in_filters.get("componentType");
		return result == null? null: (BloodComponentType) result;
	}
	
	public void setComponentType(BloodComponentType componentType) {
		if (componentType != null && componentType.getId() != 0) {
			in_filters.put("componentType", componentType);
		}
		else {
			in_filters.remove("componentType");
		}
	}
	
	public String getStatusName() {
		Object result = in_filters.get("statusId");
		return result == null? "": ApplicationHelper.getStatusName("BloodComponent", (Integer) result);
	}
	
	public void setStatusName(String statusName) {
		if (StringUtils.isNotEmpty(statusName)) {
			in_filters.put("statusId", ApplicationHelper.getStatusId("BloodComponent", statusName));
		}
		else {
			in_filters.remove("statusId");
		}
	}
	
	public Contragent getMaker() {
		Object result = in_filters.get("maker");
		return result == null? null: (Contragent) result;
	}
	
	public void setMaker(Contragent maker) {
		if (maker != null && maker.getId() != 0) {
			in_filters.put("maker", maker);
		}
		else {
			in_filters.remove("maker");
		}
	}
	
	public BloodGroup getBloodGroup() {
		Object result = in_filters.get("bloodGroup");
		return result == null? null: (BloodGroup) result;
	}
	
	public void setBloodGroup(BloodGroup bloodGroup) {
		if (bloodGroup != null && bloodGroup.getId() != 0) {
			in_filters.put("bloodGroup", bloodGroup);
		}
		else {
			in_filters.remove("bloodGroup");
		}
	}

	public Classifier getRhesusFactor() {
		Object result = in_filters.get("rhesusFactor");
		return result == null? null: (Classifier) result;
	}
	
	public void setRhesusFactor(Classifier rhesusFactor) {
		if (rhesusFactor != null && rhesusFactor.getId() != 0) {
			in_filters.put("rhesusFactor", rhesusFactor);
		}
		else {
			in_filters.remove("rhesusFactor");
		}
	}
	
	public Date getStartReadyDate() {
		return (Date) in_filters.get("startReadyDate");
	}
	
	public void setStartReadyDate(Date startReadyDate) {
		in_filters.put("startReadyDate", startReadyDate);
	}
	
	public Date getEndReadyDate() {
		return (Date) in_filters.get("endReadyDate");
	}
	
	public void setEndReadyDate(Date endReadyDate) {
		in_filters.put("endReadyDate", endReadyDate);
	}
	
	public Date getStartDistributedDate() {
		return (Date) in_filters.get("startDistributedDate");
	}
	
	public void setStartDistributedDate(Date startDistributedDate) {
		in_filters.put("startDistributedDate", startDistributedDate);
	}
	
	public Date getEndDistributedDate() {
		return (Date) in_filters.get("endDistributedDate");
	}
	
	public void setEndDistributedDate(Date endDistributedDate) {
		in_filters.put("endDistributedDate", endDistributedDate);
	}
	
	public void clear() {
		try {
			in_filters = new HashMap<>();
			refresh();
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Невозможно осуществить поиск", ""));
			e.printStackTrace();
		}
	}
	
	
	private Map<String, Object> in_filters = new HashMap<>();

	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = 1L;
}