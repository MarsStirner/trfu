package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - статус донора. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class DonorStatus extends DictionaryEntity {
	private static final long serialVersionUID = 3791811102342816497L;

	public DonorStatus() {
		super();
	}

	public DonorStatus(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}