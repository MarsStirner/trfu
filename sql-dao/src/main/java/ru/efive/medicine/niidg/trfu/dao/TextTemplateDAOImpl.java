package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.EmailTemplate;
import ru.efive.medicine.niidg.trfu.data.entity.TextTemplate;

public class TextTemplateDAOImpl extends GenericDAOHibernate<TextTemplate>{
    @Override
    protected Class<TextTemplate> getPersistentClass() {
        return TextTemplate.class;
    }
}
