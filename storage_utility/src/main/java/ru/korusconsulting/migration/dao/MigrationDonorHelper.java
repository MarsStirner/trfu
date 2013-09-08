package ru.korusconsulting.migration.dao;

import java.util.HashMap;
import java.util.Map;

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

}
