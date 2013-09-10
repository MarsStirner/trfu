package ru.korusconsulting.migration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;
import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;
import ru.korusconsulting.migration.bean.MedicalDonor;
import ru.korusconsulting.migration.dao.DAOImpl;
import ru.korusconsulting.migration.dao.MigrationDonorHelper;


/**
 * @author vkastsiuchenka
 *
 */
public class StartMigration {
	public static final Logger LOG=Logger.getLogger(StartMigration.class);
	
	private static File file = null;
	private static FileWriter fileWriter = null;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DAOImpl impl = new DAOImpl();
		/* getting full list of donors and medical donors */
		List<CommonDonor> donors = impl.getAll(Donor.class);
		List<CommonDonor> medicalDonors = impl.getAll(MedicalDonor.class);
		/* inserting data to SRPD */
		addListToSRPD(donors);
		addListToSRPD(medicalDonors);
		/* inserting returned id of SRPD to base TRFU*/
		impl.updateTRFUData(donors);
		impl.updateTRFUData(medicalDonors);
	}
	private static List<CommonDonor> addListToSRPD(List<CommonDonor> donors) throws UnsupportedEncodingException {
		SRPDDao srpdDao = new SRPDDao();
		MigrationDonorHelper migrationHelper = new MigrationDonorHelper();
		Map<FieldsInMap, Object> resMap = null;
		for(CommonDonor i : donors) {
			 resMap=srpdDao.addPDToSRPD(migrationHelper.mapFromDonor(i)); 
			 i.setTemp_storage_id(resMap.get(FieldsInMap.TEMP_STORAGE_ID).toString());
			 printIdToFile(resMap.get(FieldsInMap.TEMP_STORAGE_ID).toString());
		}
		return donors;
	}

	private static void printIdToFile(String id) {
		 try {
			 if (file == null) {
				  file = new File("result.txt");
			  }
			  if (fileWriter == null) {
				  fileWriter = new FileWriter(file);
			  }
			CharSequence cq = id + " " ;
			  fileWriter.append(cq);
			  fileWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
