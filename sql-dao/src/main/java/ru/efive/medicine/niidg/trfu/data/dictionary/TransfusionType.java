package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - вид трансфузии. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class TransfusionType extends DictionaryEntity {
	private static final long serialVersionUID = -7448395382022854767L;

	public TransfusionType() {
		super();
	}

	public TransfusionType(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}