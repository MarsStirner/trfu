package ru.korusconsulting.SRPD;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.AD;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PN;
import org.hl7.v3.PNExplicit;
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
import org.hl7.v3.PRPAIN101314UV02;
import org.hl7.v3.PRPAIN101314UV02MFMIMT700721UV01ControlActProcess;
import org.hl7.v3.PRPAIN101314UV02MFMIMT700721UV01RegistrationRequest;
import org.hl7.v3.PRPAIN101314UV02MFMIMT700721UV01Subject1;
import org.hl7.v3.PRPAIN101314UV02MFMIMT700721UV01Subject2;
import org.hl7.v3.PRPAIN101315UV02;
import org.hl7.v3.PRPAMT101301UV02IdentifiedPerson;
import org.hl7.v3.PRPAMT101301UV02OtherIDs;
import org.hl7.v3.PRPAMT101301UV02Person;
import org.hl7.v3.PRPAMT101302UV02IdentifiedPerson;
import org.hl7.v3.PRPAMT101302UV02IdentifiedPersonIdentifiedPerson;
import org.hl7.v3.PRPAMT101302UV02OtherIDsId;
import org.hl7.v3.PRPAMT101302UV02PersonAddr;
import org.hl7.v3.PRPAMT101302UV02PersonAdministrativeGenderCode;
import org.hl7.v3.PRPAMT101302UV02PersonAsOtherIDs;
import org.hl7.v3.PRPAMT101302UV02PersonBirthTime;
import org.hl7.v3.PRPAMT101302UV02PersonName;
import org.hl7.v3.PRPAMT101302UV02PersonTelecom;
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
	private static final String PHONE = "tel:";
	private static final String EMAIL = "mailto:";
	
	private static final String ROOT_ID_FROM_SRPD_FOR_UPDATE ="3.0.0.0";
	private static final String ROOT_PASSPORT_NUMBER = "3.0.0.1";
	private static final String ROOT_INSURANCE_NUMBER = "3.0.0.9";
	private static final String ROOT_INSURANCE_SERIES = "3.0.0.8";
	private static final String DATE_FORMAT_FOR_SRPD = "yyyyMMdd";
	
	private static final String SRPD_ADRESS_PROPERTIE = "srpdAdress";
	
	private static final PDManager pdm;
	
	static {
		Properties prop = null;
		try {
			prop = DonorHelper.createPropertiesForURL("storage_utility.properties");
			TmisPdm.initWsdl(prop.get(SRPD_ADRESS_PROPERTIE).toString());
		} catch (Exception e) {
			TmisPdm.initWsdl("http://198.199.126.156:8080/pdm-war/tmis-pdm?wsdl");
		}
		TmisPdm service = new TmisPdm();
		pdm = service.getPortPdm(); 
	}
	
	/* Search possible information for donors with current id */
	public  Map<String,Map<FieldsInMap, Object>> get(Map<FieldsInMap, Object> params) {
		if (params.get(FieldsInMap.LIST_STORAGE_IDS) == null || ((Collection<String>)params.get(FieldsInMap.LIST_STORAGE_IDS)).size() <= 0) {
			return null;
		}
		Map<String, Map<FieldsInMap, Object>> result = new HashMap<String,Map<FieldsInMap,Object>>();
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
        	if (i != null) {
        		final PRPAMT101307UV02IdentifiedPersonIdentifier person = factory.createPRPAMT101307UV02IdentifiedPersonIdentifier();
        		prmList.getIdentifiedPersonIdentifier().add(person);
        		final II ii = factory.createII();
        		person.getValue().add(ii);        
        		ii.setRoot((String)i);
        	}
        }
        PRPAIN101308UV02 res = pdm.getDemographics(prm);
        for (PRPAIN101308UV02MFMIMT700711UV01Subject1 i: res.getControlActProcess().getSubject()) {
        	Map<FieldsInMap, Object> resMap = parseAnswerForOnePerson(i);
        	result.put((String)resMap.get(FieldsInMap.TEMP_STORAGE_ID),resMap);
        }
        return result;   
	}
	
	public  Map<String,Map<FieldsInMap, Object>> findToSRPDByParameters(Map<FieldsInMap, Object> params) {
		if (params.get(FieldsInMap.LIST_STORAGE_IDS) == null || ((Collection<String>)params.get(FieldsInMap.LIST_STORAGE_IDS)).size() <= 0) {
			return null;
		}
		Map<String, Map<FieldsInMap, Object>> result = new HashMap<String,Map<FieldsInMap,Object>>();
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
        	Map<FieldsInMap, Object> resMap = parseAnswerForOnePerson(i);
        	result.put(resMap.get(FieldsInMap.TEMP_STORAGE_ID).toString(),resMap);
        }
        return result;   
	}
	
	/* Send information about current donor and wait for id from SRPD */
	public Map<FieldsInMap, Object> addPDToSRPD(Map<FieldsInMap, Object> information) {
		String result = addPDToSRPDData((String)information.get(FieldsInMap.FIRST_NAME), 
										(String)information.get(FieldsInMap.LAST_NAME),
										(String)information.get(FieldsInMap.MIDDLE_NAME), 
										(String)information.get(FieldsInMap.OMC_NUMBER),
										(String)information.get(FieldsInMap.OMC_SERIES),
										(String)information.get(FieldsInMap.PASSPORT_NUMBER),
										(String)information.get(FieldsInMap.PHONE), 
										(String)information.get(FieldsInMap.ADRESS),
										(String)information.get(FieldsInMap.WORK_PHONE), 
										(String)information.get(FieldsInMap.EMAIL),
										(String)information.get(FieldsInMap.EMPLOYMENT), 
										(String)information.get(FieldsInMap.BIRTH), 
										(String)information.get(FieldsInMap.GENDER));
		information.put(FieldsInMap.TEMP_STORAGE_ID, result);
		return information;
	}
	
	public Map<FieldsInMap, Object> updateToSRPD(Map<FieldsInMap, Object> params) {
		String tempStorageId = (String)params.get(FieldsInMap.TEMP_STORAGE_ID);
		String lastName = (String)params.get(FieldsInMap.LAST_NAME); 
		String firstName = (String)params.get(FieldsInMap.FIRST_NAME);
		String middleName = (String)params.get(FieldsInMap.MIDDLE_NAME); 
		String insuranceNumber = (String)params.get(FieldsInMap.OMC_NUMBER);
		String insuranceSeries = (String)params.get(FieldsInMap.OMC_SERIES);
		String passport = (String)params.get(FieldsInMap.PASSPORT_NUMBER);
		String phone = (String)params.get(FieldsInMap.PHONE); 
		String adress = (String)params.get(FieldsInMap.ADRESS);
		String workPhone = (String)params.get(FieldsInMap.WORK_PHONE); 
		String email = (String)params.get(FieldsInMap.EMAIL);
		String employment = (String)params.get(FieldsInMap.EMPLOYMENT); 
		String birth = (String)params.get(FieldsInMap.BIRTH); 
		String gender = (String)params.get(FieldsInMap.GENDER);
		if(StringUtils.isEmpty(tempStorageId)) {
			return null;
		}
		ObjectFactory factory = new ObjectFactory();
		final PRPAIN101314UV02 prm = factory.createPRPAIN101314UV02();
	    final PRPAIN101314UV02MFMIMT700721UV01ControlActProcess controlActProcess = factory.createPRPAIN101314UV02MFMIMT700721UV01ControlActProcess();
	    prm.setControlActProcess(controlActProcess);
	    final PRPAIN101314UV02MFMIMT700721UV01Subject1 subject1 = factory.createPRPAIN101314UV02MFMIMT700721UV01Subject1();
	    controlActProcess.setSubject(subject1);
	    final PRPAIN101314UV02MFMIMT700721UV01RegistrationRequest regRequest = factory.createPRPAIN101314UV02MFMIMT700721UV01RegistrationRequest();
	    subject1.setRegistrationRequest(regRequest);
	    final PRPAIN101314UV02MFMIMT700721UV01Subject2 subject2 = factory.createPRPAIN101314UV02MFMIMT700721UV01Subject2();
	    regRequest.setSubject1(subject2);
	    final PRPAMT101302UV02IdentifiedPerson identifiedPerson = factory.createPRPAMT101302UV02IdentifiedPerson();
	    subject2.setIdentifiedPerson(identifiedPerson);
	    final PRPAMT101302UV02IdentifiedPersonIdentifiedPerson person = factory.createPRPAMT101302UV02IdentifiedPersonIdentifiedPerson();
	    identifiedPerson.setIdentifiedPerson(person);
	    final PRPAMT101302UV02PersonAsOtherIDs otherId = factory.createPRPAMT101302UV02PersonAsOtherIDs();
	    person.getAsOtherIDs().add(otherId);
	    final PRPAMT101302UV02OtherIDsId id = factory.createPRPAMT101302UV02OtherIDsId();
	    otherId.getId().add(id);
	    id.setRoot(ROOT_ID_FROM_SRPD_FOR_UPDATE);
	    id.setExtension(tempStorageId);

	    final PRPAMT101302UV02PersonName pn = factory.createPRPAMT101302UV02PersonName();
	    person.getName().add(pn);
	    
	    if (StringUtils.isNotEmpty(lastName)) {
	    	EnGiven name = factory.createEnGiven();
	    	name.getContent().add(lastName);
	    	pn.getContent().add(factory.createENGiven(name));
	    }
	    if (StringUtils.isNotEmpty(middleName)) {
	    	EnGiven suffix = factory.createEnGiven(); 
	    	suffix.getContent().add(middleName);
	    	pn.getContent().add(factory.createENGiven(suffix));
	    }
	    if (StringUtils.isNotEmpty(firstName)){
	    	EnFamily family = factory.createEnFamily();
	    	family.getContent().add(firstName);
	    	pn.getContent().add(factory.createENFamily(family));
	    }
	    
	    if (StringUtils.isNotEmpty(phone)) {
	    	person.getTelecom().add(createPersonTelecom(factory, phone, TelecommunicationAddressUse.HP));
	    }
	    if (StringUtils.isNotEmpty(workPhone)) {
	    	person.getTelecom().add(createPersonTelecom(factory, workPhone, TelecommunicationAddressUse.WP));
	    }
	    /*if (StringUtils.isNotEmpty(email)) {
	    	person.getTelecom().add(createPersonTelecom(factory, email, null));
	    }*/
	    
	    /*if (StringUtils.isNotEmpty(employment)) {
	    	person.getAddr().add(createPersonAddr(factory, employment, PostalAddressUse.WP));
	    }*/
	    if (StringUtils.isNotEmpty(adress)) {
	    	person.getAddr().add(createPersonAddr(factory, adress, PostalAddressUse.H));
	    }
	    
	    if (StringUtils.isNotEmpty(birth)) {
	    	person.setBirthTime(createBirthTime(factory, birth));
	    }
	    
	    if (StringUtils.isNotEmpty(passport)) {
	    	person.getAsOtherIDs().add(createPersonOtherIDs(factory, passport, ROOT_PASSPORT_NUMBER));
	    }
	    if (StringUtils.isNotEmpty(insuranceNumber)) {
	    	person.getAsOtherIDs().add(createPersonOtherIDs(factory, insuranceNumber, ROOT_INSURANCE_NUMBER));
	    }
	    if (StringUtils.isNotEmpty(insuranceSeries)) {
	    	person.getAsOtherIDs().add(createPersonOtherIDs(factory, insuranceSeries, ROOT_INSURANCE_SERIES));
	    }
	    if (StringUtils.isNotEmpty(gender)) {
	    	person.setAdministrativeGenderCode(createGender(factory, (String)params.get(FieldsInMap.GENDER)));
	    }
	    
        final PRPAIN101315UV02 res = pdm.update(prm);
		return params;
	}
	
	public Map<String,Map<FieldsInMap, Object>> findToSRPD(Map<FieldsInMap, Object> params) {
		if (params.get(FieldsInMap.LIST_STORAGE_IDS) == null || ((Collection<String>)params.get(FieldsInMap.LIST_STORAGE_IDS)).size() <= 0) {
			System.out.println("Params = null");
			return null;
		}
		for(Object i : params.values()) {
			System.out.println(i);
		}
		Map<String, Map<FieldsInMap, Object>> result = new HashMap<String,Map<FieldsInMap,Object>>();
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
        	Map<FieldsInMap, Object> resMap = parseAnswerForOnePerson(i);
        	result.put(resMap.get(FieldsInMap.TEMP_STORAGE_ID).toString(),resMap);
        }
        return result;
	}
	
	public String addPDToSRPDData(String family, String givven, String suffix, 
			   				  String numberOMC, String seriesOMC, String numberPassport, String homePhone,
			   				  String address, String workPhone, String email, 
			   				  String employmentId, String birthDate, String genderId) {
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
        PNExplicit name = factory.createPNExplicit();
        
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
        List<ADExplicit> adreses = person.getAddr();
        List<TEL> tels = person.getTelecom();
        final ADExplicit adr = factory.createADExplicit();
        adr.getUse().add(PostalAddressUse.H);
        AdxpStreetAddressLine strAdrLine = factory.createAdxpStreetAddressLine();
        strAdrLine.getContent().add(address);
        adr.getContent().add(factory.createADStreetAddressLine(strAdrLine));
        adreses.add(adr);
        //final ADExplicit adrEmp = factory.createADExplicit();
        //adrEmp.getUse().add(PostalAddressUse.WP);
        strAdrLine = factory.createAdxpStreetAddressLine();
        strAdrLine.getContent().add(employmentId);
        //adrEmp.getContent().add(factory.createADStreetAddressLine(strAdrLine));
        //adreses.add(adrEmp);
        person.getAsOtherIDs().add(new PRPAMT101301UV02OtherIDs() );
        person.getAsOtherIDs().get(0).getId().add(new II());
        person.getAsOtherIDs().get(0).getId().get(0).setRoot(ROOT_PASSPORT_NUMBER);
        person.getAsOtherIDs().get(0).getId().get(0).setExtension(numberPassport);
        person.getAsOtherIDs().get(0).getId().add(new II());
        person.getAsOtherIDs().get(0).getId().get(1).setRoot(ROOT_INSURANCE_NUMBER);
        person.getAsOtherIDs().get(0).getId().get(1).setExtension(numberOMC);
        person.getAsOtherIDs().get(0).getId().add(new II());
        person.getAsOtherIDs().get(0).getId().get(2).setRoot(ROOT_INSURANCE_SERIES);
        person.getAsOtherIDs().get(0).getId().get(2).setExtension(seriesOMC);
        
        if (StringUtils.isNotEmpty(homePhone)) {
        	tels.add(createPersonTelecom(factory, homePhone, TelecommunicationAddressUse.HP));
        }
        if (StringUtils.isNotEmpty(workPhone)) {
        	tels.add(createPersonTelecom(factory, workPhone, TelecommunicationAddressUse.WP));
        }
        /*if (StringUtils.isNotEmpty(email)) {
        	tels.add(createPersonTelecom(factory, email, null));
        }*/
        
        TS birthTime = createBirthTime(factory, birthDate);
        person.setBirthTime(birthTime);

        CE ce = createGender(null, genderId);
        person.setAdministrativeGenderCode(ce);

        try {
        	PRPAIN101312UV02 res = pdm.add(prm);
        	String root = res.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getIdentifiedPerson().
                getIdentifiedPerson().getId().get(0).getExtension();
        	return root;
        } catch (Exception e) {
        	System.out.println(e);
			return null;
		}
	}
	/* ---------------------create TEL------------------------------ */
	private PRPAMT101302UV02PersonTelecom createPersonTelecom(ObjectFactory factory, String value, TelecommunicationAddressUse type) {
		if (StringUtils.isNotEmpty(value)) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			PRPAMT101302UV02PersonTelecom tel = factory.createPRPAMT101302UV02PersonTelecom();
			if (!"".equals(value) && value != null) {
				if (type != null) {
					switch (type) {
						case HP: 
						case WP:
							value = PHONE + value;
							tel.getUse().add(type);
							break;
						}
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
	private PRPAMT101302UV02PersonAdministrativeGenderCode createGender(ObjectFactory factory, String genderId) {
		if (genderId != null) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			PRPAMT101302UV02PersonAdministrativeGenderCode gender = factory.createPRPAMT101302UV02PersonAdministrativeGenderCode();
			if ("0".equals(genderId)) {
				gender.setCode("F");
				gender.setCodeSystem("2.16.840.1.113883.5.1");
			} else if("1".equals(genderId)) {
				gender.setCode("M");
				gender.setCodeSystem("2.16.840.1.113883.5.1");
			}
			return gender;
		}
		return null;
	}
	/* ------------------ создание даты рождения ----------------------------- */
	private PRPAMT101302UV02PersonBirthTime createBirthTime(ObjectFactory factory, String date) {
		if (StringUtils.isNotEmpty(date)) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			if (date != null) {
				PRPAMT101302UV02PersonBirthTime ts = factory.createPRPAMT101302UV02PersonBirthTime();
				ts.setValue(date);
				return ts;
			}
		}
		return null;
	}
	/* ---------------- для хранения данных о паспорте и страховке ------------ */
	private PRPAMT101302UV02PersonAsOtherIDs createPersonOtherIDs(ObjectFactory factory, String idValue, String rootType) {
		if (StringUtils.isNotEmpty(idValue) && StringUtils.isNotEmpty(rootType)) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			PRPAMT101302UV02PersonAsOtherIDs id = factory.createPRPAMT101302UV02PersonAsOtherIDs();
		    id.getId().add(new PRPAMT101302UV02OtherIDsId());
		    id.getId().get(0).setRoot(rootType);
		    id.getId().get(0).setExtension(idValue);
			return id;
		}
		return null;
	}
	/* ----------------- создание Адреса для update ---------------- */
	private PRPAMT101302UV02PersonAddr createPersonAddr(ObjectFactory factory, String value, PostalAddressUse type ) {
		if (StringUtils.isNotEmpty(value)) {
			if (factory == null) {
				factory = new ObjectFactory();
			}
			PRPAMT101302UV02PersonAddr adress = factory.createPRPAMT101302UV02PersonAddr();
			AdxpStreetAddressLine strAdrLine = factory.createAdxpStreetAddressLine();
	        strAdrLine.getContent().add(value);
	        adress.getContent().add(factory.createADStreetAddressLine(strAdrLine));
			adress.getUse().add(type);
			return adress;
		}
		return null; 
	}
	/* ----------- creation tel-format from URL-format ------------ */
	private String reParseTelcom(String telcom) {
		if (StringUtils.isNotEmpty(telcom)) {
			String[]array = telcom.split(":");
			if (array.length > 1) {
				return array[1];
			}
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
	
	private String createAdressFromAD(AD ad) {
		String adr = null;
		for(Serializable i: ad.getContent()) {
    		if(((JAXBElement)i).getDeclaredType() == AdxpStreetAddressLine.class
    				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent() != null
    				&& ((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().size() > 0 ) {
    			adr =  (String)(((AdxpStreetAddressLine)((JAXBElement) i).getValue()).getContent().get(0));
    		}
    	}
		return adr;
	}
	private Map<FieldsInMap, Object> parseAnswerForOnePerson(PRPAIN101308UV02MFMIMT700711UV01Subject1 pd ) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		 String addr = null;
	        String lastName = null;
	        String middleName = null;
	        String firstName = null;
	        String homePhone = null;
	        String workPhone = null;
	        String email =  null;
	        String birthTime = null;
	        String passportNumber = null;
	        String passportSeries = null;
	        String insuranceNumber = null;
	        String insuranceSeries = null;
	        String gender = null;
	        String employment = null;
	        String idFromSRPD = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getId().get(0).getExtension();
	        List<AD> adr =  pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAddr();
	        if(adr != null && adr.size() > 0) {
	        	for (AD i : adr) {
	        		if (i.getUse() != null && i.getUse().size() > 0) {
	        			if (PostalAddressUse.H.equals(i.getUse().get(0))) {
	        				addr = createAdressFromAD(i);
	        			}/* else if (PostalAddressUse.WP.equals(i.getUse().get(0))) {
	        				employment = createAdressFromAD(i);
	        			}*/
	        		}
	        	}
	        	
	        }
	        List<Serializable> name = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getName().get(0).getContent();
	        List<TEL> tels = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getTelecom();
	        for (TEL i : tels) {
	        	if (i.getUse() != null && i.getUse().size() > 0) {
	        		if(i.getUse().get(0).equals(TelecommunicationAddressUse.HP)) {
	        			// Home phone
	        			homePhone = reParseTelcom(i.getValue());
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
	        			String[] passport = reParseNumber(i.getId().get(0).getExtension());
	        			if (passport.length == 2) {
	        				passportNumber = passport[0];
	        				passportSeries = passport[1];
	        			}
	        		}
	        	} else if (ROOT_INSURANCE_NUMBER.equals(i.getId().get(0).getRoot())) {
	        		if (StringUtils.isNotEmpty(i.getId().get(0).getExtension().trim())) {
	        			/*String[] insurance = reParseNumber(i.getId().get(0).getExtension());
	        			if (insurance.length == 2) {
	        				insuranceNumber = insurance[0];
	        				insuranceSeries = insurance[1];
	        			}*/
	        			insuranceNumber = i.getId().get(0).getExtension();
	        		}
	        	} else if (ROOT_INSURANCE_SERIES.equals(i.getId().get(0).getRoot())) {
	        		if (StringUtils.isNotEmpty(i.getId().get(0).getExtension().trim())) {
	        			/*String[] insurance = reParseNumber(i.getId().get(0).getExtension());
	        			if (insurance.length == 2) {
	        				insuranceNumber = insurance[0];
	        				insuranceSeries = insurance[1];
	        			}*/
	        			insuranceSeries = i.getId().get(0).getExtension();
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
	        	birthTime = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getBirthTime().getValue();
	        }
	        if (pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAdministrativeGenderCode() != null) {
	        	gender = pd.getRegistrationEvent().getSubject1().getIdentifiedPerson().getIdentifiedPerson().getAdministrativeGenderCode().getCode();
	        }
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
