package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import java.util.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.AnalysisDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.ExternalAppointmentDaoImpl;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.AnalysisExternalValue;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisEntry;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAntibioticSensitivity;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalImageValue;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalMicroOrganismResult;
import ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment.AnalysisValueConverterFactory;
import ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno.ImmunoAnalysisValueConverterFactory;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@WebService(name = "trfu-laboratory-integration", targetNamespace = "http://www.korusconsulting.ru", serviceName = "trfu-laboratory-integration", portName = "trfu-laboratory-integration")
public class LaboratoryIntegration {

    @WebMethod(operationName = "setAnalysisResults", action = "urn:setAnalysisResults")
    public String setAnalysisResults(
            @WebParam(name = "orderId") int orderId,
            @WebParam(name = "orderBarCode") String orderBarCode,
            @WebParam(name = "results") List<AnalysisResult> results,
            @WebParam(name = "biomaterialDefects") String biomaterialDefects,
            @WebParam(name = "resultDoctorLisId") int resultDoctorLisId) throws Exception {
    	
    	logParameters(orderId, orderBarCode, results, biomaterialDefects, resultDoctorLisId);
    	
        String result;
        try {
            logger.warn("Received laboratory integration message: orderId=" + orderId + ", orderBarCode=" + orderBarCode);
            if (StringUtils.isEmpty(orderBarCode)) {
                result = "Не указан штрих-код";
                logger.error(result);
                throw new Exception(result);
            }

            ExternalAppointmentDaoImpl dao = (ExternalAppointmentDaoImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.EXTERNAL_APPOINTMENT_DAO);
            List<ExternalAppointment> appointments = dao.getAppoitments(orderBarCode);
            if (appointments == null || appointments.isEmpty()) {
                result = "Не найдено направление на исследование для биоматериала " + orderId + " со штрихкодом " + orderBarCode;
                logger.error(result);
                throw new Exception(result);
            }

            List<Analysis> analysisList = new ArrayList<Analysis>();
            for (ExternalAppointment externalAppointment : appointments) {
                analysisList.addAll(externalAppointment.getTests());
            }

            BloodDonationRequest donationRequest = ((BloodDonationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONATION_DAO)).findDocumentByBarCode(orderBarCode);
            if (donationRequest != null) {
                updateDonationRequest(donationRequest, results);
                ((BloodDonationRequestDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONATION_DAO)).save(donationRequest);
            }
            updateAnalysisList(analysisList, results);

            ExternalAnalysisEntry entry = createExternalAnalysisEntry(results, biomaterialDefects, resultDoctorLisId);
            appointments.get(appointments.size() - 1).addHistoryEntry(entry);

            for (ExternalAppointment externalAppointment : appointments) {
                dao.save(externalAppointment);
            }

            result = "0";
            logger.warn("Result of the laboratory integration: " + result);
        } catch (Exception e) {
            result = e.getMessage();
            logger.error("setAnalysisResult exception", e);
        }
        return result;
    }

    private void updateDonationRequest(BloodDonationRequest donationRequest, List<AnalysisResult> results) {
        for (Analysis analysis : donationRequest.getTestsImmuno()) {
            for (ExternalIndicator indicator : analysis.getType().getIndicators()){
                for (AnalysisResult analysisResult : results) {
                    if (indicator.getCode().equals(analysisResult.getIndicatorCode())) {
                        String antigen = analysis.getType().getValue();
                        String resultValue = ImmunoAnalysisValueConverterFactory.getConverter(indicator).convert(analysisResult.getResultValueText(),antigen);
                        if (resultValue != null) {
                            analysis.setValue(resultValue);
                            ((AnalysisDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.ANALYSIS_DAO)).save(analysis);
                        }
                    }
                }
            }
        }
    }

