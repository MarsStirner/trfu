package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - статус обращения на обследование. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class ExaminationStatus extends DictionaryEntity {
	private static final long serialVersionUID = -1216332995872862290L;

	public ExaminationStatus() {
		super();
	}

	public ExaminationStatus(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}