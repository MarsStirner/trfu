package ru.efive.medicine.niidg.trfu.dictionary;

import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;

//import ru.efive.dao.sql.dao.user.UserDAOHibernate;
import ru.efive.dao.sql.entity.user.User;
//import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

public class TimesheetTemplate {
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}
	
	public Date getPeriodStartDate() {
		return periodStartDate;
	}
	
	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}
	
	public Date getPeriodEndDate() {
		return periodEndDate;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public boolean compose() {
		return false;
	}
	
	protected TimeTableEntry composeEntry() {
		FacesContext context = FacesContext.getCurrentInstance();
		SessionManagementBean sessionManagement = (SessionManagementBean) context.getApplication().evaluateExpressionGet(
				context, "#{sessionManagement}", SessionManagementBean.class);
		/*User loggedUser = ((UserDAOHibernate) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.USER_DAO)).get(1);*/
		TimeTableEntry entry = new TimeTableEntry();
		entry.setAuthor(sessionManagement.getLoggedUser());
		//entry.setAuthor(loggedUser);
		entry.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
		entry.setDeleted(false);
		entry.setUser(getUser());
		return entry;
	}
	
	public boolean isPeriodic() {
		return true;
	}
	
	public int getType() {
		return 0;
	}
	
	
	/**
	 * Сотрудник
	 */
	private User user;
	
	/**
	 * Начало периода
	 */
	private Date periodStartDate;
	
	/**
	 * Конец периода
	 */
	private Date periodEndDate;
	
	/**
	 * Начало рабочего времени
	 */
	private Date startTime;
	
	/**
	 * Конец рабочего времени
	 */
	private Date endTime;
	
}