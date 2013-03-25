package ru.efive.dao.sql.entity.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;

@Entity
@Table(name = "report_templates")
public class ReportTemplate extends IdentifiedEntity {
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setType(byte type) {
		this.type = type;
	}
	
	public byte getType() {
		return type;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	@Transient
	public String getStartDateString() {
		System.out.println(startDate == null? "": new java.text.SimpleDateFormat("dd.MM.yyyy").format(startDate));
		return startDate == null? "": new java.text.SimpleDateFormat("dd.MM.yyyy").format(startDate);
	}
	
	public void setStartAlias(String startAlias) {
		this.startAlias = startAlias;
	}
	
	public String getStartAlias() {
		return startAlias;
	}
	
	public void setStartDescription(String startDescription) {
		this.startDescription = startDescription;
	}
	
	public String getStartDescription() {
		return startDescription;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	@Transient
	public String getEndDateString() {
		System.out.println(endDate == null? "": new java.text.SimpleDateFormat("dd.MM.yyyy").format(endDate));
		return endDate == null? "": new java.text.SimpleDateFormat("dd.MM.yyyy").format(endDate);
	}
	
	public void setEndAlias(String endAlias) {
		this.endAlias = endAlias;
	}
	
	public String getEndAlias() {
		return endAlias;
	}
	
	public void setEndDescription(String endDescription) {
		this.endDescription = endDescription;
	}
	
	public String getEndDescription() {
		return endDescription;
	}
	
	public void setComponentRequestList(List<BloodComponentOrderRequest> componentRequestList) {
		this.componentRequestList = componentRequestList;
	}
	
	public List<BloodComponentOrderRequest> getComponentRequestList() {
		return componentRequestList;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public void setDisplayInFolders(boolean displayInFolders) {
		this.displayInFolders = displayInFolders;
	}

	public boolean isDisplayInFolders() {
		return displayInFolders;
	}

	public void setReportParameters(List<ReportParameter> reportParameters) {
		this.reportParameters = reportParameters;
	}

	public List<ReportParameter> getReportParameters() {
		return reportParameters;
	}
	
	@Transient
	public Map<String,Object> getProperties() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("reportName", templateName);
		if (type == 2) {
			List<Integer> ids = new ArrayList<Integer>();
			for (BloodComponentOrderRequest request: componentRequestList) {
				ids.add(request.getId());
			}
			result.put(startAlias, ids);
		}
		else if (type == 3) {
			for (ReportParameter parameter: reportParameters) {
				result.put(parameter.getAlias(), parameter.getValue());
			}
		}
		else {
			result.put(startAlias, startDate);
			if (type == 1) {
				result.put(endAlias, endDate);
			}
		}
		return result;
	}


	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public List<Role> getRoleList() {
		List<Role> result = new ArrayList<Role>();
		if (roles != null) {
			result.addAll(roles);
		}
		Collections.sort(result, new Comparator<Role>() {
			public int compare(Role o1, Role o2) {
				return o1.getId() - o2.getId();
			}
		});
		return result;
	}
	

	/**
	 * Отображаемое в списке название
	 */
	private String displayName;
	
	/**
	 * Название шаблона
	 */
	private String templateName;
	
	/**
	 * Тип отчета (0 - за дату, 1 - за период, 2 - выбор заявки на заказ КК из списка, 3 - список текстовых параметров))
	 */
	private byte type;
	
	@Transient
	private Date startDate;
	
	private String startAlias;
	
	private String startDescription;
	
	@Transient
	private Date endDate;
	
	private String endAlias;
	
	private String endDescription;
	
	@Transient
	private List<BloodComponentOrderRequest> componentRequestList;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "report_template_parameters", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "parameter_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ReportParameter> reportParameters;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "report_template_roles", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	@IndexColumn(name="id")
	private Set<Role> roles;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	/**
	 * Отображение в общем списке
	 */
	private boolean displayInFolders = true;
	
	
	private static final long serialVersionUID = -5877947826192366106L;
}