package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.SelectEvent;

import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.TimeTableDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.TimeTableEntry;
import ru.efive.medicine.niidg.trfu.dictionary.TimesheetTemplate;
import ru.efive.medicine.niidg.trfu.dictionary.impl.OneDayTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.dictionary.impl.OnePlanTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.dictionary.impl.PeriodTimesheetTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

@Named("timeTable")
@SessionScoped
public class TimeTableBean extends AbstractDocumentListHolderBean<TimeTableEntry> {
	
	@Override
	public Sorting initSorting() {
		return new Sorting("startDate", true);
	}
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			logger.info("Selected date is: " + new java.text.SimpleDateFormat("dd.MM.yyyy").format(selectedDate));
			result = new Long(sessionManagement.getDAO(TimeTableDAOImpl.class, ApplicationHelper.TIMETABLE_DAO).countUserTimeTable(selectedDate,
					selectedUser, false)).intValue();
		}
		catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	@Override
	protected List<TimeTableEntry> loadDocuments() {
		List<TimeTableEntry> result = new ArrayList<TimeTableEntry>();
		try {
			logger.info("Selected date is: " + new java.text.SimpleDateFormat("dd.MM.yyyy").format(selectedDate));
			result = sessionManagement.getDAO(TimeTableDAOImpl.class, ApplicationHelper.TIMETABLE_DAO).findUserTimeTable(selectedDate,
					selectedUser, false, -1, -1, getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	@Override
	public List<TimeTableEntry> getDocuments() {
		return super.getDocuments();
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}
	
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public Date getSelectedDate() {
		return selectedDate;
	}
	
	public TimeTableEntry[] getSelectedTimeTableEntries() {
		return selectedTimeTableEntries;
	}

	public void setSelectedTimeTableEntries(TimeTableEntry[] selectedTimeTableEntries) {
		this.selectedTimeTableEntries = selectedTimeTableEntries;
	}

	public void handleDateSelect(DateSelectEvent event) {
		try {
			selectedDate = event.getDate();
			refresh();
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void handleRowSelect(SelectEvent event) {
		try {
			if (event.getObject() != null && event.getObject() instanceof User) {
				selectedUser = (User) event.getObject();
				refresh();
			}
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void handleTimeTableRowSelect(SelectEvent event) {
		try {
			logger.warn("Timetable row selection event");
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
	
	public TimesheetTemplate getTimesheetTemplate() {
		return timesheetTemplate;
	}
	
	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}
	
	public int getTemplateType() {
		return templateType;
	}
	
	public void setHolidayFill(boolean holidayFill) {
		this.holidayFill = holidayFill;
	}

	public boolean isHolidayFill() {
		return holidayFill;
	}

	public void setWorkdayCount(int workdayCount) {
		this.workdayCount = workdayCount;
	}

	public int getWorkdayCount() {
		return workdayCount;
	}

	public void setRestdayCount(int restdayCount) {
		this.restdayCount = restdayCount;
	}

	public int getRestdayCount() {
		return restdayCount;
	}
	
	public void composeTimesheet(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if (timesheetTemplate != null) {
				if (timesheetTemplate.getPeriodStartDate() == null) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не указан период формирования расписания", ""));
					return;
				}
				TimeTableDAOImpl dao = (TimeTableDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.TIMETABLE_DAO);
				if (dao.countUserTimeTable(timesheetTemplate.getPeriodStartDate(), timesheetTemplate.getPeriodEndDate(), getSelectedUser(), false) > 0) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "В указанный период уже сформировано расписание", ""));
					return;
				}
				TimesheetTemplate template = new TimesheetTemplate();
				switch (templateType) {
				case 0:
					template = new OnePlanTimesheetTemplate();
					((OnePlanTimesheetTemplate) template).setHolidayFill(isHolidayFill());
					break;
				case 1:
					template = new PeriodTimesheetTemplate();
					((PeriodTimesheetTemplate) template).setWorkdayCount(getWorkdayCount());
					((PeriodTimesheetTemplate) template).setRestdayCount(getRestdayCount());
					break;
				case 2:
					template = new OneDayTimesheetTemplate();
					break;
				}
				template.setPeriodStartDate(timesheetTemplate.getPeriodStartDate());
				template.setPeriodEndDate(timesheetTemplate.getPeriodEndDate());
				template.setStartTime(timesheetTemplate.getStartTime());
				template.setEndTime(timesheetTemplate.getEndTime());
				template.setUser(getSelectedUser());
				if (!template.compose()) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при формировании расписания", ""));
				}
				else {
					timesheetTemplate = new TimesheetTemplate();
					refresh();
				}
			}
			else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при формировании расписания", ""));
			}
		}
		catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при формировании расписания", ""));
			logger.error(e);
		}
	}
	
	public void deleteSelectedEntry() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			TimeTableDAOImpl dao = (TimeTableDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.TIMETABLE_DAO);
			if (selectedTimeTableEntries != null && selectedTimeTableEntries.length > 0) {
				for (TimeTableEntry entry: selectedTimeTableEntries) {
					entry.setDeleted(true);
					dao.save(entry);
				}
				setSelectedTimeTableEntries(new TimeTableEntry[] {});
				refresh();
			}
		}
		catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при удалении", ""));
			logger.error(e);
		}
	}
	
	public boolean isEntriesSelected() {
		return selectedTimeTableEntries != null && selectedTimeTableEntries.length > 0;
	}
	
	
	private User selectedUser;
	private Date selectedDate = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
	private TimeTableEntry[] selectedTimeTableEntries;
	private TimesheetTemplate timesheetTemplate = new TimesheetTemplate();
	
	private int templateType = 0;
	private boolean holidayFill;
	private int workdayCount;
	private int restdayCount;
	
	@Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement;
	
	
	private static final long serialVersionUID = -8190884817003318592L;
	
	private final static Logger logger = Logger.getLogger(TimeTableBean.class);
}