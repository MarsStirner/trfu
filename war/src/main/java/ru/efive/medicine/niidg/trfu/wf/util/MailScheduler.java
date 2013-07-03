package ru.efive.medicine.niidg.trfu.wf.util;

import org.apache.log4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.uifaces.beans.IndexManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.activity.MailMessage;
import ru.efive.wf.core.activity.SendMailActivity;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Singleton
@ApplicationScoped
@Startup
public class MailScheduler {
    @Inject
    @Named("propertiesHolder")
    private ApplicationPropertiesHolder propertiesHolder;

    @Inject
    @Named("indexManagement")
    private IndexManagementBean contextManagementBean;
    
    private static final Logger log = Logger.getLogger(MailScheduler.class);


    @PostConstruct
    public void schedulerTest() {
        TaskScheduler scheduler = (TaskScheduler) contextManagementBean.getContext().getBean("scheduler");
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
            	log.warn("MailScheduler initialization");
                if(checkTime())
                    sendDonors(findDonors());
            }
        }, new CronTrigger("30 * * * * *"));
    }

    private boolean checkTime(){
        Calendar t = Calendar.getInstance();
        t.setTime((Date) propertiesHolder.getProperty("application","passOffice.notification.time"));
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MINUTE) == t.get(Calendar.MINUTE) && now.get(Calendar.HOUR)==t.get(Calendar.HOUR);

    }

    private List<Donor> findDonors(){
        ExaminationRequestDAOImpl requestDAO = (ExaminationRequestDAOImpl) contextManagementBean.getContext().getBean(ApplicationHelper.EXAMINATION_DAO);
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);

        List<Donor> result = new ArrayList<Donor>();
        for (ExaminationRequest request: requestDAO.findDocumentsByByTimeBounds(false, new Date(), end.getTime(), 0,-1,"id", false)){
            if (request.getStatusId()==9)
                result.add(request.getDonor());
        }
        return result;
    }

    private void sendDonors(List<Donor> donors){
        log.warn("Donors list "+new Date());
        if (!((Boolean) this.propertiesHolder.getProperty("application","notification.mail.enabled"))) {
    		log.warn("Mail notifications disabled");
    		return;
    	}
        MailMessage message = new MailMessage(new ArrayList<String>(), new ArrayList<String>(), "Список доноров, записанных на прием", null);
        message.getSendTo().add((String) propertiesHolder.getProperty("application", "passOffice.notification.address"));
        StringBuilder stringBuilder = new StringBuilder();
        for (Donor donor: donors){
            stringBuilder.append(donor.getLastName()).append(" ").
                    append(donor.getFirstName()).append(" ").
                    append(donor.getMiddleName()).append(" ").
                    append(donor.getPassportSeries()).append(" ").
                    append(donor.getPassportNumber()).append("\n");
        }
        message.setBody(stringBuilder.toString());
        message.setContentType("text/html");

        SendMailActivity sendMailActivity = new SendMailActivity();
        sendMailActivity.setMessage(message);
        sendMailActivity.execute();
    }
}