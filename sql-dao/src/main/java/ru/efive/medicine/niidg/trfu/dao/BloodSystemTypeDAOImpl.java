package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.DictionaryDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;

public class BloodSystemTypeDAOImpl extends DictionaryDAOHibernate<BloodSystemType> {
    @Override
    protected Class<BloodSystemType> getPersistentClass() {
        return BloodSystemType.class;
    }
}
