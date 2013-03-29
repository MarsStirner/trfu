package ru.efive.medicine.niidg.trfu.wf.util;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.context.FacesContext;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.*;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.data.entity.medical.OperationReport;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ActionResult;
import ru.korusconsulting.external.DivisionInfo;
import ru.korusconsulting.external.EritrocyteMass;
import ru.korusconsulting.external.FinalVolume;
import ru.korusconsulting.external.IssueResult;
import ru.korusconsulting.external.LaboratoryMeasure;
import ru.korusconsulting.external.OrderIssueInfo;
import ru.korusconsulting.external.PatientCredentials;
import ru.korusconsulting.external.ProcedureInfo;
import ru.korusconsulting.external.TransfusionServiceImpl;
import ru.korusconsulting.laboratory.www.BiomaterialInfo;
import ru.korusconsulting.laboratory.www.DiagnosticRequestInfo;
import ru.korusconsulting.laboratory.www.IAcrossIntf_FNKC;
import ru.korusconsulting.laboratory.www.IAcrossIntf_FNKCservice;
import ru.korusconsulting.laboratory.www.IAcrossIntf_FNKCserviceLocator;
import ru.korusconsulting.laboratory.www.OrderInfo;
import ru.korusconsulting.laboratory.www.DonorInfo;
import ru.korusconsulting.laboratory.www.Tindicator;

public class IntegrationHelper {
	
