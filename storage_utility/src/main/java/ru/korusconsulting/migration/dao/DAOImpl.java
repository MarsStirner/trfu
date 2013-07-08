package ru.korusconsulting.migration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import ru.korusconsulting.migration.StartMigration;
import ru.korusconsulting.migration.bean.CommonDonor;
import ru.korusconsulting.migration.bean.Donor;
import ru.korusconsulting.migration.bean.MedicalDonor;

public class DAOImpl {
	
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
	
	/* Метод для извлечения полного списка данных о донорах */
	@SuppressWarnings("unchecked")
	public List<CommonDonor> getAll(Class<?> typeDonorForSelect) {
		List<CommonDonor> allDonors = null;
		List<CommonDonor> currentSet = null;
		Session session = null;
		int beginFrom = 0;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(typeDonorForSelect);
			/* чтение первой партии объектов */
			criteria.setFirstResult(beginFrom);
			criteria.setMaxResults(maxResults);
			currentSet = (List<CommonDonor>)criteria.list();
			allDonors = currentSet;
			beginFrom = maxResults;
			/*	чтение оставшихся объектов. проверка сущетвуют ли ещё объекты для чтения
			 *  путём подсчёта кол-ва объектов в последнем пакете */
			while (currentSet.size() == maxResults) {
				criteria.setFirstResult(beginFrom);
				currentSet = (List<CommonDonor>)criteria.list();
				/* копирование объектов считанных в текущем пакете в общий список*/
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
	
	public void updateIndexesAtTRFU(List<CommonDonor> updateDonors) {
		
	}
	
	/* Метод для обработки всего списка доноров */
	public List<CommonDonor> insertToSRPDList(List<CommonDonor> donors) {
		for (CommonDonor i : donors) {
			/* Здесь нужно изменять значение поля которое хранит внешний идентификатор из ЗХПД
			 * поле пока отсутствует в базе ТРФУ и не добавлено в bean-классы*/	
		}
		return donors;
		
	}
	/* метод для отправки запроса на добавление донора в 
	 * ЗХПД и получение идентификатора, в случае уданого 
	 * добавления*/
	private Integer insertDonorToSRPD(CommonDonor donor) {
		return new Random().nextInt();
	}
	
	/* метод для изменени идентификаторов в базе ТРФУ */
	public void updateTRFUData(List<CommonDonor> donors) {
		
	}
}
