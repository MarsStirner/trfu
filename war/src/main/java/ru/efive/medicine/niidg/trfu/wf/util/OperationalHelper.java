package ru.efive.medicine.niidg.trfu.wf.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationEntry;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.PheresisReport;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.wf.core.ActionResult;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.BLOOD_COMPONENT_DAO;
import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.DICTIONARY_DAO;

public final class OperationalHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(OperationalHelper.class);
	
	
	public static ActionResult operationalReject(final BloodDonationRequest request, final String description, final Contragent maker) throws Exception {
		final FacesContext context = FacesContext.getCurrentInstance();
		final SessionManagementBean sessionManagement = context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
		final ActionResult result = new ActionResult();
		result.setProcessed(false);
		List<BloodDonationEntry> entries = request.getFactEntryList();
		if (!entries.isEmpty()) {
			List<Integer> doseList = new ArrayList<>(0);
			for (BloodDonationEntry entry: entries) {
				if (entry.getDonationType() != null && StringUtils.equalsIgnoreCase(entry.getDonationType().getValue(), "кроводача")) {
					doseList.add(entry.getDose());
				}
			}
			
			if (doseList.isEmpty()) {
				result.setDescription("Не указан фактический объем донации");
			}
			else if (doseList.size() > 1) {
				result.setDescription("Указано более одного результата донации с типом \"Кроводача\"");
			}
			else {
				int volume = doseList.get(0);
				logger.warn("Operational reject: fact donation volume = " + volume);
				if (volume == 0) {
					result.setDescription("Фактический объем донации не может быть равен 0");
				}
				else if (volume < 0) {
					result.setDescription("Фактический объем донации не может быть отрицательным");
				}
				else {
					final BloodComponentDAOImpl componentDao = (BloodComponentDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(BLOOD_COMPONENT_DAO);
					BloodComponent component = new BloodComponent();
					component.setDonationId(request.getId());
					Date created = new Date();
					component.setCreated(created);
					if (sessionManagement != null) {
						component.setAuthor(sessionManagement.getLoggedUser());
					}
					component.setParentNumber(request.getNumber());
					int count = new Long(componentDao.countComponentsByDonation(request.getId())).intValue() + 1;
					component.setNumber(StringUtils.leftPad(String.valueOf(count), 2, '0'));
					component.setAnticoagulantVolume(63);
					component.setVolume(volume + 63);
					component.setBloodGroup(request.getDonor().getBloodGroup());
					component.setRhesusFactor(request.getDonor().getRhesusFactor());
					component.setStatusId(-1);
					component.setDonationDate(request.getFactDate());
					component.setProductionDate(request.getFactDate());
					component.setPurchased(false);
					
					Calendar expired = Calendar.getInstance();
					expired.setTime(created);
					expired.add(Calendar.DAY_OF_MONTH, 5);
					component.setExpirationDate(expired.getTime());
					
					component.setMaker(maker);
					
					List<BloodComponentType> list = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(DICTIONARY_DAO)).findComponentTypeByValue("01.03.001 Кровь консервированная");
					if (list.size() > 0) {
						component.setComponentType(list.get(0));
						HistoryEntry historyEntry = new HistoryEntry();
						historyEntry.setCreated(created);
						historyEntry.setStartDate(created);
						historyEntry.setOwner(sessionManagement != null ? sessionManagement.getLoggedUser() : null);
						historyEntry.setDocType(component.getType());
						historyEntry.setActionId(0);
						historyEntry.setFromStatusId(0);
						historyEntry.setToStatusId(1);
						historyEntry.setEndDate(created);
						historyEntry.setProcessed(true);
						historyEntry.setCommentary("");
						component.addToHistory(historyEntry);

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(created);
						calendar.add(Calendar.SECOND, 1);
						created = calendar.getTime();
						
						historyEntry = new HistoryEntry();
						historyEntry.setCreated(created);
						historyEntry.setStartDate(created);
                        historyEntry.setOwner(sessionManagement != null ? sessionManagement.getLoggedUser() : null);
						historyEntry.setDocType(component.getType());
						historyEntry.setActionId(-1);
						historyEntry.setFromStatusId(1);
						historyEntry.setToStatusId(-1);
						historyEntry.setEndDate(created);
						historyEntry.setProcessed(true);
						historyEntry.setCommentary(description);
						component.addToHistory(historyEntry);
						
						component = componentDao.save(component);
						if (component != null) {
							result.setProcessed(true);
						}
					}
					else {
						result.setDescription("Внутренняя ошибка");
					}
				}
			}
		}
		else {
			result.setDescription("Не указан тип донации");
		}
		return result;
	}
	
	public static ActionResult checkOperationalReject(BloodDonationRequest request) {
		ActionResult result = new ActionResult();
		try {
			List<BloodComponent> components = ((BloodComponentDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(BLOOD_COMPONENT_DAO)).findComponentsByDonation(request.getId());
			BloodComponent component = null;
			for (BloodComponent c: components) {
				if (c != null && c.getComponentType() != null && StringUtils.equals(c.getComponentType().toString(), "01.03.001 Кровь консервированная")) {
					component = c;
					break;
				}
			}
			if (component != null) {
				List<BloodDonationEntry> entries = request.getFactEntryList();
				int volume = 0;
				if (entries.size() > 0) {
					volume = entries.get(0).getDose();
					if (component.getVolume() == volume + 63){
						result.setProcessed(true);
					}
					else {
						result.setDescription("Объем компонента крови с типом \"Кровь консервированная\" не соответствует фактическому объему донации");
					}
				}
				else {
					result.setDescription("Не указан фактический объем донации");
				}
			}
			else {
				result.setDescription("Не зарегистрирован компонент крови с типом \"Кровь консервированная\". Выполните действие \"Брак по операционной\"");
			}
		}
		catch (Exception e) {
			logger.error("Check operational reject failed", e);
		}
		return result;
	}
	
	public static ActionResult operationalRegisterComponents(final BloodDonationRequest request, final Contragent maker) throws Exception {
		FacesContext context = FacesContext.getCurrentInstance();
		SessionManagementBean sessionManagement = context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
		ActionResult result = new ActionResult();
		result.setProcessed(false);
		if (request.getReport() != null) {
			PheresisReport report = request.getReport();
			
			if (report.getTotalPltVolume() > 0 || report.getPlasmaVolume() > 0 || report.getErVolume() > 0) {
				final DictionaryDAOImpl dictionaryDao = (DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(DICTIONARY_DAO);
				final BloodComponentDAOImpl componentDao = (BloodComponentDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(BLOOD_COMPONENT_DAO);
				final Date created = new Date();
                //Создаваемый КК
                BloodComponent component = new BloodComponent();
                //Установка общих полей
                component.setDonationId(request.getId());
                component.setCreated(created);
                if (sessionManagement != null) {
                    component.setAuthor(sessionManagement.getLoggedUser());
                }
                component.setParentNumber(request.getNumber());
                int count = new Long(componentDao.countComponentsByDonation(request.getId())).intValue() + 1;
                component.setNumber(StringUtils.leftPad(String.valueOf(count), 2, '0'));
                component.setBloodGroup(request.getDonor().getBloodGroup());
                component.setRhesusFactor(request.getDonor().getRhesusFactor());
                component.setStatusId(1);
                component.setDonationDate(request.getFactDate());
                component.setProductionDate(request.getFactDate());
                component.setPurchased(false);
                component.setMaker(maker);
                //Дата окончания срока годности, будет установлена в зависиомсти от типа
                Calendar expired = Calendar.getInstance();
                expired.setTime(created);
                //TODO else if ?
                // Регистрация тромбоцитного концентрата
				if (report.getTotalPltVolume() > 0) {
					component.setAnticoagulantVolume(report.getAcPltVolume());
					component.setVolume(new Double(report.getTotalPltVolume()).intValue());
					component.setCellCount(report.getCollectedPlt());
					expired.add(Calendar.DAY_OF_MONTH, 5);
					component.setExpirationDate(expired.getTime());
					List<BloodComponentType> list = dictionaryDao.findComponentTypeByValue("02.01.020 Тромбоцитный концентрат, полученный автоматическим аферезом");
					if (!list.isEmpty()) {
						component.setComponentType(list.get(0));
					}
				}
				// Регистрация плазмы
				if (report.getPlasmaVolume() > 0) {
					component.setAnticoagulantVolume(report.getAcPlasmaVolume());
					component.setVolume(new Double(report.getPlasmaVolume()).intValue());
					expired.add(Calendar.MONTH, 36);
					component.setExpirationDate(expired.getTime());
					List<BloodComponentType> list = dictionaryDao.findComponentTypeByValue("02.02.003 Плазма свежезамороженная, полученная автоматическим аферезом");
                    if (!list.isEmpty()) {
                        component.setComponentType(list.get(0));
                    }
				}
				// Регистрация эр. массы
				if (report.getErVolume() > 0) {
					component.setAnticoagulantVolume(report.getAcErVolume());
					component.setVolume(new Double(report.getErVolume()).intValue());
					expired.add(Calendar.DAY_OF_MONTH, 42);
					component.setExpirationDate(expired.getTime());

                    List<BloodComponentType> list = dictionaryDao.findComponentTypeByValue("01.01.014 Эритроцитарная взвесь, лейкофильтрованная, полученная автоматическим аферезом");
                    if (!list.isEmpty()) {
                        component.setComponentType(list.get(0));
                    }
                }
                if(component.getComponentType() != null){
                    //Запись в истории КК
                    HistoryEntry historyEntry = new HistoryEntry();
                    historyEntry.setCreated(created);
                    historyEntry.setStartDate(created);
                    historyEntry.setOwner(sessionManagement != null ? sessionManagement.getLoggedUser() : null);
                    historyEntry.setDocType(component.getType());
                    historyEntry.setActionId(0);
                    historyEntry.setFromStatusId(0);
                    historyEntry.setToStatusId(1);
                    historyEntry.setEndDate(created);
                    historyEntry.setProcessed(true);
                    historyEntry.setCommentary("");
                    component.addToHistory(historyEntry);
                }
                component = componentDao.save(component);
                if (component == null) {
                    result.setDescription("Внутренняя ошибка");
                    return result;
                }
                result.setProcessed(true);
			}
			else {
				result.setDescription("Не указаны объемы в протоколе цитафереза");
			}
		}
		else {
			result.setDescription("Внутренняя ошибка");
		}
		return result;
	}
	
	public static ActionResult checkOperationalRegisteredComponents(BloodDonationRequest request) {
		ActionResult result = new ActionResult();
		try {
			result.setProcessed(true);
		}
		catch (Exception e) {
			logger.error("Check operational registered components failed", e);
		}
		return result;
	}
}