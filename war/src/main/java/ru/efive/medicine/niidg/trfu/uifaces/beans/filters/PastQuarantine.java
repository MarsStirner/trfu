package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Класс предназначен для работы с признаком "Прошедшие карантинизацию". Используется в фильтре представления "Доноры".
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class PastQuarantine extends DictionaryEntity {
	private static final long serialVersionUID = -1125125763857613758L;

	public PastQuarantine() {
		super();
	}

	public PastQuarantine(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}