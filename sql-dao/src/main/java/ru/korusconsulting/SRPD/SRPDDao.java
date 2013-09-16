package ru.korusconsulting.SRPD;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.AD;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAIN101305UV02;
import org.hl7.v3.PRPAIN101307UV02;
import org.hl7.v3.PRPAIN101307UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN101308UV02;
import org.hl7.v3.PRPAIN101308UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN101311UV02;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01ControlActProcess;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01Subject1;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01Subject2;
import org.hl7.v3.PRPAIN101312UV02;
import org.hl7.v3.PRPAMT101301UV02IdentifiedPerson;
import org.hl7.v3.PRPAMT101301UV02OtherIDs;
import org.hl7.v3.PRPAMT101301UV02Person;
import org.hl7.v3.PRPAMT101303UV02OtherIDs;
import org.hl7.v3.PRPAMT101307UV02IdentifiedPersonIdentifier;
import org.hl7.v3.PRPAMT101307UV02ParameterList;
import org.hl7.v3.PRPAMT101307UV02QueryByParameter;
import org.hl7.v3.PostalAddressUse;
import org.hl7.v3.TEL;
import org.hl7.v3.TS;
import org.hl7.v3.TelecommunicationAddressUse;

import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.pdmanager.PDManager;
import ru.korusconsulting.pdmanager.TmisPdm;


public class SRPDDao {
	private static final String HOME_PHONE = "tel:";
	private static final String MOBILE_PHONE = "cell-tel:";
	private static final String WORKING_PHONE = "working-office-tel:";
	private static final String EMAIL = "mailto:";
	
	private static final String ROOT_PASSPORT_NUMBER = "3.0.0.2";
	private static final String ROOT_INSURANCE_NUMBER = "3.0.0.5";
	private static final String DATE_FORMAT_FOR_SRPD = "yyyyMMdd";
	
	private static final String SRPD_ADRESS_PROPERTIE = "srpdAdress";
	
	private PDManager initPDManager() {
		TmisPdm.initWsdl(DonorHelper.createPropertiesForURL("storage_utility.properties").get(SRPD_ADRESS_PROPERTIE).toString());
		TmisPdm service = new TmisPdm();
		return service.getPortPdm();
	}
	/* Search possible information for donors with current id */
	public  List<Map<FieldsInMap, Object>> get(Map<DonorHelper.FieldsInMap, Object> params) {
		PDManager pdm = initPDManager();
		List<Map<FieldsInMap, Object>> result = new ArrayList<Map<FieldsInMap,Object>>();
		ObjectFactory factory = new ObjectFactory();
		PRPAIN101307UV02 prm = factory.createPRPAIN101307UV02();
        final PRPAIN101307UV02QUQIMT021001UV01ControlActProcess controlActProcess = factory.createPRPAIN101307UV02QUQIMT021001UV01ControlActProcess();
        prm.setControlActProcess(controlActProcess);
        final PRPAMT101307UV02QueryByParameter query = factory.createPRPAMT101307UV02QueryByParameter();
        controlActProcess.setQueryByParameter(query);
        final PRPAMT101307UV02ParameterList prmList = factory.createPRPAMT101307UV02ParameterList();
        query.setParameterList(prmList);
        // ---------- for search List of personal data from list of SRPD ids
        List<String> ids = (List<String>)params.get(FieldsInMap.LIST_STORAGE_IDS);
        for (Object i : ids) {
        	final PRPAMT101307UV02IdentifiedPersonIdentifier person = factory.createPRPAMT101307UV02IdentifiedPersonIdentifier();
        	prmList.getIdentifiedPersonIdentifier().add(person);
        	final II ii = factory.createII();
        	person.getValue().add(ii);        
        	ii.setRoot(i.toString());
        }
        PRPAIN101308UV02 res = pdm.getDemographics(prm);
        for (PRPAIN101308UV02MFMIMT700711UV01Subject1 i: res.getControlActProcess().getSubject()) {
        	result.add(parseAnswerForOnePerson(i));
        }
        return result;
        
	}

