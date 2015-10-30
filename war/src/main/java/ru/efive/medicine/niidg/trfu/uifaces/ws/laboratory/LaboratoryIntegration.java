package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.AnalysisExternalValue;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.integration.*;
import ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.appointment.AnalysisValueConverterFactory;
import ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory.util.immuno.ImmunoAnalysisValueConverterFactory;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.*;

@WebService(name = "trfu-laboratory-integration", targetNamespace = "http://www.korusconsulting.ru", serviceName =
        "trfu-laboratory-integration", portName = "trfu-laboratory-integration")
public class LaboratoryIntegration {
    private static final String SUCCESSFUL_RESULT = "0";
    private static final String BARCODE_NOT_SET = "Не указан штрихкод";
    private static final String EXTERNAL_APPOINTMENT_NOT_FOUND = "Не найдено направление на исследование для " +
            "биоматериала %d со штрихкодом %s";


    //Код индикатора группы крови из ЛИС
    private static final String BLOOD_GROUP_INDICATOR_CODE = "Ф0210";
    //Код индикатора резус-фактора из ЛИС
    private static final String RHESUS_FACTOR_INDICATOR_CODE = "Ф0215";
    private static final String RHESUS_FACTOR_CATEGORY = "Резус-фактор";

    @WebMethod(operationName = "setAnalysisResults", action = "urn:setAnalysisResults")
    public String setAnalysisResults(@WebParam(name = "orderId") int orderId, @WebParam(name = "orderBarCode") String
            orderBarCode, @WebParam(name = "results") List<AnalysisResult> results, @WebParam(name =
            "biomaterialDefects") String biomaterialDefects, @WebParam(name = "resultDoctorLisId") int
            resultDoctorLisId) throws Exception {

        logParameters(orderId, orderBarCode, results, biomaterialDefects, resultDoctorLisId);

        String result;
        try {
            logger.warn("Received laboratory integration message: orderId=" + orderId + ", orderBarCode=" +
                    orderBarCode);
            if (StringUtils.isEmpty(orderBarCode)) {
                result = BARCODE_NOT_SET;
                logger.error(result);
                throw new Exception(result);
            }
            if (orderId == 0) {
                logger.error("Не указан идетифкатор направления на исследования");
                throw new Exception("Не указан идетифкатор направления на исследования");
            }

            final FileSystemXmlApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();

            final ExternalAppointmentDaoImpl dao = (ExternalAppointmentDaoImpl) applicationContext.getBean
                    (ApplicationHelper.EXTERNAL_APPOINTMENT_DAO);
            ExternalAppointment appointment = dao.get(orderId);
            if (appointment == null) {
                result = String.format(EXTERNAL_APPOINTMENT_NOT_FOUND, orderId, orderBarCode);
                logger.error(result);
                throw new Exception(result);
            }

            List<Analysis> analysisList = new ArrayList<>(appointment.getTests());

            BloodDonationRequest donationRequest = ((BloodDonationRequestDAOImpl) applicationContext.getBean
                    (ApplicationHelper.DONATION_DAO)).findDocumentByBarCode(orderBarCode);
            if (donationRequest != null) {
                updateDonationRequest(donationRequest, results);
                ((BloodDonationRequestDAOImpl) applicationContext.getBean(ApplicationHelper.DONATION_DAO)).save
                        (donationRequest);

                //TICKET TRFU-22/41
                if (donationRequest.getDonor() != null) {
                    updateDonor(donationRequest.getDonor(), results);
                }
            }
            updateAnalysisList(analysisList, results);

            ExternalAnalysisEntry entry = createExternalAnalysisEntry(results, biomaterialDefects, resultDoctorLisId);
            appointment.addHistoryEntry(entry);

            dao.save(appointment);

            result = SUCCESSFUL_RESULT;
            logger.warn("Result of the laboratory integration: " + result);
        } catch (Exception e) {
            result = e.getMessage();
            logger.error("setAnalysisResult exception", e);
        }
        return result;
    }

