package ru.korusconsulting.migration;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;
import ru.korusconsulting.migration.bean.MedicalDonor;
import ru.korusconsulting.migration.dao.DAOImpl;
import ru.korusconsulting.migration.dao.SRPDDao;

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
								"16535596", "3217 111587", "585-85-37", "Москва,Борисовские пруды дом 8 корпус 3 квартира 133",
								"", "", "Фонд 'Подари жизнь',340-16-34", "19720531");
		/* getting full list of donors and medical donors */
		List<CommonDonor> donors = impl.getAll(Donor.class);
		List<CommonDonor> medicalDonors = impl.getAll(MedicalDonor.class);
		/* inserting data to SRPD */
		SRPDDao srpdDao = new SRPDDao();
		donors = srpdDao.insertToSRPDList(donors);
		medicalDonors = srpdDao.insertToSRPDList(medicalDonors);
		/* inserting returned id of SRPD to base TRFU*/
		impl.updateTRFUData(donors);
		impl.updateTRFUData(medicalDonors);
	}

}
