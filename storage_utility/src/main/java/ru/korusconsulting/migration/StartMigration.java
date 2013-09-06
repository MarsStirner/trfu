package ru.korusconsulting.migration;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.korusconsulting.SRPD.DonorHelper;
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

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		DAOImpl impl = new DAOImpl();
		/*new SRPDDao().makeQuery("Petrov", "Petr", "Petrovich", 
								"16535596", "3217 111587", "585-85-37", "ÐœÐ¾Ñ�ÐºÐ²Ð°,Ð‘Ð¾Ñ€Ð¸Ñ�Ð¾Ð²Ñ�ÐºÐ¸Ðµ Ð¿Ñ€ÑƒÐ´Ñ‹ Ð´Ð¾Ð¼ 8 ÐºÐ¾Ñ€Ð¿ÑƒÑ� 3 ÐºÐ²Ð°Ñ€Ñ‚Ð¸Ñ€Ð° 133",
								"", "", "Ð¤Ð¾Ð½Ð´ 'ÐŸÐ¾Ð´Ð°Ñ€Ð¸ Ð¶Ð¸Ð·Ð½ÑŒ',340-16-34", "19720531");
		/* getting full list of donors and medical donors */
		List<CommonDonor> donors = impl.getAll(Donor.class);
		List<CommonDonor> medicalDonors = impl.getAll(MedicalDonor.class);
		/* inserting data to SRPD */
		//donors = srpdDao.insertToSRPDList(donors);
		//medicalDonors = srpdDao.insertToSRPDList(medicalDonors);
		addListToSRPD(donors);
		addListToSRPD(medicalDonors);
		/* inserting returned id of SRPD to base TRFU*/
		impl.updateTRFUData(donors);
		impl.updateTRFUData(medicalDonors);
	}
	private static List<CommonDonor> addListToSRPD(List<CommonDonor> donors) {
		SRPDDao srpdDao = new SRPDDao();
		DonorHelper donorHelper = new DonorHelper();
		MigrationDonorHelper migrationHelper = new MigrationDonorHelper();
		Map<FieldsInMap, Object> resMap = null;
		for(CommonDonor i : donors) {
			 resMap = srpdDao.addToSRPD(migrationHelper.mapFromDonor(i)); 
			 i.setTemp_storage_id(resMap.get(FieldsInMap.TEMP_STORAGE_ID).toString());
		}
		return donors;
	}

}
