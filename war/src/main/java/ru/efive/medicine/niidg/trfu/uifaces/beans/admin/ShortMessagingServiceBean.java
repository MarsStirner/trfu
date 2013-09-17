package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.RequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.uifaces.beans.BloodComponentListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DonorListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DonorListSelectModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.RequestsListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.UserListSelectModalBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.smsdelivery.*;

@Named("smsService")
@SessionScoped
public class ShortMessagingServiceBean implements Serializable {
    private String message;
    private Date activationDate;
    private List<User> users = new ArrayList<User>();
    private List<Donor> donors = new ArrayList<Donor>();
    private static final Logger logger = Logger.getLogger(ShortMessagingServiceBean.class);

    private static final long serialVersionUID = 1L;

	@Inject 
	@Named("userList")
    private transient UserListHolderBean userList;
    @Inject
    @Named("propertiesHolder")
    private ApplicationPropertiesHolder propertiesHolder;
    @Inject 
    @Named("requests")
    private RequestsListHolderBean requestsListHolder;
    @Inject 
    @Named("bloodComponentList")
    private BloodComponentListHolderBean bloodComponentListHolder;
    @Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
    
    private DonorListHolderBean donorList = new DonorListHolderBean() {
    	@Override
    	protected List<Donor> loadDocuments() {
    		try {
    			donors = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO)
                		.findDocumentsByDonationInCertificationWithBCInIssued(activationDate, getPagination().getOffset(), getPagination().getPageSize(), null, false);
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    		return donors;
    	}
    	
    	@Override
    	protected int getTotalCount() {
    		int result = 0;
    		try {
    			long count = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao")
    					.countDocumentByDonationInCertificationWithBCInIssued(activationDate);
    			return new Long(count).intValue();
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    		return result;
    	}
    };

    @PostConstruct
    public void init() {
    	setDefaultMessage();
    	setActivationDate();
    	
//        List<Donor> donors = donorsSelectModal.getDonorList().getDocuments();
//        while (i < donors.size()) {
//        	Donor currentDonor = donors.get(i); 
//        	List<BloodDonationRequest> donations = getDonationsByDonor(currentDonor.getId());
//        	if (isEmptyPhone(currentDonor.getPhone()) || !hasDonationInCertificationWithBC(donations)) {
//            	donors.remove(i);
//            } else {
//                i++;
//            }
//        }
//        i = 0;
//        List<User> users = usersSelectModal.getUserList().getDocuments();
//        while (i < users.size()) {
//            if (isEmptyPhone(users.get(i).getMobilePhone())) {
//            	users.remove(i);
//            }
//            else {
//                i++;
//            }
//        }
        
    }
    
    private void setActivationDate() {
    	activationDate = (Date) propertiesHolder.getProperty("application", "smsdelivery.activation.date");
    }
    
    private void setDefaultMessage() {
    	message = (String) propertiesHolder.getProperty("application", "smsdelivery.message");
    }
    
    private UserListSelectModalBean usersSelectModal = new UserListSelectModalBean() {

        @Override
        protected void doSave() {
            super.doSave();
            users.clear();
            users.addAll(this.getUsers());
        }

        @Override
        protected void doHide() {
            super.doHide();
            getUserList().setFilter("");
            getUserList().markNeedRefresh();
            setUsers(new ArrayList<User>());
        }
    };

    private DonorListSelectModalBean donorsSelectModal = new DonorListSelectModalBean() {
    	@Override
    	public DonorListHolderBean getDonorList() {
    		return donorList;
    	}
    };

    public List<Donor> getDonors() {
        return donors;
    }

    public void setDonors(List<Donor> donors) {
        this.donors = donors;
    }


    public DonorListSelectModalBean getDonorsSelectModal() {
        return donorsSelectModal;
    }

    public void setDonorsSelectModal(DonorListSelectModalBean donorsSelectModal) {
        this.donorsSelectModal = donorsSelectModal;
    }


    public UserListSelectModalBean getUsersSelectModal() {
        return usersSelectModal;
    }

    public void setUsersSelectModal(UserListSelectModalBean usersSelectModal) {
        this.usersSelectModal = usersSelectModal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void saveMessage() throws IOException {
    	propertiesHolder.setProperty("application", "smsdelivery.message", message);
    }
    
    public void send() throws IOException {
        String login = (String) propertiesHolder.getProperty("application", "smsdelivery.login");
        String pass = (String) propertiesHolder.getProperty("application", "smsdelivery.password");
        String appToken = (String) propertiesHolder.getProperty("application", "smsdelivery.application_token");
        String authUrlProperty = (String) propertiesHolder.getProperty("application", "smsdelivery.auth_url");
        String smsUrlProperty = (String) propertiesHolder.getProperty("application", "smsdelivery.sms_url");
        
        String sha1Pass = DigestUtils.shaHex(pass);
        
        String paramsAuth = getAuthRequestParams(login, sha1Pass, appToken);
        URL authUrl = new URL(authUrlProperty);
        String responseAuth = sendRequestAndGetRespons(authUrl, paramsAuth);
        
        JSONObject jsonAuth = (JSONObject)JSONSerializer.toJSON(responseAuth);
        int code = jsonAuth.getInt("code");
        if (code == 200) {
        	JSONObject data = jsonAuth.getJSONObject("data");
        	String userToken = data.getString("user_token");
        	String sender = data.getString("sender");
        	String stationId = data.getString("stationId");
        	
        	URL smsUrl = new URL(smsUrlProperty);
        	sendTestSMS(smsUrl, appToken, userToken, sender, stationId);
//        	sendSMSToDonors(smsUrl, appToken, userToken, sender, stationId);
//        	sendSMSToUsers(smsUrl, appToken, userToken, sender, stationId);
        } else {
//        	JSONObject error = jsonAuth.getJSONObject("error");
//        	String errorMessage = null;
//        	JSONObject email = jsonAuth.getJSONObject("email");
//        	JSONObject password = jsonAuth.getJSONObject("password");
//        	JSONObject email = jsonAuth.getJSONObject("email");
//        	if (email.isNullObject()) {
//        		
//        	} else {
//        		
//        	}
        }
        
        
//        
    }
    
    private void sendTestSMS(URL url, String appToken, String userToken, String sender, String stationId) {
    	String fio = "Test Test Test";
        String content = message.replace("...", fio);
        String phone = "+79218893491";
        String paramsSMS = getSmsRequestParams(appToken, userToken, sender, stationId, phone, content);
//        String paramsSMS = getSmsRequestParams2(appToken, userToken, phone, content, sender, stationId);
        String responseSMS = sendRequestAndGetRespons(url, paramsSMS);
        String result;
        
        JSONObject jsonSMS = (JSONObject)JSONSerializer.toJSON(responseSMS);
        int code = jsonSMS.getInt("code");
        if (code == 200) {
        	JSONObject data = jsonSMS.getJSONObject("data");
        	result = "OK\ncontent:\n" + data.getString("content") + "\nphone:\n" + data.getString("phone") + "\nsender:\n" + data.getString("sender") + "\nsendStatus:\n" + data.getString("sendStatus") + "\nerrorDescription:\n" + data.getString("errorDescription") + "\ncreatedAt:\n" + data.getString("createdAt") + "\nupdatedAt:\n" + data.getString("updatedAt") + "\nid:\n" + data.getString("id");
        } else {
        	result = "ERROR";
        }
        System.out.println(result);
    }
    
    private void sendSMSToDonors(URL url, String appToken, String userToken, String sender, String stationId) {
    	for (Donor donor : donors) {
          String fio = donor.getFirstName() + " " + donor.getMiddleName() + " " + donor.getLastName();
          String content = message.replace("...", fio);
          String phone = donor.getPhone();
          String paramsSMS = getSmsRequestParams(appToken, userToken, sender, stationId, phone, content);
          String responseSMS = sendRequestAndGetRespons(url, paramsSMS);
    	}
    }
    
    private void sendSMSToUsers(URL url, String appToken, String userToken, String sender, String stationId) {
    	for (User user : users) {
    		 String fio = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
             String content = message.replace("...", fio);
             String phone = user.getPhone();
             String paramsSMS = getSmsRequestParams(appToken, userToken, sender, stationId, phone, content);
             String responseSMS = sendRequestAndGetRespons(url, paramsSMS);
    	}
    }
    
    private String getAuthRequestParams(String login, String sha1Pass, String token) {
    	return String.format("login=%s&password=%s&application_token=%s", login, sha1Pass, token);
    }
    
    private String getSmsRequestParams(String appToken, String userToken, String sender, String stationId, String phone, String content) {
    	return String.format("application_token=%s&user_token=%s&sender=%s&stationId=%s&Message[phone]=%s&Message[content]=%s"
    			, appToken, userToken, sender, stationId, phone, content);
    }
    
    private String getSmsRequestParams2(String appToken, String userToken, String phone, String content, String sender, String stationId) {
    	return String.format("application_token=%s&user_token=%s&Message[phone]=%s&Message[content]=%s&sender=%s&stationId=%s"
    			, appToken, userToken, phone, content, sender, stationId);
    }
    
    private String sendRequestAndGetRespons(URL url, String data) {
    	StringBuffer answer = new StringBuffer();
    	HttpURLConnection connection = null;
        try {
            // Send the request
        	connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            
            //write parameters
            writer.write(data);
            writer.flush();
            writer.close();
            
            // Get the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            
            reader.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
            	connection.disconnect(); 
            }
        }
        
        return answer.toString();
    }
}