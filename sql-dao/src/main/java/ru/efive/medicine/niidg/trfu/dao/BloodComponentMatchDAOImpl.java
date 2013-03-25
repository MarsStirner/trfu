package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentMatch;

public class BloodComponentMatchDAOImpl extends GenericDAOHibernate<BloodComponentMatch> {
	
	@Override
	protected Class<BloodComponentMatch> getPersistentClass() {
		return BloodComponentMatch.class;
	}
	
}