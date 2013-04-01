package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - статус компонента крови. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class BloodComponentStatus extends DictionaryEntity {
	private static final long serialVersionUID = -1216332995872862290L;

	public BloodComponentStatus() {
		super();
	}

	public BloodComponentStatus(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}