	/* search list of possible donors, which equals parameters for search*/
	/*public Map<Integer,Map<DonorHelper.FieldsInMap, Object>> getDonors(Map<DonorHelper.FieldsInMap, Object> parametersForSearch) {
		PDManager pdm = initPDManager();
		/* full list, which contain:
		 * key - id from SRPD
		 * value - map of PD from SRPD
		 */
		/*Map<Integer,Map<DonorHelper.FieldsInMap, Object>> fullListWithDonorsInformation = null;
		fullListWithDonorsInformation = null;//new DonorHelper().makeMapsFromAnswerAfterSearch(pdm.findCandidates(createFindToSRPD(parametersForSearch)));
		return fullListWithDonorsInformation;
		
	}*/
	
	/* Send information about current donor and wait for id from SRPD */
	public Map<FieldsInMap, Object> addPDToSRPD(Map<FieldsInMap, Object> information) {
		String firstName = null;
		String lastName = null;
		String middleName = null;
		String omcNumber = null;
		String passportNumber = null;
		String phone = null;
		String adress = null;
		String workPhone = null;
		String email = null;
		String employment = null;
		Date birth = null;
		Integer gender = null;
		if (information.get(FieldsInMap.LAST_NAME) != null) {
			lastName = information.get(FieldsInMap.LAST_NAME).toString();
		}
		if (information.get(FieldsInMap.FIRST_NAME) != null) {
			firstName = information.get(FieldsInMap.FIRST_NAME).toString();
		}
		if (information.get(FieldsInMap.MIDDLE_NAME) != null) {
			middleName = information.get(FieldsInMap.MIDDLE_NAME).toString();
		}
		if (information.get(FieldsInMap.OMC_NUMBER) != null) {
			omcNumber = information.get(FieldsInMap.OMC_NUMBER).toString();
		}
		if (information.get(FieldsInMap.PASSPORT_NUMBER) != null) {
	 	   passportNumber = information.get(FieldsInMap.PASSPORT_NUMBER).toString();
		}
		if (information.get(FieldsInMap.PHONE) != null) {
			phone = information.get(FieldsInMap.PHONE).toString();
		}
		if (information.get(FieldsInMap.ADRESS) != null) {
			adress = information.get(FieldsInMap.ADRESS).toString();
		}
		if (information.get(FieldsInMap.WORK_PHONE) != null) {
	 		workPhone = information.get(FieldsInMap.WORK_PHONE).toString();
		}
		if (information.get(FieldsInMap.EMAIL) != null) {
			email = information.get(FieldsInMap.EMAIL).toString();
		}
		if (information.get(FieldsInMap.EMPLOYMENT) != null) {
			employment = information.get(FieldsInMap.EMPLOYMENT).toString();
		}
		if (information.get(FieldsInMap.BIRTH) != null) {
			birth = (Date)information.get(FieldsInMap.BIRTH);
		}
		if (information.get(FieldsInMap.GENDER) != null) {
			gender = (Integer)information.get(FieldsInMap.GENDER);
		}
		String result = addPDToSRPDData(lastName, firstName, middleName, omcNumber, passportNumber, phone, adress, workPhone, email, employment, birth, gender);
		information.put(FieldsInMap.TEMP_STORAGE_ID, result);
		return information;
	}
	/*public List<Map<FieldsInMap, Object>> addPDToSRPD(List<Map<FieldsInMap, Object>> information) {
		List<Map<FieldsInMap, Object>> result = new ArrayList<Map<FieldsInMap,Object>>();
		for (Map<FieldsInMap, Object> i : information) {
			result.add(addPDToSRPD(i));
		}
		return result;
	}*/
	/* Creation parameters for findCandidates-method of Web-service */
	private PRPAIN101305UV02 createFindToSRPD(Map<DonorHelper.FieldsInMap, Object> fieldsForSearch) {
		PRPAIN101305UV02 parameters = new PRPAIN101305UV02();
		return parameters;
	}
	
