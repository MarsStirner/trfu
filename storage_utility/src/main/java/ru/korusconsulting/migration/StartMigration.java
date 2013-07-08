package ru.korusconsulting.migration;
import java.util.List;

import org.apache.log4j.Logger;

import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;
import ru.korusconsulting.migration.bean.MedicalDonor;
import ru.korusconsulting.migration.dao.DAOImpl;

/**
 * @author vkastsiuchenka
 *
 */
public class StartMigration {
	public static final Logger LOG=Logger.getLogger(StartMigration.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DAOImpl impl = new DAOImpl();
		/**/
		List<CommonDonor> donors = impl.getAll(Donor.class);
		List<CommonDonor> medicalDodors = impl.getAll(MedicalDonor.class);
		impl.insertToSRPDList(donors);
		impl.insertToSRPDList(medicalDodors);
		System.out.println(donors.size());
		System.out.println(medicalDodors.size());
		
	}

}
