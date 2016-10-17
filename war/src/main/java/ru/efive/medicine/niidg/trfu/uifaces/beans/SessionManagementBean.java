package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.dao.sql.dao.DictionaryDAO;
import ru.efive.dao.sql.dao.GenericDAO;
import ru.efive.dao.sql.dao.user.UserDAO;
import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;
import ru.hitsl.helper.AuthorizationData;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import static ru.efive.medicine.niidg.trfu.uifaces.beans.utils.MessageHolder.*;

@Named("sessionManagement")
@SessionScoped
public class SessionManagementBean implements Serializable {

    public static final String AUTH_KEY = "app.user.name";
    public static final String BACK_URL = "app.back.url";

    private final static Logger LOGGER = LoggerFactory.getLogger("AUTH");

    private AuthorizationData authData;

    private String userName;
    private String password;
    private String backUrl;

    @Inject
    @Named("indexManagement")
    private IndexManagementBean indexManagement;

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
                final UserDAO dao = getDAO(UserDAO.class, USER_DAO);
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
                    if (loggedUser.getSelectedRole() == null) {
                        loggedUser.setSelectedRole(loggedUser.getRoleList().get(0));
                        loggedUser = dao.save(loggedUser);
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




    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends DictionaryDAO> T getDictionaryDAO(Class<T> persistentClass, String beanName) {
        return (T) indexManagement.getContext().getBean(beanName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends GenericDAO> T getDAO(Class<T> persistentClass, String beanName) {
        return (T) indexManagement.getContext().getBean(beanName);
    }


    public String logOut() {
        LOGGER.info("LOGOUT: {}", authData);
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


    public Role getCurrentRole(){
        return authData.getAuthorized().getSelectedRole();
    }

    public void setCurrentRole(final Role role) {
        authData.getAuthorized().setSelectedRole(role);
        getDAO(UserDAOHibernate.class, USER_DAO).save(authData.getAuthorized());
    }

    private String getBackUrl() {
        final StringBuilder result = new StringBuilder(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
        if (StringUtils.isEmpty(backUrl)) {
            result.append(authData.getDefaultPage());
        } else {
           result.append(backUrl);
            //Должно стрелять только один раз
            backUrl = "";
        }
        LOGGER.info("redirectUrl: \'{}\'", result.toString());
        return result.toString();
    }

    public AuthorizationData getAuthData() {
        return authData;
    }
}