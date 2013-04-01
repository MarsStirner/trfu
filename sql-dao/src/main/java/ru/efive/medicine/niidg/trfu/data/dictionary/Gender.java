package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - пол донора.
 * 
 * @author Siarhei Ushanau
 */
public class Gender extends DictionaryEntity {
	private static final long serialVersionUID = 2494780342830929832L;

	public Gender() {
		super();
	}

	public Gender(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}