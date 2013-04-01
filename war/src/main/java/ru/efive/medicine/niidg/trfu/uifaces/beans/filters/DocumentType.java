package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Класс предназначен для работы с информацией о типах документов. Используется в фильтре представления "Доноры".
 * Для удобства реализован в виде наследника DictionaryEntity.
 * 
 * @author Siarhei Ushanau
 */
public class DocumentType extends DictionaryEntity {
	private static final long serialVersionUID = -2280528031632526030L;

	public DocumentType() {
		super();
	}

	public DocumentType(int id, String value) {
		super();
		setId(id);
		setValue(value);
	}
}