    private void updateDonor(Donor donor, List<AnalysisResult> results) {
        //Флаг, показывающий, необходимо ли в БД обновлять запись донора (обновление необходимо лишь в том случае
        // когда не совпадают параметры)
        boolean needFlushToDatabase = false;
        final int donorId = donor.getId();
        try {
            for (AnalysisResult analysisResult : results) {
                if (BLOOD_GROUP_INDICATOR_CODE.equals(analysisResult.getIndicatorCode()) && StringUtils.isNotEmpty
                        (analysisResult.getResultValueText())) {
                    //Установить группу крови
                    if (donor.getBloodGroup() == null) {
                        //У донора не высталена группа крови
                        final BloodGroup param = getBloodGroupFromString(analysisResult.getResultValueText());
                        if (param != null) {
                            donor.setBloodGroup(param);
                            needFlushToDatabase = true;
                            logger.info(String.format("Donor[%d] set bloodGroup[%s] from LIS", donorId, param
                                    .getValue()));
                        }
                    } else {
                        //У донора есть группа крови и она не совпадает с результатом из ЛИС
                        if (!donor.getBloodGroup().getValue().equals(analysisResult.getResultValueText())) {
                            final BloodGroup param = getBloodGroupFromString(analysisResult.getResultValueText());
                            if (param != null) {
                                donor.setBloodGroup(param);
                                needFlushToDatabase = true;
                                logger.info(String.format("Donor[%d] had another bloodGroup[%s]. Set bloodGroup[%s] " +
                                        "from LIS", donorId, donor.getBloodGroup().getValue(), param.getValue()));
                            }
                        }
                    }
                } else if (RHESUS_FACTOR_INDICATOR_CODE.equals(analysisResult.getIndicatorCode()) && StringUtils
                        .isNotEmpty(analysisResult.getResultValueText())) {
                    //Установить резус-фатор
                    if (donor.getRhesusFactor() == null) {
                        //У донора не выстален резус-фатор
                        final Classifier param = getRhesusFactorFromString(analysisResult.getResultValueText());
                        if (param != null) {
                            donor.setRhesusFactor(param);
                            needFlushToDatabase = true;
                            logger.info(String.format("Donor[%d] set rhesusFactor[%s] from LIS", donorId, param
                                    .getValue()));
                        }
                    } else {
                        //У донора есть резус-фактор и он не совпадает с результатом из ЛИС
                        if (!donor.getRhesusFactor().getValue().equals(analysisResult.getResultValueText())) {
                            final Classifier param = getRhesusFactorFromString(analysisResult.getResultValueText());
                            if (param != null) {
                                donor.setRhesusFactor(param);
                                needFlushToDatabase = true;
                                logger.info(String.format("Donor[%d] had another rhesusFactor[%s]. Set " +
                                        "rhesusFactor[%s] from LIS", donorId, donor.getBloodGroup().getValue(), param
                                        .getValue()));
                            }
                        }
                    }
                }
            }
            if (needFlushToDatabase) {
                ((DonorDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DONOR_DAO)
                ).save(donor);
                logger.info(String.format("Donor[%d] upadated", donorId));
            }
        } catch (Exception e) {
            //При ошибке обновления донора - работать дальше
            logger.error("updateDonor exception", e);
        }
    }

