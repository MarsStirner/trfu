package ru.korusconsulting.migration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class TestMigration {
	private final static String PROPERTIE_FILE = "parameters.properties";
	private final static String IDS = "ids";
	private static File file = null;
	private static  FileWriter fileWriter = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties properties = new Properties();
		String[] ids = null;
		try {
			properties.load(StartMigration.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE));
			if (!"".equals(properties.get(IDS))) {
				ids = properties.get(IDS).toString().split(",");
			}
			SRPDDao srpdDao = new SRPDDao();
			for(String i: ids) {
				printMapOfResult(srpdDao.findDonor(i.trim()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void printMapOfResult(Map<FieldsInMap,Object> map) {
		String s = "lastName=" + map.get(FieldsInMap.LAST_NAME)
						 + ", middleName=" + map.get(FieldsInMap.MIDDLE_NAME)
						 + ", firstName=" + map.get(FieldsInMap.FIRST_NAME)
						 + ", gender=" + map.get(FieldsInMap.GENDER) 
						 + ", birth=" + map.get(FieldsInMap.BIRTH)
						 + ", passportNumber=" + map.get(FieldsInMap.PASSPORT_NUMBER)  
						 + ", insuranceNumber=" + map.get(FieldsInMap.OMC_NUMBER) 
						 + ", employment="	+ map.get(FieldsInMap.EMPLOYMENT) 
						 + ", workPhone=" + map.get(FieldsInMap.WORK_PHONE)
						 + ", mobilePhone=" + map.get(FieldsInMap.MOBILE_PHONE)
						 + ", registrationAddress=" + map.get(FieldsInMap.ADRESS) 
						 + ", phone=" + map.get(FieldsInMap.PHONE) 
						 + ", email=" + map.get(FieldsInMap.EMAIL) 
						 + ", employment=" + map.get(FieldsInMap.EMPLOYMENT) 
						 + ", temp_storage_id=" + map.get(FieldsInMap.TEMP_STORAGE_ID) + "\r\n";
		 try {
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
