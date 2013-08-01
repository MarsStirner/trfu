package ru.korusconsulting.migration.dao;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

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
	private final String PROCESSED_DONORS_FILE = "temp.out";
	
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
			getInRange(criteria);
			getInListIds(criteria);
			/* Setting point of begin reading and max number Donors from base at one time*/
			criteria.setFirstResult(beginFrom);
			criteria.setMaxResults(maxResults);
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
	private Criteria getInRange(Criteria crit) {
		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(StartMigration.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE));
			crit.add(Restrictions.between("id", Integer.parseInt(properties.getProperty(FIRST_DONOR)), 
												Integer.parseInt(properties.getProperty(LAST_DONOR))));
		} catch (Exception e) {
			StartMigration.LOG.error(e);
		} 
		return crit;
	}
	private Criteria getInListIds(Criteria criteria) {
		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(StartMigration.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE));
			String [] idsString = properties.getProperty(LIST_IDS_DONORS).split(" ");
			List<Integer> ids = new ArrayList<Integer>();
			/* Creation List of Integers from Array Strings */
			for(String i : idsString) {
				/* test for empty elements*/
				if(!"".equals(i)) {
					ids.add(Integer.parseInt(i));
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
	
	/* Method for sending data about List of donors to SRPD  */
	public List<CommonDonor> insertToSRPDList(List<CommonDonor> donors) {
		int currentIdSRPD;
		ObjectOutputStream oos = null;
		/*remove donors, that was inserted to SRPD */
		try {
			donors.removeAll(getProcessedDonors(false));
			SRPDDao dao = new SRPDDao();
			for (CommonDonor i : donors) {
				currentIdSRPD = insertDonorToSRPD(dao, i);
				StartMigration.LOG.info("Data for: " + i + " were send. External id: " + currentIdSRPD);
				/* this code need for writing objects to file, and recover
				 * work after failure */
				oos = insertProcessedDonor(i, oos);
				i.setTemp_stogate_id(currentIdSRPD);
			}
			return getProcessedDonors(true);
		} catch (Exception e) {
			StartMigration.LOG.error(e.getLocalizedMessage());
		}
		return null;	
	}
	/* Send information about current donor and wait for id from SRPD */
	private Integer insertDonorToSRPD(SRPDDao dao, CommonDonor donor) {
		if (dao == null) {
			dao = new SRPDDao();
		}
		return new Random().nextInt(200);
	}
	/* reader from file, for inserted Donor */
	private List<CommonDonor> getProcessedDonors(Boolean removeFile) throws IOException, ClassNotFoundException {
		List<CommonDonor> processedDonors = new ArrayList<CommonDonor>();
		try {
			FileInputStream in = new FileInputStream(PROCESSED_DONORS_FILE);
			ObjectInputStream ois = new ObjectInputStream(in);
			CommonDonor donor = (CommonDonor) ois.readObject();
			while (true) {
				processedDonors.add(donor);
				donor = (CommonDonor) ois.readObject();
			}
		} catch (EOFException e) {
			/* after this exception - */
		}
		/*if removeFile is true - remove contain of file*/
		if (removeFile) {
			FileWriter fstream1 = new FileWriter(PROCESSED_DONORS_FILE);
	        BufferedWriter out1 = new BufferedWriter(fstream1);
	        out1.write("");
	        out1.close();
		}
		return processedDonors;
	}
	/* write inserted donors of SRPD to file */
	private ObjectOutputStream insertProcessedDonor(CommonDonor donor, ObjectOutputStream oos) throws IOException {
		if(oos == null) {
			FileOutputStream fos = new FileOutputStream(PROCESSED_DONORS_FILE);
			oos = new ObjectOutputStream(fos);
		}
		oos.writeObject(donor);
		/*oos.flush();
		oos.close();*/
		return oos;
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
