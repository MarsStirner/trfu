package ru.korusconsulting.migration.dao;

import java.io.IOException;
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
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150000UV02Organization;
import org.hl7.v3.COCTMT710007UV07Place;
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
import org.hl7.v3.PRPAMT101301UV02BirthPlace;
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
	public void makeQuery() {
		TmisPdm service = new TmisPdm();
		PDManager pdm = service.getPortPdm();
		PRPAIN101311UV02 parameters = new PRPAIN101311UV02();
		/* -----------------set TimeCreation--------------------- */
		TS ts = new TS();
		ts.setValue(new Date().toString());
		parameters.setCreationTime(ts);
		/* ------------------------------------------------------ */
		/* ----------------set interaction Id-------------------- */
		II ii = new II();
		ii.setRoot("2.16.840.1.113883.1.6");///////////////////////////////// перегенерировать
		parameters.setInteractionId(ii);
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
			subj.setRegistrationRequest(makeRegistartionRequest());
			/* -------------------------- */
		cap.setSubject(subj);
		parameters.setControlActProcess(cap);
		/* ------------------------------------------------------ */
		System.out.println("run method");
		try {
			System.out.println(pdm.add(parameters));
		} catch(Exception e) {
			System.out.println(e);
		}
		//System.out.println(parameters);
	}
	private PRPAIN101311UV02MFMIMT700721UV01RegistrationRequest makeRegistartionRequest() {
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
		II idAssignEntity = new II();
		idAssignEntity.setNullFlavor(NullFlavor.NI);
		assignEntity.getId().add(idAssignEntity);
		/* ---------------end of ID for pd --------- */
		autor.setAssignedEntity(assignEntity);
		regReq.setAuthor(autor);
		/* ---------------------------------------- end of setting Autor -------- -------------------------------------------*/
		/* ---------------------------------------- set Subject1------------------------------------------------------------ */
		PRPAIN101311UV02MFMIMT700721UV01Subject2 subject1 = new PRPAIN101311UV02MFMIMT700721UV01Subject2();
		subject1.setTypeCode(ParticipationTargetSubject.SBJ);
		/* ------------ set identifiedPerson ------ */
		PRPAMT101301UV02IdentifiedPerson identifiedPerson = new PRPAMT101301UV02IdentifiedPerson();
		II numberPassport = new II();
		numberPassport.setExtension("123456");
		numberPassport.setRoot("OID_НОМЕР_ПАСПОРТА");
		identifiedPerson.getId().add(numberPassport);
		/* --- end of setting  identifiedPerson --- */
		/* --- set StausCode ---------------------- */
		CS statusCodeIdPerson = new CS();
		statusCodeIdPerson.setCode("active");
		identifiedPerson.setStatusCode(statusCodeIdPerson);
		/* --- end of setting StatusCode----------- */
		/* --- setting identified person to Identified Person */
		identifiedPerson.setIdentifiedPerson(insertPD("Петров", "Пётр", "Петрович", "454545", "4545454", "545454", "petr@email.ru", "25/05/1967"));
		/* --- end ofsetting identified person to Identified Person */
		/* --- setting assigningOrganization --- */
		COCTMT150000UV02Organization assignOrganization = new COCTMT150000UV02Organization();
		assignOrganization.setClassCode(EntityClassOrganization.ORG);
		assignOrganization.setDeterminerCode(EntityDeterminerSpecific.INSTANCE);
		II idAssignOrg = new II();
		idAssignOrg.setRoot("ORGNAME");// NAME OF ORGANIZATION
		assignOrganization.getId().add(idAssignOrg);
		identifiedPerson.setAssigningOrganization(assignOrganization);
		/* - end of setting assigningOrganization - */
		subject1.setIdentifiedPerson(identifiedPerson);
		regReq.setSubject1(subject1);
		regReq.setAuthor(makeAutor());
		/* ------------------------------------- end of setting Subject1 --------------------------------------------------- */
		return regReq;
	}
	private PRPAMT101301UV02Person insertPD(String family, String given, String suffix, String homePhone, String mobilePhone, String workPhone,
												String email, String birthTime/*, Map<PostalAddressUse, String[]> adresses, String[] brthPlace*/) {
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
		name.getContent().add(factory.createENFamily(fam));
		name.getContent().add(factory.createENGiven(giv));
		name.getContent().add(factory.createENSuffix(suff));
		idPersonToIdPerson.getName().add(name);
		/* setting phones and email */
		TEL telHome = new TEL();
		TEL telMobile = new TEL();
		TEL telWork = new TEL();
		TEL telEmail = new TEL();
		telHome.setValue("tel:" + homePhone);
		telMobile.setValue("cell-tel:" + mobilePhone);
		telWork.setValue("working-office-tel:" + workPhone);
		telEmail.setValue("tel:" + email);
		idPersonToIdPerson.getTelecom().add(telHome);
		idPersonToIdPerson.getTelecom().add(telMobile);
		idPersonToIdPerson.getTelecom().add(telWork);
		idPersonToIdPerson.getTelecom().add(telEmail);
		/* end of setting phones and email */
		TS birthT = new TS();
		birthT.setValue(birthTime);
		idPersonToIdPerson.setBirthTime(birthT);
		/* setting all adresses */
		//for(String i: typeAddress) {
		idPersonToIdPerson.getAddr().add(makeAdress(factory, PostalAddressUse.H,
									"САНКТ-ПЕТЕРБУРГ",
									"199178, Россия, Санкт-Петербург, Малый пр. В.О. 54, корп. 4", 
									"Малый пр. В.О.", 
									"199178",
									"САНКТ-ПЕТЕРБУРГ"));
		//}
		/* setting birthPlace */
		COCTMT710007UV07Place place = new COCTMT710007UV07Place();
		place.setAddr(makeAdress(factory, null, "САНКТ-ПЕТЕРБУРГ", null, null, null, null));
		PRPAMT101301UV02BirthPlace birthPlace = new PRPAMT101301UV02BirthPlace();
		birthPlace.setBirthplace(place);
		idPersonToIdPerson.setBirthPlace(birthPlace);
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
		/* setting id */
		II idAssigneEntity = new II();
		idAssigneEntity.setNullFlavor(NullFlavor.NI);
		assignedEntity.getId().add(idAssigneEntity);
		/* end of setting id */
		autor.setAssignedEntity(assignedEntity);
		return autor;
	}
}
