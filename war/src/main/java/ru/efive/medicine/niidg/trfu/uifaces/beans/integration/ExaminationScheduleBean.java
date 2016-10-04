package ru.efive.medicine.niidg.trfu.uifaces.beans.integration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@Named("examinationShedule")
@SessionScoped
public class ExaminationScheduleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
    @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement ;
    @Inject @Named("propertiesHolder")
    private ApplicationPropertiesHolder propertiesHolder;
    private Integer startTime = 9;
    private Integer finishTime = 14;
    private Integer minuteTimeStep = 20;
    private String error = "Ошибка! Время начала не может быть позднее времени окончания.\n Закройте это окно и сделайте новый выбор.";
    private Boolean errorState = false;
    private Date date = new Date();
    private Date possibleStartTime;
    private Date possibleFinishTime;
    private Date selectedTime;
    private List<Date> possibleTimes = new ArrayList<>();
    private List<ExaminationRequest> previousRequests;
    ExaminationRequest examinationRequest;

    @PostConstruct
    public void init() {
        possibleStartTime = (Date) propertiesHolder.getProperty("application", "examinationShedule.startTime");
        possibleFinishTime = (Date) propertiesHolder.getProperty("application", "examinationShedule.finishTime");
        minuteTimeStep = (Integer) propertiesHolder.getProperty("application", "examinationShedule.minuteTimeStep");
        ExaminationRequestDAOImpl dao = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO);
        previousRequests = dao.findDocumentsByDonor(false, sessionManagement.getLoggedDonor().getId(), -1, -1, "planDate", true);
        int i = 0;
        while (i <  previousRequests.size()) {
            if (previousRequests.get(i).getPlanDate()==null){
                previousRequests.remove(i);
            } else
                if(previousRequests.get(i).getPlanDate().after(new Date())){
                    examinationRequest = previousRequests.remove(i);
                } else {
                    i++;
                }
        }
        if (examinationRequest == null){
            initRequest();
        }
        Collections.sort(previousRequests, new Comparator<ExaminationRequest>() {
            @Override
            public int compare(ExaminationRequest o1, ExaminationRequest o2) {
                if(o1==null || o1.getPlanDate()==null )
                    return 1;
                if(o2==null || o2.getPlanDate()==null )
                    return -1;
                return -(o1.getPlanDate().compareTo(o2.getPlanDate()));

            }
        });
    }

    private void initRequest() {
        examinationRequest = new ExaminationRequest();
        examinationRequest.setDonor(sessionManagement.getLoggedDonor());
        examinationRequest.setCreated(new Date());
        examinationRequest.setDeleted(false);
        examinationRequest.setStatusId(9);
        examinationRequest.setExaminationType(sessionManagement.getLoggedDonor().getCategory());
    }

    public void process(ActionEvent actionEvent){
        possibleStartTime.setYear(date.getYear());
        possibleStartTime.setMonth(date.getMonth());
        possibleStartTime.setDate(date.getDate());

        possibleFinishTime.setYear(date.getYear());
        possibleFinishTime.setMonth(date.getMonth());
        possibleFinishTime.setDate(date.getDate());

        List<ExaminationRequest> requestsInBounds  = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).
                findDocumentsByByTimeBounds(false, possibleStartTime, possibleFinishTime, -1, -1, "planDate", true);

        possibleTimes.clear();
        Calendar currentPretender = Calendar.getInstance(ApplicationHelper.getLocale());
        currentPretender.setTime(possibleStartTime);

        Iterator<ExaminationRequest> existIterator = requestsInBounds.iterator();
        Date currentExist = null;
        Date now = new Date();
        if(existIterator.hasNext())
            currentExist = existIterator.next().getPlanDate();
        while (currentPretender.getTime().getTime()<possibleFinishTime.getTime()){
            if (currentExist!=null && currentExist.compareTo(currentPretender.getTime())==0){
                if(existIterator.hasNext()) {
                    currentExist = existIterator.next().getPlanDate();
                } else {
                    currentExist = null;
                }
            } else {
                if (currentPretender.getTime().after(now))
                    possibleTimes.add(currentPretender.getTime());
            }
            currentPretender.add(Calendar.MINUTE, minuteTimeStep);
        }
        checkTimes();
    }

    private void checkTimes(){
        errorState = !(possibleStartTime.after(possibleFinishTime));
        if (!errorState){
            error = "Ошибка! Время начала не может быть позднее времени окончания.\n Закройте это окно и сделайте новый выбор.";
            return;
        }
        errorState = !(possibleTimes.isEmpty());
        if (!errorState){
            error = "Ошибка! Нет ни одного подходящего варианта.\n Закройте это окно и сделайте новый выбор.";
        }
    }



    public void commit(ActionEvent actionEvent){
        if (selectedTime == null)
            return;
        examinationRequest.setPlanDate(selectedTime);
        examinationRequest = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, ApplicationHelper.EXAMINATION_DAO).save(examinationRequest);
        if (examinationRequest == null)
            setError("Возникла ошибка при сохранении. Перезагрузите страницу и повторите попытку.");
    }



    public Date getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(Date selectedTime) {
        this.selectedTime = selectedTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getPossibleStartTime() {
        return possibleStartTime;
    }

    public void setPossibleStartTime(Date possibleStartTime) {
        this.possibleStartTime = possibleStartTime;
    }

    public Date getPossibleFinishTime() {
        return possibleFinishTime;
    }

    public void setPossibleFinishTime(Date possibleFinishTime) {
        this.possibleFinishTime = possibleFinishTime;
    }

    public List<ExaminationRequest> getPreviousRequests(){
        return previousRequests;
    }

    public List<Date> getPossibleTimes() {
        return possibleTimes;
    }

    public void setPossibleTimes(List<Date> possibleTimes) {
        this.possibleTimes = possibleTimes;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getMinuteTimeStep() {
        return minuteTimeStep;
    }

    public void setMinuteTimeStep(Integer minuteTimeStep) {
        this.minuteTimeStep = minuteTimeStep;
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

    public Boolean getErrorState() {
        return errorState;
    }

    public void setErrorState(Boolean errorState) {
        this.errorState = errorState;
    }

    public Date getMinDate(){
        //String s = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());
        //return s;
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(new Date());
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return tomorrow.getTime();
    }

    public ExaminationRequest getExaminationRequest() {
        return examinationRequest;
    }
}