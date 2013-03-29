package misexchange;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hl7.v3.*;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.wf.core.ActionResult;

import javax.faces.context.FacesContext;
import javax.xml.bind.*;
import java.io.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class PharmacyExchangeUtils {
    private ApplicationPropertiesHolder propertiesHolder;
    JAXBContext jaxbContext;
    private MISExchange misExchange;
    
    private static final Logger logger = Logger.getLogger(PharmacyExchangeUtils.class);

    public PharmacyExchangeUtils() throws JAXBException, java.net.MalformedURLException {
    	FacesContext context = FacesContext.getCurrentInstance();
    	if (context != null) {
    		propertiesHolder = (ApplicationPropertiesHolder) context.getApplication().evaluateExpressionGet(context, "#{propertiesHolder}", ApplicationPropertiesHolder.class);
    	}
    	if (propertiesHolder == null || (Boolean) propertiesHolder.getProperty("application","pharmacy.integration.enabled")) {
    		Object address = this.propertiesHolder.getProperty("application", "pharmacy.integration.address");
    		if (address == null || StringUtils.isEmpty(address.toString())) {
    			misExchange = new MISExchange();
    		}
    		else {
    			misExchange = new MISExchange(new java.net.URL(address.toString()));
    		}
    		jaxbContext = JAXBContext.newInstance(POCDMT000040ClinicalDocument.class);
        }
    }
    
    public PharmacyExchangeUtils(ApplicationPropertiesHolder propertiesHolder) throws JAXBException, java.net.MalformedURLException {
    	FacesContext context = FacesContext.getCurrentInstance();
    	this.propertiesHolder = propertiesHolder;
    	if (this.propertiesHolder == null || (Boolean) this.propertiesHolder.getProperty("application","pharmacy.integration.enabled")) {
    		Object address = this.propertiesHolder.getProperty("application", "pharmacy.integration.address");
    		if (address == null || StringUtils.isEmpty(address.toString())) {
    			misExchange = new MISExchange();
    		}
    		else {
    			misExchange = new MISExchange(new java.net.URL(address.toString()));
    		}
    		jaxbContext = JAXBContext.newInstance(POCDMT000040ClinicalDocument.class);
        }
    }

    public void downloadDrugList(String filePath) throws IOException, JAXBException {
        MISExchangePortType portType = misExchange.getMISExchangeSoap();
        List<POCDMT000040LabeledDrug> drugsList = portType.getDrugList().getDrug();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "cp1251");
        BufferedWriter bw = new BufferedWriter(out);
        //out.write("displayName, code");
        for (POCDMT000040LabeledDrug drug:drugsList){
            //out.write(drug.getCode().getDisplayName()+", "+drug.getCode().getCode()+"\n");
        	if (StringUtils.equals(drug.getCode().getCode(), "2450bf0b-8a08-11e1-a073-005056a41f97")) {
        		bw.write(marshalDocument(drug));
            	bw.newLine();
        	}
        }
        bw.close();
    }

    public void downloadStorageList(String filePath) throws IOException {
        MISExchangePortType portType = misExchange.getMISExchangeSoap();
        List<Storage> storageList = portType.getStorageList().getStorage();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath));
        out.write("displayName, code");
        for (Storage storage:storageList){
            out.write(storage.getRef()+" "+storage+"\n");
        }
    }

    public ActionResult checkDonationRequest(BloodDonationRequest donationRequest){
        ActionResult result = new ActionResult();
        if (donationRequest.getBloodSystems()==null||donationRequest.getBloodSystems().isEmpty()){
            result.setProcessed(false);
            result.setDescription("Пустой список систем крови");
            return result;
        }
        Map<BloodSystemType, Integer> systems= new HashMap<BloodSystemType, Integer>();
        for (BloodSystem system:donationRequest.getBloodSystems()){
            if (systems.containsKey(system.getType())) {
                systems.put(system.getType(), systems.get(system.getType()) + system.getCount());
            }else
                systems.put(system.getType(), system.getCount());
        }
        for (BloodSystemType systemType:systems.keySet()){
            if (!checkBloodSystemCount(systemType, systems.get(systemType))){
                result.setProcessed(false);
                result.setDescription("В наличие нет достаточного количества систем типа" + systemType.getValue());
                return result;
            }
        }

        result.setProcessed(true);
        return result;
    }

    public boolean checkBloodSystemCount(BloodSystemType systemType, Integer count) {
        if (!(Boolean) propertiesHolder.getProperty("application","pharmacy.integration.enabled")) {
        	logger.warn("1C integration disabled");
            return true;
        }
        MISExchangePortType portType = misExchange.getMISExchangeSoap();
        POCDMT000040LabeledDrug drug = createDrug(systemType);
        BalanceOfGoods balanceOfGoods = portType.balanceOfGoodsInStorage(drug, (String) propertiesHolder.getProperty("application","pharmacy.integration.fccho.uuid"), (String)propertiesHolder.getProperty("application","pharmacy.integration.storage.uuid"));

        return balanceOfGoods.getBalanceOfGood()!=null && Integer.parseInt(balanceOfGoods.getBalanceOfGood().get(0).getQty().getValue()) >= count;
    }



    public boolean decreaseBloodSystemsCount(BloodDonationRequest donationRequest) throws UnsupportedEncodingException, JAXBException {
    	if (!(Boolean) propertiesHolder.getProperty("application","pharmacy.integration.enabled")) {
    		logger.warn("1C integration disabled");
            return true;
        }
        MISExchangePortType portType = misExchange.getMISExchangeSoap();
        for (BloodSystem system:donationRequest.getBloodSystems()){
            POCDMT000040ClinicalDocument document = createDocument(donationRequest, createDrug(system.getType()), system.getCount(), system.getType().getUnit());
            RCMRIN000002UV02 message = createMessage(document);
            String s1 = marshalDocument(document);
            String s2 = marshalDocument(message.getMessage());
            //System.out.println(s1);
            //System.out.println(s2);
            MCCIIN000002UV01 result = portType.processHL7V3Message(message);
            logger.warn("Result of blood system write-off:");
            logger.warn(marshalDocument(result));
        }
        return true;
    }

    private RCMRIN000002UV02 createMessage(POCDMT000040ClinicalDocument document) throws JAXBException {
    	RCMRIN000002UV02 result = new RCMRIN000002UV02();
        org.hl7.v3.RCMRIN000002UV02 message = new org.hl7.v3.RCMRIN000002UV02();

        message.setId(createId(UUID.randomUUID().toString()));
        message.setITSVersion("XML_1.0");
        message.setCreationTime(createTS(new Date()));

        II intId = createId("2.16.840.1.113883.1.18");
        intId.setExtension("RCMR_IN000002UV02");
        message.setInteractionId(intId);

        CS procCode = new CS();
        procCode.setCode("P");
        message.setProcessingCode(procCode);

        CS procModeCode = new CS();
        procModeCode.setCode("T");
        message.setProcessingModeCode(procModeCode);

        CS acceptCode = new CS();
        acceptCode.setCode("AL");
        message.setAcceptAckCode(acceptCode);

        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
        II senId = new II();
        senId.setNullFlavor(NullFlavor.NI);
        device.getId().add(senId);
        sender.setDevice(device);
        message.setSender(sender);

        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        device = new MCCIMT000100UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
        II recId = new II();
        recId.setNullFlavor(NullFlavor.NI);
        device.getId().add(recId);
        receiver.setDevice(device);
        message.getReceiver().add(receiver);

        ED ed = new ED();
        ed.setRepresentation(BinaryDataEncoding.B_64);
        ed.setMediaType("multipart/related");
        
        ed.getContent().add("MIME-Version: 1.0\n");
        ed.getContent().add("Content-Type: multipart/related; boundary=\"HL7-CDA-boundary\"; type=\"text/xml\";\n");
        ed.getContent().add("Content-Transfer-Encoding: BASE64\n\n");
        ed.getContent().add("--HL7-CDA-boundary \n");
        ed.getContent().add("Content-Type: text/xml; charset=UTF-8\n\n");
        ed.getContent().add(DatatypeConverter.printBase64Binary(marshalDocument(document).getBytes()));
        ed.getContent().add("\n\n--HL7-CDA-boundary-- ");

        RCMRIN000002UV02MCAIMT700201UV01ControlActProcess actProcess = new RCMRIN000002UV02MCAIMT700201UV01ControlActProcess();
        actProcess.setText(ed);
        actProcess.setClassCode(ActClassControlAct.CACT);
        actProcess.setMoodCode(XActMoodIntentEvent.EVN);
        message.setControlActProcess(actProcess);
        
        result.setMessage(message);
        
        return result;
    }

    private String marshalDocument(Object document) throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(document, stringWriter);
        return stringWriter.toString();
    }
    
    private <T> T unmarshalXml(Class<T> clazz, String xml) throws JAXBException {
    	JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		return (T) jaxbUnmarshaller.unmarshal(is);
    }

    private POCDMT000040ClinicalDocument createDocument(BloodDonationRequest donationRequest, POCDMT000040LabeledDrug drug, Integer count, String unit) {
    	org.hl7.v3.ObjectFactory objectFactory = new org.hl7.v3.ObjectFactory();
        POCDMT000040ClinicalDocument document = new POCDMT000040ClinicalDocument();

        CS realm = new CS();
        realm.setCode("RU");
        document.getRealmCode().add(realm);

        POCDMT000040InfrastructureRootTypeId typeId = new POCDMT000040InfrastructureRootTypeId();
        typeId.setExtension("POCD_HD000040");
        typeId.setRoot("2.16.840.1.113883.1.3");
        document.setTypeId(typeId);

        document.setId(createId(UUID.randomUUID().toString()));

        CE code = new CE();
        code.setCodeSystem("2.16.840.1.113883.6.1");
        code.setCode("18610-6");
        code.setDisplayName("MEDICATION ADMINISTERED");
        code.setCodeSystemName("LOINC");
        document.setCode(code);

        document.setEffectiveTime(createTS(new Date()));

        CE confCode = new CE();
        confCode.setCode("N");
        confCode.setCodeSystem("2.16.840.1.113883.5.25");
        confCode.setDisplayName("Normal");
        document.setConfidentialityCode(confCode);

        CS lang = new CS();
        lang.setCode("ru-RU");
        document.setLanguageCode(lang);

        INT vnum = new INT();
        vnum.setValue(BigInteger.valueOf(1));
        document.setVersionNumber(vnum);

        POCDMT000040RecordTarget target = new POCDMT000040RecordTarget();
        POCDMT000040PatientRole role = new POCDMT000040PatientRole();
        II patienId = createId(donationRequest.getDonor().getUuid());
        String extensionNumber = StringUtils.isEmpty(donationRequest.getDonor().getExtensionNumber())? "2012/11782": donationRequest.getDonor().getExtensionNumber();
        patienId.setExtension(extensionNumber); // нужно указывать номер карты пациента
        role.getId().add(patienId);
        POCDMT000040Patient patient = new POCDMT000040Patient();
        PN patName = new PN();
        EnGiven enGiven = new EnGiven();
        enGiven.getContent().add(donationRequest.getDonor().getFirstName());
        patName.getContent().add(objectFactory.createENGiven(enGiven));
        enGiven = new EnGiven();
        enGiven.getContent().add(donationRequest.getDonor().getMiddleName());
        patName.getContent().add(objectFactory.createENGiven(enGiven));
        EnFamily enFamily = new EnFamily();
        enFamily.getContent().add(donationRequest.getDonor().getLastName());
        patName.getContent().add(objectFactory.createENFamily(enFamily));
        patient.getName().add(patName);
        patient.setBirthTime(createDateTS(donationRequest.getDonor().getBirth()));
        CE gender = new CE();
        gender.setCode(donationRequest.getDonor().getGender()==0?"F":"M");
        gender.setCodeSystem("2.16.840.1.113883.5.1");
        patient.setAdministrativeGenderCode(gender);
        role.setPatient(patient);
        target.setPatientRole(role);
        document.getRecordTarget().add(target);

        POCDMT000040Author author = new POCDMT000040Author();
        POCDMT000040AssignedAuthor assignedAuthor = new POCDMT000040AssignedAuthor();
        assignedAuthor.getId().add(createId(donationRequest.getStaffNurse().getUuid()));
        POCDMT000040Person person = new POCDMT000040Person();
        PN authName = new PN();
        EnGiven enGiven1 = new EnGiven();
        enGiven1.getContent().add(donationRequest.getStaffNurse().getFirstName());
        authName.getContent().add(objectFactory.createENGiven(enGiven1));
        enGiven1 = new EnGiven();
        enGiven1.getContent().add(donationRequest.getStaffNurse().getMiddleName());
        authName.getContent().add(objectFactory.createENGiven(enGiven1));
        EnFamily enFamily1 = new EnFamily();
        enFamily1.getContent().add(donationRequest.getStaffNurse().getLastName());
        authName.getContent().add(objectFactory.createENFamily(enFamily1));
        person.getName().add(authName);
        assignedAuthor.setAssignedPerson(person);
        author.setAssignedAuthor(assignedAuthor);
        author.setTime(createTS(donationRequest.getFactDate()));
        document.getAuthor().add(author);

        POCDMT000040Custodian custodian = new POCDMT000040Custodian();
        POCDMT000040AssignedCustodian assignedCustodian = new POCDMT000040AssignedCustodian();
        POCDMT000040CustodianOrganization custodianOrganization = new POCDMT000040CustodianOrganization();
        ON on = new ON();
        on.getContent().add("ФНКЦ ДГОИ");
        custodianOrganization.setName(on);
        custodianOrganization.getId().add(createId((String) propertiesHolder.getProperty("application","pharmacy.integration.fccho.uuid")));

        assignedCustodian.setRepresentedCustodianOrganization(custodianOrganization);
        custodian.setAssignedCustodian(assignedCustodian);
        document.setCustodian(custodian);

        POCDMT000040Component1 componentOf = new POCDMT000040Component1();
        POCDMT000040EncompassingEncounter encounter = new POCDMT000040EncompassingEncounter();
        IVLTS ts2 = new IVLTS();
        ts2.setNullFlavor(NullFlavor.NI);
        encounter.setEffectiveTime(ts2);
        II cardId = createId("1b264840-5555-4444-89fd-1f6b355dfa91");
        cardId.setExtension(extensionNumber);
        encounter.getId().add(cardId);
        //encounter.getId().add(createId(donationRequest.getUuid()));
        CE ce = new CE();
        ce.setCode("IMP");
        ce.setDisplayName("Inpatient encounter");
        ce.setCodeSystemName("actCode");
        ce.setCodeSystem("2.16.840.1.113883.5.4");
        encounter.setCode(ce);
        componentOf.setEncompassingEncounter(encounter);
        document.setComponentOf(componentOf);

        POCDMT000040Component2 component = new POCDMT000040Component2();
            POCDMT000040StructuredBody structuredBody = new POCDMT000040StructuredBody();
                POCDMT000040Component3 component3 = new POCDMT000040Component3();
                    POCDMT000040Section section = new POCDMT000040Section();
                        StrucDocText text = new StrucDocText();
                            StrucDocList list = new StrucDocList();
                                StrucDocItem item = new StrucDocItem();
                                item.getContent().add("Ispol'zovanie sistem krovi v donacii");
                            list.getItem().add(item);
                        text.getContent().add(objectFactory.createStrucDocTdList(list));
                    section.setText(text);
                        POCDMT000040Entry entry = new POCDMT000040Entry();
                            POCDMT000040SubstanceAdministration administration = new POCDMT000040SubstanceAdministration();
                            administration.setClassCode(ActClass.SBADM);
                            administration.setMoodCode(XDocumentSubstanceMood.RQO);
                            administration.getId().add(createId(donationRequest.getUuid()));
                                POCDMT000040Consumable consumable = new POCDMT000040Consumable();
                                    POCDMT000040ManufacturedProduct product = new POCDMT000040ManufacturedProduct();
                                    product.setManufacturedLabeledDrug(drug);
                                consumable.setManufacturedProduct(product);
                            administration.setConsumable(consumable);
                                SXCMTS sxcmts = new SXCMTS();
                                sxcmts.setValue(createTS(donationRequest.getFactDate()).getValue());
                            administration.getEffectiveTime().add(sxcmts);
                                IVLPQ ivlpq = new IVLPQ();
                                //ivlpq.setValue("1");
                                PQ center = new PQ();
                                center.setValue(Integer.toString(count));
                                center.setUnit(unit);
                                PQR translation = new PQR();
                                translation.setCodeSystemName("RLS");
                                ED value = new ED();
                                value.getContent().add(unit);
                                translation.setOriginalText(value);
                                center.getTranslation().add(translation);
                                ivlpq.setCenter(center);
                            administration.setDoseQuantity(ivlpq);
                                CE routeCode = new CE();
                                routeCode.setCode("PO");
                                routeCode.setCodeSystem("2.16.840.1.113883.5.112");
                                routeCode.setCodeSystemName("RouteOfAdministration");
                            administration.setRouteCode(routeCode);
                        entry.setSubstanceAdministration(administration);
                    section.getEntry().add(entry);
	                    entry = new POCDMT000040Entry();
		                    administration = new POCDMT000040SubstanceAdministration();
		                    administration.setClassCode(ActClass.SBADM);
		                    administration.setMoodCode(XDocumentSubstanceMood.EVN);
		                    administration.getId().add(createId(donationRequest.getUuid()));
		                        consumable = new POCDMT000040Consumable();
		                            product = new POCDMT000040ManufacturedProduct();
		                            product.setManufacturedLabeledDrug(drug);
		                        consumable.setManufacturedProduct(product);
		                    administration.setConsumable(consumable);
		                        sxcmts = new SXCMTS();
		                        sxcmts.setValue(createTS(donationRequest.getFactDate()).getValue());
		                    administration.getEffectiveTime().add(sxcmts);
		                        ivlpq = new IVLPQ();
		                        //ivlpq.setValue("1");
		                        center = new PQ();
                                center.setValue(Integer.toString(count));
                                center.setUnit(unit);
                                translation = new PQR();
                                translation.setCodeSystemName("RLS");
                                value = new ED();
                                value.getContent().add(unit);
                                translation.setOriginalText(value);
                                center.getTranslation().add(translation);
                                ivlpq.setCenter(center);
		                    administration.setDoseQuantity(ivlpq);
		                        routeCode = new CE();
		                        routeCode.setCode("PO");
		                        routeCode.setCodeSystem("2.16.840.1.113883.5.112");
		                        routeCode.setCodeSystemName("RouteOfAdministration");
		                    administration.setRouteCode(routeCode);
	                    entry.setSubstanceAdministration(administration);
	                section.getEntry().add(entry);
                component3.setSection(section);
            structuredBody.getComponent().add(component3);
        component.setStructuredBody(structuredBody);
        document.setComponent(component);

        return document;
    }

    private II createId(String root){
        II ii2 = new II();
        ii2.setRoot(root);
        return ii2;
    }

    private TS createTS(Date date){
        TS ts = new TS();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ts.setValue(formatter.format(date));
        return ts;
    }

    private TS createDateTS(Date date){
        TS ts = new TS();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        ts.setValue(formatter.format(date));
        return ts;
    }

    private POCDMT000040LabeledDrug createDrug(BloodSystemType systemType) {
        POCDMT000040LabeledDrug drug = new POCDMT000040LabeledDrug();
        CE code = new CE();
        code.setCode(systemType.getCode());
        code.setDisplayName(systemType.getValue());
        code.setCodeSystem("1.2.643.2.0");
        drug.setCode(code);
        return drug;
    }

    public void setPropertiesHolder(ApplicationPropertiesHolder propertiesHolder) {
        this.propertiesHolder = propertiesHolder;
    }
}
