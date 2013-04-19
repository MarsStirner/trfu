package ru.efive.medicine.niidg.trfu.data.dictionary;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Справочная информация - категория донора.
 * 
 * @author Siarhei Ushanau
 */
public class DonorCategory extends DictionaryEntity {
	private static final long serialVersionUID = -7044337847285408014L;

	public DonorCategory() {
		super();
	}

	public DonorCategory(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}