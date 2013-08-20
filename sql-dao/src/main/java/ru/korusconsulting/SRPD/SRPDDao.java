package ru.korusconsulting.SRPD;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hl7.v3.AD;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.ActClassRegistration;
import org.hl7.v3.ActMoodRequest;
import org.hl7.v3.ActRelationshipHasSubject;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpState;
import org.hl7.v3.AdxpStreetAddressLine;
import org.hl7.v3.AdxpStreetName;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150000UV02Organization;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.EntityClassOrganization;
import org.hl7.v3.EntityDeterminerSpecific;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MFMIMT700721UV01Author2;
import org.hl7.v3.NullFlavor;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAIN101305UV02;
import org.hl7.v3.PRPAIN101311UV02;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01ControlActProcess;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01Subject1;
import org.hl7.v3.PRPAIN101311UV02MFMIMT700721UV01Subject2;
import org.hl7.v3.PRPAMT101301UV02IdentifiedPerson;
import org.hl7.v3.PRPAMT101301UV02Person;
import org.hl7.v3.ParticipationAuthorOriginator;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.PostalAddressUse;
import org.hl7.v3.RoleClassAssignedEntity;
import org.hl7.v3.TEL;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;

import ru.korusconsulting.pdmanager.PDManager;
import ru.korusconsulting.pdmanager.TmisPdm;


public class SRPDDao {
	private static final String NUMBER_PASSPORT = "OID_НОМЕР_ПАСПОРТА";
	private static final String OMC = "OID_ОМС";
	
	private static final String HOME_PHONE = "tel:";
	private static final String MOBILE_PHONE = "cell-tel:";
	private static final String WORKING_PHONE = "working-office-tel:";
	private static final String EMAIL = "mailto:";
	
	private PDManager initPDManager() {
		TmisPdm service = new TmisPdm();
		return service.getPortPdm();
	}
	
	/* search list of possible donors, which equals parameters for search*/
	public Map<Integer,Map<DonorHelper.FieldsInMap, Object>> getDonors(Map<DonorHelper.FieldsInMap, Object> parametersForSearch) {
		PDManager pdm = initPDManager();
		/* full list, which contain:
		 * key - id from SRPD
		 * value - map of PD from SRPD
		 */
		Map<Integer,Map<DonorHelper.FieldsInMap, Object>> fullListWithDonorsInformation = null;
		fullListWithDonorsInformation = new DonorHelper().makeMapsFromAnswerAfterSearch(pdm.findCandidates(createFindToSRPD(parametersForSearch)));
		return fullListWithDonorsInformation;
		
	}
	
	/* Search possible information for donors with current id */
	public Map<DonorHelper.FieldsInMap, Object> get(Map<DonorHelper.FieldsInMap, Object> params) {
		TmisPdm service = new TmisPdm();
		PDManager pdm = service.getPortPdm();
		return new DonorHelper().parseAnswerFromSRPDAfterFind(pdm.findCandidates(createFindToSRPD(params)));
	}
	
	/* Send information about current donor and wait for id from SRPD */
	public Map<DonorHelper.FieldsInMap, Object> addToSRPD(Map<DonorHelper.FieldsInMap, Object> information) {
		PDManager pdm = initPDManager();
		PRPAIN101311UV02 parameters = createAddToSRPD(information.get(DonorHelper.FieldsInMap.LAST_NAME).toString(),
								 			   information.get(DonorHelper.FieldsInMap.FIRST_NAME).toString(),
								 			   information.get(DonorHelper.FieldsInMap.MIDDLE_NAME).toString(),
								 			   information.get(DonorHelper.FieldsInMap.OMC_NUMBER).toString(),
								 			   information.get(DonorHelper.FieldsInMap.PASSPORT_NUMBER).toString(),
								 			   information.get(DonorHelper.FieldsInMap.PHONE).toString(),
								 			   information.get(DonorHelper.FieldsInMap.ADRESS).toString(),
								 			   information.get(DonorHelper.FieldsInMap.WORK_PHONE).toString(),
								 			   information.get(DonorHelper.FieldsInMap.EMAIL).toString(),
								 			   information.get(DonorHelper.FieldsInMap.EMPLOYMENT).toString(),
								 			   information.get(DonorHelper.FieldsInMap.BIRTH).toString(),
								 			   (Integer)information.get(DonorHelper.FieldsInMap.GENDER));
		////////////////////////////////////////Добавить параметр для stogate_id если понадобится
		return new DonorHelper().parseAnswerFromSRPDAfterAdd(pdm.add(parameters));
	}
	