    private void updateAnalysisList(List<Analysis> analysisList, List<AnalysisResult> results) {
        for (AnalysisResult analysisResult : results) {
            for (Analysis analysis : analysisList) {
                for (ExternalIndicator externalIndicator : analysis.getType().getIndicators()) {
                    if (externalIndicator.getCode().equals(analysisResult.getIndicatorCode())) {
                        AnalysisExternalValue externalValue = new AnalysisExternalValue();
                        externalValue.setDeleted(false);
                        externalValue.setIndicator(externalIndicator);
                        externalValue.setValue(AnalysisValueConverterFactory.getConverter(externalIndicator).convert(analysisResult.getResultValueText()));
                        externalValue.setUnit(analysisResult.getResultUnit());
                        externalValue.setNormValue(analysisResult.getResultNormString());
                        logger.warn("Analysis result: " + externalValue.getValue() + "; unit: " + externalValue.getUnit() + "; norm: " + externalValue.getNormValue());
                        analysis.addExternalValue(externalValue);
                    }
                }
            }
        }

        for (Analysis analysis : analysisList) {
            if (analysis.getExternalValues() != null) {
                List<AnalysisExternalValue> externalValues = analysis.getExternalValues();
                Collections.sort(externalValues, new Comparator<AnalysisExternalValue>() {
                    @Override
                    public int compare(AnalysisExternalValue o1, AnalysisExternalValue o2) {
                        return o1.getIndicator().getId() - o2.getIndicator().getId();
                    }
                });
                for (AnalysisExternalValue externalValue : externalValues) {
                    if (analysis.getExternalValues().size() > 1) {
                        analysis.setValueType(2); //множественные внешние значенияs
                    } else {
                        analysis.setValueType(1);
                        analysis.setValue(externalValue.getValue()); // единичное внешнее значение
                        analysis.setUnit(externalValue.getUnit());
                        analysis.setNormValue(externalValue.getNormValue());
                    }
                }
            }
        }
    }

    private ExternalAnalysisEntry createExternalAnalysisEntry(List<AnalysisResult> results, String biomaterialDefects, int resultDoctorLisId) {
        ExternalAnalysisEntry entry = new ExternalAnalysisEntry();
        entry.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
        entry.setDeleted(false);
        entry.setBiomaterialDefects(biomaterialDefects);
        entry.setLaboratoryPhysicianId(resultDoctorLisId);
        if (results != null && !results.isEmpty()) {
            logger.warn("Analysis result list size: " + results.size());
            for (AnalysisResult analysisResult : results) {
                entry.addResult(createExternalAnalysisResult(analysisResult));
            }

        } else {
            logger.warn("Empty analysis result list");
        }
        return entry;
    }

    private ExternalAnalysisResult createExternalAnalysisResult(AnalysisResult analysisResult) {
        ExternalAnalysisResult externalAnalysisResult = new ExternalAnalysisResult();
        externalAnalysisResult.setDeviceName(analysisResult.getDeviceName());
        externalAnalysisResult.setIndicatorCode(analysisResult.getIndicatorCode());
        externalAnalysisResult.setIndicatorName(analysisResult.getIndicatorName());
        externalAnalysisResult.setResultComment(analysisResult.getResultComment());
        externalAnalysisResult.setResultNormalityIndex(analysisResult.getResultNormalityIndex());
        externalAnalysisResult.setResultNormString(analysisResult.getResultNormString());
        externalAnalysisResult.setResultSignDate(analysisResult.getResultSignDate());
        externalAnalysisResult.setResultUnit(analysisResult.getResultUnit());
        externalAnalysisResult.setResultValueText(analysisResult.getResultValueText());
        externalAnalysisResult.setValueType(analysisResult.getValueType());
        if (analysisResult.getImageValues() != null) {
            for (ImageValue imageValue : analysisResult.getImageValues()) {
                ExternalImageValue externalImageValue = new ExternalImageValue();
                externalImageValue.setImageData(imageValue.getImageData());
                externalImageValue.setImageString(imageValue.getImageString());
                externalAnalysisResult.addImageValue(externalImageValue);
            }
        }

        if (analysisResult.getMicroValues() != null) {
            for (MicroOrganismResult microOrganismResult : analysisResult.getMicroValues()) {
                ExternalMicroOrganismResult externalExternalMicroOrganismResult = new ExternalMicroOrganismResult();
                externalExternalMicroOrganismResult.setOrganismLisId(microOrganismResult.getOrganismLisId());
                externalExternalMicroOrganismResult.setOrganismName(microOrganismResult.getOrganismName());
                externalExternalMicroOrganismResult.setOrganismConcetration(microOrganismResult.getOrganismConcetration());
                externalAnalysisResult.addMicroValue(externalExternalMicroOrganismResult);
            }
        }

        if (analysisResult.getMicroSensitivity() != null) {
            for (AntibioticSensitivity antibioticSensitivity : analysisResult.getMicroSensitivity()) {
                ExternalAntibioticSensitivity externalAntibioticSensitivity = new ExternalAntibioticSensitivity();
                externalAntibioticSensitivity.setAntibioticLisId(antibioticSensitivity.getAntibioticLisId());
                externalAntibioticSensitivity.setAntibioticName(antibioticSensitivity.getAntibioticName());
                externalAntibioticSensitivity.setAntibioticActivityValue(antibioticSensitivity.getAntibioticActivityValue());
                externalAntibioticSensitivity.setMic(antibioticSensitivity.getMic());
                externalAnalysisResult.addMicroSensitivity(externalAntibioticSensitivity);
            }
        }
        return externalAnalysisResult;
    }


