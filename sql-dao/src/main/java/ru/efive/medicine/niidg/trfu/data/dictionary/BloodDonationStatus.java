package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - статус донации крови. 
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class BloodDonationStatus extends DictionaryEntity {
	private static final long serialVersionUID = 2709697564642274459L;

	public BloodDonationStatus() {
		super();
	}

	public BloodDonationStatus(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}