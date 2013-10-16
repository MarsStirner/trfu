package ru.korusconsulting.SRPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.PRPAIN101306UV02;
import org.hl7.v3.PRPAIN101312UV02;

import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter;
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
		/* !!не использовать для задания условий выборки!! */
		TEMP_STORAGE_ID,
		LIST_STORAGE_IDS
	}
	private enum TELCOM {
		TEL,
		EMAIL
	}
	private final static String FEMALE = "F";
	/* flag for using or unusing SRPD */
	public final static Boolean USE_SRPD = true;
	protected final String pattern = "yyyyMMdd";
	
	/** creation Map from {@link DonorsFilter}, which will be contain {@link List}s of parameters for search */
	public Map<FieldsInMap, Object> makeMapFromDonorsFilter(DonorsFilter donorsFilter, List<Donor> donors) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap,Object>();
		List<String> listIds = new ArrayList<String>();
		for (Donor i: donors) {
			listIds.add(i.getTempStorageId());
		}
		map.put(FieldsInMap.LIST_STORAGE_IDS, listIds);
		map.put(FieldsInMap.FIRST_NAME, donorsFilter.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donorsFilter.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donorsFilter.getMiddleName());
		/*map.put(FieldsInMap.OMC_NUMBER, donor.getInsuranceNumber() + " " + donor.getInsuranceSeries());
		map.put(FieldsInMap.PASSPORT_NUMBER, donor.getPassportNumber() + " " + donor.getPassportSeries());
		map.put(FieldsInMap.ADRESS, donorsFilter.getRegistrationAdress());*/
		return map;
	}
	
	public Map<FieldsInMap, Object> makeMapFromDonor(Donor donor) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		map.put(FieldsInMap.OMC_NUMBER, donor.getInsuranceNumber() + " " + donor.getInsuranceSeries());
		map.put(FieldsInMap.PASSPORT_NUMBER, donor.getPassportNumber() + " " + donor.getPassportSeries());
		map.put(FieldsInMap.PHONE, cretateTelFromPhoneForSRPD(TELCOM.TEL, donor.getPhone()));
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, cretateTelFromPhoneForSRPD(TELCOM.TEL, donor.getWorkPhone()));
		map.put(FieldsInMap.EMAIL, cretateTelFromPhoneForSRPD(TELCOM.EMAIL,donor.getMail()));
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, createStringFromDate(donor.getBirth()));
		map.put(FieldsInMap.GENDER, ((Integer)donor.getGender()).toString());
		map.put(FieldsInMap.TEMP_STORAGE_ID, donor.getTempStorageId());
		return map;
	}
	
	public Map<FieldsInMap, Object> makeMapForGet(String idDonorFromSRPD) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		List<String> listIds = new ArrayList<String>();
		listIds.add(idDonorFromSRPD);
		map.put(FieldsInMap.LIST_STORAGE_IDS, listIds);
		return map;
	}
	
	public Donor mergeDonorAndMap(Donor donor, Map<FieldsInMap, Object> mapWithValues) {
		donor.setFirstName((String)mapWithValues.get(FieldsInMap.FIRST_NAME));
		donor.setLastName((String)mapWithValues.get(FieldsInMap.LAST_NAME));
		donor.setMiddleName((String)mapWithValues.get(FieldsInMap.MIDDLE_NAME));
		if (mapWithValues.get(FieldsInMap.OMC_NUMBER) != null) {
			String[] insurance = ((String)mapWithValues.get(FieldsInMap.OMC_NUMBER)).split(" ");
			if (insurance.length > 1) {
				donor.setInsuranceNumber(insurance[0]);
				donor.setInsuranceSeries(insurance[1]);
			}
		}
		if (mapWithValues.get(FieldsInMap.PASSPORT_NUMBER) != null) {
			String[] passport = ((String)mapWithValues.get(FieldsInMap.PASSPORT_NUMBER)).split(" ");
			if (passport.length > 1) {
				donor.setPassportNumber(passport[0]);
				donor.setPassportSeries(passport[1]);
			}
		}
		if (mapWithValues.get(FieldsInMap.PHONE) != null) {
			donor.setPhone(parseTelcom(((String)mapWithValues.get(FieldsInMap.PHONE))));
		}
		donor.setRegistrationAddress(((String)mapWithValues.get(FieldsInMap.ADRESS)));
		if (mapWithValues.get(FieldsInMap.WORK_PHONE) != null) {
			donor.setWorkPhone(parseTelcom((String)mapWithValues.get(FieldsInMap.WORK_PHONE)));
		}
		if (mapWithValues.get(FieldsInMap.EMAIL) != null) {
			donor.setMail(parseTelcom((String)mapWithValues.get(FieldsInMap.EMAIL)));
		}
			donor.setEmployment((String)mapWithValues.get(FieldsInMap.EMPLOYMENT));
		if (mapWithValues.get(FieldsInMap.BIRTH) != null) {
			donor.setBirth(createDate(mapWithValues.get(FieldsInMap.BIRTH)));
		}
		if (mapWithValues.get(FieldsInMap.GENDER) != null) {
			donor.setGender(createGender((String)mapWithValues.get(FieldsInMap.GENDER)));
		}
			donor.setTempStorageId((String)mapWithValues.get(FieldsInMap.TEMP_STORAGE_ID));
		return donor;
	}
	
	public List<Donor> mergeDonorsAndMap(List<Donor> donors, Map<String, Map<FieldsInMap, Object>> map) {
		Map<FieldsInMap, Object> values;
		for(Donor i: donors) {
			values = map.get(i.getTempStorageId());
			i = mergeDonorAndMap(i, values);
		}
		return donors;
	}
	public List<BiomaterialDonor> mergeBiomaterialDonorsAndMap(List<BiomaterialDonor> donors, Map<String, Map<FieldsInMap, Object>> map) {
		Map<FieldsInMap, Object> values;
		for(BiomaterialDonor i: donors) {
			values = map.get(i.getTempStorageId());
			i = mergeDonorAndMap(i, values);
		}
		return donors;
	}
	public List<Operation> mergeOperationsAndMap(List<Operation> operations, Map<String, Map<FieldsInMap, Object>> map) {
		Map<FieldsInMap, Object> values;
		for(Operation i: operations) {
			values = map.get(i.getDonor().getTempStorageId());
			i.setDonor(mergeDonorAndMap(i.getDonor(), values));
		}
		return operations;
	}
	public List<Biomaterial> mergeBiomaterialsAndMap(List<Biomaterial> biomaterials, Map<String, Map<FieldsInMap, Object>> map) {
		Map<FieldsInMap, Object> values;
		for(Biomaterial i: biomaterials) {
			values = map.get(i.getOperation().getDonor().getTempStorageId());
			i.getOperation().setDonor(mergeDonorAndMap(i.getOperation().getDonor(), values));
		}
		return biomaterials;
	}
	public Map<FieldsInMap, Object> makeMapFromDonor(BiomaterialDonor donor) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		List<String> listIds = new ArrayList<String>();
		listIds.add(donor.getTempStorageId());
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		map.put(FieldsInMap.OMC_NUMBER, donor.getInsuranceNumber() + " " + donor.getInsuranceSeries());
		map.put(FieldsInMap.PASSPORT_NUMBER, donor.getPassportNumber() + " " + donor.getPassportSeries());
		map.put(FieldsInMap.PHONE, cretateTelFromPhoneForSRPD(TELCOM.TEL, donor.getPhone()));
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, cretateTelFromPhoneForSRPD(TELCOM.TEL, donor.getWorkPhone()));
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, createStringFromDate(donor.getBirth()));
		map.put(FieldsInMap.GENDER, ((Integer)donor.getGender()).toString());
		map.put(FieldsInMap.TEMP_STORAGE_ID, donor.getTempStorageId());
		//map.put(FieldsInMap.LIST_STORAGE_IDS, listIds);
		return map;
	}
	
	public BiomaterialDonor mergeDonorAndMap(BiomaterialDonor donor, Map<FieldsInMap, Object> mapWithValues) {
		donor.setFirstName((String)mapWithValues.get(FieldsInMap.FIRST_NAME));
		donor.setLastName((String)mapWithValues.get(FieldsInMap.LAST_NAME));
		donor.setMiddleName((String)mapWithValues.get(FieldsInMap.MIDDLE_NAME));
		if (mapWithValues.get(FieldsInMap.OMC_NUMBER) != null) {
			String[] insurance = ((String)mapWithValues.get(FieldsInMap.OMC_NUMBER)).split(" ");
			if (insurance.length > 1) {
				donor.setInsuranceNumber(insurance[0]);
				donor.setInsuranceSeries(insurance[1]);
			}
		}
		if (mapWithValues.get(FieldsInMap.PASSPORT_NUMBER) != null) {
			String[] passport = ((String)mapWithValues.get(FieldsInMap.PASSPORT_NUMBER)).split(" ");
			if (passport.length > 1) {
				donor.setPassportNumber(passport[0]);
				donor.setPassportSeries(passport[1]);
			}
		}
		if (mapWithValues.get(FieldsInMap.PHONE) != null) {
			donor.setPhone(parseTelcom((String)mapWithValues.get(FieldsInMap.PHONE)));
		}
		donor.setRegistrationAddress((String)mapWithValues.get(FieldsInMap.ADRESS));
		donor.setWorkPhone((String)mapWithValues.get(FieldsInMap.WORK_PHONE));
		donor.setEmployment((String)mapWithValues.get(FieldsInMap.EMPLOYMENT));
		if (mapWithValues.get(FieldsInMap.BIRTH) != null) {
			donor.setBirth(createDate((String)mapWithValues.get(FieldsInMap.BIRTH)));
		}
		if (mapWithValues.get(FieldsInMap.GENDER) != null) {
			donor.setGender(createGender((String)mapWithValues.get(FieldsInMap.GENDER)));
		}		
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
	/* creation of birh date */
	private Date createDate(Object valueDate) {
		Date date = null;
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
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		map.put(FieldsInMap.LIST_STORAGE_IDS, donorsSRPDids);
		return map;
	}
	
	public List<String> getMailsFromMap(Map<String, Map<FieldsInMap,Object>> listMaps) {
		List<String> list = new ArrayList<String>();
		String mail;
		for(Map<FieldsInMap, Object> i : listMaps.values()) {
			mail = i.get(FieldsInMap.EMAIL).toString();
			if(mail != null && StringUtils.isNotEmpty(mail)) {
				list.add(mail);
			}
		}
		return list;
	}
	/**
	 * Используется для мержа данных из ЗХПД и списка объектов типа BloodDonationRequest.
	 * Место использования: BloodDonationRequestDAOImpl
	 */
	public List<BloodDonationRequest> mergeListBloodDonationRequestAndMap(List<BloodDonationRequest> list, Map<Integer, Map<FieldsInMap, Object>> map) {
		for(BloodDonationRequest i : list) {
			i.setDonor(mergeDonorAndMap(i.getDonor(), map.get(i.getDonor().getTempStorageId())));
		}
		return list;
	}
	/**
	 * Используется для мержа данных из ЗХПД и списка объектов типа BloodComponent.
	 * Место использования: BloodComponentDAOImpl
	 */
	public List<BloodComponent> mergeListBloodComponentAndMap(List<BloodComponent> list, Map<String, Map<FieldsInMap, Object>> map) {
		for(BloodComponent i : list) {
			i.getDonation().setDonor(mergeDonorAndMap(i.getDonation().getDonor(), map.get(i.getDonation().getDonor().getTempStorageId())));
		}
		return list;
	}
	/**
	 * Используется для создания Map, для дальнейшей передачи в Обработчик ЗХПД-клиента
	 * Место использования: BloodDonationRequestDAOImpl, MedicalOperationDAOImpl, DonorDAOImpl
	 */
	public Map<FieldsInMap,Object> listIdsDonorsForFilter(AppendSRPDFilter filter) {
		Map<FieldsInMap, Object> mapForSearch = new HashMap<FieldsInMap, Object>();
		String firstName = filter.getFirstName();
		String lastName = filter.getLastName();
		String middleName = filter.getMiddleName();
		String passportSeries = filter.getPassportSeries();
		String passportNumber = filter.getPassportNumber();
		String insuranceSeries = filter.getInsuranceSeries();
		String insuranceNumber = filter.getInsuranceNumber();
		String employment = filter.getEmployment();
		String workPhone = filter.getWorkPhone();
		String phone = filter.getPhone();
		String registrationAdress = filter.getRegistrationAdress();
		Collection<String> listIds = filter.getListSRPDIds();
		if (StringUtils.isNotEmpty(lastName)) {
			mapForSearch.put(FieldsInMap.LAST_NAME, lastName);
		}
		if (StringUtils.isNotEmpty(middleName)) {
			mapForSearch.put(FieldsInMap.MIDDLE_NAME, middleName);
		}
		if (StringUtils.isNotEmpty(firstName)) {
			mapForSearch.put(FieldsInMap.FIRST_NAME, firstName);
		}
		if (StringUtils.isNotEmpty(passportSeries) && StringUtils.isNotEmpty(passportNumber)) {
			//mapForSearch.put(FieldsInMap.PASSPORT_NUMBER, passportSeries + passportNumber);
		}
		if (StringUtils.isNotEmpty(insuranceSeries) && StringUtils.isNotEmpty(insuranceNumber)) {
			//mapForSearch.put(FieldsInMap.OMC_NUMBER, insuranceSeries + insuranceSeries);
		}
		if (StringUtils.isNotEmpty(employment)) {
			mapForSearch.put(FieldsInMap.EMPLOYMENT, employment);
		}
		if (StringUtils.isNotEmpty(workPhone)) {
			mapForSearch.put(FieldsInMap.WORK_PHONE, workPhone);
		}
		if (StringUtils.isNotEmpty(phone)) {
			mapForSearch.put(FieldsInMap.PHONE, phone);
		}
		if (StringUtils.isNotEmpty(registrationAdress)) {
			mapForSearch.put(FieldsInMap.ADRESS, registrationAdress);
		}
		if (listIds != null) {
			mapForSearch.put(FieldsInMap.LIST_STORAGE_IDS, listIds);
		}
		return mapForSearch;
	}
	/**
	 * Формирует и возвращает список идентификаторов ЗХПД из списка Donor-ов
	 * Используется: MedicalOperationDAOImpl
	 */
	public List<String> listIdsSRPDFromDonors(List<Donor> donors) {
		List<String> ids = new ArrayList<String>();
		for (Donor i : donors) {
			ids.add(i.getTempStorageId());
		}
		return ids;
	}
	/**
	 * Формирует и возвращает список идентификаторов ЗХПД из списка BiomaterialDonor-ов
	 * Используется: MedicalOperationDAOImpl
	 */
	public List<String> listIdsSRPDFromBiomaterialDonors(List<BiomaterialDonor> donors) {
		List<String> ids = new ArrayList<String>();
		for (BiomaterialDonor i : donors) {
			ids.add(i.getTempStorageId());
		}
		return ids;
	}
	/**
	 * Формирует и возвращает список идентификаторов ЗХПД из списка Operation
	 * Используется: MedicalOperationDAOImpl
	 */
	public List<String> listIdsSRPDFromOperation(List<Operation> operations) {
		List<String> ids = new ArrayList<String>();
		for (Operation i : operations) {
			ids.add(i.getDonor().getTempStorageId());
		}
		return ids;
	}
	/**
	 * Формирует и возвращает список идентификаторов ЗХПД из списка Biomaterial-ов
	 * Используется: MedicalOperationDAOImpl
	 */
	public List<String> listIdsSRPDFromBiomaterial(List<Biomaterial> biomaterials) {
		List<String> ids = new ArrayList<String>();
		for (Biomaterial i : biomaterials) {
			ids.add(i.getOperation().getDonor().getTempStorageId());
		}
		return ids;
	}
	/* for creation url to file*/
	public static Properties createPropertiesForURL(String url) {
		Properties prop = null;
		try {
			File file = null;
			prop = new Properties();
			try {
				file = new File(url);
				prop.load(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				file = new File("src/main/resources/" + url);
				prop.load(new FileInputStream(file));
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
			return prop;
	}
	/** Метод для создания и записи даты по одному формату  */
	public String createStringFromDate(Date date) {
		if (date != null) {
			DateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}
	
	public String cretateTelFromPhoneForSRPD(TELCOM type, String tel) {
		switch (type) {
		case TEL:
			return "tel:" + tel;

		case EMAIL:
			return "mailto:" + tel;
		}
	return null;
	}
}
