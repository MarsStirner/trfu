package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;

public class AnalysisDAOImpl extends GenericDAOHibernate<Analysis> {
	
	@Override
	protected Class<Analysis> getPersistentClass() {
		return Analysis.class;
	}
	
}