    private Classifier getRhesusFactorFromString(String valueText) {
        final List<Classifier> rhesusFactors = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext()
                .getBean(ApplicationHelper.DICTIONARY_DAO)).findByValueAndCategory(Classifier.class, valueText,
                RHESUS_FACTOR_CATEGORY);
        final Iterator<Classifier> iterator = rhesusFactors.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            logger.error(String.format("Cannot find rhesusFactor by value \'%s\'", valueText));
            return null;
        }
    }

    private BloodGroup getBloodGroupFromString(String valueText) {
        final List<BloodGroup> bloodGroups = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext()
                .getBean(ApplicationHelper.DICTIONARY_DAO)).findByValue(BloodGroup.class, valueText);
        final Iterator<BloodGroup> iterator = bloodGroups.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            logger.error(String.format("Cannot find bloodGroup by value \'%s\'", valueText));
            return null;
        }
    }

    private void updateDonationRequest(BloodDonationRequest donationRequest, List<AnalysisResult> results) {
        for (Analysis analysis : donationRequest.getTestsImmuno()) {
            for (ExternalIndicator indicator : analysis.getType().getIndicators()) {
                for (AnalysisResult analysisResult : results) {
                    if (indicator.getCode().equals(analysisResult.getIndicatorCode())) {
                        String antigen = analysis.getType().getValue();
                        String resultValue = ImmunoAnalysisValueConverterFactory.getConverter(indicator).convert
                                (analysisResult.getResultValueText(), antigen);
                        if (resultValue != null) {
                            analysis.setValue(resultValue);
                            ((AnalysisDAOImpl) ApplicationContextHelper.getApplicationContext().getBean
                                    (ApplicationHelper.ANALYSIS_DAO)).save(analysis);
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
                        externalValue.setValue(AnalysisValueConverterFactory.getConverter(externalIndicator).convert
                                (analysisResult.getResultValueText()));
                        externalValue.setUnit(analysisResult.getResultUnit());
                        externalValue.setNormValue(analysisResult.getResultNormString());
                        logger.warn("Analysis result: " + externalValue.getValue() + "; unit: " + externalValue
                                .getUnit() + "; norm: " + externalValue.getNormValue());
                        analysis.addExternalValue(externalValue);
                    }
                }
            }
        }

        for (Analysis analysis : analysisList) {
            if (analysis.getExternalValues() != null) {
                for (AnalysisExternalValue externalValue : analysis.getExternalValues()) {
                    if (analysis.getExternalValues().size() > 1) {
                        analysis.setValueType(2); //множественные внешние значения
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

    private ExternalAnalysisEntry createExternalAnalysisEntry(List<AnalysisResult> results, String
            biomaterialDefects, int resultDoctorLisId) {
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
                externalExternalMicroOrganismResult.setOrganismConcetration(microOrganismResult
                        .getOrganismConcetration());
                externalAnalysisResult.addMicroValue(externalExternalMicroOrganismResult);
            }
        }

        if (analysisResult.getMicroSensitivity() != null) {
            for (AntibioticSensitivity antibioticSensitivity : analysisResult.getMicroSensitivity()) {
                ExternalAntibioticSensitivity externalAntibioticSensitivity = new ExternalAntibioticSensitivity();
                externalAntibioticSensitivity.setAntibioticLisId(antibioticSensitivity.getAntibioticLisId());
                externalAntibioticSensitivity.setAntibioticName(antibioticSensitivity.getAntibioticName());
                externalAntibioticSensitivity.setAntibioticActivityValue(antibioticSensitivity
                        .getAntibioticActivityValue());
                externalAntibioticSensitivity.setMic(antibioticSensitivity.getMic());
                externalAnalysisResult.addMicroSensitivity(externalAntibioticSensitivity);
            }
        }
        return externalAnalysisResult;
    }


    private static final Logger logger = LoggerFactory.getLogger(LaboratoryIntegration.class);

    private void logParameters(int orderId, String orderBarCode, List<AnalysisResult> results, String
            biomaterialDefects, int resultDoctorLisId) {
        try {
            logger.warn("setAnalysisResults call parameters: orderId - " + orderId + "; orderBarCode - " +
                    orderBarCode + "; biomaterialDefects - " + biomaterialDefects + "; resultDoctorLisId - " +
                    resultDoctorLisId + ".");

            logger.warn("Analysis result list elements (list size - " + results.size() + "):");
            for (AnalysisResult ar : results) {
                logger.warn("Analysis result: indicatorName - " + ar.getIndicatorName() + "; indicatorCode - " + ar
                        .getIndicatorCode() + "; deviceName - " + ar.getDeviceName() + "; valueType - " + ar
                        .getValueType() + "; resultValueText - " + ar.getResultValueText() + "; resultNormString - "
                        + ar.getResultNormString() + "; resultNormalityIndex - " + ar.getResultNormalityIndex() + "; " +
                        "resultUnit - " + ar.getResultUnit() + "; resultSignDate - " + ar.getResultSignDate() + "; " +
                        "resultStatus - " + ar.getResultStatus() + "; resultComment - " + ar.getResultComment() + ".");

                List<ImageValue> imageValues = ar.getImageValues();
                if (imageValues != null && imageValues.size() > 0) {
                    logger.warn("Image values list elements (list size - " + imageValues.size() + "):");
                    for (ImageValue imageValue : imageValues) {
                        logger.warn("Image value: imageString - " + imageValue.getImageString() + ".");
                    }
                }

                List<MicroOrganismResult> microValues = ar.getMicroValues();
                if (microValues != null && microValues.size() > 0) {
                    logger.warn("Micro values list elements (list size - " + microValues.size() + "):");
                    for (MicroOrganismResult mor : microValues) {
                        logger.warn("Micro organism result: organismLisId - " + mor.getOrganismLisId() + "; " +
                                "organismName - " + mor.getOrganismName() + "; organismConcetration - " + mor
                                .getOrganismConcetration() + ".");
                    }
                }

                List<AntibioticSensitivity> microSensitivity = ar.getMicroSensitivity();
                if (microSensitivity != null && microSensitivity.size() > 0) {
                    logger.warn("Micro sensitivity list elements (list size - " + microSensitivity.size() + "):");
                    for (AntibioticSensitivity as : microSensitivity) {
                        logger.warn("Antibiotic Sensitivity: antibioticLisId - " + as.getAntibioticLisId() + "; " +
                                "antibioticName - " + as.getAntibioticName() + "; mic - " + as.getMic() + "; " +
                                "antibioticActivityValue - " + as.getAntibioticActivityValue() + ".");
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}