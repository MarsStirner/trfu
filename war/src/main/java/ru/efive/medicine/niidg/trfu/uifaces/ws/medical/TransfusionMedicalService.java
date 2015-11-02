package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DivisionDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.medical.MedicalOperationDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderPhenotype;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Division;
import ru.efive.medicine.niidg.trfu.data.entity.medical.*;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DictionaryManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.smsdelivery.MessageResponse;
import ru.smsdelivery.SMSWebService;
import ru.smsdelivery.SMSWebServiceLocator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.*;

@WebService(name = "transfusionMedicalService", targetNamespace = "http://www.korusconsulting.ru", serviceName = "transfusion-medical-service", portName = "transfusionMedicalService")
public class TransfusionMedicalService {

    @Inject
    @Named("propertiesHolder")
    private transient ApplicationPropertiesHolder propertiesHolder;
    @Inject
    @Named("dictionaryManagement")
    private transient DictionaryManagementBean dictionaryManagement;

    private static final Logger logger = LoggerFactory.getLogger("MIS");
    private static volatile int requestNumber= 0;

    private static synchronized int countRequest(){
       return requestNumber++;
    }


    @WebMethod(operationName = "orderBloodComponents", action = "urn:orderBloodComponents")
    public OrderResult orderBloodComponents(
            @WebParam(name = "patientCredentials") PatientCredentials patientCredentials,
            @WebParam(name = "orderInformation") OrderInformation orderInformation) {
        final int currentRequest = countRequest();
        logger.warn(String.format("#%d TransfusionMedicalService - Received new blood components order message", currentRequest));
        logParameters(patientCredentials, currentRequest);
        logParameters(orderInformation, currentRequest);
        final OrderResult result = new OrderResult();
        if (patientCredentials == null) {
            logger.warn("TransfusionMedicalService - failed to order blood components. No patient credentials");
            result.setResult(false);
            result.setDescription("Не указана информация о пациенте");
            return result;
        }
        if (orderInformation == null) {
            logger.warn("TransfusionMedicalService - failed to order blood components. No order information");
            result.setResult(false);
            result.setDescription("Не указана информация о требовании");
            return result;
        }
        if (orderInformation.getId() == null || orderInformation.getId() <= 0) {
            result.setResult(false);
            result.setDescription("Не указан уникальный идентификатор требования МИС");
            logger.warn("TransfusionMedicalService - failed to order blood components. No external id");
            return result;
        }
        try {
            final BloodComponentOrderRequestDAOImpl dao = (BloodComponentOrderRequestDAOImpl) ApplicationContextHelper.getApplicationContext()
                    .getBean(ApplicationHelper.COMPONENT_ORDER_DAO);

            final BloodComponentOrderRequest byExternalNumber = dao.findComponentOrderRequestByExternalNumber(orderInformation.getId().toString());
            logger.warn(
                    "By externalNumber \'" + orderInformation.getId() + "\' founded order[" +
                            (byExternalNumber == null ? 0 : byExternalNumber.getId()) +
                            "]"
            );
            if(byExternalNumber != null && byExternalNumber.getStatusId() >= 3) {
                result.setResult(false);
                result.setNumber(byExternalNumber.getNumber());
                result.setRequestId(byExternalNumber.getId());
                result.setDescription("Требование уже обработано и не может быть изменено");
                logger.warn("TransfusionMedicalService - failed to order blood components. Already processed!");
                return result;
            }
            final BloodComponentOrderRequest request = byExternalNumber != null ? byExternalNumber :  new BloodComponentOrderRequest();
            initializeBloodComponentOrderRequest(request);
            request.setDoseCount(orderInformation.getDoseCount());
            request.setExternalNumber(orderInformation.getId().toString());
            request.setIbNumber(orderInformation.getIbNumber());
            request.setIndication(orderInformation.getIndication());
            request.setNumber(orderInformation.getNumber());
            request.setRecipient(patientCredentials.getLastName());
            request.setRecipientBirth(patientCredentials.getBirth());
            request.setRecipientFirstName(patientCredentials.getFirstName());
            request.setRecipientMiddleName(patientCredentials.getMiddleName());
            request.setRecipientId(patientCredentials.getId());
            request.setAttendingDoctorFirstName(orderInformation.getAttendingPhysicianFirstName());
            request.setAttendingDoctorId(orderInformation.getAttendingPhysicianId());
            request.setAttendingDoctorLastName(orderInformation.getAttendingPhysicianLastName());
            request.setAttendingDoctorMiddleName(orderInformation.getAttendingPhysicianMiddleName());
            request.setPlanDate(orderInformation.getPlanDate());
            request.setFromMIS(true);
            final Date now = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
            request.setCreated(now);

            BloodGroup bg = null;
            if (patientCredentials.getBloodGroupId() != null) {
                bg = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).
                        findBloodGroupByNumber(patientCredentials.getBloodGroupId());
            }
            if (bg == null) {
                result.setResult(false);
                result.setDescription("Не найдена группа крови, соответствующая входным данным");
                logger.warn("TransfusionMedicalService - failed to order blood components. Blood group not found");
                return result;
            }
            request.setBloodGroup(bg);
            if (orderInformation.getBloodGroupId() != null) {
                //Тут -1 означает -2 ибо так надо (разные справочники в МИС)
                final BloodGroup bg_o = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).
                        findBloodGroupByNumber(orderInformation.getBloodGroupId() == -1 ? -2 : orderInformation.getBloodGroupId());
                if (bg_o != null) {
                    request.setOrderBloodGroup(bg_o);
                }
            }


