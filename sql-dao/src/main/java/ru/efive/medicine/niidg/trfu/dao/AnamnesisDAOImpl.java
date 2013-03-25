package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.Anamnesis;

public class AnamnesisDAOImpl extends GenericDAOHibernate<Anamnesis> {
	
	@Override
	protected Class<Anamnesis> getPersistentClass() {
		return Anamnesis.class;
	}
	
}