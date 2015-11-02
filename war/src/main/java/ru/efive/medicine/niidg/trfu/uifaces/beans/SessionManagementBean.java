package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
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
import ru.hitsl.helper.AuthorizationData;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import static ru.efive.medicine.niidg.trfu.uifaces.beans.utils.MessageHolder.*;

@Named("sessionManagement")
@SessionScoped
public class SessionManagementBean implements Serializable {

    public static final String AUTH_KEY = "app.user.name";
    public static final String BACK_URL = "app.back.url";
    public static final String AUTH_TYPE = "app.user.type";

    private final static Logger LOGGER = LoggerFactory.getLogger("AUTH");
    private static final long serialVersionUID = -916300301346029630L;

    private AuthorizationData authData;

    private String userName;
    private String password;
    private String backUrl;
    private Role currentRole;

    private Donor loggedDonor;

    @Inject
    @Named("indexManagement")
    private transient IndexManagementBean indexManagement;

    @Inject
    @Named("operationalSession")
    private transient OperationalSessionBean operationalSession;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY) != null;
    }

    public synchronized void logIn() {
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            LOGGER.info("Try to login [{}][{}]", userName, password);
            try {
                UserDAO dao = getDAO(UserDAO.class, "userDao");
                User loggedUser = dao.findByLoginAndPassword(userName, password);
                if (loggedUser != null) {
                    LOGGER.debug("By userName[{}] founded User[{}]", userName, loggedUser.getId());
                    //Проверка удаленности\уволенности сотрудника
                    if (loggedUser.isDeleted()) {
                        LOGGER.error("USER[{}] IS DELETED", loggedUser.getId());
                        FacesContext.getCurrentInstance().addMessage(null, MSG_AUTH_DELETED);
                        return;
                    }
                    //Проверка наличия у пользователя ролей
                    if (loggedUser.getRoles().isEmpty()) {
                        LOGGER.warn("USER[{}] HAS NO ONE ROLE", loggedUser.getId());
                        FacesContext.getCurrentInstance().addMessage(null, MSG_AUTH_NO_ROLE);
                        return;
                    }

                        if (loggedUser.getSelectedRole() != null) {
                            currentRole = loggedUser.getSelectedRole();
                        } else {
                            currentRole = loggedUser.getRoleList().get(0);
                            loggedUser.setSelectedRole(currentRole);
                            loggedUser = getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO).save(loggedUser);
                        }

                    this.authData = new AuthorizationData(loggedUser);

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(AUTH_KEY, loggedUser.getLogin());

                    Object requestUrl = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(BACK_URL);
                    if (requestUrl != null) {
                        backUrl = requestUrl.toString();
                        LOGGER.info("back url={}", backUrl);
                    }

                    LOGGER.info("SUCCESSFUL LOGIN:{}\n AUTH_DATA={}", loggedUser.getId(), authData);
                } else {
                    LOGGER.error("USER[{}] NOT FOUND", userName);
                    FacesContext.getCurrentInstance().addMessage(null, MSG_AUTH_NOT_FOUND);
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
                FacesContext.getCurrentInstance().addMessage(null, MSG_AUTH_ERROR);
                this.authData = null;
                LOGGER.error("Exception while processing login action:", e);
            }
        }
    }

    @Deprecated
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
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Введены неверные данные", ""
                            )
                    );
                }
            } catch (DataAccessResourceFailureException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
                if (FacesContext.getCurrentInstance().getMessageList().contains(
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""
                        )
                )) {
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Отсутствует связь с базой данных", ""
                            )
                    );
                }
                loggedDonor = null;
                e.printStackTrace();
            } catch (JDBCConnectionException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
                String message = "Ошибка при входе в систему. Попробуйте повторить позже";
                if (StringUtils.contains(e.getMessage(), "Cannot open connection")) {
                    message = "Отсутствует связь с базой данных";
                }
                if (FacesContext.getCurrentInstance().getMessageList().contains(
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, message, ""
                        )
                )) {
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, message, ""
                            )
                    );
                }
                loggedDonor = null;
                e.printStackTrace();
            } catch (DataAccessException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
                if (FacesContext.getCurrentInstance().getMessageList().contains(
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""
                        )
                )) {
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Ошибка доступа к базе данных", ""
                            )
                    );
                }
                loggedDonor = null;
                e.printStackTrace();
            } catch (NullPointerException e) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(AUTH_KEY);
                if (FacesContext.getCurrentInstance().getMessageList().contains(
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""
                        )
                )) {
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка. Обратитесь в техническую поддержку", ""
                            )
                    );
                }
                loggedDonor = null;
                e.printStackTrace();
            }
        }
    }



    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends DictionaryDAO> T getDictionaryDAO(Class<T> persistentClass, String beanName) {
        return (T) indexManagement.getContext().getBean(beanName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends GenericDAO> T getDAO(Class<T> persistentClass, String beanName) {
        return (T) indexManagement.getContext().getBean(beanName);
    }


    public String logOut() {
        LOGGER.info("LOGOUT {}", authData);
        authData = null;
        userName = null;
        password = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.invalidateSession();
        }
        return "/index?faces-redirect=true";
    }


    @PreDestroy
    public void destroy() {
        logOut();
    }

    public User getLoggedUser() {
        return authData.getAuthorized();
    }

    public Donor getLoggedDonor() {
        return loggedDonor;
    }

    public String setCurrentRole(Role currentRole) {
        authData.getAuthorized().setSelectedRole(currentRole);
        this.currentRole = currentRole;
        getDAO(UserDAOHibernate.class, ApplicationHelper.USER_DAO).save(authData.getAuthorized());
        return getNavigationRule() + "?faces-redirect=true";
    }

    public Role getCurrentRole() {
        return currentRole;
    }


    public List<Role> getAvailableRoles() {
        List<Role> result = new ArrayList<>();
        for (Role role : authData.getRoles()) {
            if (!currentRole.equals(role)) {
                result.add(role);
            }
        }
        Collections.sort(
                result, new Comparator<Role>() {

                    @Override
                    public int compare(Role o1, Role o2) {
                        return o1.getName().compareTo(o2.getName());
                    }

                }
        );
        return result;
    }

    public String getTemplateName() {
        StringBuilder sb = new StringBuilder("/WEB-INF/template/");
        if (currentRole == null) {
            sb.append("minimal.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
            sb.append("admin_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.REGISTRATOR)) {
            sb.append("registrator_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.THERAPIST)) {
            sb.append("therapist_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.EXPEDITION)) {
            sb.append("expedition_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.QUARANTINE)) {
            sb.append("quarantine_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.DRAFT_OUT)) {
            sb.append("therapist_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.LABELING)) {
            sb.append("therapist_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.OPERATIONAL)) {
            sb.append("operational_template.xhtml");
            //TODO
//    		if (operationalSession != null && operationalSession.isExpired()) {
//    			operationalSession.initDocument();
//    		}
        } else if (currentRole.getRoleType().equals(RoleType.RECTIFICATION)) {
            sb.append("rectification_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.LABORATORY)) {
            sb.append("laboratory_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.IN_CONTROL)) {
            sb.append("control_template.xhtml");
        }
        /*else if (currentRole.getRoleType().equals(RoleType.INACTIVATION)) {
            sb.append("inactivation_template.xhtml");
    	}*/
        else if (currentRole.getRoleType().equals(RoleType.HEAD_NURSE)) {
            sb.append("head_nurse_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.DIVISION_SUPERINTENDENT)) {
            sb.append("division_superintendent_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.MEDICAL)) {
            sb.append("medical_template.xhtml");
        } else if (currentRole.getRoleType().equals(RoleType.SECONDARY_PROCESSING)) {
            sb.append("secondary_processing_template.xhtml");
        } else {
            sb.append("minimal.xhtml");
        }
        return sb.toString();
    }

    private String getNavigationRule() {
        LOGGER.info("redirectUrl:{}", backUrl);
        if (StringUtils.isNotEmpty(backUrl) && backUrl.contains("/component/")){
            final String res = backUrl;
            LOGGER.info("Use & clear backUrl. Redirect to \'{}\'", res);
            backUrl = null;
            return res;
        }
        final StringBuilder sb = new StringBuilder("/component/");
        if (currentRole == null) {
            sb.append("filter/donors");
        } else if (currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
            sb.append("admin/settings");
        } else if (currentRole.getRoleType().equals(RoleType.REGISTRATOR)) {
            sb.append("filter/donors");
        } else if (currentRole.getRoleType().equals(RoleType.THERAPIST)) {
            sb.append("personal_requests");
        } else if (currentRole.getRoleType().equals(RoleType.EXPEDITION)) {
            sb.append("blood_components_ready");
        } else if (currentRole.getRoleType().equals(RoleType.QUARANTINE)) {
            sb.append("blood_components_in_quarantine");
        } else if (currentRole.getRoleType().equals(RoleType.DRAFT_OUT)) {
            sb.append("blood_components");
        } else if (currentRole.getRoleType().equals(RoleType.LABELING)) {
            sb.append("blood_components");
        } else if (currentRole.getRoleType().equals(RoleType.OPERATIONAL)) {
            sb.append("blood_donations_operational");
        } else if (currentRole.getRoleType().equals(RoleType.RECTIFICATION)) {
            sb.append("blood_donations_rectification");
        } else if (currentRole.getRoleType().equals(RoleType.LABORATORY)) {
            sb.append("laboratory");
        } else if (currentRole.getRoleType().equals(RoleType.IN_CONTROL)) {
            sb.append("blood_components_in_control");
        }
    	/*else if (currentRole.getRoleType().equals(RoleType.INACTIVATION)) {
    		sb.append("inactivation");
    	}*/
        else if (currentRole.getRoleType().equals(RoleType.HEAD_NURSE)) {
            sb.append("blood_components_quarantined");
        } else if (currentRole.getRoleType().equals(RoleType.DIVISION_SUPERINTENDENT)) {
            sb.append("filter/donors");
        } else if (currentRole.getRoleType().equals(RoleType.MEDICAL)) {
            sb.append("medical/donors");
        } else if (currentRole.getRoleType().equals(RoleType.SECONDARY_PROCESSING)) {
            sb.append("secondary_processing");
        } else {
            sb.append("filter/donors");
        }
        //Если в URL нет GET-параметров, то добавить в конец ".xhtml"
        if(sb.indexOf("?") == -1 ) {
            sb.append(".xhtml");
        }
        LOGGER.info("Navigation rule = \'{}\'", sb.toString());
        return sb.toString();
    }

    public String getStartTemplateName() {
        return FacesContext.getCurrentInstance().getExternalContext().encodeActionURL(getNavigationRule());
    }

    public boolean isAdmin() {
        return currentRole.getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION);
    }

    public boolean isOperational() {
        return currentRole.getRoleType().equals(RoleType.OPERATIONAL);
    }
}