            BloodComponentType bloodComponentType = null;
            if (orderInformation.getComponentTypeId() != null && orderInformation.getComponentTypeId() > 0) {
                bloodComponentType = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).get(BloodComponentType.class, orderInformation.getComponentTypeId());
            }
            if (bloodComponentType == null) {
                result.setResult(false);
                result.setDescription("Не найден тип компонента крови, соответствующий входным данным");
                logger.warn("TransfusionMedicalService - failed to order blood components. Component type not found");
                return result;
            }
            request.setComponentType(bloodComponentType);

            List<Classifier> rhList = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).findByCategory(Classifier.class, "Резус-фактор", false);
            String rhString;

            switch (patientCredentials.getRhesusFactorId()){
                case 1: rhString= "Отрицательный";
                    break;
                case 0: rhString = "Положительный";
                    break;
                case -1: rhString = "Не интерпретируется";
                    break;
                default: rhString ="";
            }

            if(orderInformation.getRhesusFactorId() != null) {
                String rh_o;
                switch (orderInformation.getRhesusFactorId()) {
                    case 1:
                        rh_o = "Отрицательный";
                        break;
                    case 0:
                        rh_o = "Положительный";
                        break;
                    case -1:
                        rh_o = "По индивидуальному подбору";
                        break;
                    default:
                        rh_o = "";
                }
                for (Classifier cand : rhList) {
                    if (cand.getValue().equals(rh_o)){
                        request.setOrderRhesusFactor(cand);
                        break;
                    }
                }
            }

            Classifier rh = null;
            for (Classifier cand : rhList) {
                if (cand.getValue().equals(rhString)){
                    rh = cand;
                    break;
                }
            }
            if (rh == null) {
                result.setResult(false);
                result.setDescription("Не найдено значение резус-фактора, соответствующее входным данным");
                logger.warn("TransfusionMedicalService - failed to order blood components. Rhesus factor not found");
                return result;
            }
            request.setRhesusFactor(rh);


            request.setDiagnosis(orderInformation.getDiagnosis());
            Division ans = null;
            if (orderInformation.getDivisionId() != null && orderInformation.getDivisionId() > 0) {
                ans = ((DivisionDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DIVISION_DAO)).findByExternalId(orderInformation.getDivisionId());
            }
            if (ans == null) {
                result.setResult(false);
                result.setDescription("Не найдено отделение, соответствующее входным данным");
                logger.warn("TransfusionMedicalService - failed to order blood components. Division not found");
                return result;
            }
            request.setDivision(ans.getName());

            if (orderInformation.getTransfusionType() == null || orderInformation.getTransfusionType() < 0 || orderInformation.getTransfusionType() > 1) {
                result.setResult(false);
                if (orderInformation.getTransfusionType() == null) {
                    result.setDescription("Не указан идентификатор типа трансфузии");
                } else {
                    result.setDescription("Указан некорректный идентификатор типа трансфузии");
                }
                logger.warn("TransfusionMedicalService - failed to order blood components. No transfusion type");
                return result;
            }
            request.setTransfusionType(orderInformation.getTransfusionType());
            request.setCount(orderInformation.getVolume());
            //Установка фенотипов
            if (patientCredentials.getBloodPhenotype() != null && !patientCredentials.getBloodPhenotype().isEmpty()) {
                //Получаем список фенотипов из классификатора "Иммуносерология"
                final List<AnalysisType> phenotypeAnalysisTypeList =
                        ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO))
                                .findAnalysisTypes("Иммуносерология", false);
                //Конвертируем полученые фенотипы во внутреннее представление
                for (BloodPhenotype currentBloodPhenotype : patientCredentials.getBloodPhenotype()) {
                    final String phenotype = currentBloodPhenotype.getPhenotype();
                    if (phenotype != null && !phenotype.isEmpty()) {
                        boolean phenotypeFounded = false;
                        //Поиск подходящего по коду типа фенотипа
                        for (AnalysisType currentAnalysisType : phenotypeAnalysisTypeList) {
                            if (phenotype.equals(currentAnalysisType.getValue())) {
                                phenotypeFounded = true;
                                request.addToPhenotypeList(new BloodComponentOrderPhenotype(request, currentAnalysisType, currentBloodPhenotype.getValue() ? "+" : "-"));
                                break;
                            }
                        }
                        if (!phenotypeFounded) {
                            logger.warn('\'' + phenotype + "\'["+Integer.toHexString(phenotype.codePointAt(0))+"] not Founded such fenotypeCode");
                        }
                    }
                }
            }
            //Установка Kell -антигена
            if (patientCredentials.getBloodKell() != null) {
                final List<Classifier> list = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO))
                        .findByValueAndCategory (Classifier.class, patientCredentials.getBloodKell() ? "Положительный" : "Отрицательный", "Kell - антиген");
                if (!list.isEmpty()) {
                    request.setKellAntigen(list.get(0));
                } else {
                    logger.warn("Not founded Kell-antigen \'" + patientCredentials.getBloodKell() + '\'');
                }
            } else {
                request.setKellAntigen(null);
            }

            dao.save(request);
            if (request.getId() != 0) {
                request.setNumber(StringUtils.leftPad(String.valueOf(request.getId()), 5, '0'));
                dao.save(request);
                result.setResult(true);
                result.setNumber(request.getNumber());
                result.setRequestId(request.getId());
                logger.warn("TransfusionMedicalService - blood components successfully ordered.");
                if (request.getTransfusionType() == 1) {
                    processSmsNotification(request);
                }
            } else {
                result.setResult(false);
                result.setDescription("Не удалось зарегистрировать заявку. Внутренняя ошибка");
                logger.warn("TransfusionMedicalService - failed to order blood components. Request not saved");
            }
        } catch (Exception e) {
            logger.error("TransfusionMedicalService - exception while processing blood components order", e);
            result.setResult(false);
            result.setDescription("Не удалось зарегистрировать заявку. Внутренняя ошибка");
        }
        return result;
    }

    private void logParameters(final PatientCredentials patientCredentials, final int request) {
        final StringBuilder sb = new StringBuilder("#");
        sb.append(request).append(" ").append(patientCredentials);
        logger.warn(sb.toString());
    }


    private void logParameters(final OrderInformation orderInformation, final int request) {
        final StringBuilder sb = new StringBuilder("#");
        sb.append(request).append(" ").append(orderInformation);
        logger.warn(sb.toString());
    }

    @WebMethod(operationName = "orderMedicalProcedure", action = "urn:orderMedicalProcedure")
    public OrderResult orderMedicalProcedure(
            @WebParam(name = "donorInfo") DonorInfo donorInfo,
            @WebParam(name = "patientCredentials") PatientCredentials patientCredentials,
            @WebParam(name = "procedureInfo") ProcedureInfo procedureInfo) {
        OrderResult result = new OrderResult();
        result.setOrderComment("");
        try {
            logger.warn("TransfusionMedicalService - Received new medical procedure order message");

            logParameters(donorInfo, patientCredentials, procedureInfo);

            MedicalOperationDAOImpl dao = (MedicalOperationDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.MEDICAL_DAO);
            if (patientCredentials == null) {
                result.setResult(false);
                result.setDescription("Не указана информация о пациенте");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. No patient credentials");
                return result;
            }
            if (patientCredentials.getId() == null || patientCredentials.getId() <= 0) {
                result.setResult(false);
                result.setDescription("Не указан уникальный идентификатор пациента");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. No patient id");
                return result;
            }
            if (procedureInfo == null) {
                result.setResult(false);
                result.setDescription("Не указана информация о процедуре");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. No procedure information");
                return result;
            }

            Operation operation = new Operation();
            initializeMedicalOperation(operation);

            BloodGroup bg = null;
            Classifier rh = null;
            if (patientCredentials.getBloodGroupId() != null && patientCredentials.getBloodGroupId() > 0) {
                bg = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).
                        findBloodGroupByNumber(patientCredentials.getBloodGroupId());
            }
            if (bg == null) {
                result.setResult(false);
                result.setDescription("Не найдена группа крови, соответствующая входным данным");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Blood group not found");
                return result;
            }

            List<Classifier> rhList = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).findByCategory(Classifier.class, "Резус-фактор", false);
            String rhString = patientCredentials.getRhesusFactorId() == 1 ? "Отрицательный" : "Положительный";
            for (Classifier cand : rhList) {
                if (cand.getValue().equals(rhString)) rh = cand;
            }
            if (rh == null) {
                result.setResult(false);
                result.setDescription("Не найдено значение резус-фактора, соответствующее входным данным");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Rhesus factor not found");
                return result;
            }

            BiomaterialDonor donor = null;
            if (donorInfo != null && donorInfo.getId() != null && donorInfo.getId() > 0) {
                donor = ((MedicalOperationDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.MEDICAL_DAO)).getDonorByExternalId(donorInfo.getId(), false);
                if (donor == null) {
                    donor = new BiomaterialDonor();
                    initializeBiomaterialDonor(donor, donorInfo.getId());
                }
                donor.setNumber(donorInfo.getNumber());
                donor.setFirstName(donorInfo.getFirstName());
                donor.setMiddleName(donorInfo.getMiddleName());
                donor.setLastName(donorInfo.getLastName());
                donor.setBloodGroup(bg);
                donor.setRhesusFactor(rh);
            } else {
                donor = dao.getDonorByExternalId(patientCredentials.getId(), false);
                if (donor == null) {
                    donor = new BiomaterialDonor();
                    initializeBiomaterialDonor(donor, patientCredentials.getId());
                }
                donor.setFirstName(patientCredentials.getFirstName());
                donor.setMiddleName(patientCredentials.getMiddleName());
                donor.setLastName(patientCredentials.getLastName());
                donor.setBloodGroup(bg);
                donor.setRhesusFactor(rh);
                donor.setBirth(patientCredentials.getBirth());
            }
            donor = dao.save(BiomaterialDonor.class, donor);
            if (donor != null) {
                if (StringUtils.isEmpty(donor.getNumber())) {
                    donor.setNumber(StringUtils.leftPad(String.valueOf(donor.getId()), 5, '0'));
                    donor = dao.save(BiomaterialDonor.class, donor);
                }
                logger.warn("TransfusionMedicalService - donor successfully updated.");
            } else {
                result.setResult(false);
                result.setDescription("Не удалось зарегистрировать лечебную процедуру. Внутренняя ошибка при обновлении донора");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Donor not saved");
            }

            operation.setUnp(Integer.toString(patientCredentials.getId()));
            operation.setExternalId(procedureInfo.getId());
            operation.setRecipientExternalId(patientCredentials.getId());
            operation.setDonor(donor);
            operation.setRecipient(patientCredentials.getFirstName());
            operation.setRecipientMiddleName(patientCredentials.getMiddleName());
            operation.setRecipientLastName(patientCredentials.getLastName());

            Classifier operationType = null;
            if (procedureInfo.getOperationType() != null && procedureInfo.getOperationType() > 0) {
                operationType = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).get(Classifier.class, procedureInfo.getOperationType());
            }
            if (operationType == null) {
                result.setResult(false);
                result.setDescription("Не найден тип лечебной процедуры, соответствующий входным данным");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Operation type not found");
                return result;
            }
            operation.setOperationForm(operationType);

            Division ans = null;
            if (procedureInfo.getDivisionId() != null && procedureInfo.getDivisionId() > 0) {
                ans = ((DivisionDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DIVISION_DAO)).findByExternalId(procedureInfo.getDivisionId());
            }
            if (ans == null) {
                result.setResult(false);
                result.setDescription("Не найдено отделение, соответствующее входным данным");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Division not found");
                return result;
            }
            operation.setDivision(ans);
            operation.setIbNumber(procedureInfo.getIbNumber());
            operation.setPlanDate(procedureInfo.getPlanDate());
            operation.setAppointmentDate(procedureInfo.getRegistrationDate());
            operation.setAttendingDoctorFirstName(procedureInfo.getAttendingPhysicianFirstName());
            operation.setAttendingDoctorId(procedureInfo.getAttendingPhysicianId());
            operation.setAttendingDoctorLastName(procedureInfo.getAttendingPhysicianLastName());
            operation.setAttendingDoctorMiddleName(procedureInfo.getAttendingPhysicianMiddleName());

            operation = dao.save(Operation.class, operation);
            if (operation.getId() != 0) {
                operation.setNumber(StringUtils.right("00000" + operation.getId(), 5));
                operation = dao.save(Operation.class, operation);
                result.setResult(true);
                result.setNumber(operation.getNumber());
                result.setRequestId(operation.getId());
                logger.warn("TransfusionMedicalService - medical procedure successfully registered.");
            } else {
                result.setResult(false);
                result.setDescription("Не удалось зарегистрировать лечебную процедуру. Внутренняя ошибка");
                logger.warn("TransfusionMedicalService - failed to register medical procedure. Procedure not saved");
            }
        } catch (Exception e) {
            logger.error("TransfusionMedicalService - exception while processing medical procedure order", e);
            result.setResult(false);
            result.setDescription("Не удалось зарегистрировать лечебную процедуру. Внутренняя ошибка");
        }
        return result;
    }

    @WebMethod(operationName = "getProcedureTypes", action = "urn:getProcedureTypes")
    public List<ProcedureType> getProcedureTypes() {
        List<ProcedureType> result = new ArrayList<ProcedureType>();
        try {
            List<Classifier> classifierList = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).findByCategory(Classifier.class, "Вид лечебной процедуры", false);
            for (Classifier classifier : classifierList) {
                ProcedureType procedureType = new ProcedureType();
                procedureType.setId(classifier.getId());
                procedureType.setValue(classifier.getValue());
                result.add(procedureType);
            }
        } catch (Exception e) {
            logger.error("TransfusionMedicalService - procedure types not sent", e);
        }
        return result;
    }

    @WebMethod(operationName = "getLaboratoryMeasureTypes", action = "urn:getLaboratoryMeasureTypes")
    public List<LaboratoryMeasureType> getLaboratoryMeasureTypes() {
        List<LaboratoryMeasureType> result = new ArrayList<LaboratoryMeasureType>();
        try {
            List<Classifier> classifierList = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext().getBean(ApplicationHelper.DICTIONARY_DAO)).findByCategory(Classifier.class, "Тип лабораторных измерений", false);
            for (Classifier classifier : classifierList) {
                LaboratoryMeasureType laboratoryMeasureType = new LaboratoryMeasureType();
                laboratoryMeasureType.setId(classifier.getId());
                laboratoryMeasureType.setValue(classifier.getValue());
                result.add(laboratoryMeasureType);
            }
        } catch (Exception e) {
            logger.error("TransfusionMedicalService - laboratory measure types not sent", e);
        }
        return result;
    }

    @WebMethod(operationName = "getComponentTypes", action = "urn:getComponentTypes")
    public List<ComponentType> getComponentTypes() {
        logger.warn("Called service getComponentTypes");
        List<ComponentType> result = new ArrayList<ComponentType>();
        try {
            List<BloodComponentType> bloodComponentTypes = ((DictionaryDAOImpl) ApplicationContextHelper.getApplicationContext()
                    .getBean(ApplicationHelper.DICTIONARY_DAO)).findBloodComponentTypes(
                    false, "code", true
            );
            for (BloodComponentType bloodComponentType : bloodComponentTypes) {
                ComponentType componentType = new ComponentType();
                componentType.setId(bloodComponentType.getId());
                componentType.setValue(bloodComponentType.getValue());
                componentType.setCode(bloodComponentType.getCode());
                result.add(componentType);
            }
        } catch (Exception e) {
            logger.error("TransfusionMedicalService - component types not sent", e);
        }
        logger.warn("Successfully end of service getComponentTypes. Return " + result.size() + " items");
        if(logger.isDebugEnabled()){
            if(!result.isEmpty()){
                logger.debug(" ID | CODE | VALUE ");
            }
            for(ComponentType bct : result) {
                logger.debug(bct.getId()+" | "+bct.getCode()+ " | "+bct.getValue());
            }
        }
        return result;
    }


    private void processSmsNotification(BloodComponentOrderRequest orderRequest) {
        try {
            logger.warn("Processing sms notification for blood component order");
            Boolean smsEnabled = (Boolean) propertiesHolder.getProperty("application", "notification.sms.enabled");

            if (smsEnabled) {
                String smsNumber = (String) propertiesHolder.getProperty("application", "notification.sms.number");
                String smsText = ((String) propertiesHolder.getProperty("application", "notification.sms.body")) + orderRequest.getId();
                String smsLogin = (String) propertiesHolder.getProperty("application", "smsdelivery.login");
                String smsPwd = (String) propertiesHolder.getProperty("application", "smsdelivery.password");

                SMSWebService service = new SMSWebServiceLocator();
                MessageResponse response = service.getSMSWebServiceSoap().sendMessage(smsLogin, smsPwd, false, 24, smsNumber, "", smsText);
                logger.warn("Blood component order request information sent to sms gate. Result: " + response.getResult());
            } else {
                logger.warn("Sms notification disabled for application");
            }
        } catch (RemoteException e) {
            logger.error("Failure while processing sms sending request", e);
        } catch (ServiceException e) {
            logger.error("Failure while processing sms sending request", e);
        }
    }

    private void initializeBloodComponentOrderRequest(BloodComponentOrderRequest request) {
        final Date now = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
        request.setCreated(now);
        request.setFromMIS(true);
        if(request.getId() == 0) {
            HistoryEntry historyEntry = new HistoryEntry();
            historyEntry.setCreated(now);
            historyEntry.setStartDate(now);
            historyEntry.setDocType(request.getType());
            historyEntry.setParentId(request.getId());
            historyEntry.setActionId(0);
            historyEntry.setFromStatusId(1);
            historyEntry.setEndDate(now);
            historyEntry.setProcessed(true);
            historyEntry.setCommentary("По требованию МИС");
            Set<HistoryEntry> history = new HashSet<HistoryEntry>();
            history.add(historyEntry);

            Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.add(Calendar.SECOND, 1);
            Date registered = calendar.getTime();
            historyEntry = new HistoryEntry();
            historyEntry.setCreated(registered);
            historyEntry.setStartDate(registered);
            historyEntry.setDocType(request.getType());
            historyEntry.setParentId(request.getId());
            historyEntry.setActionId(1);
            historyEntry.setFromStatusId(1);
            historyEntry.setToStatusId(2);
            historyEntry.setEndDate(registered);
            historyEntry.setProcessed(true);
            historyEntry.setCommentary("");
            history.add(historyEntry);

            request.setHistory(history);
        } else {
            HistoryEntry historyEntry = new HistoryEntry();
            historyEntry.setCreated(now);
            historyEntry.setStartDate(now);
            historyEntry.setDocType(request.getType());
            historyEntry.setParentId(request.getId());
            historyEntry.setActionId(0);
            historyEntry.setFromStatusId(1);
            historyEntry.setEndDate(now);
            historyEntry.setProcessed(true);
            historyEntry.setCommentary("Изменение из МИС");
            if(request.getHistory() == null){
                request.setHistory(new HashSet<HistoryEntry>(1));
            }
            request.getHistory().add(historyEntry);
        }

        request.setStatusId(2);
    }

    private void initializeMedicalOperation(Operation operation) {
        Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
        OperationReport report = new OperationReport();
        report.setCreated(created);
        report.setDeleted(false);
        operation.setStatusId(1);
        operation.setFromMIS(true);
        report.setHemodynamicsBefore(new Hemodynamics());
        report.setHemodynamicsAfter(new Hemodynamics());
        for (Classifier classifier : dictionaryManagement.getByCategory("Тип расходного материала")) {
            report.addSupply(new Supply(classifier));
        }
        report.setEritrocyteMass(new EritrocyteMass());
        report.setInitialParameters(new OperationParameters());
        report.setChangedParameters(new OperationParameters());
        for (Classifier classifier : dictionaryManagement.getByCategory("Тип лабораторных измерений")) {
            report.addLaboratoryMeasure(new LaboratoryMeasure(classifier));
        }
        report.setLiquidBalance(new LiquidBalance());

        operation.setOperationReport(report);
    }

    private void initializeBiomaterialDonor(BiomaterialDonor donor, int externalId) {
        donor.setExternalId(externalId);
        donor.setFromMIS(true);
        donor.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
        donor.setDeleted(false);
        donor.setStatusId(1);
    }

    private void logParameters(DonorInfo donorInfo, PatientCredentials patientCredentials, ProcedureInfo procedureInfo) {
        try {
            if (donorInfo != null) {
                logger.warn("Data of DonorInfo:" +
                        "\nFirstName: " + donorInfo.getFirstName() +
                        "\nLastName: " + donorInfo.getLastName() +
                        "\nMiddleName: " + donorInfo.getMiddleName() +
                        "\nNumber: " + donorInfo.getNumber() +
                        "\nId: " + donorInfo.getId());
            }
            if (patientCredentials != null) {
                logger.warn("Data of PatientCredentials:" +
                        "\nFirstName: " + patientCredentials.getFirstName() +
                        "\nLastName: " + patientCredentials.getLastName() +
                        "\nMiddleName: " + patientCredentials.getMiddleName() +
                        "\nBloodGroupId: " + patientCredentials.getBloodGroupId() +
                        "\nId: " + patientCredentials.getId() +
                        "\nRhesusFactorId: " + patientCredentials.getRhesusFactorId() +
                        "\nBirth: " + patientCredentials.getBirth());
            }
            if (procedureInfo != null) {
                logger.warn("Data of ProcedureInfo:" +
                        "\nAttendingPhysicianFirstName: " + procedureInfo.getAttendingPhysicianFirstName() +
                        "\nAttendingPhysicianLastName: " + procedureInfo.getAttendingPhysicianLastName() +
                        "\nAttendingPhysicianMiddleName: " + procedureInfo.getAttendingPhysicianMiddleName() +
                        "\nIbNumber: " + procedureInfo.getIbNumber() +
                        "\nAttendingPhysicianId: " + procedureInfo.getAttendingPhysicianId() +
                        "\nDivisionId: " + procedureInfo.getDivisionId() +
                        "\nId: " + procedureInfo.getId() +
                        "\nOperationType: " + procedureInfo.getOperationType() +
                        "\nPlanDate: " + procedureInfo.getPlanDate() +
                        "\nRegistrationDate: " + procedureInfo.getRegistrationDate());
            }
        } catch (Exception e) {
            // do nothing
        }

    }

}