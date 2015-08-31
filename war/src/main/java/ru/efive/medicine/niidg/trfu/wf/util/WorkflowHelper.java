package ru.efive.medicine.niidg.trfu.wf.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationEntryType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Recommendation;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.Anamnesis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentProcessingMapping;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationEntry;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.PheresisReport;
import ru.efive.medicine.niidg.trfu.data.entity.QuarantineType;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DictionaryManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.ExaminationEntryListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.util.EngineHelper;

public final class WorkflowHelper {
	
	public static boolean initializeExamination(ExaminationRequest examination) {
    	boolean result = false;
    	try {
    		FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
    		if (examination.getExaminationType() == 0 && examination.getAnamnesis() == null) {
    			Anamnesis anamnesis = new Anamnesis();
    			anamnesis.setHeredity("Не отягощена");
    			anamnesis.setSicknesses("Нет");
    			anamnesis.setTransfusions("Нет");
    			anamnesis.setVaccinations("Нет");
				examination.setAnamnesis(anamnesis);
			}
			if (examination.getAnamnesis() != null) {
				sessionManagement.getDAO(AnamnesisDAOImpl.class, ApplicationHelper.ANAMNESIS_DAO).save(examination.getAnamnesis());
			}
			List<AnalysisType> types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Обследование", false);
			List<Analysis> analysisList = new ArrayList<Analysis>();
			AnalysisDAOImpl adao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis = adao.save(analysis);
				analysisList.add(analysis);
			}
			ExternalAppointment appointment = new ExternalAppointment();
			appointment.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
			appointment.setAuthor(sessionManagement.getLoggedUser());
			appointment.setActive(true);
			appointment.setDeleted(false);
			appointment.setTests(analysisList);
			//examination.setTests(analysisList);
			examination.setAppointment(appointment);
			
			Set<ExaminationEntry> entryList = new HashSet<ExaminationEntry>();
			DictionaryDAOImpl ddao = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO);
			ExaminationEntryDAOImpl dao = sessionManagement.getDAO(ExaminationEntryDAOImpl.class, ApplicationHelper.EXAMINATION_ENTRY_DAO);
			List<ExaminationEntryType> list = ddao.findBaseExaminationEntryTypes(false);
			for (ExaminationEntryType type:list) {
				List<ExaminationEntryType> child = ddao.findChildExaminationEntryTypes(false, type.getId());
				ExaminationEntry entry = new ExaminationEntry();
				entry.setDeleted(false);
				entry.setExaminationRequest(examination);
				entry.setType(type);
				entry.setValue(type.getDefaultValue());
				entry = dao.save(entry);
				entryList.add(entry);
				for (ExaminationEntryType childType: child) {
					ExaminationEntry childEntry = new ExaminationEntry();
					childEntry.setDeleted(false);
					childEntry.setExaminationRequest(examination);
					childEntry.setType(childType);
					childEntry.setValue(childType.getDefaultValue());
					childEntry.setParentEntry(entry);
					childEntry = dao.save(childEntry);
					entryList.add(childEntry);
				}
			}
			ExaminationEntryListHolderBean examinationEntryList = 
				(ExaminationEntryListHolderBean) context.getApplication().evaluateExpressionGet(context, "#{examinationEntryList}", ExaminationEntryListHolderBean.class);
			examination.setExaminationEntryList(examinationEntryList.getEntriesByExaminationRequest(examination.getId()));
			
			List<Classifier> recommendationTypes = ddao.findByCategory(Classifier.class, "Типы рекомендаций", false);
			for (Classifier type: recommendationTypes) {
				Recommendation recommendation = new Recommendation();
				recommendation.setType(type);
				recommendation.setDeleted(false);
				recommendation.setDecisionReceived(false);
				recommendation.setPrescripted(false);
				examination.addRecommendation(recommendation);
			}
			
