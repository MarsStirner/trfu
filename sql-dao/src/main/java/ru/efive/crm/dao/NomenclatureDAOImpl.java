package ru.efive.crm.dao;

import ru.efive.dao.sql.dao.DictionaryDAOHibernate;
import ru.efive.crm.data.ContragentNomenclature;

@org.springframework.transaction.annotation.Transactional
public class NomenclatureDAOImpl extends DictionaryDAOHibernate<ContragentNomenclature> {
	
	@Override
	protected Class<ContragentNomenclature> getPersistentClass() {
		return ContragentNomenclature.class;
	}
	
}