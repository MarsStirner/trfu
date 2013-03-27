package ru.efive.wf.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.EmailTemplateDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.activity.InvokeMethodActivity;
import ru.efive.wf.core.activity.ParametrizedPropertyLocalActivity;
import ru.efive.wf.core.activity.SendMailActivity;
import ru.efive.wf.core.activity.SetPropertyActivity;
import ru.efive.wf.core.activity.enums.EditablePropertyScope;
import ru.efive.wf.core.data.AlertForm;
import ru.efive.wf.core.data.DonorRejectionForm;
import ru.efive.wf.core.data.InputDateTimeForm;
import ru.efive.wf.core.data.InputReasonForm;
import ru.efive.wf.core.util.EngineHelper;
import ru.efive.wf.core.util.GroovyProcessor;

public final class ProcessFactory {
	
	public static <T extends ProcessedData> Process getProcessByType(T t) {
		Process process = null;
		try {
			System.out.println("process factory initialization");

			process = new Process();
			process.setProcessedData(t);
			Map<Integer, Status<T>> statuses = new HashMap<Integer, Status<T>>();
			if (t.getType().equals("Donor")) {
				System.out.println("Initialization process for donor");
				
				// Кандидат - Донор
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Кандидат");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				StatusChangeAction toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(1);
				toStatusAction.setName("Направить на донорство");
				toStatusAction.setInitialStatus(status);
				
				Status<T> toStatus = new Status<T>();
				toStatus.setId(2);
				toStatus.setName("Донор");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				statuses.put(toStatus.getId(), toStatus);
				
				// Временный отвод - Кандидат
				toStatus = status;
				status = new Status<T>();
				status.setId(-1);
				status.setName("Временный отвод");
				status.setProcessedData(t);
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							//ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							/*if (!result) {
								Object rejection = PropertyUtils.getProperty(data, "rejection");
								if (rejection != null) {
									Object rejectionType = PropertyUtils.getProperty(rejection, "rejectionType");
									if (rejectionType != null) {
										Object type = PropertyUtils.getProperty(rejectionType, "type");
										if (type != null) {
											byte enc_type = (Byte) type;
											if (enc_type == 0) {
												Object expiration = PropertyUtils.getProperty(rejection, "expiration");
												if (expiration != null) {
													System.out.println("Дата возврата к донорству: " + expiration);
													if (new Date().after((Date) expiration)) {
														result = true;
													}
													else {
														result = false;
													}
												}
												else {
													result = false;
												}
											}
											else {
												result = true;
											}
										}
									}
								}
							}*/
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-1);
				toStatusAction.setName("Допустить к донорству");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				
				Status<T> absoluteRejectionStatus = new Status<T>();
				absoluteRejectionStatus.setId(-2);
				absoluteRejectionStatus.setName("Абсолютный отвод");
				absoluteRejectionStatus.setProcessedData(t);
				
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							//ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							/*if (!result) {
								Object rejection = PropertyUtils.getProperty(data, "rejection");
								if (rejection != null) {
									Object rejectionType = PropertyUtils.getProperty(rejection, "rejectionType");
									if (rejectionType != null) {
										Object type = PropertyUtils.getProperty(rejectionType, "type");
										if (type != null) {
											byte enc_type = (Byte) type;
											if (enc_type == 4) {
												result = false;
											}
											else {
												result = true;
											}
										}
									}
								}
							}*/
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-2);
				toStatusAction.setName("Допустить к донорству");
				toStatusAction.setInitialStatus(absoluteRejectionStatus);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				absoluteRejectionStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(absoluteRejectionStatus.getId(), absoluteRejectionStatus);
			}
			if (t.getType().equals("Examination")) {
				System.out.println("Initialization process for examination");
				
				// Заполнение - Обследование
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Заполнение");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				StatusChangeAction toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "registrator");
								User registrator = (prop == null ? null : (User) prop);
								if (registrator != null && registrator.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(1);
				toStatusAction.setName("Направить на обследование");
				toStatusAction.setInitialStatus(status);
				
				List<IActivity> activites = new ArrayList<IActivity>();
				InvokeMethodActivity activity = new InvokeMethodActivity();
				List<Object> list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "initializeExamination", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPostActionActivities(activites);
				
				Status<T> toStatus = new Status<T>();
				toStatus.setId(2);
				toStatus.setName("Обследование");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				
				// Заполнение - Запланировано
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "registrator");
								User registrator = (prop == null ? null : (User) prop);
								if (registrator != null && registrator.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(9);
				toStatusAction.setName("Запланировать");
				toStatusAction.setInitialStatus(status);
				
				Status<T> planStatus = new Status<T>();
				planStatus.setId(9);
				planStatus.setName("Запланировано");
				planStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(planStatus);
				
				activites = new ArrayList<IActivity>();
				ParametrizedPropertyLocalActivity localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				InputDateTimeForm dateForm = new InputDateTimeForm() {
					
					@Override
					public ActionResult validate() {
						ActionResult result = new ActionResult();
						try {
							ExaminationRequestDAOImpl dao = (ExaminationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.EXAMINATION_DAO);
							Calendar actionCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
							actionCalendar.setTime(getActionDate());
							Calendar actionTimeCalendar = Calendar.getInstance(ApplicationHelper.getLocale());
							actionTimeCalendar.setTime(getActionTime());
							actionCalendar.set(Calendar.HOUR_OF_DAY, actionTimeCalendar.get(Calendar.HOUR_OF_DAY));
							actionCalendar.set(Calendar.MINUTE, actionTimeCalendar.get(Calendar.MINUTE));
							actionCalendar.set(Calendar.SECOND, actionTimeCalendar.get(Calendar.SECOND));
							if (dao.countPlannedDocument(actionCalendar.getTime(), false) > 0) {
								result.setProcessed(false);
								result.setDescription("В выбранный промежуток времени уже зарегистрирована запись");
							}
							else {
								result.setProcessed(true);
							}
						}
						catch (Exception e) {
							result.setProcessed(false);
							result.setDescription(EngineHelper.DEFAULT_ERROR_MESSAGE);
						}
						return result;
					}
					
				};
				dateForm.setBeanName("examination");
				dateForm.setActionDateField("planDate");
				dateForm.setScope(EditablePropertyScope.GLOBAL);
				localActivity.setDocument(dateForm);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				statuses.put(planStatus.getId(), planStatus);
				
				// Запланировано - Обследование
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "registrator");
								User registrator = (prop == null ? null : (User) prop);
								if (registrator != null && registrator.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(10);
				toStatusAction.setName("Направить на обследование");
				toStatusAction.setInitialStatus(planStatus);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "initializeExamination", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPostActionActivities(activites);


				fromStatusActions.add(toStatusAction);
				
				
				// Запланировано - Отменено
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "registrator");
								User registrator = (prop == null ? null : (User) prop);
								if (registrator != null && registrator.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(-5);
				toStatusAction.setName("Отменить");
				toStatusAction.setInitialStatus(planStatus);
				
				Status<T> notHappenedStatus = new Status<T>();
				notHappenedStatus.setId(-2);
				notHappenedStatus.setName("Отменено");
				notHappenedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(notHappenedStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				InputReasonForm reasonForm = new InputReasonForm();
				reasonForm.setBeanName("examination");
				reasonForm.setActionCommentaryField("WFResultDescription");
				reasonForm.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(reasonForm);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				planStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(notHappenedStatus.getId(), notHappenedStatus);
				
				// Обследование - Допущен
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(4);
				toStatusAction.setName("Допущен");
				toStatusAction.setInitialStatus(status);

				Status<T> grantedStatus = new Status<T>();
				grantedStatus.setId(5);
				grantedStatus.setName("Допущен");
				grantedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(grantedStatus);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setCandidate", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				statuses.put(grantedStatus.getId(), grantedStatus);

				fromStatusActions.add(toStatusAction);
				
				// Обследование - Отвод от донорства
				StatusChangeAction toRejectedAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toRejectedAction.setId(-2);
				toRejectedAction.setName("Отвести от донорства");
				toRejectedAction.setCommentNecessary(true);
				toRejectedAction.setInitialStatus(status);
				
				// status rejected
				Status<T> statusRejected = new Status<T>();
				statusRejected.setId(-1);
				statusRejected.setName("Отвод от донорства");
				status.setProcessedData(t);
				toRejectedAction.setDestinationStatus(statusRejected);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toRejectedAction);
				DonorRejectionForm form = new DonorRejectionForm();
				form.setBeanName("examination");
				localActivity.setDocument(form);
				activites.add(localActivity);
				toRejectedAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setDonorRejected", list);
				activity.setParentAction(toRejectedAction);
				activites.add(activity);
				toRejectedAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toRejectedAction);
				
				statuses.put(statusRejected.getId(), statusRejected);

				// Обследование - На дообследование
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(5);
				toStatusAction.setName("На дообследование");
				toStatusAction.setInitialStatus(status);

				toStatus = new Status<T>();
				toStatus.setId(6);
				toStatus.setName("Направлен на дообследование");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				
				
				// Обследование - Отменено
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(-4);
				toStatusAction.setName("Отменить");
				toStatusAction.setInitialStatus(status);
				
				toStatusAction.setDestinationStatus(notHappenedStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				reasonForm = new InputReasonForm();
				reasonForm.setBeanName("examination");
				reasonForm.setActionCommentaryField("WFResultDescription");
				reasonForm.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(reasonForm);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				statuses.put(toStatus.getId(), toStatus);
				
				// Направлен на дообследование - Получение результатов анализов
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(2);
				toStatusAction.setName("Внести результаты анализов");
				toStatusAction.setInitialStatus(status);
				
				toStatus = new Status<T>();
				toStatus.setId(3);
				toStatus.setName("Получение результатов анализов");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);
				
				
				// Получение результатов анализов - Допущен
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(7);
				toStatusAction.setName("Допущен");
				toStatusAction.setInitialStatus(toStatus);
				toStatusAction.setDestinationStatus(grantedStatus);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setCandidate", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				statuses.put(grantedStatus.getId(), grantedStatus);

				fromStatusActions.add(toStatusAction);
				
				// Получение результатов анализов - Направлен на дообследование
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(6);
				toStatusAction.setName("На дообследование");
				toStatusAction.setInitialStatus(toStatus);
				toStatusAction.setDestinationStatus(status);
				fromStatusActions.add(toStatusAction);
				
				// Получение результатов анализов - Отвод от донорства
				toRejectedAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toRejectedAction.setId(-3);
				toRejectedAction.setName("Отвести от донорства");
				toRejectedAction.setCommentNecessary(true);
				toRejectedAction.setInitialStatus(toStatus);
				toRejectedAction.setDestinationStatus(statusRejected);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toRejectedAction);
				form = new DonorRejectionForm();
				form.setBeanName("examination");
				localActivity.setDocument(form);
				activites.add(localActivity);
				toRejectedAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setDonorRejected", list);
				activity.setParentAction(toRejectedAction);
				activites.add(activity);
				toRejectedAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toRejectedAction);
				
				toStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(toStatus.getId(), toStatus);
				
				
				// Действия по оповещению донора
				List<NoStatusAction> noStatusActions = new ArrayList<NoStatusAction>();
				
				//Оповестить донора
				NoStatusAction noStatusAction = new NoStatusAction(process)  {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							if (data.getStatusId() != 9) return false;
							
							ProcessUser currentUser = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(currentUser.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.REGISTRATOR)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.THERAPIST)) {
									return true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				noStatusAction.setId(1011);
				noStatusAction.setHistoryAction(false);
				noStatusAction.setName("Оповестить донора");
				
				activites = new ArrayList<IActivity>();
				
				Object prop = PropertyUtils.getProperty(t, "donor");
				if (prop != null) {
					activity = new InvokeMethodActivity();
					List<Object> objlist = new ArrayList<Object>();
					objlist.add(t);
					activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "checkDonorInformation", objlist);
					activity.setParentAction(noStatusAction);
					activites.add(activity);
					

					activites.add(createMailActivity((Donor) prop, t));
				}
				
				noStatusAction.setPreActionActivities(activites);
				
				noStatusActions.add(noStatusAction);
				
				process.setNoStatusActions(noStatusActions);
			}
			else if (t.getType().equals("BloodComponent")) {
				System.out.println("Initialization process for blood component");
				
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Зарегистрирован");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				
				// Зарегистрирован - Готов к выдаче
				StatusChangeAction toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							BloodComponent component = (BloodComponent) data;
							BloodDonationRequest request = ((BloodDonationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONATION_DAO)).get(component.getDonationId());
							if (request != null && (request.getStatusId() == 2 || request.getStatusId() == 21 || request.getStatusId() == 3) && component.getComponentType() != null && !component.getComponentType().isLite()) {
								return false;
							}
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(2);
				toStatusAction.setName("Готов к выдаче");
				toStatusAction.setInitialStatus(status);
				
				Status<T> readyStatus = new Status<T>();
				readyStatus.setId(3);
				readyStatus.setName("Готов к выдаче");
				readyStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(readyStatus);
				
				List<IActivity> activites = new ArrayList<IActivity>();
				InvokeMethodActivity activity = new InvokeMethodActivity();
				List<Object> list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "checkDonationInfections", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				statuses.put(readyStatus.getId(), readyStatus);
				
				// Зарегистрирован - Утилизирован
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(-10);
				toStatusAction.setName("Утилизирован");
				toStatusAction.setInitialStatus(status);
				
				Status<T> utilizedStatus = new Status<T>();
				utilizedStatus.setId(-10);
				utilizedStatus.setName("Утилизирован");
				utilizedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(utilizedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				statuses.put(utilizedStatus.getId(), utilizedStatus);
				
				// Зарегистрирован - В карантине
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							BloodComponent component = (BloodComponent) data;
							BloodDonationRequest request = ((BloodDonationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONATION_DAO)).get(component.getDonationId());
							if (request != null && (request.getStatusId() == 2 || request.getStatusId() == 21) && component.getComponentType() != null && !component.getComponentType().isLite()) {
								return false;
							}
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(1);
				toStatusAction.setName("В карантин");
				toStatusAction.setInitialStatus(status);
				
				Status<T> toStatus = new Status<T>();
				toStatus.setId(2);
				toStatus.setName("В карантине");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				SetPropertyActivity activitySetProp = new SetPropertyActivity();
				Map<String, Object> propertyChanges = new HashMap<String, Object>();
				propertyChanges.put("quarantineStartDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
				activitySetProp.setPropertyChanges(propertyChanges);
				activites.add(activitySetProp);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setQuarantineFinishDate", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				// Зарегистрирован - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-1);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(status);
				
				Status<T> defectStatus = new Status<T>();
				defectStatus.setId(-1);
				defectStatus.setName("Брак");
				defectStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				ParametrizedPropertyLocalActivity localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				InputReasonForm form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField(EngineHelper.PROP_WF_RESULT_DESCRIPTION);
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(defectStatus.getId(), defectStatus);
				statuses.put(status.getId(), status);
				
				
				// В карантине - Готов к выдаче
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(3);
				toStatusAction.setName("Готов к выдаче");
				toStatusAction.setInitialStatus(status);
				
				toStatusAction.setDestinationStatus(readyStatus);
				
				fromStatusActions.add(toStatusAction);
				
				// В карантине - Задержка
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(4);
				toStatusAction.setName("Задержка");
				toStatusAction.setInitialStatus(status);
				
				Status<T> waitStatus = new Status<T>();
				waitStatus.setId(4);
				waitStatus.setName("Задержка");
				waitStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(waitStatus);

				fromStatusActions.add(toStatusAction);
				
				statuses.put(waitStatus.getId(), waitStatus);
				
				// В карантине - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-2);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				
				// В карантине - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-4);
				toStatusAction.setName("Списать");
				toStatusAction.setInitialStatus(status);
				
				Status<T> discardStatus = new Status<T>();
				discardStatus.setId(-2);
				discardStatus.setName("Списан");
				discardStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(discardStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				statuses.put(discardStatus.getId(), discardStatus);
				
				// Задержка - Готов к выдаче
				status = waitStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(5);
				toStatusAction.setName("Готов к выдаче");
				toStatusAction.setInitialStatus(status);
				
				toStatusAction.setDestinationStatus(readyStatus);
				
				fromStatusActions.add(toStatusAction);
				
				// Задержка - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-3);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				// Задержка - Списан
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-5);
				toStatusAction.setName("Списать");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(discardStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);
				
				
				// Готов к выдаче - Списан
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							if (user.getSelectedRole().getRoleType().equals(RoleType.IN_CONTROL)) {
								return false;
							}
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-6);
				toStatusAction.setName("Списать");
				toStatusAction.setInitialStatus(readyStatus);
				toStatusAction.setDestinationStatus(discardStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				
				// Готов к выдаче - Выдан
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							if (user.getSelectedRole().getRoleType().equals(RoleType.IN_CONTROL)) {
								return false;
							}
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(6);
				toStatusAction.setName("Выдан");
				toStatusAction.setInitialStatus(readyStatus);
				
				status = new Status<T>();
				status.setId(10);
				status.setName("Выдан");
				status.setProcessedData(t);
				toStatusAction.setDestinationStatus(status);

				fromStatusActions.add(toStatusAction);
				
				// Готов к выдаче - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							Object prop = PropertyUtils.getProperty(data, "inControl");
							Boolean inControl = (prop == null ? null : (Boolean) prop);
							if (inControl != null && !inControl) {
								return false;
							}
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (user.getSelectedRole().getRoleType().equals(RoleType.IN_CONTROL)) {
								return true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-7);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(readyStatus);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activitySetProp = new SetPropertyActivity();
				propertyChanges = new HashMap<String, Object>();
				propertyChanges.put("inControl", Boolean.FALSE);
				activitySetProp.setPropertyChanges(propertyChanges);
				activites.add(activitySetProp);
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				readyStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				
				status = new Status<T>();
				status.setId(100);
				status.setName("Разделен");
				status.setProcessedData(t);
				statuses.put(status.getId(), status);
				
				
				// Выдача из карантина
				status = new Status<T>();
				status.setId(5);
				status.setName("Готов к выдаче из карантина");
				status.setProcessedData(t);
				
				// Готов к выдаче из карантина - Списан
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-8);
				toStatusAction.setName("Списать");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(discardStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				
				// Готов к выдаче из карантина - Готов к выдаче
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(7);
				toStatusAction.setName("Готов к выдаче");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(readyStatus);

				fromStatusActions.add(toStatusAction);
				
				// Готов к выдаче из карантина - Брак
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-9);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				
				
				// Брак из карантина
				status = new Status<T>();
				status.setId(6);
				status.setName("Брак из карантина");
				status.setProcessedData(t);
				
				// Брак из карантина - Брак
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							
							Object prop = PropertyUtils.getProperty(data, "componentType");
							Object componentType = (prop == null ? null : prop);
							if (componentType != null) {
								prop = PropertyUtils.getProperty(componentType, "lite");
								Boolean lite = (prop == null ? null : (Boolean) prop);
								result = !lite;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-11);
				toStatusAction.setName("Брак");
				toStatusAction.setInitialStatus(status);
				toStatusAction.setDestinationStatus(defectStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				form = new InputReasonForm();
				form.setBeanName("bloodComponent");
				form.setActionCommentaryField("WFResultDescription");
				form.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(form);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				
				// Контроль качества
				List<NoStatusAction> noStatusActions = new ArrayList<NoStatusAction>();
				
				//На контроль качества
				NoStatusAction noStatusAction = new NoStatusAction(process)  {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							if (data.getStatusId() != 3) return false;
							
							Object prop = PropertyUtils.getProperty(data, "purchased");
							Boolean purchased = (prop == null? null: (Boolean) prop);
							if (purchased) {
								return false;
							}
							
							prop = PropertyUtils.getProperty(data, "qualityControlList");
							List qualityControlList = (prop == null ? null : (List) prop);
							if (qualityControlList != null && !qualityControlList.isEmpty()) {
								return false;
							}
							
							ProcessUser currentUser = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(currentUser.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.IN_CONTROL)) {
									return true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				noStatusAction.setId(1001);
				noStatusAction.setName("На контроле качества");
				
				activites = new ArrayList<IActivity>();
				InvokeMethodActivity invokeActivity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				invokeActivity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "initializeBloodComponentQualityControl", list);
				activity.setParentAction(noStatusAction);
				activites.add(invokeActivity);
				noStatusAction.setPreActionActivities(activites);
				
				activites = new ArrayList<IActivity>();
				invokeActivity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				invokeActivity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(noStatusAction);
				activites.add(invokeActivity);
				noStatusAction.setPostActionActivities(activites);
				
				noStatusActions.add(noStatusAction);
				
				
				//На контроль качества
				noStatusAction = new NoStatusAction(process)  {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							if (data.getStatusId() != 3) return false;
							
							Object prop = PropertyUtils.getProperty(data, "inControl");
							Boolean inControl = (prop == null ? null : (Boolean) prop);
							if (inControl != null && !inControl) {
								return false;
							}
							
							ProcessUser currentUser = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(currentUser.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.IN_CONTROL)) {
									return true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				noStatusAction.setId(1002);
				noStatusAction.setName("Контроль пройден");
				
				activites = new ArrayList<IActivity>();
				activitySetProp = new SetPropertyActivity();
				propertyChanges = new HashMap<String, Object>();
				propertyChanges.put("inControl", Boolean.FALSE);
				activitySetProp.setPropertyChanges(propertyChanges);
				activites.add(activitySetProp);
				noStatusAction.setPreActionActivities(activites);
				
				noStatusActions.add(noStatusAction);

				process.setNoStatusActions(noStatusActions);
			}
			else if (t.getType().equals("BloodDonation")) {
				System.out.println("Initialization process for blood donation");

				// Заполнение - Донация
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Заполнение");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				StatusChangeAction toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "transfusiologist");
								User registrator = (prop == null ? null : (User) prop);
								if (registrator != null && registrator.getId() == user.getId())
									result = true;
								prop = PropertyUtils.getProperty(data, "therapist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(1);
				toStatusAction.setName("Направить на донацию");
				toStatusAction.setInitialStatus(status);
				
				List<IActivity> activites = new ArrayList<IActivity>();
				InvokeMethodActivity activity = new InvokeMethodActivity();
				List<Object> list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "initializeBloodDonation", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				Status<T> toStatus = new Status<T>();
				toStatus.setId(2);
				toStatus.setName("Донация");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				
				// Донация - Донация не состоялась
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.OPERATIONAL)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "transfusiologist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(-2);
				toStatusAction.setName("Донация не состоялась");
				toStatusAction.setInitialStatus(status);
				
				toStatus = new Status<T>();
				toStatus.setId(-2);
				toStatus.setName("Донация не состоялась");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.OperationalHelper", "checkOperationalReject", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
                activity = new InvokeMethodActivity();
                list = new ArrayList<Object>();
                list.add(t);
                activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setOperational", list);
                activity.setParentAction(toStatusAction);
                activites.add(activity);

                activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("misexchange.PharmacyExchangeUtils", "checkDonationRequest", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				

				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				// Донация - Фракционирование
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							boolean process = false;
							Object prop = PropertyUtils.getProperty(data, "factEntries");
							Set factEntries = (prop == null ? null : (Set) prop);
							if (factEntries != null && !factEntries.isEmpty()) {
								for (Object entry: factEntries) {
									prop = PropertyUtils.getProperty(entry, "donationType");
									if (prop != null) {
										prop = PropertyUtils.getProperty(prop, "value");
										if (prop != null && StringUtils.contains(prop.toString(), "Кроводача")) {
											process = true;
										}
									}
								}
							}
							
							if (process) {
								List<Role> roleList = new ArrayList<Role>();
								roleList.addAll(user.getRoles());
								for (int i = 0; i < roleList.size(); i++) {
									if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
										return true;
									}
									else if (roleList.get(i).getRoleType().equals(RoleType.OPERATIONAL)) {
										return true;
									}
								}
								if (!result) {
									prop = PropertyUtils.getProperty(data, "transfusiologist");
									User therapist = (prop == null ? null : (User) prop);
									if (therapist != null && therapist.getId() == user.getId())
										result = true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(11);
				toStatusAction.setName("Отправить на фракционирование");
				toStatusAction.setInitialStatus(status);
				
				toStatus = new Status<T>();
				toStatus.setId(21);
				toStatus.setName("Фракционирование");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();

                activity = new InvokeMethodActivity();
                list = new ArrayList<Object>();
                list.add(t);
                activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setOperational", list);
                activity.setParentAction(toStatusAction);
                activites.add(activity);

				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("misexchange.PharmacyExchangeUtils", "checkDonationRequest", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				Status<T> tmpStatus = toStatus;
				
				// Донация - Получение результатов анализов
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.OPERATIONAL)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "transfusiologist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(12);
				toStatusAction.setName("Отправить на получение анализов");
				toStatusAction.setInitialStatus(status);
				
				toStatus = new Status<T>();
				toStatus.setId(3);
				toStatus.setName("Получение результатов анализов");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.OperationalHelper", "checkOperationalRegisteredComponents", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
                activity = new InvokeMethodActivity();
                list = new ArrayList<Object>();
                list.add(t);
                activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setOperational", list);
                activity.setParentAction(toStatusAction);
                activites.add(activity);

				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("misexchange.PharmacyExchangeUtils", "checkDonationRequest", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(2);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setDonorStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);

				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "queryAppointment", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				// Донация - Отменена
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.OPERATIONAL)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "transfusiologist");
								User therapist = (prop == null ? null : (User) prop);
								if (therapist != null && therapist.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(-3);
				toStatusAction.setName("Отменить донацию");
				toStatusAction.setInitialStatus(status);
				
				Status<T> notHappenedStatus = new Status<T>();
				notHappenedStatus.setId(-3);
				notHappenedStatus.setName("Донация отменена");
				notHappenedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(notHappenedStatus);
				
				activites = new ArrayList<IActivity>();
				ParametrizedPropertyLocalActivity localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toStatusAction);
				InputReasonForm reasonForm = new InputReasonForm();
				reasonForm.setBeanName("bloodDonation");
				reasonForm.setActionCommentaryField("WFResultDescription");
				reasonForm.setScope(EditablePropertyScope.LOCAL);
				localActivity.setDocument(reasonForm);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setOperational", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				statuses.put(notHappenedStatus.getId(), notHappenedStatus);
				
				// Фракционирование - Получение результатов анализов
				status = tmpStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.THERAPIST)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.RECTIFICATION)) {
									return true;
								}
							}
							Object prop = PropertyUtils.getProperty(data, "transfusiologist");
							User transfusiologist = (prop == null ? null : (User) prop);
							if (transfusiologist != null && transfusiologist.getId() == user.getId())
								return true;
							prop = PropertyUtils.getProperty(data, "staffNurse");
							User operational = (prop == null ? null : (User) prop);
							if (operational != null && operational.getId() == user.getId())
								return true;
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(21);
				toStatusAction.setName("Разделение КК выполнено");
				toStatusAction.setInitialStatus(status);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(2);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setDonorStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				/*activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setTransfusiologist", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);*/
				
				toStatusAction.setPreActionActivities(activites);
				
				toStatusAction.setDestinationStatus(toStatus);
				
				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(status.getId(), status);
				
				// Получение результатов анализов - Паспортизация
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.THERAPIST)) {
									return true;
								}
							}
							Object prop = PropertyUtils.getProperty(data, "transfusiologist");
							User transfusiologist = (prop == null ? null : (User) prop);
							if (transfusiologist != null && transfusiologist.getId() == user.getId())
								result = true;
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toStatusAction.setId(3);
				toStatusAction.setName("Результаты анализов получены");
				toStatusAction.setInitialStatus(status);

				toStatus = new Status<T>();
				toStatus.setId(4);
				toStatus.setName("Паспортизация");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity() {
					@Override
					public boolean isAllowed() {
						boolean result = false;
						try {
							String script = "context.getBean(\"bloodComponentDao\").countDocumentsInQuarantineByDonor(donation.getDonor(), \"\", false)";
							Object scriptResult = GroovyProcessor.processScript(script, "donation", getProcessedData());
							if ((Long) scriptResult > 0) {
								StringBuffer buffer = new StringBuffer("Внимание!@В карантине находятся компоненты крови, полученные от этого донора.@");
								
								script = "context.getBean(\"bloodComponentDao\").findDocumentsInQuarantineByDonor(donation.getDonor(), \"\", false, -1, -1, \"parentNumber,number\", false)";
								scriptResult = GroovyProcessor.processScript(script, "donation", getProcessedData());
								
								if (scriptResult instanceof List) {
									buffer.append("Номера компонентов:@");
									for (Object object: (List) scriptResult) {
										buffer.append(PropertyUtils.getProperty(object, "parentNumber"));
										buffer.append("-");
										buffer.append(PropertyUtils.getProperty(object, "number"));
										buffer.append("@");
									}
								}
								
								buffer.append("Статус этих компонентов может быть автоматически изменен в соответствии с результатами анализов.");
								
								AlertForm alertForm = (AlertForm) getDocument();
								alertForm.setAlert(buffer.toString());
								setDocument(alertForm);
								result = true;
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						return result;
					}
				};
				localActivity.setParentAction(toStatusAction);
				AlertForm alertForm = new AlertForm();
				alertForm.setBeanName("bloodDonation");
				localActivity.setDocument(alertForm);
				activites.add(localActivity);
				toStatusAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "sendBloodGroupToDonor", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				/*activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setTransfusiologist", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);*/
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "checkAndUpdateQuarantinedBloodComponentsStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				
				// Обработка биоматериала - Отвод от донорства
				status = toStatus;
				fromStatusActions = new ArrayList<StatusChangeAction>();
				StatusChangeAction toRejectedAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.THERAPIST)) {
									return true;
								}
							}
							Object prop = PropertyUtils.getProperty(data, "therapist");
							User therapist = (prop == null ? null : (User) prop);
							if (therapist != null && therapist.getId() == user.getId())
								result = true;
							prop = PropertyUtils.getProperty(data, "transfusiologist");
							User transfusiologist = (prop == null ? null : (User) prop);
							if (transfusiologist != null && transfusiologist.getId() == user.getId())
								result = true;
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				toRejectedAction.setId(-1);
				toRejectedAction.setName("Отвести от донорства");
				toRejectedAction.setCommentNecessary(true);
				toRejectedAction.setInitialStatus(status);
				
				Status<T> statusRejected = new Status<T>();
				statusRejected.setId(-1);
				statusRejected.setName("Отвод от донорства");
				statusRejected.setProcessedData(t);
				toRejectedAction.setDestinationStatus(statusRejected);
				
				activites = new ArrayList<IActivity>();
				localActivity = new ParametrizedPropertyLocalActivity();
				localActivity.setParentAction(toRejectedAction);
				DonorRejectionForm form = new DonorRejectionForm();
				form.setBeanName("bloodDonation");
				localActivity.setDocument(form);
				activites.add(localActivity);
				toRejectedAction.setLocalActivities(activites);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setDonorRejected", list);
				activity.setParentAction(toRejectedAction);
				activites.add(activity);
				
				/*activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setTransfusiologist", list);
				activity.setParentAction(toRejectedAction);
				activites.add(activity);*/
				
				toRejectedAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toRejectedAction);
				
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				statuses.put(statusRejected.getId(), statusRejected);
				
				
				// Дополнительные действия
				List<NoStatusAction> noStatusActions = new ArrayList<NoStatusAction>();
				
				//
				NoStatusAction noStatusAction = new NoStatusAction(process)  {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							if (data.getStatusId() < 3) return false;
							ProcessUser currentUser = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(currentUser.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
								if (roleList.get(i).getRoleType().equals(RoleType.DIVISION_SUPERINTENDENT)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.HEAD_NURSE)) {
									return true;
								}
								else if (roleList.get(i).getRoleType().equals(RoleType.REGISTRATOR)) {
									return true;
								}
							}
							
							Object prop = PropertyUtils.getProperty(data, "registrator");
							User registrator = (prop == null ? null : (User) prop);
							if (registrator != null && registrator.getId() == currentUser.getId())
								result = true;
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};
				noStatusAction.setId(1001);
				noStatusAction.setHistoryAction(false);
				noStatusAction.setName("Внести номер справки строгой отчетности");
				
				activites = new ArrayList<IActivity>();
				ParametrizedPropertyLocalActivity localParametrizedActivity = new ParametrizedPropertyLocalActivity();
				localParametrizedActivity.setParentAction(noStatusAction);
				InputReasonForm inputForm = new InputReasonForm();
				inputForm.setBeanName("bloodDonation");
				inputForm.setTitle("Номер справки");
				inputForm.setActionCommentaryField("reportBlankNumber");
				inputForm.setScope(EditablePropertyScope.GLOBAL);
				localParametrizedActivity.setDocument(inputForm);
				activites.add(localParametrizedActivity);
				noStatusAction.setLocalActivities(activites);
				
				noStatusActions.add(noStatusAction);
				
				process.setNoStatusActions(noStatusActions);
			}
			else if (t.getType().equals("BloodComponentOrder")) {
				System.out.println("Initialization process for blood component order");

				// Заполнение - Обработка
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Заполнение");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				StatusChangeAction toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User doctor = (prop == null ? null : (User) prop);
								if (doctor != null && doctor.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(1);
				toStatusAction.setName("Зарегистрировать");
				toStatusAction.setInitialStatus(status);
				
				Status<T> toStatus = new Status<T>();
				toStatus.setId(2);
				toStatus.setName("Обработка");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);

				fromStatusActions.add(toStatusAction);
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				
				
				// Обработка - Регистрация
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								boolean tmp_result = false;
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User staffNurse = (prop == null ? null : (User) prop);
								if (staffNurse != null && staffNurse.getId() == user.getId())
									tmp_result = true;
								prop = PropertyUtils.getProperty(data, "transfusionType");
								int priority = (Integer) prop;
								if (priority == 0 && tmp_result) {
									result = true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-1);
				toStatusAction.setName("Компонента нет в наличии");
				toStatusAction.setInitialStatus(toStatus);
				toStatusAction.setDestinationStatus(status);
				fromStatusActions.add(toStatusAction);
				
				// Обработка - Отказ
				status = toStatus;
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								boolean tmp_result = false;
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User staffNurse = (prop == null ? null : (User) prop);
								if (staffNurse != null && staffNurse.getId() == user.getId())
									tmp_result = true;
								prop = PropertyUtils.getProperty(data, "transfusionType");
								int priority = (Integer) prop;
								if (priority == 0 && tmp_result) {
									result = true;
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-2);
				toStatusAction.setName("Отказать");
				toStatusAction.setInitialStatus(status);
				
				Status<T> statusRejected = new Status<T>();
				statusRejected.setId(-1);
				statusRejected.setName("Отказ");
				statusRejected.setProcessedData(t);
				toStatusAction.setDestinationStatus(statusRejected);

				fromStatusActions.add(toStatusAction);

				statuses.put(statusRejected.getId(), statusRejected);
				
				// Обработка - Компонент выдан
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User staffNurse = (prop == null ? null : (User) prop);
								if (staffNurse != null && staffNurse.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(2);
				toStatusAction.setName("Компонент выдан");
				toStatusAction.setInitialStatus(status);
				
				toStatus = new Status<T>();
				toStatus.setId(3);
				toStatus.setName("Компонент выдан");
				toStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(toStatus);
				
				List<IActivity> activites = new ArrayList<IActivity>();
				SetPropertyActivity activitySetProp = new SetPropertyActivity();
				Map<String, Object> propertyChanges = new HashMap<String, Object>();
				propertyChanges.put("factDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
				activitySetProp.setPropertyChanges(propertyChanges);
				activites.add(activitySetProp);
				
				InvokeMethodActivity activity = new InvokeMethodActivity();
				List<Object> list = new ArrayList<Object>();
				list.add(t);
				list.add(10);
				list.add(6);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setChildBloodComponentStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "processComponentRequest", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				
				// Обработка - Забронировано
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User staffNurse = (prop == null ? null : (User) prop);
								if (staffNurse != null && staffNurse.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(3);
				toStatusAction.setName("Забронировать");
				toStatusAction.setInitialStatus(status);
				
				Status<T> reservedStatus = new Status<T>();
				reservedStatus.setId(4);
				reservedStatus.setName("Забронировано");
				reservedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(reservedStatus);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(11);
				list.add(11);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setChildBloodComponentStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				Collections.sort(fromStatusActions, new Comparator<StatusChangeAction>() {
					@Override
					public int compare(StatusChangeAction o1, StatusChangeAction o2) {
						return o2.getId() - o1.getId();
					}
				});
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				
				
				// Забронировано - Компонент выдан
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User staffNurse = (prop == null ? null : (User) prop);
								if (staffNurse != null && staffNurse.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(4);
				toStatusAction.setName("Компонент выдан");
				toStatusAction.setInitialStatus(reservedStatus);
				toStatusAction.setDestinationStatus(toStatus);
				
				activites = new ArrayList<IActivity>();
				activitySetProp = new SetPropertyActivity();
				propertyChanges = new HashMap<String, Object>();
				propertyChanges.put("factDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
				activitySetProp.setPropertyChanges(propertyChanges);
				activites.add(activitySetProp);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(10);
				list.add(6);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setChildBloodComponentStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.IntegrationHelper", "processComponentRequest", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);

				fromStatusActions.add(toStatusAction);
				
				// Забронировано - Обработка
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User doctor = (prop == null ? null : (User) prop);
								if (doctor != null && doctor.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(5);
				toStatusAction.setName("Отказ от бронирования");
				toStatusAction.setInitialStatus(reservedStatus);
				toStatusAction.setDestinationStatus(status);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(3);
				list.add(12);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setChildBloodComponentStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				reservedStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(reservedStatus.getId(), reservedStatus);
				
				// Компонент выдан - Обработка
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User doctor = (prop == null ? null : (User) prop);
								if (doctor != null && doctor.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-3);
				toStatusAction.setName("Пробы не пройдены");
				toStatusAction.setInitialStatus(toStatus);
				toStatusAction.setDestinationStatus(status);
				fromStatusActions.add(toStatusAction);
				
				// Компонент выдан - Утилизация
				status = toStatus;
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User doctor = (prop == null ? null : (User) prop);
								if (doctor != null && doctor.getId() == user.getId())
									result = true;
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-4);
				toStatusAction.setName("КК нет применения");
				toStatusAction.setInitialStatus(status);
				
				Status<T> statusUtilized = new Status<T>();
				statusUtilized.setId(-2);
				statusUtilized.setName("Утилизация");
				statusUtilized.setProcessedData(t);
				toStatusAction.setDestinationStatus(statusUtilized);

				fromStatusActions.add(toStatusAction);

				statuses.put(statusUtilized.getId(), statusUtilized);
				
				
				// Компонент выдан - Отменена
				toStatusAction = new StatusChangeAction(process) {
					@Override
					public boolean isAvailable() {
						boolean result = false;
						try {
							ProcessedData data = getProcess().getProcessedData();
							ProcessUser user = getProcess().getProcessUser();
							
							List<Role> roleList = new ArrayList<Role>();
							roleList.addAll(user.getRoles());
							for (int i = 0; i < roleList.size(); i++) {
								if (roleList.get(i).getRoleType().equals(RoleType.ENTERPRISE_ADMINISTRATION)) {
									return true;
								}
							}
							if (!result) {
								Object prop = PropertyUtils.getProperty(data, "staffNurse");
								User doctor = (prop == null ? null : (User) prop);
								if (doctor != null && doctor.getId() == user.getId()) {
									Calendar current = Calendar.getInstance(ApplicationHelper.getLocale());
									prop = PropertyUtils.getProperty(data, "factDate");
									Date factDate = (prop == null? null: (Date) prop);
									if (prop != null) {
										Calendar fact = Calendar.getInstance(ApplicationHelper.getLocale());
										fact.setTime(factDate);
										System.out.println("Time difference in millis: " + (current.getTimeInMillis() - fact.getTimeInMillis()));
										if (current.getTimeInMillis() - fact.getTimeInMillis() <= 5400000) {
											result = true;
										}
									}
								}
							}
						}
						catch (Exception e) {
							result = false;
							e.printStackTrace();
						}
						return result;
					}
				};

				toStatusAction.setId(-5);
				toStatusAction.setName("Возврат");
				toStatusAction.setInitialStatus(status);
				
				Status<T> cancelStatus = new Status<T>();
				cancelStatus.setId(-3);
				cancelStatus.setName("Отменена");
				cancelStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(cancelStatus);
				
				activites = new ArrayList<IActivity>();
				activity = new InvokeMethodActivity();
				list = new ArrayList<Object>();
				list.add(t);
				list.add(3);
				list.add(13);
				activity.setInvokeInformation("ru.efive.medicine.niidg.trfu.wf.util.WorkflowHelper", "setChildBloodComponentStatus", list);
				activity.setParentAction(toStatusAction);
				activites.add(activity);
				
				toStatusAction.setPreActionActivities(activites);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);

				statuses.put(status.getId(), status);
				statuses.put(cancelStatus.getId(), cancelStatus);
			}
			else if (t.getType().equals("Biomaterial")) {
				System.out.println("Initialization process for biomaterial");
				
				Status<T> status = new Status<T>();
				status.setId(1);
				status.setName("Зарегистрирован");
				status.setProcessedData(t);
				List<StatusChangeAction> fromStatusActions = new ArrayList<StatusChangeAction>();
				
				// Зарегистрирован - Готов к обработке
				StatusChangeAction toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(1);
				toStatusAction.setName("Готов к обработке");
				toStatusAction.setInitialStatus(status);
				
				Status<T> readyStatus = new Status<T>();
				readyStatus.setId(2);
				readyStatus.setName("Готов к обработке");
				readyStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(readyStatus);
				
				fromStatusActions.add(toStatusAction);
				
				statuses.put(readyStatus.getId(), readyStatus);
				
				// Зарегистрирован - Утилизирован
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(-1);
				toStatusAction.setName("Утилизирован");
				toStatusAction.setInitialStatus(status);
				
				Status<T> utilizedStatus = new Status<T>();
				utilizedStatus.setId(-10);
				utilizedStatus.setName("Утилизирован");
				utilizedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(utilizedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				status.setAvailableActions(fromStatusActions);
				
				statuses.put(utilizedStatus.getId(), utilizedStatus);
				statuses.put(status.getId(), status);
				
				
				// Готов к обработке - Обработка
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(2);
				toStatusAction.setName("Начать обработку");
				toStatusAction.setInitialStatus(readyStatus);
				
				Status<T> processingStatus = new Status<T>();
				processingStatus.setId(3);
				processingStatus.setName("Обработка");
				processingStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(processingStatus);
				
				fromStatusActions.add(toStatusAction);
				
				
				// Готов к обработке - Выдан
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(3);
				toStatusAction.setName("Выдать");
				toStatusAction.setInitialStatus(readyStatus);
				
				Status<T> issuedStatus = new Status<T>();
				issuedStatus.setId(4);
				issuedStatus.setName("Выдан");
				issuedStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(issuedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				readyStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(readyStatus.getId(), readyStatus);
				
				// Обработка - Выдан
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(4);
				toStatusAction.setName("Выдать");
				toStatusAction.setInitialStatus(processingStatus);
				toStatusAction.setDestinationStatus(issuedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				
				// Обработка - Заморожен
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(5);
				toStatusAction.setName("Заморозить");
				toStatusAction.setInitialStatus(processingStatus);
				
				Status<T> frozenStatus = new Status<T>();
				frozenStatus.setId(5);
				frozenStatus.setName("Заморожен");
				frozenStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(frozenStatus);
				
				fromStatusActions.add(toStatusAction);
				
				
				// Обработка - Утилизирован
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(-2);
				toStatusAction.setName("Утилизировать");
				toStatusAction.setInitialStatus(processingStatus);
				toStatusAction.setDestinationStatus(utilizedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				processingStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(processingStatus.getId(), processingStatus);
				statuses.put(issuedStatus.getId(), issuedStatus);
				
				
				// Заморожен - Разморожен
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(7);
				toStatusAction.setName("Разморозить");
				toStatusAction.setInitialStatus(frozenStatus);
				Status<T> unfrozenStatus = new Status<T>();
				unfrozenStatus.setId(6);
				unfrozenStatus.setName("Разморожен");
				unfrozenStatus.setProcessedData(t);
				toStatusAction.setDestinationStatus(unfrozenStatus);
				
				fromStatusActions.add(toStatusAction);
				
				// Заморожен - Выдан
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(6);
				toStatusAction.setName("Выдать");
				toStatusAction.setInitialStatus(frozenStatus);
				toStatusAction.setDestinationStatus(issuedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				// Заморожен - Утилизирован
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(-3);
				toStatusAction.setName("Утилизировать");
				toStatusAction.setInitialStatus(frozenStatus);
				toStatusAction.setDestinationStatus(utilizedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				frozenStatus.setAvailableActions(fromStatusActions);
				
				
				// Разморожен - Выдан
				fromStatusActions = new ArrayList<StatusChangeAction>();
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(8);
				toStatusAction.setName("Выдать");
				toStatusAction.setInitialStatus(unfrozenStatus);
				toStatusAction.setDestinationStatus(issuedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				// Заморожен - Утилизирован
				toStatusAction = new StatusChangeAction(process);
				toStatusAction.setId(-4);
				toStatusAction.setName("Утилизировать");
				toStatusAction.setInitialStatus(unfrozenStatus);
				toStatusAction.setDestinationStatus(utilizedStatus);
				
				fromStatusActions.add(toStatusAction);
				
				unfrozenStatus.setAvailableActions(fromStatusActions);
				
				statuses.put(frozenStatus.getId(), frozenStatus);
				statuses.put(unfrozenStatus.getId(), unfrozenStatus);
				
				status = new Status<T>();
				status.setId(100);
				status.setName("Разделен");
				status.setProcessedData(t);
				statuses.put(status.getId(), status);
			}
			System.out.println("new process initialization");

			Status<T> currentStatus = statuses.get(t.getStatusId());
			if (currentStatus != null) {
				System.out.println("found current status: " + currentStatus.getName());
				process.setCurrentStatus(currentStatus);
			}
		} catch (Exception e) {
			System.out.println("exception in initialization process");
			process = null;
			e.printStackTrace();
		}
		return process;
	}

    private static  IActivity createMailActivity(Donor donor, ProcessedData t) throws Exception, NoSuchMethodException, IllegalAccessException {
        EmailTemplate emailTemplate = ((EmailTemplateDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.EMAIL_TEMPLATE_DAO)).findByName("Приглашение на донацию");
        SendMailActivity mailActivity = new SendMailActivity();
        Map<String, Object> localContext = new HashMap<String, Object>();
        localContext.put("donor", donor);
        mailActivity.setLocalContext(localContext);
        mailActivity.setEmailTemplate(emailTemplate);

        return mailActivity;
    }
}