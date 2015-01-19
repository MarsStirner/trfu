package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import ru.efive.dao.InitializationException;
import ru.efive.dao.alfresco.AlfrescoDAO;
import ru.efive.dao.alfresco.AlfrescoNode;
import ru.efive.dao.sql.dao.DictionaryDAO;
import ru.efive.dao.sql.dao.GenericDAO;
import ru.efive.dao.sql.dao.user.UserDAO;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@Named("sessionManagement")
@SessionScoped
public class SessionManagementBean implements Serializable {
    
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isLoggedIn() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY) != null;
    }
	
	public boolean isRoleSelected() {
		return currentRole != null;
	}
    
    public synchronized void logIn() {
    	if (userName != null && !userName.equals("") && password != null && !password.equals("")) {
    		try {
    			UserDAO dao = getDAO(UserDAO.class, "userDao");
    			loggedUser = dao.findByLoginAndPassword(userName, password);
        		if (loggedUser != null) {
        			FacesContext facesContext = FacesContext.getCurrentInstance();
        			ExternalContext externalContext = facesContext.getExternalContext();
        			externalContext.getSessionMap().put(AUTH_KEY, loggedUser.getLogin());
        			externalContext.getSessionMap().put(AUTH_TYPE, "user");
        			if (loggedUser.getRoleList().size() > 0) {
                		if (loggedUser.getSelectedRole() != null) {
                			currentRole = loggedUser.getSelectedRole();
            			}
                		else {
                			currentRole = loggedUser.getRoleList().get(0);
                			loggedUser.setSelectedRole(currentRole);
                			loggedUser = getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO).save(loggedUser);
                		}
                	}
        		}
        		else {
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
        					FacesMessage.SEVERITY_ERROR, "Введены неверные данные", ""));
        		}
        	}
    		catch (DataAccessResourceFailureException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""))) {
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""));
    			}
        		loggedUser = null;
        		e.printStackTrace();
    		}
    		catch (JDBCConnectionException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			String message = "Ошибка при входе в систему. Попробуйте повторить позже";
    			if (StringUtils.contains(e.getMessage(), "Cannot open connection")) {
    				message = "Отсутствует связь с базой данных";
    			}
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""))) {
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    			}
        		loggedUser = null;
        		e.printStackTrace();
    		}
    		catch (DataAccessException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""))) {
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""));
    			}
        		loggedUser = null;
        		e.printStackTrace();
    		}
        	catch (NullPointerException e) {
        		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
        		if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""))) {
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""));
        		}
        		loggedUser = null;
        		e.printStackTrace();
        	}
    	}
    }
    
    public void donorLogIn() {
    	if (userName != null && !userName.equals("") && password != null && !password.equals("")) {
    		try {
    			DonorDAOImpl dao = getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
    			loggedDonor = dao.findByLoginAndPassword(userName, password);
        		if (loggedDonor != null) {
        			FacesContext facesContext = FacesContext.getCurrentInstance();
        			ExternalContext externalContext = facesContext.getExternalContext();
        			externalContext.getSessionMap().put(AUTH_KEY, loggedDonor.getMail());
        			externalContext.getSessionMap().put(AUTH_TYPE, "donor");
        		}
        		else {
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Введены неверные данные", ""));
        		}
        	}
    		catch (DataAccessResourceFailureException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""))) {
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""));
    			}
    			loggedDonor = null;
        		e.printStackTrace();
    		}
    		catch (JDBCConnectionException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			String message = "Ошибка при входе в систему. Попробуйте повторить позже";
    			if (StringUtils.contains(e.getMessage(), "Cannot open connection")) {
    				message = "Отсутствует связь с базой данных";
    			}
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""))) {
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    			}
    			loggedDonor = null;
        		e.printStackTrace();
    		}
    		catch (DataAccessException e) {
    			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
    			if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""))) {
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""));
    			}
    			loggedDonor = null;
        		e.printStackTrace();
    		}
        	catch (NullPointerException e) {
        		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
        		if (FacesContext.getCurrentInstance().getMessageList().contains(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""))) {
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""));
        		}
        		loggedDonor = null;
        		e.printStackTrace();
        	}
    	}
    }
    
    public void checkDatabaseConnection() {
    	try {
    		UserDAO dao = getDAO(UserDAO.class, "userDao");
			loggedUser = dao.findByLoginAndPassword(getLoggedUser().getLogin(), getLoggedUser().getPassword());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "База данных доступна", ""));
    	}
    	catch (DataAccessResourceFailureException e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""));
    		e.printStackTrace();
		}
		catch (JDBCConnectionException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""));
    		e.printStackTrace();
		}
		catch (DataAccessException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""));
    		e.printStackTrace();
		}
    	catch (NullPointerException e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""));
    		e.printStackTrace();
    	}
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends DictionaryDAO> T getDictionaryDAO(Class<T> persistentClass, String beanName) {
    	return (T) indexManagement.getContext().getBean(beanName);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends GenericDAO> T getDAO(Class<T> persistentClass, String beanName) {
    	return (T) indexManagement.getContext().getBean(beanName);
    }
    
    public synchronized <M extends AlfrescoNode> AlfrescoDAO<M> getAlfrescoDAO(Class<M> class_) {
		AlfrescoDAO<M> dao = null;
		try {
			dao = new AlfrescoDAO<M>(class_);
			dao.setUserName("admin");
			dao.setPassword("admin");
			if (!dao.connect()) throw new InitializationException();
		}
		catch (InitializationException e) {
			System.out.println("Unable to instantiate connection to Alfresco remote service");
			dao = null;
		}
		return dao;
	}
    
    public synchronized String logOut() {
    	loggedUser = null;
    	currentRole = null;
    	userName = null;
    	password = null;
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	ExternalContext externalContext = facesContext.getExternalContext();
    	externalContext.getSessionMap().remove(AUTH_KEY);
    	externalContext.getSessionMap().remove(AUTH_TYPE);
    	System.out.println("is logged: " + isLoggedIn());
    	return "/index?faces-redirect=true";//
    }
    
    public User getLoggedUser() {
    	return loggedUser;
    }
    
	public Donor getLoggedDonor() {
		return loggedDonor;
	}
	
	public String setCurrentRole(Role currentRole) {
		this.currentRole = currentRole;
		try {
			loggedUser.setSelectedRole(currentRole);
			loggedUser = getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO).save(loggedUser);
			if (reportList != null) {
				reportList.markNeedRefresh();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return getNavigationRule() + "?faces-redirect=true";
	}
	
	public Role getCurrentRole() {
		return currentRole;
	}
	
	public List<Role> getAvailableRoles() {
		List<Role> result = new ArrayList<Role>();
		List<Role> list = loggedUser.getRoleList();
		for (Role role: list) {
			if (!currentRole.equals(role)) {
				result.add(role);
			}
		}
		Collections.sort(result, new Comparator<Role>() {
			
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getName().compareTo(o2.getName());
			}
			
		});
		return result;
	}

	public String getTemplateName() {
    	StringBuffer buffer = new StringBuffer("/WEB-INF/template/");
    	if (currentRole == null) {
    		buffer.append("minimal.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
    		buffer.append("admin_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.REGISTRATOR)) {
    		buffer.append("registrator_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.THERAPIST)) {
    		buffer.append("therapist_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.EXPEDITION)) {
    		buffer.append("expedition_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.QUARANTINE)) {
    		buffer.append("quarantine_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.DRAFT_OUT)) {
    		buffer.append("therapist_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.LABELING)) {
    		buffer.append("therapist_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.OPERATIONAL)) {
    		buffer.append("operational_template.xhtml");
			//TODO
//    		if (operationalSession != null && operationalSession.isExpired()) {
//    			operationalSession.initDocument();
//    		}
    	}
    	else if (currentRole.getRoleType().equals(RoleType.RECTIFICATION)) {
    		buffer.append("rectification_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.LABORATORY)) {
    		buffer.append("laboratory_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.IN_CONTROL)) {
    		buffer.append("control_template.xhtml");
    	}
    	/*else if (currentRole.getRoleType().equals(RoleType.INACTIVATION)) {
    		buffer.append("inactivation_template.xhtml");
    	}*/
    	else if (currentRole.getRoleType().equals(RoleType.HEAD_NURSE)) {
    		buffer.append("head_nurse_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.DIVISION_SUPERINTENDENT)) {
    		buffer.append("division_superintendent_template.xhtml");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.MEDICAL)) {
    		buffer.append("medical_template.xhtml");
		} 
    	else if (currentRole.getRoleType().equals(RoleType.VIRUSINACTIVATION)) {
    		buffer.append("virusinactivation_template.xhtml");
    	}
    	else {
    		buffer.append("minimal.xhtml");
    	}
    	return buffer.toString();
    }
    
	private String getNavigationRule() {
		StringBuffer buffer = new StringBuffer("/component/");
		if (currentRole == null) {
			buffer.append("filter/donors");
		}
		else if (currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
    		buffer.append("admin/settings");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.REGISTRATOR)) {
    		buffer.append("filter/donors");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.THERAPIST)) {
    		buffer.append("personal_requests");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.EXPEDITION)) {
    		buffer.append("blood_components_ready");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.QUARANTINE)) {
    		buffer.append("blood_components_in_quarantine");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.DRAFT_OUT)) {
    		buffer.append("blood_components");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.LABELING)) {
    		buffer.append("blood_components");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.OPERATIONAL)) {
    		buffer.append("blood_donations_operational");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.RECTIFICATION)) {
    		buffer.append("blood_donations_rectification");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.LABORATORY)) {
    		buffer.append("laboratory");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.IN_CONTROL)) {
    		buffer.append("blood_components_in_control");
    	}
    	/*else if (currentRole.getRoleType().equals(RoleType.INACTIVATION)) {
    		buffer.append("inactivation");
    	}*/
    	else if (currentRole.getRoleType().equals(RoleType.HEAD_NURSE)) {
    		buffer.append("blood_components_quarantined");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.DIVISION_SUPERINTENDENT)) {
    		buffer.append("filter/donors");
    	}
    	else if (currentRole.getRoleType().equals(RoleType.MEDICAL)) {
    		buffer.append("medical/donors");
		}
    	else if (currentRole.getRoleType().equals(RoleType.VIRUSINACTIVATION)) {
    		buffer.append("virusinactivation");
		}
    	else {
    		buffer.append("filter/donors");
    	}
    	return buffer.toString();
	}
	
    public String getStartTemplateName() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        return externalContext.encodeActionURL(facesContext.getApplication().getViewHandler().getActionURL(facesContext, getNavigationRule() + ".xhtml"));
    }
    
    public boolean isAdmin() {
    	return currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION);
    }
    
    public boolean isOperational() {
    	return currentRole.getRoleType().equals(RoleType.OPERATIONAL);
    }
    
    
	private User loggedUser;
	private Donor loggedDonor;
	private Role currentRole;
	
	private String userName;
	private String password;
    
	
	@Inject @Named("indexManagement")
	private transient IndexManagementBean indexManagement;
	@Inject @Named("reportTemplateList")
	private transient ReportTemplateListBean reportList;
	@Inject @Named("operationalSession")
	private transient OperationalSessionBean operationalSession;
	
    
    public static final String AUTH_KEY = "app.user.name";
    public static final String AUTH_TYPE = "app.user.type";
    
    private static final long serialVersionUID = -916300301346029630L;
}