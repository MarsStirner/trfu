package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.PheresisReport;
@org.springframework.transaction.annotation.Transactional
public class PheresisDAOImpl extends GenericDAOHibernate<PheresisReport> {
	
	@Override
	protected Class<PheresisReport> getPersistentClass() {
		return PheresisReport.class;
	}
	
}