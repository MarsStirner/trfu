package ru.korusconsulting.migration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;
import ru.korusconsulting.migration.dao.MigrationDonorHelper;

public class TestMigration {
	private final static String PROPERTIE_FILE = "parameters.properties";
	private final static String IDS = "ids";
	private static File file = null;
	private static  FileWriter fileWriter = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties properties = null;
		String[] ids = null;
		List<String> listIds = new ArrayList<String>();
		try {
			properties = DonorHelper.createPropertiesForURL(PROPERTIE_FILE);
			if (!"".equals(properties.get(IDS))) {
				ids = properties.get(IDS).toString().split(",");
			}
			for(String i : ids) {
				listIds.add(i.trim());
			}
			SRPDDao srpdDao = new SRPDDao();
			Map<FieldsInMap, Object> map = MigrationDonorHelper.createMapForListIds(listIds);
			List<Map<FieldsInMap, Object>> result = srpdDao.get(map);
			for(Map<FieldsInMap, Object> i: result) {
				printMapOfResult(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void printMapOfResult(Map<FieldsInMap,Object> map) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		String s = "lastName=" + map.get(FieldsInMap.LAST_NAME)
						 + ", middleName=" + map.get(FieldsInMap.MIDDLE_NAME)
						 + ", firstName=" + map.get(FieldsInMap.FIRST_NAME)
						 + ", gender=" + map.get(FieldsInMap.GENDER) 
						 + ", birth=" + map.get(FieldsInMap.BIRTH)
						 + ", passportNumber=" + map.get(FieldsInMap.PASSPORT_NUMBER)  
						 + ", insuranceNumber=" + map.get(FieldsInMap.OMC_NUMBER) 
						 + ", employment="	+ map.get(FieldsInMap.EMPLOYMENT) 
						 + ", workPhone=" + map.get(FieldsInMap.WORK_PHONE)
						 + ", registrationAddress=" + map.get(FieldsInMap.ADRESS) 
						 + ", phone=" + map.get(FieldsInMap.PHONE) 
						 + ", email=" + map.get(FieldsInMap.EMAIL)  
						 + ", temp_storage_id=" + map.get(FieldsInMap.TEMP_STORAGE_ID) + "\r\n";
		try {
			System.setProperty("file.encoding","UTF-8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
			if (file == null) {
				file = new File("result.txt");
			}
			if (fileWriter == null) {
				fileWriter = new FileWriter(file);
			}
			CharSequence cq = s ;
			  fileWriter.append(cq);
			  fileWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
