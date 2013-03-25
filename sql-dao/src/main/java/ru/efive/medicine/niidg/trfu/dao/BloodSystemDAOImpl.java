package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;

public class BloodSystemDAOImpl extends GenericDAOHibernate<BloodSystem> {
	
	@Override
	protected Class<BloodSystem> getPersistentClass() {
		return BloodSystem.class;
	}
	
}