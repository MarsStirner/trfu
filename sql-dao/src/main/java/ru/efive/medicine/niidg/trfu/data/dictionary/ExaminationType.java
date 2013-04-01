package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - тип обращения на обследование. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class ExaminationType extends DictionaryEntity {
	private static final long serialVersionUID = -1216332995872862290L;

	public ExaminationType() {
		super();
	}

	public ExaminationType(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}