    private static final Logger logger = Logger.getLogger(LaboratoryIntegration.class);

    private void logParameters(int orderId, String orderBarCode,
			List<AnalysisResult> results, String biomaterialDefects,
			int resultDoctorLisId) {
		try {
			logger.warn("setAnalysisResults call parameters: orderId - "
					+ orderId + "; orderBarCode - " + orderBarCode
					+ "; biomaterialDefects - " + biomaterialDefects
					+ "; resultDoctorLisId - " + resultDoctorLisId + ".");
			
			logger.warn("Analysis result list elements (list size - "
					+ results.size() + "):");
			for (AnalysisResult ar : results) {
                logger.warn("Analysis result: indicatorName - "
						+ ar.getIndicatorName() + "; indicatorCode - "
						+ ar.getIndicatorCode() + "; deviceName - "
						+ ar.getDeviceName() + "; valueType - "
						+ ar.getValueType() + "; resultValueText - "
						+ ar.getResultValueText() + "; resultNormString - "
						+ ar.getResultNormString()
						+ "; resultNormalityIndex - "
						+ ar.getResultNormalityIndex() + "; resultUnit - "
						+ ar.getResultUnit() + "; resultSignDate - "
						+ ar.getResultSignDate() + "; resultStatus - "
						+ ar.getResultStatus() + "; resultComment - "
						+ ar.getResultComment() + ".");
                
                List<ImageValue> imageValues = ar.getImageValues();
				if (imageValues != null && imageValues.size() > 0) {
					logger.warn("Image values list elements (list size - "
							+ imageValues.size() + "):");
                	for (ImageValue imageValue : imageValues) {
            			logger.warn("Image value: imageString - "
            					+ imageValue.getImageString() + ".");
					}
                }
				
				List<MicroOrganismResult> microValues = ar.getMicroValues();
				if (microValues != null && microValues.size() > 0) {
					logger.warn("Micro values list elements (list size - "
							+ microValues.size() + "):");
					for (MicroOrganismResult mor : microValues) {
						logger.warn("Micro organism result: organismLisId - "
								+ mor.getOrganismLisId() + "; organismName - "
								+ mor.getOrganismName()
								+ "; organismConcetration - "
								+ mor.getOrganismConcetration() + ".");
					}
				}
				
				List<AntibioticSensitivity> microSensitivity = ar.getMicroSensitivity();
				if (microSensitivity != null && microSensitivity.size() > 0) {
					logger.warn("Micro sensitivity list elements (list size - "
							+ microValues.size() + "):");
					for (AntibioticSensitivity as : microSensitivity) {
						logger.warn("Antibiotic Sensitivity: antibioticLisId - "
								+ as.getAntibioticLisId()
								+ "; antibioticName - "
								+ as.getAntibioticName()
								+ "; mic - "
								+ as.getMic()
								+ "; antibioticActivityValue - "
								+ as.getAntibioticActivityValue() + ".");
					}
				}
			}
		} catch(Exception e) {
			// do nothing
		}
	}
}