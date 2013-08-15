package ru.korusconsulting.SRPD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hl7.v3.PRPAIN101306UV02;
import org.hl7.v3.PRPAIN101312UV02;

import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;

public class DonorHelper {
	public enum FieldsInMap {
		FIRST_NAME,
		LAST_NAME ,
		MIDDLE_NAME,
		OMC_NUMBER,
		PASSPORT_NUMBER,
		PHONE,
		ADRESS,
		WORK_PHONE,
		EMAIL,
		EMPLOYMENT,
		BIRTH ,
		GENDER,
		TEMP_STOGATE_ID
	}
	
	private final static String FEMALE = "F";
	/* flag for using or unusing SRPD */
	public final static Boolean USE_SRPD = false; 
	
	public Map<FieldsInMap, Object> makeMapFromDonor(Donor donor) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.FIRST_NAME, donor.getLastName());
		map.put(FieldsInMap.FIRST_NAME, donor.getMiddleName());
		//map.put(FieldsInMap.FIRST_NAME, OMC_NUMBER);
		//map.put(FieldsInMap.FIRST_NAME, passport_number);
		map.put(FieldsInMap.FIRST_NAME, donor.getPhone());
		map.put(FieldsInMap.FIRST_NAME, donor.getRegistrationAddress());
		map.put(FieldsInMap.FIRST_NAME, donor.getWorkPhone());
		map.put(FieldsInMap.FIRST_NAME, donor.getMail());
		map.put(FieldsInMap.FIRST_NAME, donor.getEmployment());
		map.put(FieldsInMap.FIRST_NAME, donor.getBirth());
		map.put(FieldsInMap.FIRST_NAME, donor.getGender());
		//map.put(FieldsInMap.FIRST_NAME, donor.getTempStogateId());
		return map;
	}
	
	public Donor mergeDonorAndMap(Donor donor, Map<FieldsInMap, Object> mapWithValues) {
		donor.setFirstName(mapWithValues.get(FieldsInMap.FIRST_NAME).toString());
		donor.setLastName(mapWithValues.get(FieldsInMap.LAST_NAME).toString());
		donor.setMiddleName(mapWithValues.get(FieldsInMap.MIDDLE_NAME).toString());
		//donor.setInsuranceNumber(insuranceNumber);
		//donor.setInsuranceSeries(insuranceSeries);
		//donor.setPassportNumber(passportNumber);
		//donor.setPassportSeries(passportSeries);
		donor.setPhone(parseTelcom(mapWithValues.get(FieldsInMap.PHONE).toString()));
		donor.setRegistrationAddress(mapWithValues.get(FieldsInMap.ADRESS).toString());
		donor.setWorkPhone(parseTelcom(mapWithValues.get(FieldsInMap.WORK_PHONE).toString()));
		donor.setMail(parseTelcom(mapWithValues.get(FieldsInMap.EMAIL).toString()));
		donor.setEmployment(mapWithValues.get(FieldsInMap.EMPLOYMENT).toString());
		donor.setBirth(createDate(mapWithValues.get(FieldsInMap.FIRST_NAME).toString()));
		donor.setGender(createGender(mapWithValues.get(FieldsInMap.GENDER).toString()));
		//donor.setTempStogateId(mapWithValues.get(FieldsInMap.TEMP_STOGATE_ID).toString());*/
		return donor;
	}
	public Map<FieldsInMap, Object> makeMapFromDonor(BiomaterialDonor donor) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.FIRST_NAME, donor.getLastName());
		map.put(FieldsInMap.FIRST_NAME, donor.getMiddleName());
		//map.put(FieldsInMap.FIRST_NAME, OMC_NUMBER);
		//map.put(FieldsInMap.FIRST_NAME, passport_number);
		map.put(FieldsInMap.FIRST_NAME, donor.getPhone());
		map.put(FieldsInMap.FIRST_NAME, donor.getRegistrationAddress());
		map.put(FieldsInMap.FIRST_NAME, donor.getWorkPhone());
		map.put(FieldsInMap.FIRST_NAME, donor.getEmployment());
		map.put(FieldsInMap.FIRST_NAME, donor.getBirth());
		map.put(FieldsInMap.FIRST_NAME, donor.getGender());
		//map.put(FieldsInMap.FIRST_NAME, donor.getTempStogateId());
		return map;
	}
	
	public BiomaterialDonor mergeDonorAndMap(BiomaterialDonor donor, Map<FieldsInMap, Object> mapWithValues) {
		donor.setFirstName(mapWithValues.get(FieldsInMap.FIRST_NAME).toString());
		donor.setLastName(mapWithValues.get(FieldsInMap.LAST_NAME).toString());
		donor.setMiddleName(mapWithValues.get(FieldsInMap.MIDDLE_NAME).toString());
		//donor.setInsuranceNumber(insuranceNumber);
		//donor.setInsuranceSeries(insuranceSeries);
		//donor.setPassportNumber(passportNumber);
		//donor.setPassportSeries(passportSeries);
		donor.setPhone(parseTelcom(mapWithValues.get(FieldsInMap.PHONE).toString()));
		donor.setRegistrationAddress(mapWithValues.get(FieldsInMap.ADRESS).toString());
		donor.setWorkPhone(parseTelcom(mapWithValues.get(FieldsInMap.WORK_PHONE).toString()));
		donor.setEmployment(mapWithValues.get(FieldsInMap.EMPLOYMENT).toString());
		donor.setBirth(createDate(mapWithValues.get(FieldsInMap.FIRST_NAME).toString()));
		donor.setGender(createGender(mapWithValues.get(FieldsInMap.GENDER).toString()));
		//donor.setTempStogateId(mapWithValues.get(FieldsInMap.TEMP_STOGATE_ID).toString());*/
		return donor;
	}
		
	public Map<FieldsInMap, Object> parseAnswerFromSRPDAfterFind(PRPAIN101306UV02 answer) {
		Map<FieldsInMap, Object> answerMap = null;
		return answerMap;
	}
	public Map<FieldsInMap, Object> parseAnswerFromSRPDAfterAdd(PRPAIN101312UV02 answer) {
		Map<FieldsInMap, Object> answerMap = null;
		return answerMap;
	}
	/* creation of birhdate */
	private Date createDate(Object valueDate) {
		Date date = null;
		String pattern = "yyyyMMdd";
		try {
			date = new SimpleDateFormat(pattern).parse(valueDate.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	private Integer createGender(String gender) {
		if (FEMALE.equals(gender)) {
			return 0;
		} else {
			return 1;
		}
	}
	/* Creation free Telcom values */
	private String parseTelcom(String value) {
		String resValue = null;
		int index = value.indexOf(":");
		resValue = value.substring(index+1);
		return resValue;
	}

}
