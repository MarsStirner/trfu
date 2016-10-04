package ru.efive.medicine.niidg.trfu.dao;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.TextTemplate;

@org.springframework.transaction.annotation.Transactional
public class TextTemplateDAOImpl extends GenericDAOHibernate<TextTemplate>{
    @Override
    protected Class<TextTemplate> getPersistentClass() {
        return TextTemplate.class;
    }
}
