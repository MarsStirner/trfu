package ru.korusconsulting.migration.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import ru.korusconsulting.migration.StartMigration;
import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;
import ru.korusconsulting.migration.bean.MedicalDonor;

public class DAOImpl {
	private final String PROPERTIE_FILE = "storage_utitlity.properties";
	private final String FIRST_DONOR = "first";
	private final String LAST_DONOR = "last";
	private final String LIST_IDS_DONORS = "listIds";
	private final String MEDICAL_FIRST_DONOR = "mfirst";
	private final String MEDICAL_LAST_DONOR = "mlast";
	private final String MEDICAL_LIST_IDS_DONORS = "mlistIds";
	private final String ID_FFROM_SRPD_NAME = "temp_storage_id";
	
	private SessionFactory sessionFactory;
	private final int maxResults = 200;
	
	public DAOImpl() {
		try {
			sessionFactory = new AnnotationConfiguration()
			.addAnnotatedClass(Donor.class)
			.addAnnotatedClass(MedicalDonor.class)
			.buildSessionFactory();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/* Method for getting all Donors or MedicalDonors from base of TRFU */
	@SuppressWarnings("unchecked")
	public List<CommonDonor> getAll(Class<?> typeDonorForSelect) {
		List<CommonDonor> allDonors = null;
		List<CommonDonor> currentSet = null;
		Session session = null;
		int beginFrom = 0;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(typeDonorForSelect);
			getInRange(criteria, typeDonorForSelect);
			getInListIds(criteria, typeDonorForSelect);
			/* Setting point of begin reading and max number Donors from base at one time*/
			criteria.setFirstResult(beginFrom);
			criteria.setMaxResults(maxResults);
			criteria.add(Restrictions.isNull(ID_FFROM_SRPD_NAME));
			currentSet = (List<CommonDonor>)criteria.list();
			allDonors = currentSet;
			beginFrom = maxResults;
			/*	getting Donors from base trfu by "pages" with some number of donrs */
			while (currentSet.size() == maxResults) {
				criteria.setFirstResult(beginFrom);
				currentSet = (List<CommonDonor>)criteria.list();
				/* append current number of donors to main list of donors */
				allDonors.addAll(currentSet);
				beginFrom = beginFrom + maxResults;
			}
		} catch(Exception e) {
			StartMigration.LOG.error(e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e1) {
					StartMigration.LOG.error(e1);
				}
			}
		}
		return allDonors;
	}
	/* Method could get Donors in some diapazone */
	private Criteria getInRange(Criteria crit, Class<?> currentClass) {
		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(StartMigration.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE));
			if(currentClass == Donor.class) {
				crit.add(Restrictions.between("id", Integer.parseInt(properties.getProperty(FIRST_DONOR).trim()), 
													Integer.parseInt(properties.getProperty(LAST_DONOR).trim())));
			} else if(currentClass == MedicalDonor.class) {
				crit.add(Restrictions.between("id", Integer.parseInt(properties.getProperty(MEDICAL_FIRST_DONOR).trim()), 
													Integer.parseInt(properties.getProperty(MEDICAL_LAST_DONOR).trim())));
			}
		} catch (Exception e) {
			StartMigration.LOG.error(e);
		} 
		return crit;
	}
	private Criteria getInListIds(Criteria criteria, Class<?> currentClass) {
		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(StartMigration.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE));
			String [] idsString = null;
			if(currentClass == Donor.class) {
				idsString = properties.getProperty(LIST_IDS_DONORS).split(" ");
			} else if(currentClass == MedicalDonor.class) {
				idsString = properties.getProperty(MEDICAL_LIST_IDS_DONORS).split(" ");
			}
			List<Integer> ids = new ArrayList<Integer>();
			/* Creation List of Integers from Array Strings */
			for(String i : idsString) {
				/* test for empty elements*/
				if(!"".equals(i)) {
					ids.add(Integer.parseInt(i.trim()));
				}
			}
			if(ids.size() > 0) {
				criteria.add(Restrictions.in("id", ids));
			}
		} catch (Exception e) {
			StartMigration.LOG.error(e);
		} 
		return criteria;
	}
	/* Insert ids, that was return from SRPD, to base TRFU */
	public void updateTRFUData(List<CommonDonor> donors) {
		try {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			for(CommonDonor i : donors) {
				session.update(i);
				StartMigration.LOG.info("For " + i + " updated id from SRPD");
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			StartMigration.LOG.error(e);
		}
	}
}
