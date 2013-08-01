package ru.korusconsulting.migration.dao;

import java.util.Date;

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
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150000UV02Organization;
import org.hl7.v3.CS;
import org.hl7.v3.EnFamily;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EnSuffix;
import org.hl7.v3.EntityClassOrganization;
import org.hl7.v3.EntityDeterminerSpecific;
import org.hl7.v3.II;
import org.hl7.v3.MFMIMT700721UV01Author2;
import org.hl7.v3.NullFlavor;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PN;
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
	
	public void makeQuery(String family, 
						  String givven, 
						  String suffix, 
						  String numberOMC, 
						  String numberPassport, 
						  String homePhone,
						  String address, 
						  String workPhone, 
						  String email, 
						  String employmentId, 
						  String birthDate) {
		TmisPdm service = new TmisPdm();
		PDManager pdm = service.getPortPdm();
		PRPAIN101311UV02 parameters = new PRPAIN101311UV02();
		/* -----------------set TimeCreation--------------------- */
		TS ts = new TS();
		ts.setValue(new Date().toString());
		parameters.setCreationTime(ts);
		/* ------------------------------------------------------ */
		/* ----------------set interaction Id-------------------- */
		parameters.setInteractionId(createII("2.16.840.1.113883.1.6", null, null));///////////////////////////////// перегенерировать
		/* ------------------------------------------------------ */
		/*--------------- set Processing Code-------------------- */
		CS pc = new CS();
		pc.setCode("P");
		parameters.setProcessingCode(pc);
		/*------------------------------------------------------- */
		/*------------ set processing Mode Code ----------------- */
		CS pmc = new CS();
		pmc.setCode("T");
		parameters.setProcessingModeCode(pmc);
		/* ------------------------------------------------------ */
		/* ------------------ set accept Ack Code --------------- */
		CS aac = new CS();
		aac.setCode("AL");
		parameters.setAcceptAckCode(aac);
		/* ------------------------------------------------------ */
		/* ------------- set Reciever --------------------------- */
		/* ------------------------------------------------------ */
		/* ------------- set Sender ----------------------------- */
		/* ------------------------------------------------------ */
		/* -------------- set controlActProcess ----------------- */
		PRPAIN101311UV02MFMIMT700721UV01ControlActProcess cap = new PRPAIN101311UV02MFMIMT700721UV01ControlActProcess();
		cap.setClassCode(ActClassControlAct.CACT);
		cap.setMoodCode(XActMoodIntentEvent.RQO);
			/* ---set values of subject--- */
			PRPAIN101311UV02MFMIMT700721UV01Subject1 subj = new PRPAIN101311UV02MFMIMT700721UV01Subject1();
			subj.setTypeCode(ActRelationshipHasSubject.SUBJ);
			subj.setRegistrationRequest(makeRegistartionRequest(family, givven, suffix, numberOMC, numberPassport, homePhone, address, workPhone, 
																email, employmentId, birthDate));
			/* -------------------------- */
		cap.setSubject(subj);
		parameters.setControlActProcess(cap);
		/* ------------------------------------------------------ */
		//try {
			System.out.println(pdm.add(parameters));
/*		} catch(Exception e) {
			System.out.println(e);
		}*/
		//System.out.println(parameters);
	}
	private PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest makeRegistartionRequest(String family, 
																						String givven, 
																						String suffix, 
																						String numberOMC, 
																						String numberPassport, 
																						String homePhone,
																						String address, 
																						String workPhone, 
																						String email, 
																						String employmentId, 
																						String birthDate) {
		PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest regReq = new PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest();
		regReq.setClassCode(ActClassRegistration.REG);
		regReq.setMoodCode(ActMoodRequest.RQO);
		/* --------------- set Status -------------- */
		CS statusCode = new CS();
		statusCode.setCode("active");
		regReq.setStatusCode(statusCode);
		/* ----------------------------------------- */
		/* ------------------------------------------------ set Autor -------------------------------------------------------- */
		MFMIMT700721UV01Author2 autor = new MFMIMT700721UV01Author2();
		autor.setTypeCode(ParticipationAuthorOriginator.AUT);
		/* -------------- set Asiigned Entity ------ */
		COCTMT090003UV01AssignedEntity assignEntity = new COCTMT090003UV01AssignedEntity();
		assignEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
		/* -------------- set Id of PD --------------*/
		assignEntity.getId().add(createII(null,null, NullFlavor.NI));
		/* ---------------end of ID for pd --------- */
		autor.setAssignedEntity(assignEntity);
		regReq.setAuthor(autor);
		/* ---------------------------------------- end of setting Autor -------- -------------------------------------------*/
		/* ---------------------------------------- set Subject1------------------------------------------------------------ */
		PRPAIN101311UV02MFMIMT700721UV01Subject2 subject1 = new PRPAIN101311UV02MFMIMT700721UV01Subject2();
		subject1.setTypeCode(ParticipationTargetSubject.SBJ);
		/* ------------ set identifiedPerson ------ */
		PRPAMT101301UV02IdentifiedPerson identifiedPerson = new PRPAMT101301UV02IdentifiedPerson();
		identifiedPerson.getId().add(createII(NUMBER_PASSPORT, numberPassport, null));
		identifiedPerson.getId().add(createII(OMC, numberOMC, null));
		/* --- end of setting  identifiedPerson --- */
		/* --- set StausCode ---------------------- */
		CS statusCodeIdPerson = new CS();
		statusCodeIdPerson.setCode("active");
		identifiedPerson.setStatusCode(statusCodeIdPerson);
		/* --- end of setting StatusCode----------- */
		/* --- setting identified person to Identified Person */
		identifiedPerson.setIdentifiedPerson(insertPD(family, givven, suffix, homePhone, null, workPhone, email, birthDate, address));
		/* --- end ofsetting identified person to Identified Person */
		/* --- setting assigningOrganization --- */
		COCTMT150000UV02Organization assignOrganization = new COCTMT150000UV02Organization();
		assignOrganization.setClassCode(EntityClassOrganization.ORG);
		assignOrganization.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
		assignOrganization.getId().add(createII(employmentId, null, null));
		identifiedPerson.setAssigningOrganization(assignOrganization);
		/* - end of setting assigningOrganization - */
		subject1.setIdentifiedPerson(identifiedPerson);
		regReq.setSubject1(subject1);
		regReq.setAuthor(makeAutor());
		/* ------------------------------------- end of setting Subject1 --------------------------------------------------- */
		return regReq;
	}
	private PRPAMT101301UV02Person insertPD(String family, 
											String given, 
											String suffix, 
											String homePhone, 
											String mobilePhone, 
											String workPhone, 
											String email, 
											String birthTime, 
											String adress) {
		PRPAMT101301UV02Person idPersonToIdPerson = new PRPAMT101301UV02Person();
		ObjectFactory factory = new ObjectFactory();
		PN name = factory.createPN();
		EnFamily fam = factory.createEnFamily();
		//fam.getContent().add(family);
		EnGiven giv = factory.createEnGiven();
		//giv.getContent().add(given);
		EnSuffix suff = factory.createEnSuffix();
		//suff.getContent().add(suffix);
		/* add name */
		name.getContent().add(/*factory.createENFamily(fam)*/family);
		name.getContent().add(/*factory.createENGiven(giv)*/given);
		name.getContent().add(/*factory.createENSuffix(suff)*/suffix);
		idPersonToIdPerson.getName().add(name);
		/* setting phones and email */
		idPersonToIdPerson.getTelecom().add(createTEL(HOME_PHONE + homePhone));
		idPersonToIdPerson.getTelecom().add(createTEL(MOBILE_PHONE + mobilePhone));
		idPersonToIdPerson.getTelecom().add(createTEL(WORKING_PHONE + workPhone));
		idPersonToIdPerson.getTelecom().add(createTEL(EMAIL + email));
		/* end of setting phones and email */
		TS birthT = new TS();
		birthT.setValue(birthTime);
		idPersonToIdPerson.setBirthTime(birthT);
		/* setting all adresses */
		idPersonToIdPerson.getAddr().add(makeAdress(factory, PostalAddressUse.H,
									null,
									adress, 
									null, 
									null,
									null));
		/* end of setting birthPlace */
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
			//adxpCity.getContent().add(city);
			adress.getContent().add(factory.createADCity(adxpCity));
		}
		if (!"".equals(streetAddressLine) && streetAddressLine != null) {
			AdxpStreetAddressLine adxpAddressLine = factory.createAdxpStreetAddressLine();
			//adxpAddressLine.getContent().add(streetAddressLine);
			adress.getContent().add(factory.createADStreetAddressLine(adxpAddressLine));
		}
		if (!"".equals(streetName) && streetName != null) {
			AdxpStreetName adxpStreetName = factory.createAdxpStreetName();
			//adxpStreetName.getContent().add(streetName);
			adress.getContent().add(factory.createADStreetName(adxpStreetName));
		}
		if (!"".equals(postalCode) && postalCode != null) {
			AdxpPostalCode adxpPostalCode = factory.createAdxpPostalCode();
			//adxpPostalCode.getContent().add(postalCode);
			adress.getContent().add(factory.createADPostalCode(adxpPostalCode));
		}
		if (!"".equals(state) && state != null) {
			AdxpState adxpState = factory.createAdxpState();
			//adxpState.getContent().add(state);
			adress.getContent().add(factory.createADState(adxpState));			
		}
		return adress;
	}
	/* --------------- method for creation autor ------------------- */
	private MFMIMT700721UV01Author2 makeAutor() {
		MFMIMT700721UV01Author2 autor = new MFMIMT700721UV01Author2();
		autor.setTypeCode(ParticipationAuthorOriginator.AUT);
		COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
		assignedEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
		assignedEntity.getId().add(createII(null,null, NullFlavor.NI));
		autor.setAssignedEntity(assignedEntity);
		return autor;
	}
	/* --------------------- creation id ------------------------- */
	private II createII(String root, String extension, NullFlavor nullFlavor) {
		II id = new II();
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
	private TEL createTEL(String value) {
		TEL tel = new TEL();
		if (!"".equals(value) && value != null) {
			tel.setValue(value);
		}
		return tel;
	}
}