	public String addPDToSRPDData(String family, String givven, String suffix, 
			   				  String numberOMC, String numberPassport, String homePhone,
			   				  String address, String workPhone, String email, 
			   				  String employmentId, Date birthDate, Integer genderId) {
		PDManager pdm = initPDManager();
		PRPAIN101311UV02 prm = new PRPAIN101311UV02();
        PRPAIN101311UV02MFMIMT700721UV01ControlActProcess controlActProcess = new PRPAIN101311UV02MFMIMT700721UV01ControlActProcess();
        PRPAIN101311UV02MFMIMT700721UV01Subject1 subject1 = new PRPAIN101311UV02MFMIMT700721UV01Subject1();
        PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest registrationRequest = new PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest();
        PRPAMT101301UV02IdentifiedPerson identifiedPerson = new PRPAMT101301UV02IdentifiedPerson();
        PRPAIN101311UV02MFMIMT700721UV01Subject2 subject2 = new PRPAIN101311UV02MFMIMT700721UV01Subject2();
        PRPAMT101301UV02Person person = new PRPAMT101301UV02Person();

        prm.setControlActProcess(controlActProcess);
        controlActProcess.setSubject(subject1);
        subject1.setRegistrationRequest(registrationRequest);
        registrationRequest.setSubject1(subject2);
        subject2.setIdentifiedPerson(identifiedPerson);
        identifiedPerson.setIdentifiedPerson(person);

        ObjectFactory factory = new ObjectFactory();
        PN name = factory.createPN();
        
        EnGiven giv = factory.createEnGiven();
        giv.getContent().add(givven);
        name.getContent().add(factory.createENGiven(giv));

        EnGiven mn = factory.createEnGiven();
        mn.getContent().add(suffix);
        name.getContent().add(factory.createENGiven(mn));
        
        EnFamily fam = factory.createEnFamily();
        fam.getContent().add(family);
        name.getContent().add(factory.createENFamily(fam));
        
        person.getName().add(name);
        List<AD> adreses = person.getAddr();
        List<TEL> tels = person.getTelecom();
        final AD adr = new AD();
        adr.getUse().add(PostalAddressUse.H);
        AdxpStreetAddressLine strAdrLine = factory.createAdxpStreetAddressLine();
        strAdrLine.getContent().add(address);
        adr.getContent().add(factory.createADStreetAddressLine(strAdrLine));
        adreses.add(adr);
        final AD adrEmp = new AD();
        adrEmp.getUse().add(PostalAddressUse.WP);
        strAdrLine = factory.createAdxpStreetAddressLine();
        strAdrLine.getContent().add(employmentId);
        adrEmp.getContent().add(factory.createADStreetAddressLine(strAdrLine));
        adreses.add(adr);
        person.getAsOtherIDs().add(new PRPAMT101301UV02OtherIDs() );
        person.getAsOtherIDs().get(0).getId().add(new II());
        person.getAsOtherIDs().get(0).getId().get(0).setRoot(ROOT_PASSPORT_NUMBER);
        person.getAsOtherIDs().get(0).getId().get(0).setExtension(numberPassport);
        person.getAsOtherIDs().get(0).getId().add(new II());
        person.getAsOtherIDs().get(0).getId().get(1).setRoot(ROOT_INSURANCE_NUMBER);
        person.getAsOtherIDs().get(0).getId().get(1).setExtension(numberOMC);
        
        if (StringUtils.isNotEmpty(homePhone)) {
        	tels.add(createTEL(factory, homePhone, TelecommunicationAddressUse.HP));
        }
        if (StringUtils.isNotEmpty(workPhone)) {
        	tels.add(createTEL(factory, workPhone, TelecommunicationAddressUse.WP));
        }
        if (StringUtils.isNotEmpty(email)) {
        	tels.add(createTEL(factory, email, null));
        }
        
        TS birthTime = createTS(factory, birthDate);
        person.setBirthTime(birthTime);

        CE ce = createGender(null, genderId);
        person.setAdministrativeGenderCode(ce);

        PRPAIN101312UV02 res = pdm.add(prm);
        String root = res.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getIdentifiedPerson().
                getIdentifiedPerson().getId().get(0).getExtension();
		return root;
	}
	/* ---------------------create TEL------------------------------ */
	private TEL createTEL(ObjectFactory factory, String value, TelecommunicationAddressUse type) {
		if (StringUtils.isNotEmpty(value)) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			TEL tel = factory.createTEL();
			if (!"".equals(value) && value != null) {
				if (type != null) {
					switch (type) {
					case HP:
						value = HOME_PHONE + value;	
						break;
					case MC:
						value = MOBILE_PHONE + value;	
						break;
					case WP:
						value = WORKING_PHONE + value;	
						break;
					}
					tel.getUse().add(type);
				} else {
					value = EMAIL + value;
				}
				tel.setValue(value);
			}
			return tel;
		}
		return null;
	}
	/* -------------------- creation Gender ----------------------- */
	private CE createGender(ObjectFactory factory, Integer genderId) {
		if (genderId != null) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			CE gender = factory.createCE();
			if (genderId == 0) {
				gender.setCode("F");
				gender.setCodeSystem("2.16.840.1.113883.5.1");
			} else if(genderId == 1) {
				gender.setCode("M");
				gender.setCodeSystem("2.16.840.1.113883.5.1");
			}
			return gender;
		}
		return null;
	}
	/* ------------------ creation TS ----------------------------- */
	private TS createTS(ObjectFactory factory, Date date) {
		if (date != null) {
			TS ts = factory.createTS();
			DateFormat format = new SimpleDateFormat(DATE_FORMAT_FOR_SRPD);
			ts.setValue(format.format(date));
			return ts;
		}
		return null;
	}
	/* ----------- creation tel-format from URL-format ------------ */
	private String reParseTelcom(String telcom) {
		if (StringUtils.isNotEmpty(telcom)) {
			return telcom.split(":")[1];
		}
		return null;
	}
	
	private String[] reParseNumber(String number) {
		if(StringUtils.isNotEmpty(number)) {
			return number.split(" ");
		}
		return null;
	}
	
	private Date parseDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_SRPD);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
		return newDate;
	}
	
	private void createAdressFromAD(String adr, AD ad) {
		for(Serializable i: ad.getContent()) {
    		if(((JAXBElement)i).getDeclaredType() == AdxpStreetAddressLine.class) {
    			adr =  ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().get(0).toString();
    		}
    	}
	}
	private Map<FieldsInMap, Object> parseAnswerForOnePerson(PRPAIN101308UV02MFMIMT700711UV01Subject1 pd ) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		 String addr = null;
	        String lastName = null;
	        String middleName = null;
	        String firstName = null;
	        String homePhone = null;
	        String mobilePhone = null;
	        String workPhone = null;
	        String email =  null;
	        Date birthTime = null;
	        String passportNumber = null;
	        String passportSeries = null;
	        String insuranceNumber = null;
	        String insuranceSeries = null;
	        String gender = null;
	        String employment = null;
	        String idFromSRPD = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getId().get(0).getExtension();
	        List<AD> adr =  pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAddr();
	        if(adr != null && adr.size() > 0) {
	        	for (Serializable i : adr.get(0).getContent()) {
	        		if (((JAXBElement)i).getDeclaredType().equals(AdxpStreetAddressLine.class) 
	        				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent() != null
	        				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().size() > 0) {
	        			addr = ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().get(0).toString();
	        			break;
	        		}
	        	}
	        	for (Serializable i : adr.get(1).getContent()) {
	        		if (i != null && ((JAXBElement)i).getDeclaredType().equals(AdxpStreetAddressLine.class)
	        				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent() != null
	        				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().size() > 0) {
	        			employment = ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().get(0).toString();
	        			break;
	        		}
	        	}
	        	/*for (AD i : adr) {
	        		if (PostalAddressUse.H.equals(i.getUse().get(0))) {
	        			createAdressFromAD(addr, i);
	        		} else if (PostalAddressUse.WP.equals(i.getUse().get(0))) {
	        			createAdressFromAD(employment, i);
	        		}
	        	}*/
	        	
	        }
	        List<Serializable> name = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getName().get(0).getContent();
	        List<TEL> tels = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getTelecom();
	        for (TEL i : tels) {
	        	if (i.getUse() != null && i.getUse().size() > 0) {
	        		if(i.getUse().get(0).equals(TelecommunicationAddressUse.HP)) {
	        			// Home phone
	        			homePhone = reParseTelcom(i.getValue());
	        		} else if(i.getUse().get(0).equals(TelecommunicationAddressUse.MC)) {
	        			// Mobile phone
	        			mobilePhone = reParseTelcom(i.getValue());
	        		} else if (i.getUse().get(0).equals(TelecommunicationAddressUse.WP)) {
	        			// Work phone
	        			workPhone = reParseTelcom(i.getValue());
	        		} 
	        	} else if (StringUtils.isNotEmpty(i.getValue())) {
	        		email = reParseTelcom(i.getValue());
	        	}
	        }
	        List<PRPAMT101303UV02OtherIDs> numbers = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAsOtherIDs();
	        for (PRPAMT101303UV02OtherIDs i : numbers) {
	        	if (ROOT_PASSPORT_NUMBER.equals(i.getId().get(0).getRoot())) {
	        		if (StringUtils.isNotEmpty(i.getId().get(0).getExtension().trim())) {
	        			passportNumber = reParseNumber(i.getId().get(0).getExtension())[0];
	        			passportSeries = reParseNumber(i.getId().get(0).getExtension())[1];
	        		}
	        	} else if (ROOT_INSURANCE_NUMBER.equals(i.getId().get(0).getRoot())) {
	        		if (StringUtils.isNotEmpty(i.getId().get(0).getExtension().trim())) {
	        			insuranceNumber = reParseNumber(i.getId().get(0).getExtension())[0];
	        			insuranceSeries = reParseNumber(i.getId().get(0).getExtension())[1];
	        		}
	        	}
	        }
	        if (((JAXBElement<EnGiven>)name.get(0)).getValue().getContent() != null && 
	        		((JAXBElement<EnGiven>)name.get(0)).getValue().getContent().size() > 0) {
	        	lastName = ((JAXBElement<EnGiven>)name.get(0)).getValue().getContent().get(0).toString();
	        }
	        if (((JAXBElement<EnGiven>)name.get(1)).getValue().getContent() != null &&
	        		((JAXBElement<EnGiven>)name.get(1)).getValue().getContent().size() > 0) {
	        	middleName = ((JAXBElement<EnGiven>)name.get(1)).getValue().getContent().get(0).toString();
	        }
	        if (((JAXBElement<EnFamily>)name.get(2)).getValue().getContent() != null &&
	        		((JAXBElement<EnFamily>)name.get(2)).getValue().getContent().size() > 0) {
	        	firstName = ((JAXBElement<EnFamily>)name.get(2)).getValue().getContent().get(0).toString();
	        }
	        if (pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getBirthTime() != null) {
	        	birthTime = parseDate(pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getBirthTime().getValue());
	        }
	        gender = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAdministrativeGenderCode().getCode();
	        map.put(FieldsInMap.ADRESS, addr);
	        map.put(FieldsInMap.FIRST_NAME, firstName);
	        map.put(FieldsInMap.LAST_NAME, lastName);
	        map.put(FieldsInMap.MIDDLE_NAME, middleName);
	        map.put(FieldsInMap.PHONE, homePhone);
	        map.put(FieldsInMap.WORK_PHONE, workPhone);
	        map.put(FieldsInMap.PASSPORT_NUMBER, passportNumber + " " + passportSeries);
	        map.put(FieldsInMap.OMC_NUMBER, insuranceNumber + " " + insuranceSeries);
	        map.put(FieldsInMap.EMAIL, email);
	        map.put(FieldsInMap.BIRTH, birthTime);
	        map.put(FieldsInMap.GENDER, gender);
	        map.put(FieldsInMap.EMPLOYMENT, employment);
	        map.put(FieldsInMap.TEMP_STORAGE_ID, idFromSRPD);
		return map;
	}
	
}
