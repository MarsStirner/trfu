package ru.korusconsulting.migration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;

public class MigrationDonorHelper extends DonorHelper {
	
	public Map<FieldsInMap,Object> mapFromDonor(CommonDonor donor) {
		Map<FieldsInMap, Object> map = new HashMap<FieldsInMap, Object>();
		map.put(FieldsInMap.FIRST_NAME, donor.getFirstName());
		map.put(FieldsInMap.LAST_NAME, donor.getLastName());
		map.put(FieldsInMap.MIDDLE_NAME, donor.getMiddleName());
		map.put(FieldsInMap.OMC_NUMBER, donor.getInsuranceNumber() + " " + donor.getInsuranceSeries());
		map.put(FieldsInMap.PASSPORT_NUMBER, donor.getPassportNumber() + " " + donor.getPassportSeries());
		map.put(FieldsInMap.PHONE, createStringFromPhone(donor.getPhone()));
		map.put(FieldsInMap.ADRESS, donor.getRegistrationAddress());
		map.put(FieldsInMap.WORK_PHONE, createStringFromPhone(donor.getWorkPhone()));
		map.put(FieldsInMap.EMPLOYMENT, donor.getEmployment());
		map.put(FieldsInMap.BIRTH, createStringFromDate(donor.getBirth()));
		map.put(FieldsInMap.GENDER, ((Integer)donor.getGender()).toString());
		map.put(FieldsInMap.TEMP_STORAGE_ID, donor.getTemp_storage_id());
		if (donor instanceof Donor) {
			map.put(FieldsInMap.EMAIL, ((Donor)donor).getMail());
		}
		return map;
	}
	public static Map<FieldsInMap, Object> createMapForListIds(List<String> ids) {
		Map<FieldsInMap, Object> params = new HashMap<DonorHelper.FieldsInMap, Object>();
		params.put(FieldsInMap.LIST_STORAGE_IDS, ids);
		return params;
	}
}