	/* Creation parameters for findCandidates-method of Web-service */
	private PRPAIN101305UV02 createFindToSRPD(Map<DonorHelper.FieldsInMap, Object> fieldsForSearch) {
		PRPAIN101305UV02 parameters = new PRPAIN101305UV02();
		return parameters;
	}
	
	/* Creation parameters for add-method of Web-service */
	private PRPAIN101311UV02 createAddToSRPD(String family, 
			  								   String givven, 
			  								   String suffix, 
			  								   String numberOMC, 
			  								   String numberPassport, 
			  								   String homePhone,
			  								   String address, 
			  								   String workPhone, 
			  								   String email, 
			  								   String employmentId, 
			  								   String birthDate,
			  								   Integer genderId) {
		ObjectFactory factory = new ObjectFactory();
		PRPAIN101311UV02 parameters = factory.createPRPAIN101311UV02();
		/* -----------------set TimeCreation--------------------- */
		parameters.setCreationTime(createTS(factory, new Date().toString()));
		/* ----------------set interaction Id-------------------- */
		parameters.setInteractionId(createII(factory,"2.16.840.1.113883.1.6", null, null));///////////////////////////////// перегенерировать
		/*--------------- set Processing Code-------------------- */
		parameters.setProcessingCode(createCS(factory, "P"));
		/*------------ set processing Mode Code ----------------- */
		parameters.setProcessingModeCode(createCS(factory, "T"));
		/* ------------------ set accept Ack Code --------------- */
		parameters.setAcceptAckCode(createCS(factory, "AL"));
		/* ------------- set Reciever --------------------------- */
		parameters.getReceiver().add(createReciever(factory));
		/* ------------------------------------------------------ */
		/* ------------- set Sender ----------------------------- */
		parameters.setSender(createSender(factory));
		/* ------------------------------------------------------ */
		/* -------------- set controlActProcess ----------------- */
		PRPAIN101311UV02MFMIMT700721UV01ControlActProcess cap = factory.createPRPAIN101311UV02MFMIMT700721UV01ControlActProcess();
		cap.setClassCode(ActClassControlAct.CACT);
		cap.setMoodCode(XActMoodIntentEvent.RQO);
			/* ---set values of subject--- */
			PRPAIN101311UV02MFMIMT700721UV01Subject1 subj = factory.createPRPAIN101311UV02MFMIMT700721UV01Subject1();
			subj.setTypeCode(ActRelationshipHasSubject.SUBJ);
			subj.setRegistrationRequest(makeRegistartionRequest(factory, family, givven, suffix, numberOMC, numberPassport, homePhone, address, workPhone, 
																email, employmentId, birthDate, genderId));
			/* -------------------------- */
		cap.setSubject(subj);
		parameters.setControlActProcess(cap);
		return parameters;
		
	}
	
