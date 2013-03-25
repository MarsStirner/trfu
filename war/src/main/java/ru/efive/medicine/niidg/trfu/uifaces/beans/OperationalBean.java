package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.dictionary.OperationalSetup;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@Named("operational")
@ApplicationScoped
public class OperationalBean implements Serializable {
	
	public void addOperationalSetup(OperationalSetup operationalSetup) {
		if (operationalSetupList == null) {
			operationalSetupList = new ArrayList<OperationalSetup>();
		}
		operationalSetupList.add(operationalSetup);
	}
	
	public List<OperationalSetup> getOperationalSetupList() {
		return operationalSetupList;
	}
	
	public void setOperationalSetupList(List<OperationalSetup> operationalSetupList) {
		this.operationalSetupList = operationalSetupList;
	}
	
	public void update() {
		try {
			if (operationalSetupList != null) {
				for (OperationalSetup operationalSetup: operationalSetupList) {
					if (operationalSetup.getCreated() != null) {
						Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
						calendar.setTime(operationalSetup.getCreated());
						Calendar currentCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
						if (currentCalendar.get(Calendar.DATE) != calendar.get(Calendar.DATE)) {
							operationalSetupList.remove(operationalSetup);
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private List<OperationalSetup> operationalSetupList;
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement;
	
	private static final long serialVersionUID = 2826284364763461742L;
}