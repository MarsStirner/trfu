package ru.korusconsulting.migration.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;

public class MigrationDonorHelper {
	
	public Map<FieldsInMap,Object> mapFromDonor(CommonDonor donor) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		map.put(FieldsInMap.OMC_NUMBER, donor.getInsuranceNumber() + " " + donor.getInsuranceSeries());
		map.put(FieldsInMap.PASSPORT_NUMBER, donor.getPassportNumber() + " " + donor.getPassportSeries());
		map.put(FieldsInMap.PHONE, donor.getPhone());
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, donor.getWorkPhone());
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, donor.getBirth());
		map.put(FieldsInMap.GENDER, donor.getGender());
		map.put(FieldsInMap.TEMP_STORAGE_ID, donor.getTemp_storage_id());
		if (donor instanceof Donor) {
			map.put(FieldsInMap.EMAIL, ((Donor)donor).getMail());
		}
		return map;
	}
	
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
	public static Map<FieldsInMap, Object> createMapForListIds(List<String> ids) {
		Map<FieldsInMap, Object> params = new HashMap<DonorHelper.FieldsInMap, Object>();
		params.put(FieldsInMap.LIST_STORAGE_IDS, ids);
		return params;
	}

}