			examination.setAppointment(sessionManagement.getDAO(ExternalAppointmentDaoImpl.class, "externalAppointmentDao").save(appointment));
			
			result = true;
    	}
    	catch (Exception e) {
    		result = false;
    		e.printStackTrace();
    	}
    	return result;
    }
	
	public static boolean initializeBloodDonation(BloodDonationRequest donation) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			if (donation.isPheresis() && donation.getReport() == null) {
				donation.setReport(new PheresisReport());
			}
			if (donation.getReport() != null) {
				sessionManagement.getDAO(PheresisDAOImpl.class, ApplicationHelper.PHERESIS_DAO).save(donation.getReport());
			}
			List<AnalysisType> types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Донация", false);
			List<Analysis> analysisList = new ArrayList<Analysis>();
			AnalysisDAOImpl adao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis = adao.save(analysis);
				analysisList.add(analysis);
			}
			
			ExternalAppointment appointment = new ExternalAppointment();
			appointment.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
			appointment.setAuthor(sessionManagement.getLoggedUser());
			appointment.setActive(true);
			appointment.setDeleted(false);
			appointment.setTests(analysisList);
			//donation.setTests(analysisList);
			donation.setAppointment(appointment);
			
			types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Иммуносерология", false);
			Set<Analysis> analysisSet = new HashSet<Analysis>();
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis.setValue("-");
				analysis = adao.save(analysis);
				analysisSet.add(analysis);
			}
			donation.setTestsImmuno(analysisSet);
			
			donation.setAppointment(sessionManagement.getDAO(ExternalAppointmentDaoImpl.class, "externalAppointmentDao").save(appointment));
			
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	public static ActionResult checkBloodComponentOrderNotChangedExternally(BloodComponentOrderRequest request){
		System.out.println("Start checkBloodComponentOrderNotChangedExternally");
		final FacesContext context = FacesContext.getCurrentInstance();
		final SessionManagementBean sessionManagement = context.getApplication().evaluateExpressionGet(
				context, "#{sessionManagement}", SessionManagementBean.class
		);
		final BloodComponentOrderRequestDAOImpl dao = sessionManagement.getDAO(
				BloodComponentOrderRequestDAOImpl.class, ApplicationHelper.COMPONENT_ORDER_DAO
		);
		final BloodComponentOrderRequest currentState = dao.get(request.getId());
		final boolean processed = currentState.getCreated().equals(request.getCreated());
		System.out.println("End checkBloodComponentOrderNotChangedExternally = " + processed);
		final ActionResult result = new ActionResult();
		result.setProcessed(processed);
		if(!processed) {
			result.setDescription("Обрабатываемая вами заявка на выдачу компонентов крови была изменена из МИС. Обновите страницу и проверьте данные.");
		}
		return result;
	}
	
	public static boolean setChildBloodComponentStatus(BloodDonationRequest donation, Integer statusId) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			List<BloodComponent> list = dao.findComponentsByDonation(donation.getId());
			for (BloodComponent bloodComponent: list) {
				bloodComponent.setStatusId(statusId);
				dao.save(bloodComponent);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setChildBloodComponentStatus(BloodComponentOrderRequest request, Integer statusId) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			List<BloodComponent> list = dao.findComponentsByOrder(request.getId());
			for (BloodComponent bloodComponent: list) {
				bloodComponent.setStatusId(statusId);
				dao.save(bloodComponent);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setChildBloodComponentStatus(BloodComponentOrderRequest request, Integer statusId, Integer actionId) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			List<BloodComponent> list = dao.findComponentsByOrder(request.getId());
			for (BloodComponent bloodComponent: list) {
				HistoryEntry historyEntry = new HistoryEntry();
				Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
				historyEntry.setCreated(date);
				historyEntry.setStartDate(date);
				historyEntry.setOwner(sessionManagement.getLoggedUser());
				historyEntry.setDocType(bloodComponent.getType());
				historyEntry.setParentId(bloodComponent.getId());
				historyEntry.setActionId(actionId);
				historyEntry.setFromStatusId(bloodComponent.getStatusId());
				bloodComponent.setStatusId(statusId);
				historyEntry.setToStatusId(bloodComponent.getStatusId());
				historyEntry.setEndDate(date);
				historyEntry.setProcessed(true);
				historyEntry.setCommentary("");
				Set<HistoryEntry> history = bloodComponent.getHistory();
				if (history == null) {
					history = new HashSet<HistoryEntry>();
				}
				history.add(historyEntry);
				bloodComponent.setHistory(history);
				if (actionId == 12) {
					bloodComponent.setOrderId(0);
				}
				dao.save(bloodComponent);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setDonorStatus(ExaminationRequest request, Integer statusId) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			Donor donor = dao.get(request.getDonor().getId());
			if (donor != null) {
				donor.setStatusId(statusId);
				donor = dao.save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setDonorStatus(BloodDonationRequest request, Integer statusId) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			Donor donor = dao.get(request.getDonor().getId());
			if (donor != null) {
				donor.setStatusId(statusId);
				donor = dao.save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean sendBloodGroupToDonor(BloodDonationRequest donation) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			List<Analysis> list = donation.getTestList();
			Donor donor = donation.getDonor();
			String bloodGroup = "";
			String rhesusFactor = "";
			for (Analysis analysis:list) {
				if (StringUtils.contains(analysis.getType().getValue(), "Группа крови")) {
					bloodGroup = analysis.getValue();
				}
				else if (StringUtils.contains(analysis.getType().getValue(), "Rh - фактор")) {
					rhesusFactor = analysis.getValue();
				}
			}
			boolean needUpdate = false;
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			List<BloodComponent> components = dao.findComponentsByDonation(donation.getId());
			DictionaryDAOImpl dictionaryDao = (DictionaryDAOImpl) sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO);
			List<BloodGroup> bloodGroupList = dictionaryDao.findByValue(BloodGroup.class, bloodGroup);
			if (bloodGroupList.size() > 0 && bloodGroupList.get(0).getValue() != null && !bloodGroupList.get(0).getValue().equals("") && !bloodGroupList.get(0).getValue().equals("Не определена")) {
				if (donor.getBloodGroup().getValue().equals("Не определена")) {
					donor.setBloodGroup(bloodGroupList.get(0));
					needUpdate = true;
				}
				for (BloodComponent component: components) {
					component.setBloodGroup(bloodGroupList.get(0));
					dao.save(component);
				}
			}
			List<Classifier> rhesusFactorList = dictionaryDao.findByValue(Classifier.class, rhesusFactor);
			if (rhesusFactorList.size() > 0 && rhesusFactorList.get(0) != null && !rhesusFactorList.get(0).getValue().equals("") && !rhesusFactorList.get(0).getValue().equals("Не определен")) {
				if (donor.getRhesusFactor().getValue().equals("Не определен")) {
					donor.setRhesusFactor(rhesusFactorList.get(0));
					needUpdate = true;
				}
				for (BloodComponent component: components) {
					component.setRhesusFactor(rhesusFactorList.get(0));
					dao.save(component);
				}
			}
			if (needUpdate) {
				sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO).save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setDonorRejected(ExaminationRequest request) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			DonorRejectionDAOImpl rejdao = sessionManagement.getDAO(DonorRejectionDAOImpl.class, ApplicationHelper.REJECTION_DAO);
			Donor donor = dao.get(request.getDonor().getId());
			DonorRejection rejection = request.getRejection();
			if (donor != null && rejection != null) {
				HistoryEntry historyEntry = new HistoryEntry();
				Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
				historyEntry.setCreated(date);
				historyEntry.setStartDate(date);
				historyEntry.setOwner(sessionManagement.getLoggedUser());
				historyEntry.setDocType(donor.getType());
				historyEntry.setParentId(donor.getId());
				historyEntry.setActionId(-3);
				historyEntry.setFromStatusId(donor.getStatusId());
				donor.setStatusId(rejection.getRejectionType().getType() < 3? -1: -2);
				rejection.setDonor(donor);
				rejection.setRequest("e_" + request.getId());
				rejection = rejdao.save(rejection);
				donor.setRejection(rejection);
				historyEntry.setToStatusId(donor.getStatusId());
				historyEntry.setEndDate(date);
				historyEntry.setProcessed(true);
				historyEntry.setCommentary(rejection.getRejectionType().getValue());

				donor.addToHistory(historyEntry);

				donor = dao.save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setDonorRejected(BloodDonationRequest request) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			DonorRejectionDAOImpl rejdao = sessionManagement.getDAO(DonorRejectionDAOImpl.class, ApplicationHelper.REJECTION_DAO);
			Donor donor = dao.get(request.getDonor().getId());
			DonorRejection rejection = request.getRejection();
			if (donor != null && rejection != null) {
				HistoryEntry historyEntry = new HistoryEntry();
				Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
				historyEntry.setCreated(date);
				historyEntry.setStartDate(date);
				historyEntry.setOwner(sessionManagement.getLoggedUser());
				historyEntry.setDocType(donor.getType());
				historyEntry.setParentId(donor.getId());
				historyEntry.setActionId(-3);
				historyEntry.setFromStatusId(donor.getStatusId());
				donor.setStatusId(rejection.getRejectionType().getType() < 3? -1: -2);
				rejection.setDonor(donor);
				rejection.setRequest("d_" + request.getId());
				rejection = rejdao.save(rejection);
				donor.setRejection(rejection);
				historyEntry.setToStatusId(donor.getStatusId());
				historyEntry.setEndDate(date);
				historyEntry.setProcessed(true);
				historyEntry.setCommentary(rejection.getRejectionType().getValue());

				donor.addToHistory(historyEntry);

				donor = dao.save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setOperational(BloodDonationRequest request) {
		boolean result = false;
		try {
			if (request.getStaffNurse() == null || request.getStaffNurse().getId() == 0) {
				FacesContext context = FacesContext.getCurrentInstance();
				SessionManagementBean sessionManagement = 
					(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
				request.setStaffNurse(sessionManagement.getLoggedUser());
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setQuarantineFinishDate(BloodComponent component) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			QuarantineTypeDAOImpl dao = sessionManagement.getDAO(QuarantineTypeDAOImpl.class, ApplicationHelper.QUARANTINE_TYPE_DAO);
			List<QuarantineType> types = dao.findDocumentsByComponentType(component.getComponentType().getId(), false);
			if (types.size() > 0) {
				QuarantineType type = types.get(0);
				Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				if (type.getYears() != 0) calendar.add(Calendar.YEAR, type.getYears());
				if (type.getMonths() != 0) calendar.add(Calendar.MONTH, type.getMonths());
				if (type.getDays() != 0) calendar.add(Calendar.DATE, type.getDays());
				component.setQuarantineFinishDate(calendar.getTime());
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setCandidate(ExaminationRequest request) {
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DonorDAOImpl dao = sessionManagement.getDAO(DonorDAOImpl.class, ApplicationHelper.DONOR_DAO);
			Donor donor = dao.get(request.getDonor().getId());
			if (donor != null && (donor.getStatusId() == -1 || donor.getStatusId() == -2)) {
				HistoryEntry historyEntry = new HistoryEntry();
				Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
				historyEntry.setCreated(date);
				historyEntry.setStartDate(date);
				historyEntry.setOwner(sessionManagement.getLoggedUser());
				historyEntry.setDocType(donor.getType());
				historyEntry.setParentId(donor.getId());
				historyEntry.setActionId(-1);
				historyEntry.setFromStatusId(donor.getStatusId());
				donor.setStatusId(1);
				historyEntry.setToStatusId(donor.getStatusId());
				historyEntry.setEndDate(date);
				historyEntry.setProcessed(true);
				historyEntry.setCommentary("");

				donor.addToHistory(historyEntry);

				donor = dao.save(donor);
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean checkAndUpdateQuarantinedBloodComponentsStatus(BloodDonationRequest request) {
		boolean result = false;
		try {
			List<Analysis> tests = request.getTestList();
			boolean infected = false;
			Analysis infectionDetectedTest = null;
			for (Analysis test: tests) {
				if (test.getType().isInfectionTest() && test.getValue().equals("Положительный")) {
					infected = true;
					infectionDetectedTest = test;
				}
			}
			FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
			
			if (infected) {
				// Перевод компонентов из карантина в брак
				List<BloodComponent> quarantinedComponents = dao.findDocumentsInQuarantineByDonor(request.getDonor(), "", false, -1, -1, null, false);
				
				for (BloodComponent bloodComponent: quarantinedComponents) {
					HistoryEntry historyEntry = new HistoryEntry();
					Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
					historyEntry.setCreated(date);
					historyEntry.setStartDate(date);
					historyEntry.setOwner(sessionManagement.getLoggedUser());
					historyEntry.setDocType(bloodComponent.getType());
					historyEntry.setParentId(bloodComponent.getId());
					historyEntry.setActionId(-99);
					historyEntry.setFromStatusId(bloodComponent.getStatusId());
					bloodComponent.setStatusId(6);
					historyEntry.setToStatusId(bloodComponent.getStatusId());
					historyEntry.setEndDate(date);
					historyEntry.setProcessed(true);
					historyEntry.setCommentary("Положительная реакция на " + infectionDetectedTest.getType().getValue());
					Set<HistoryEntry> history = bloodComponent.getHistory();
					if (history == null) {
						history = new HashSet<HistoryEntry>();
					}
					history.add(historyEntry);
					bloodComponent.setHistory(history);
					dao.save(bloodComponent);
				}
			}
			else {
				List<BloodComponent> quarantinedComponents = dao.findQuarantinedDocumentsByDonor(request.getDonor(), "", false, -1, -1, null, false);
				BloodComponentProcessingMappingDAOImpl mappingDao = sessionManagement.getDAO(BloodComponentProcessingMappingDAOImpl.class, "componentProcessingMappingDao");
				for (BloodComponent bloodComponent: quarantinedComponents) {
					HistoryEntry historyEntry = new HistoryEntry();
					Date date = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
					historyEntry.setCreated(date);
					historyEntry.setStartDate(date);
					historyEntry.setOwner(sessionManagement.getLoggedUser());
					historyEntry.setDocType(bloodComponent.getType());
					historyEntry.setParentId(bloodComponent.getId());
					historyEntry.setActionId(99);
					historyEntry.setFromStatusId(bloodComponent.getStatusId());
					bloodComponent.setStatusId(5);
					historyEntry.setToStatusId(bloodComponent.getStatusId());
					historyEntry.setEndDate(date);
					historyEntry.setProcessed(true);
					historyEntry.setCommentary("Отрицательные результаты анализов");
					Set<HistoryEntry> history = bloodComponent.getHistory();
					if (history == null) {
						history = new HashSet<HistoryEntry>();
					}
					history.add(historyEntry);
					bloodComponent.setHistory(history);
					BloodComponentProcessingMapping mapping = mappingDao.findMapping(bloodComponent.getComponentType(), 0);
					if (mapping != null && mapping.getDestinationType() != null) {
						bloodComponent.setComponentType(mapping.getDestinationType());
					}
					dao.save(bloodComponent);
				}
			}
			
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean initializeBloodComponentQualityControl(BloodComponent bloodComponent) {
		boolean result = false;
    	try {
    		FacesContext context = FacesContext.getCurrentInstance();
			SessionManagementBean sessionManagement = 
				(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
			DictionaryManagementBean dictionaryManagement = 
				(DictionaryManagementBean) context.getApplication().evaluateExpressionGet(context, "#{dictionaryManagement}", DictionaryManagementBean.class);
			
			List<AnalysisType> types = dictionaryManagement.getQualityControlMappingEntry(bloodComponent.getComponentType()).getAnalysisTypes();
			List<Analysis> analysisList = new ArrayList<Analysis>();
			AnalysisDAOImpl adao = sessionManagement.getDAO(AnalysisDAOImpl.class, ApplicationHelper.ANALYSIS_DAO);
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis = adao.save(analysis);
				analysisList.add(analysis);
			}
			ExternalAppointment appointment = new ExternalAppointment();
			appointment.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
			appointment.setAuthor(sessionManagement.getLoggedUser());
			appointment.setActive(true);
			appointment.setDeleted(false);
			appointment.setTests(analysisList);
			//bloodComponent.setQualityControlList(analysisList);
			bloodComponent.setAppointment(appointment);
			bloodComponent.setInControl(true);
			
			result = true;
    	}
    	catch (Exception e) {
    		result = false;
    		e.printStackTrace();
    	}
    	return result;
	}
	
	public static boolean setTransfusiologist(BloodDonationRequest request) {
		boolean result = false;
		try {
			if (request.getTransfusiologist() == null || request.getTransfusiologist().getId() == 0) {
				FacesContext context = FacesContext.getCurrentInstance();
				SessionManagementBean sessionManagement = 
					(SessionManagementBean) context.getApplication().evaluateExpressionGet(context, "#{sessionManagement}", SessionManagementBean.class);
				request.setTransfusiologist(sessionManagement.getLoggedUser());
			}
			result = true;
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static ActionResult checkDonorInformation(ExaminationRequest examination) {
		ActionResult result = new ActionResult();
		result.setProcessed(false);
		try {
			if (examination != null) {
				if (examination.getDonor() == null || StringUtils.isEmpty(examination.getDonor().getDescription())) {
					result.setDescription("Не указан донор");
					return result;
				}
				if (StringUtils.isEmpty(examination.getDonor().getMail())) {
					result.setDescription("Не указан адрес электронной почты донора");
					return result;
				}
				if (examination.getPlanDate() == null) {
					result.setDescription("Не указана запланированная дата донации");
					return result;
				}
				if (examination.getPlanDate().before(new Date())) {
					result.setDescription("Запланированная дата уже прошла. Невозможно уведомить донора");
					return result;
				}
				result.setProcessed(true);
			}
		}
		catch (Exception e) {
			result.setProcessed(false);
			result.setDescription(EngineHelper.DEFAULT_ERROR_MESSAGE);
			e.printStackTrace();
		}
		return result;
	}
	
	public static ActionResult checkDonationInfections(BloodComponent component) {
		ActionResult result = new ActionResult();
		result.setProcessed(true);
		try {
			BloodDonationRequest request = ((BloodDonationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONATION_DAO)).get(component.getDonationId());
			
			if (request != null) {
				List<Analysis> tests = request.getTestList();
				boolean infected = false;
				Analysis infectionDetectedTest = null;
				for (Analysis test: tests) {
					if (test.getType() != null && test.getType().isInfectionTest() && StringUtils.equals(test.getValue(), "Положительный")) {
						infected = true;
						infectionDetectedTest = test;
					}
				}
				if (infected) {
					result.setDescription("Компонент не может быть выдан. Положительная реакция на " + infectionDetectedTest.getType().getValue());
					result.setProcessed(false);
				}
			}
		}
		catch (Exception e) {
			result.setProcessed(false);
			result.setDescription(EngineHelper.DEFAULT_ERROR_MESSAGE);
			e.printStackTrace();
		}
		return result;
	}
	
}