	public static ActionResult queryAppointment(ExaminationRequest examination) {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationPropertiesHolder propertiesHolder = 
			(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
		
		ActionResult result = new ActionResult();
		result.setProcessed(false);
		
		if (propertiesHolder == null || examination == null || examination.getId() == 0) {
			result.setDescription("Внутренняя ошибка");
			return result;
		}
		if (examination.getDonor() == null) {
			result.setDescription("Не указан донор");
			return result;
		}
		if (examination.getAppointment() == null) {
			result.setDescription("Ошибка при формировании направления на исследования");
			return result;
		}
		
		boolean process = true;
		Object enabled = propertiesHolder.getProperty("application", "laboratory.integration.enabled");
		if (enabled == null) {
			logger.warn("Wrong system configuration. Property laboratory.integration.enabled is not set");
		}
		else {
			try {
				process = (Boolean) enabled;
			}
			catch (ClassCastException e) {
				logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
				process = true;
			}
		}
		Object serviceAddress = propertiesHolder.getProperty("application", "laboratory.integration.address");
		
		if (process) {
			try {
				boolean failureFlag = false;
				List<String> failureDescription = new ArrayList<String>();
				IAcrossIntf_FNKCservice service = new IAcrossIntf_FNKCserviceLocator();
				DonorInfo donor = new DonorInfo();
				donor.setId(examination.getDonor().getId());
				if (examination.getDonor().getBirth() == null) {
					failureFlag = true;
					failureDescription.add("Не указана дата рождения донора");
				}
				else {
					donor.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").format(examination.getDonor().getBirth()));
				}
				donor.setLastName(examination.getDonor().getLastName());
				donor.setFirstName(examination.getDonor().getFirstName());
				donor.setMiddleName(examination.getDonor().getMiddleName());
				donor.setGender(examination.getDonor().getGender() == 0? 2: 1);
				
				DiagnosticRequestInfo request = new DiagnosticRequestInfo();
				request.setOrderId(examination.getAppointment().getId());
				Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				calendar.setTime(examination.getAppointment().getCreated());
				request.setOrderDate(calendar);
				request.setOrderPhysicianLastName(examination.getAppointment().getAuthor().getLastName());
				request.setOrderPhysicianFirstName(examination.getAppointment().getAuthor().getFirstName());
				request.setOrderPhysicianMiddleName(examination.getAppointment().getAuthor().getMiddleName());
				request.setOrderPhysicianId("0");
				
				BiomaterialInfo biomaterial = new BiomaterialInfo();
				biomaterial.setOrderBiomaterialCode("");
				biomaterial.setOrderBiomaterialName("");
				biomaterial.setOrderBarCode(examination.getNumber());
				calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				calendar.setTime(examination.getCreated());
				biomaterial.setOrderProbeDate(calendar);
				
				OrderInfo order = new OrderInfo();
				order.setOrderPriority(2);
				if (examination.getAppointment().getTests() != null && examination.getAppointment().getTests().size() > 0) {
					order.setIndicators(getIndicators(examination.getAppointment()).toArray(new Tindicator[0]));
				}
				else {
					failureFlag = true;
					failureDescription.add("Внутренняя ошибка при формировании списка исследований");
				}
				
				if (!failureFlag) {
					if (order.getIndicators().length > 0) {
						IAcrossIntf_FNKC port;
						if (serviceAddress == null) {
							port = service.getIAcrossIntf_FNKCPort();
						}
						else {
							port = service.getIAcrossIntf_FNKCPort(new java.net.URL(serviceAddress.toString()));
						}
						int serviceResult = port.queryAnalysis(donor, request, biomaterial, order);
						
						logger.warn("Laboratory appointment upload result: " + serviceResult);
					}
					else {
						logger.warn("Empty result indicator list");
					}
					result.setProcessed(true);
				}
				else {
					result.setDescription(StringUtils.join(failureDescription, ":"));
				}
			}
			catch (Exception e) {
				result.setProcessed(false);
				result.setDescription("Ошибка при отправке направления на исследования в лабораторную систему");
				logger.error("Laboratory appointment upload failed", e);
			}
		}
		else {
			logger.warn("Laboratory integration processing disabled");
			result.setProcessed(true);
		}
		return result;
	}
	
	public static ActionResult queryAppointment(BloodDonationRequest donation) {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationPropertiesHolder propertiesHolder = 
			(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
		
		ActionResult result = new ActionResult();
		result.setProcessed(false);
		
		if (propertiesHolder == null || donation == null || donation.getId() == 0) {
			result.setDescription("Внутренняя ошибка");
			return result;
		}
		if (donation.getDonor() == null) {
			result.setDescription("Не указан донор");
			return result;
		}
		if (donation.getAppointment() == null) {
			result.setDescription("Ошибка при формировании направления на исследования");
			return result;
		}
		
		boolean process = true;
		Object enabled = propertiesHolder.getProperty("application", "laboratory.integration.enabled");
		if (enabled == null) {
			logger.warn("Wrong system configuration. Property laboratory.integration.enabled is not set");
		}
		else {
			try {
				process = (Boolean) enabled;
			}
			catch (ClassCastException e) {
				logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
				process = true;
			}
		}
		Object serviceAddress = propertiesHolder.getProperty("application", "laboratory.integration.address");
		
		if (process) {
			try {
				boolean failureFlag = false;
				List<String> failureDescription = new ArrayList<String>();
				IAcrossIntf_FNKCservice service = new IAcrossIntf_FNKCserviceLocator();
				DonorInfo donor = new DonorInfo();
				donor.setId(donation.getDonor().getId());
				if (donation.getDonor().getBirth() == null) {
					failureFlag = true;
					failureDescription.add("Не указана дата рождения донора");
				}
				else {
					donor.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").format(donation.getDonor().getBirth()));
				}
				donor.setLastName(donation.getDonor().getLastName());
				donor.setFirstName(donation.getDonor().getFirstName());
				donor.setMiddleName(donation.getDonor().getMiddleName());
				donor.setGender(donation.getDonor().getGender() == 0? 2: 1);
				
				DiagnosticRequestInfo request = new DiagnosticRequestInfo();
				request.setOrderId(donation.getAppointment().getId());
				Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				calendar.setTime(donation.getAppointment().getCreated());
				request.setOrderDate(calendar);
				request.setOrderPhysicianLastName(donation.getAppointment().getAuthor().getLastName());
				request.setOrderPhysicianFirstName(donation.getAppointment().getAuthor().getFirstName());
				request.setOrderPhysicianMiddleName(donation.getAppointment().getAuthor().getMiddleName());
				request.setOrderPhysicianId("0");
				
				BiomaterialInfo biomaterial = new BiomaterialInfo();
				biomaterial.setOrderBiomaterialCode("");
				biomaterial.setOrderBiomaterialName("");
				biomaterial.setOrderBarCode(donation.getNumber());
				calendar = Calendar.getInstance(ApplicationHelper.getLocale());
				calendar.setTime(donation.getCreated());
				biomaterial.setOrderProbeDate(calendar);
				
				OrderInfo order = new OrderInfo();
				order.setOrderPriority(2);
				if (donation.getAppointment().getTests() != null && donation.getAppointment().getTests().size() > 0) {
					order.setIndicators(getIndicators(donation.getAppointment()).toArray(new Tindicator[0]));
				}
				else {
					failureFlag = true;
					failureDescription.add("Внутренняя ошибка при формировании списка исследований");
				}
				
				if (!failureFlag) {
					if (order.getIndicators().length > 0) {
						IAcrossIntf_FNKC port;
						if (serviceAddress == null) {
							port = service.getIAcrossIntf_FNKCPort();
						}
						else {
							port = service.getIAcrossIntf_FNKCPort(new java.net.URL(serviceAddress.toString()));
						}
						int serviceResult = port.queryAnalysis(donor, request, biomaterial, order);
						
						logger.warn("Laboratory appointment upload result: " + serviceResult);
					}
					else {
						logger.warn("Empty result indicator list");
					}
					result.setProcessed(true);
				}
				else {
					result.setDescription(StringUtils.join(failureDescription, ":"));
				}
			}
			catch (Exception e) {
				result.setProcessed(false);
				result.setDescription("Ошибка при отправке направления на исследования в лабораторную систему");
				logger.error("Laboratory appointment upload failed", e);
			}
		}
		else {
			logger.warn("Laboratory integration processing disabled");
			result.setProcessed(true);
		}
		return result;
	}
	
	public static ActionResult queryAppointment(BloodComponent component) {
		FacesContext context = FacesContext.getCurrentInstance();
		ApplicationPropertiesHolder propertiesHolder = 
			(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
		
		ActionResult result = new ActionResult();
		result.setProcessed(false);
		
		if (propertiesHolder == null || component == null) {
			result.setDescription("Внутренняя ошибка");
			return result;
		}
		if (component.getAppointment() == null) {
			result.setDescription("Ошибка при формировании направления на исследования");
			return result;
		}
		
		boolean process = true;
		Object enabled = propertiesHolder.getProperty("application", "laboratory.integration.enabled");
		if (enabled == null) {
			logger.warn("Wrong system configuration. Property laboratory.integration.enabled is not set");
		}
		else {
			try {
				process = (Boolean) enabled;
			}
			catch (ClassCastException e) {
				logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
				process = true;
			}
		}
		Object serviceAddress = propertiesHolder.getProperty("application", "laboratory.integration.address");
		
		if (process) {
			try {
				//TODO: интеграция с ЛИС для контроля качества
				result.setProcessed(true);
			}
			catch (Exception e) {
				result.setProcessed(false);
				result.setDescription("Ошибка при отправке направления на исследования в лабораторную систему");
				logger.error("Laboratory appointment upload failed", e);
			}
		}
		else {
			logger.warn("Laboratory integration processing disabled");
			result.setProcessed(true);
		}
		return result;
	}
	
	
	private static List<Tindicator> getIndicators(ExternalAppointment appointment) {
		List<Tindicator> indicators = new ArrayList<Tindicator>();
		try {
			for (Analysis analysis: appointment.getTests()) {
				if (analysis.getType().isLaboratoryTest()) {
					for (ExternalIndicator externalIndicator: analysis.getType().getIndicators()) {
						Tindicator indicator = new Tindicator();
						indicator.setIndicatorCode(externalIndicator.getCode());
						indicator.setIndicatorName(externalIndicator.getName());
						indicators.add(indicator);
					}
				}
			}
			logger.warn("Indicator list size for appointment " + appointment.getId() + " - " + indicators.size());
		}
		catch (Exception e) {
			logger.error("Failed to get indicator list for appointment " + appointment.getId(), e);
		}
		return indicators;
	}
	
    public static ActionResult processComponentRequest(BloodComponentOrderRequest orderRequest) throws java.net.MalformedURLException {
    	ActionResult result = new ActionResult();
    	if (orderRequest.isFromMIS()) {
	    	
	    	FacesContext context = FacesContext.getCurrentInstance();
			ApplicationPropertiesHolder propertiesHolder = 
				(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
			
	    	boolean process = true;
			Object enabled = propertiesHolder.getProperty("application", "mis.integration.enabled");
			if (enabled == null) {
				logger.warn("Wrong system configuration. Property mis.integration.enabled is not set");
			}
			else {
				try {
					process = (Boolean) enabled;
				}
				catch (ClassCastException e) {
					logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
					process = true;
				}
			}
			Object serviceAddress = propertiesHolder.getProperty("application", "mis.integration.address");
			
			if (process) {
		    	TransfusionServiceImpl medicalService = new TransfusionServiceImpl(new java.net.URL(serviceAddress.toString()));
		    	
		        GregorianCalendar calendar = new GregorianCalendar();
		        calendar.setTime(orderRequest.getFactDate());
		        XMLGregorianCalendar factDate = new XMLGregorianCalendarImpl(calendar);
		        OrderIssueInfo component = new OrderIssueInfo();
		
		        component.setBloodGroupId(orderRequest.getBloodGroup().getNumber());
		        component.setComponentTypeId(orderRequest.getComponentType().getId());
		        component.setDonorId(orderRequest.getRecipientId());
		        component.setDoseCount(orderRequest.getDoseCount());
		        component.setNumber(orderRequest.getNumber());
		        component.setRhesusFactorId(orderRequest.getRhesusFactor().getValue().equals("отрицательный")?1:0);
		        //component.setId(Integer.valueOf(orderRequest.getExternalNumber()));
		        component.setVolume(orderRequest.getVolume());
		        
		        IssueResult issueResult = medicalService.getPortTransfusion().setOrderIssueResult(orderRequest.getId(),factDate, Arrays.asList(component), orderRequest.getCommentary());
		        
		        result.setProcessed(issueResult.isResult());
		        result.setDescription(issueResult.getDescription());
			}
			else {
				System.out.println("MIS integration disabled");
				result.setProcessed(true);
			}
    	}
    	else {
    		result.setProcessed(true);
    	}
        return result;
    }
    
    public static ActionResult processMedicalOperation(Operation operation) throws java.net.MalformedURLException {
    	ActionResult result = new ActionResult();
    	
    	FacesContext context = FacesContext.getCurrentInstance();
		ApplicationPropertiesHolder propertiesHolder = 
			(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
		
    	boolean process = true;
		Object enabled = propertiesHolder.getProperty("application", "mis.integration.enabled");
		if (enabled == null) {
			logger.warn("Wrong system configuration. Property mis.integration.enabled is not set");
		}
		else {
			try {
				process = (Boolean) enabled;
			}
			catch (ClassCastException e) {
				logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
				process = true;
			}
		}
		Object serviceAddress = propertiesHolder.getProperty("application", "mis.integration.address");
		
		if (process) {
	    	TransfusionServiceImpl medicalService = new TransfusionServiceImpl(new java.net.URL(serviceAddress.toString()));
	    	
	    	PatientCredentials patientCredentials = new PatientCredentials();
	    	if (operation.getDonor() == null) {
	    		result.setProcessed(false);
	    		result.setDescription("В лечебной процедуре не указан донор");
	    		return result;
	    	}
	    	patientCredentials.setId(operation.getDonor().getExternalId());
	    	patientCredentials.setFirstName(operation.getDonor().getFirstName());
	    	patientCredentials.setMiddleName(operation.getDonor().getMiddleName());
	    	patientCredentials.setLastName(operation.getDonor().getLastName());
	    	if (operation.getDonor().getBloodGroup() == null) {
	    		result.setProcessed(false);
	    		result.setDescription("Не указана группа крови донора");
	    		return result;
	    	}
	    	patientCredentials.setBloodGroupId(operation.getDonor().getBloodGroup().getId());
	    	if (operation.getDonor().getRhesusFactor() == null) {
	    		result.setProcessed(false);
	    		result.setDescription("Не указан резус-фактор донора");
	    		return result;
	    	}
	    	patientCredentials.setRhesusFactorId(StringUtils.equalsIgnoreCase(operation.getDonor().getRhesusFactor().getValue(), "положительный")? 0: 1);
	    	
	    	if (operation.getDonor().getBirth() == null) {
	    		result.setProcessed(false);
	    		result.setDescription("Не указана дата рождения донора");
	    		return result;
	    	}
	    	GregorianCalendar calendar = new GregorianCalendar();
	    	calendar.setTime(operation.getDonor().getBirth());
	    	patientCredentials.setBirth(new XMLGregorianCalendarImpl(calendar));
	    	
		    ProcedureInfo procedureInfo = new ProcedureInfo();
		    procedureInfo.setId(operation.getId());
		    calendar = new GregorianCalendar();
	    	calendar.setTime(operation.getFactDate() == null? operation.getCreated(): operation.getFactDate());
		    procedureInfo.setFactDate(new XMLGregorianCalendarImpl(calendar));
		    
		    EritrocyteMass eritrocyteMass = new EritrocyteMass();
		    List<LaboratoryMeasure> measures = new ArrayList<LaboratoryMeasure>();
		    List<FinalVolume> finalVolumeList = new ArrayList<FinalVolume>();
		    
		    if (operation.getOperationReport() != null) {
		    	OperationReport report = operation.getOperationReport();
		    	
		    	procedureInfo.setContraindication(report.getContraindication());
		    	if (report.getHemodynamicsBefore() != null) {
		    		procedureInfo.setBeforeHemodynamicsPulse(report.getHemodynamicsBefore().getPulse());
		    		procedureInfo.setBeforeHemodynamicsArterialPressure(report.getHemodynamicsBefore().getArterialPressure());
		    		procedureInfo.setBeforeHemodynamicsTemperature(report.getHemodynamicsBefore().getTemperature());
		    	}
		    	if (report.getHemodynamicsAfter() != null) {
		    		procedureInfo.setAfterHemodynamicsPulse(report.getHemodynamicsAfter().getPulse());
		    		procedureInfo.setAfterHemodynamicsArterialPressure(report.getHemodynamicsAfter().getArterialPressure());
		    		procedureInfo.setAfterHemodynamicsTemperature(report.getHemodynamicsAfter().getTemperature());
		    	}
		    	procedureInfo.setComplications(report.getComplications());
		    	
		    	if (report.getInitialParameters() != null) {
		    		procedureInfo.setInitialVolume(report.getInitialParameters().getVolume());
		    		procedureInfo.setInitialTbv(report.getInitialParameters().getTbv());
		    		procedureInfo.setInitialSpeed(report.getInitialParameters().getSpeed());
		    		procedureInfo.setInitialInletAcRatio(report.getInitialParameters().getInletAcRatio());
		    		procedureInfo.setInitialTime(report.getInitialParameters().getTime());
		    		procedureInfo.setInitialProductVolume(report.getInitialParameters().getProductVolume());
		    	}
		    	if (report.getChangedParameters() != null) {
		    		procedureInfo.setChangeVolume(report.getChangedParameters().getVolume());
		    		procedureInfo.setChangeTbv(report.getChangedParameters().getTbv());
		    		procedureInfo.setChangeSpeed(report.getChangedParameters().getSpeed());
		    		procedureInfo.setChangeInletAcRatio(report.getChangedParameters().getInletAcRatio());
		    		procedureInfo.setChangeTime(report.getChangedParameters().getTime());
		    		procedureInfo.setChangeProductVolume(report.getChangedParameters().getProductVolume());
		    	}
		    	
		    	if (report.getLiquidBalance() != null) {
		    		procedureInfo.setAcdLoad(report.getLiquidBalance().getAcdLoad());
		    		procedureInfo.setNaClLoad(report.getLiquidBalance().getNaClLoad());
		    		procedureInfo.setCaLoad(report.getLiquidBalance().getCaLoad());
		    		procedureInfo.setOtherLoad(report.getLiquidBalance().getOtherLoad());
		    		procedureInfo.setTotalLoad(report.getLiquidBalance().getTotalLoad());
		    		procedureInfo.setPackRemove(report.getLiquidBalance().getPackRemove());
		    		procedureInfo.setOtherRemove(report.getLiquidBalance().getOtherRemove());
		    		procedureInfo.setTotalRemove(report.getLiquidBalance().getTotalRemove());
		    		procedureInfo.setBalance(report.getLiquidBalance().getBalance());
		    	}
		    	
		    	if (operation.isUsingEr() && report.getEritrocyteMass() != null) {
		    		eritrocyteMass.setMaker(report.getEritrocyteMass().getErMaker());
		    		eritrocyteMass.setNumber(report.getEritrocyteMass().getErNumber());
		    		if (report.getEritrocyteMass().getBloodGroup() != null) {
		    			eritrocyteMass.setBloodGroupId(report.getEritrocyteMass().getBloodGroup().getId());
		    		}
		    		if (report.getEritrocyteMass().getRhesusFactor() != null) {
		    			eritrocyteMass.setRhesusFactorId(StringUtils.equalsIgnoreCase(report.getEritrocyteMass().getRhesusFactor().getValue(), "положительный")? 0: 1);
		    		}
		    		eritrocyteMass.setVolume(report.getEritrocyteMass().getErVolume());
		    		
		    		calendar = new GregorianCalendar();
		        	calendar.setTime(report.getEritrocyteMass().getProductionDate());
		    		eritrocyteMass.setProductionDate(new XMLGregorianCalendarImpl(calendar));
		    		
		    		calendar = new GregorianCalendar();
		        	calendar.setTime(report.getEritrocyteMass().getExpirationDate());
		    		eritrocyteMass.setExpirationDate(new XMLGregorianCalendarImpl(calendar));
		    		
		    		eritrocyteMass.setHt(report.getEritrocyteMass().getErHt());
		    		eritrocyteMass.setSalineVolume(report.getEritrocyteMass().getSalineVolume());
		    		eritrocyteMass.setFinalHt(report.getEritrocyteMass().getFinalHt());
		    	}
		    	
		    	if (report.getMeasureList() != null) {
		    		for (ru.efive.medicine.niidg.trfu.data.entity.medical.LaboratoryMeasure measure: report.getMeasureList()) {
		    			LaboratoryMeasure lm = new LaboratoryMeasure();
		    			lm.setId(measure.getType().getId());
		    			lm.setBeforeOperation(measure.getBeforeOperation());
		    			lm.setDuringOperation(measure.getDuringOperation());
		    			lm.setInProduct(measure.getInProduct());
		    			lm.setAfterOperation(measure.getAfterOperation());
		    			measures.add(lm);
		    		}
		    	}
		    	
		    	if (report.getFinalVolumeList() != null) {
		    		for (ru.efive.medicine.niidg.trfu.data.entity.medical.FinalVolume finalVolume: report.getFinalVolumeList()) {
		    			FinalVolume fv = new FinalVolume();
		    			fv.setTime(Double.valueOf(finalVolume.getTime()));
		    			fv.setAnticoagulantVolume(finalVolume.getAnticoagulantVolume());
		    			fv.setInletVolume(finalVolume.getInletVolume());
		    			fv.setPlasmaVolume(finalVolume.getPlasmaVolume());
		    			fv.setCollectVolume(finalVolume.getCollectVolume());
		    			fv.setAnticoagulantInCollect(finalVolume.getAnticoagulantInCollect());
		    			fv.setAnticoagulantInPlasma(finalVolume.getAnticoagulantInPlasma());
		    			finalVolumeList.add(fv);
		    		}
		    	}
	    	}
	    	
	    	IssueResult issueResult = medicalService.getPortTransfusion().setProcedureResult(patientCredentials, procedureInfo, eritrocyteMass, measures, finalVolumeList);
	    	
	        result.setProcessed(issueResult.isResult());
	        result.setDescription(issueResult.getDescription());
		}
		else {
			System.out.println("MIS integration disabled");
			result.setProcessed(true);
		}
        return result;
    }
    
    public static boolean updateDivisions() {
    	boolean result = false;
    	try {
    		FacesContext context = FacesContext.getCurrentInstance();
    		ApplicationPropertiesHolder propertiesHolder = 
    			(ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
    		
        	boolean process = true;
    		Object enabled = propertiesHolder.getProperty("application", "mis.integration.enabled");
    		if (enabled == null) {
    			logger.warn("Wrong system configuration. Property mis.integration.enabled is not set");
    		}
    		else {
    			try {
    				process = (Boolean) enabled;
    			}
    			catch (ClassCastException e) {
    				logger.warn("Wrong system configuration. Property laboratory.integration.enabled must be boolean");
    				process = true;
    			}
    		}
    		Object serviceAddress = propertiesHolder.getProperty("application", "mis.integration.address");
    		
    		if (process) {
    	    	TransfusionServiceImpl medicalService = new TransfusionServiceImpl(new java.net.URL(serviceAddress.toString()));
	        	List<DivisionInfo> divisionsInfo = medicalService.getPortTransfusion().getDivisions();
	        	DivisionDAOImpl dao = (DivisionDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DIVISION_DAO);
	            for (DivisionInfo divisionInfo: divisionsInfo) {
	            	Division division = dao.findByExternalId(divisionInfo.getId());
	            	if (division == null) {
	            		division = new Division();
	            		division.setCreated(new Date());
	            		division.setDeleted(false);
	            		division.setEdited(new Date());
	            		division.setName(divisionInfo.getName());
	            		division.setExternalId(divisionInfo.getId());
	            	}
	            	else {
	            		division.setDeleted(false);
	            		division.setEdited(new Date());
	            		division.setName(divisionInfo.getName());
	            	}
	            	dao.save(division);
	            }
    		}
    		else {
    			System.out.println("MIS integration disabled");
    		}
            result = true;
    	}
    	catch (Exception e) {
    		logger.error(e);
    		result = false;
    	}
    	return result;
    }
	
	private static final Logger logger = Logger.getLogger(IntegrationHelper.class);
}