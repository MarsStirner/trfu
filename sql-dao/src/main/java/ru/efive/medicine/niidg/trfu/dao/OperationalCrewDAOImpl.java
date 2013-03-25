package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.OperationalCrew;

public class OperationalCrewDAOImpl extends GenericDAOHibernate<OperationalCrew> {
	
	@Override
	protected Class<OperationalCrew> getPersistentClass() {
		return OperationalCrew.class;
	}
	
}