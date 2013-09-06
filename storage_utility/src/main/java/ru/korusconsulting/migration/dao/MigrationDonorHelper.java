package ru.korusconsulting.migration.dao;

import java.util.HashMap;
import java.util.Map;

import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.migration.bean.CommonDonor;

public class MigrationDonorHelper {
	
	public Map<FieldsInMap,Object> mapFromDonor(CommonDonor donor) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		//map.put(FieldsInMap.FIRST_NAME, OMC_NUMBER);
		//map.put(FieldsInMap.FIRST_NAME, passport_number);
		map.put(FieldsInMap.PHONE, donor.getPhone());
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, donor.getWorkPhone());
		map.put(FieldsInMap.EMAIL, donor.getEmail());
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, donor.getBirth());
		map.put(FieldsInMap.GENDER, donor.getGender());
		map.put(FieldsInMap.FIRST_NAME, donor.getTemp_storage_id());
		return map;
	}

}
