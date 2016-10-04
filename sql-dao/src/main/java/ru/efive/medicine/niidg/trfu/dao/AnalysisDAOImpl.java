package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
@org.springframework.transaction.annotation.Transactional
public class AnalysisDAOImpl extends GenericDAOHibernate<Analysis> {
	
	@Override
	protected Class<Analysis> getPersistentClass() {
		return Analysis.class;
	}
	
}