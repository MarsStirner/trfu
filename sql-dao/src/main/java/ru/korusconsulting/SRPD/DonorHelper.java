package ru.korusconsulting.SRPD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.PRPAIN101306UV02;
import org.hl7.v3.PRPAIN101312UV02;

import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;

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
		TEMP_STORAGE_ID,
		LIST_STORAGE_IDS
	}
	
	private final static String FEMALE = "F";
	/* flag for using or unusing SRPD */
	public final static Boolean USE_SRPD = false;
	
	/** create {@link Map} of {@link Map}s from result of search in SRPD */
	public Map<Integer,Map<DonorHelper.FieldsInMap, Object>> makeMapsFromAnswerAfterSearch(PRPAIN101306UV02 answer) {
		Map<Integer,Map<DonorHelper.FieldsInMap, Object>> map = null;
		return map;
	}
	/** creation Map from {@link DonorsFilter}, which will be contain {@link List}s of parameters for search */
	public Map<DonorHelper.FieldsInMap, Object> makeMapFromDonorsFilter(DonorsFilter donorsFilter, List<Donor> donors) {
		Map<DonorHelper.FieldsInMap, Object> map = new HashMap<FieldsInMap,Object>();
		List<Integer> listIds = new ArrayList<Integer>();
		for (Donor i: donors) {
			listIds.add(i.getTempStorageId());
		}
		map.put(FieldsInMap.LIST_STORAGE_IDS, listIds);
		map.put(FieldsInMap.FIRST_NAME, donorsFilter.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donorsFilter.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donorsFilter.getMiddleName());
		//map.put(FieldsInMap.FIRST_NAME, OMC_NUMBER);
		//map.put(FieldsInMap.FIRST_NAME, passport_number);
		//map.put(FieldsInMap.ADRESS, donorsFilter.getRegistrationAddress());
		return map;
	}
	
	public Map<FieldsInMap, Object> makeMapFromDonor(Donor donor) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		//map.put(FieldsInMap.FIRST_NAME, OMC_NUMBER);
		//map.put(FieldsInMap.FIRST_NAME, passport_number);
		map.put(FieldsInMap.PHONE, donor.getPhone());
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, donor.getWorkPhone());
		map.put(FieldsInMap.EMAIL, donor.getMail());
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, donor.getBirth());
		map.put(FieldsInMap.GENDER, donor.getGender());
		map.put(FieldsInMap.FIRST_NAME, donor.getTempStorageId());
		return map;
	}
	
	public Map<FieldsInMap, Object> makeMapForGet(Integer idDonorFromSRPD) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		map.put(FieldsInMap.TEMP_STORAGE_ID, idDonorFromSRPD);
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
		donor.setBirth(createDate(mapWithValues.get(FieldsInMap.BIRTH).toString()));
		donor.setGender(createGender(mapWithValues.get(FieldsInMap.GENDER).toString()));
		donor.setTempStorageId(Integer.parseInt(mapWithValues.get(FieldsInMap.TEMP_STORAGE_ID).toString()));
		return donor;
	}
	
	public List<Donor> mergeDonorsAndMap(List<Donor> donors, Map<Integer, Map<FieldsInMap, Object>> map) {
		Map<FieldsInMap, Object> values;
		for(Donor i: donors) {
			values = map.get(i.getTempStorageId());
			mergeDonorAndMap(i, values);
		}
		return donors;
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
		map.put(FieldsInMap.FIRST_NAME, donor.getTempStorageId());
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
		donor.setTempStorageId(Integer.parseInt(mapWithValues.get(FieldsInMap.TEMP_STORAGE_ID).toString()));
		
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
	
	public Map<FieldsInMap, Object> makeParametersforSearchMails(List<Integer> donorsSRPDids) {
		Map<FieldsInMap, Object> map = new HashMap<DonorHelper.FieldsInMap, Object>();
		map.put(FieldsInMap.LIST_STORAGE_IDS, donorsSRPDids);
		return map;
	}
	
	public List<String> getMailsFromMap(Map<Integer, Map<FieldsInMap,Object>> map) {
		List<String> list = new ArrayList<String>();
		String mail;
		for(Map<FieldsInMap, Object> i : map.values()) {
			mail = i.get(FieldsInMap.EMAIL).toString();
			if(mail != null && StringUtils.isNotEmpty(mail)) {
				list.add(mail);
			}
		}
		return list;
	}

}