	private PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest makeRegistartionRequest(ObjectFactory factory, String family, 
																						String givven, 
																						String suffix, 
																						String numberOMC, 
																						String numberPassport, 
																						String homePhone,
																						String address, 
																						String workPhone, 
																						String email, 
																						String employmentId, 
																						String birthDate,
																						Integer genderId) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest regReq = factory.createPRPAIN101311UV02MFMIMT700721UV01RegistrationRequest();
		regReq.setClassCode(ActClassRegistration.REG);
		regReq.setMoodCode(ActMoodRequest.RQO);
		/* --------------- set Status -------------- */
		regReq.setStatusCode(createCS(factory, "active"));
		/* ------------------------------------------------ set Autor -------------------------------------------------------- */
		MFMIMT700721UV01Author2 autor = factory.createMFMIMT700721UV01Author2();
		autor.setTypeCode(ParticipationAuthorOriginator.AUT);
		/* -------------- set Asiigned Entity ------ */
		COCTMT090003UV01AssignedEntity assignEntity = factory.createCOCTMT090003UV01AssignedEntity();
		assignEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
		/* -------------- set Id of PD --------------*/
		assignEntity.getId().add(createII(factory, null,null, NullFlavor.NI));
		/* ---------------end of ID for pd --------- */
		autor.setAssignedEntity(assignEntity);
		regReq.setAuthor(autor);
		/* ---------------------------------------- end of setting Autor -------- -------------------------------------------*/
		/* ---------------------------------------- set Subject1------------------------------------------------------------ */
		PRPAIN101311UV02MFMIMT700721UV01Subject2 subject1 = factory.createPRPAIN101311UV02MFMIMT700721UV01Subject2();
		subject1.setTypeCode(ParticipationTargetSubject.SBJ);
		/* ------------ set identifiedPerson ------ */
		PRPAMT101301UV02IdentifiedPerson identifiedPerson = factory.createPRPAMT101301UV02IdentifiedPerson();
		identifiedPerson.getId().add(createII(factory, NUMBER_PASSPORT, numberPassport, null));
		identifiedPerson.getId().add(createII(factory, OMC, numberOMC, null));
		/* --- end of setting  identifiedPerson --- */
		/* --- set StausCode ---------------------- */
		identifiedPerson.setStatusCode(createCS(factory, "active"));
		/* --- setting identified person to Identified Person */
		identifiedPerson.setIdentifiedPerson(insertPD(factory,family, givven, suffix, homePhone, null, workPhone, email, birthDate, address, genderId));
		/* --- end ofsetting identified person to Identified Person */
		/* --- setting assigningOrganization --- */
		COCTMT150000UV02Organization assignOrganization = new COCTMT150000UV02Organization();
		assignOrganization.setClassCode(EntityClassOrganization.ORG);
		assignOrganization.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
		assignOrganization.getId().add(createII(factory, employmentId, null, null));
		identifiedPerson.setAssigningOrganization(assignOrganization);
		/* - end of setting assigningOrganization - */
		subject1.setIdentifiedPerson(identifiedPerson);
		regReq.setSubject1(subject1);
		regReq.setAuthor(makeAutor(factory));
		/* ------------------------------------- end of setting Subject1 --------------------------------------------------- */
		return regReq;
	}
	private PRPAMT101301UV02Person insertPD(ObjectFactory factory, String family, 
											String given, 
											String suffix, 
											String homePhone, 
											String mobilePhone, 
											String workPhone, 
											String email, 
											String birthTime, 
											String adress,
											Integer genderId) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		PRPAMT101301UV02Person idPersonToIdPerson = factory.createPRPAMT101301UV02Person();
		/* setting gender */
		idPersonToIdPerson.setAdministrativeGenderCode(createGender(factory, genderId));
		PN name = factory.createPN();
		EnFamily fam = factory.createEnFamily();
		fam.getContent().add(family);
		EnGiven giv = factory.createEnGiven();
		giv.getContent().add(given);
		EnSuffix suff = factory.createEnSuffix();
		suff.getContent().add(suffix);
		/* add name */
		name.getContent().add(factory.createENFamily(fam));
		name.getContent().add(factory.createENGiven(giv));
		name.getContent().add(factory.createENSuffix(suff));
		idPersonToIdPerson.getName().add(name);
		/* setting phones and email */
		idPersonToIdPerson.getTelecom().add(createTEL(factory, HOME_PHONE + homePhone));
		idPersonToIdPerson.getTelecom().add(createTEL(factory, MOBILE_PHONE + mobilePhone));
		idPersonToIdPerson.getTelecom().add(createTEL(factory, WORKING_PHONE + workPhone));
		idPersonToIdPerson.getTelecom().add(createTEL(factory, EMAIL + email));
		/* end of setting phones and email */
		TS birthT = new TS();
		birthT.setValue(birthTime);
		idPersonToIdPerson.setBirthTime(createTS(factory, birthTime));
		/* setting all adresses */
		idPersonToIdPerson.getAddr().add(makeAdress(factory, PostalAddressUse.H,
													null,
													adress, 
													null, 
													null,
													null));
		return idPersonToIdPerson;
	}
	
	
	/* method for creation address */
	private AD makeAdress(ObjectFactory factory, PostalAddressUse typeAdress, String city, String streetAddressLine, String streetName, 
							String postalCode, String state) {
		AD adress;
		if (factory == null) {
			factory = new ObjectFactory();
		}
		adress = factory.createAD();
		if (typeAdress != null) {
			adress.getUse().add(typeAdress);
		}
		if (!"".equals(city) && city != null) {
			AdxpCity adxpCity = factory.createAdxpCity();
			adxpCity.getContent().add(city);
			adress.getContent().add(factory.createADCity(adxpCity));
		}
		if (!"".equals(streetAddressLine) && streetAddressLine != null) {
			AdxpStreetAddressLine adxpAddressLine = factory.createAdxpStreetAddressLine();
			adxpAddressLine.getContent().add(streetAddressLine);
			adress.getContent().add(factory.createADStreetAddressLine(adxpAddressLine));
		}
		if (!"".equals(streetName) && streetName != null) {
			AdxpStreetName adxpStreetName = factory.createAdxpStreetName();
			adxpStreetName.getContent().add(streetName);
			adress.getContent().add(factory.createADStreetName(adxpStreetName));
		}
		if (!"".equals(postalCode) && postalCode != null) {
			AdxpPostalCode adxpPostalCode = factory.createAdxpPostalCode();
			adxpPostalCode.getContent().add(postalCode);
			adress.getContent().add(factory.createADPostalCode(adxpPostalCode));
		}
		if (!"".equals(state) && state != null) {
			AdxpState adxpState = factory.createAdxpState();
			adxpState.getContent().add(state);
			adress.getContent().add(factory.createADState(adxpState));			
		}
		return adress;
	}
	/* --------------- method for creation autor ------------------- */
	private MFMIMT700721UV01Author2 makeAutor(ObjectFactory factory) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		MFMIMT700721UV01Author2 autor = new MFMIMT700721UV01Author2();
		autor.setTypeCode(ParticipationAuthorOriginator.AUT);
		COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
		assignedEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
		assignedEntity.getId().add(createII(factory, null,null, NullFlavor.NI));
		autor.setAssignedEntity(assignedEntity);
		return autor;
	}
	/* --------------------- creation id ------------------------- */
	private II createII(ObjectFactory factory, String root, String extension, NullFlavor nullFlavor) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		II id = factory.createII();
		if (!"".equals(root) && root != null) {
			id.setRoot(root);
		}
		if (!"".equals(extension) && extension != null) {
			id.setExtension(extension);
		}
		if (nullFlavor != null) {
			id.setNullFlavor(nullFlavor);
		}
		return id;
	}
	/* ---------------------create TEL------------------------------ */
	private TEL createTEL(ObjectFactory factory, String value) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		TEL tel = factory.createTEL();
		if (!"".equals(value) && value != null) {
			tel.setValue(value);
		}
		return tel;
	}
	/* -------------------- create reciever ---------------------- */
	private MCCIMT000100UV01Receiver createReciever(ObjectFactory factory) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		MCCIMT000100UV01Receiver reciever = factory.createMCCIMT000100UV01Receiver();
		reciever.setTypeCode(CommunicationFunctionType.RCV);
		reciever.setDevice(createDevice(factory));
		return reciever;
	}
	/* -------------------- creation sender ------------------------- */
	private MCCIMT000100UV01Sender createSender(ObjectFactory factory) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		sender.setDevice(createDevice(factory));
		return sender;
	}
	/* -------------------- creation device ------------------------ */
	private MCCIMT000100UV01Device createDevice(ObjectFactory factory) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		MCCIMT000100UV01Device device = factory.createMCCIMT000100UV01Device();
		device.setClassCode(EntityClassDevice.DEV);
		device.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
		device.getId().add(createII(factory, null,null,null));
		return device;
	}
	/* -------------------- creation Gender ----------------------- */
	private CE createGender(ObjectFactory factory, Integer genderId) {
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
	/* -------------------creation CS ----------------------------- */
	private CS createCS(ObjectFactory factory, String value) {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		CS code = factory.createCS();
		code.setCode(value);
		return code;
	}
	/* ------------------ creation TS ----------------------------- */
	private TS createTS(ObjectFactory factory, String date) {
		TS ts = factory.createTS();
		ts.setValue(date);
		return ts;
	}
}
