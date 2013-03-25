package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - запись осмотра
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_examination_entry_types")
public class ExaminationEntryType extends DictionaryEntity {
	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setParentEntry(ExaminationEntryType parentEntry) {
		this.parentEntry = parentEntry;
	}

	public ExaminationEntryType getParentEntry() {
		return parentEntry;
	}
	
	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
	public int getFieldType() {
		return fieldType;
	}

	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof ExaminationEntryType ) ) {
            return false;
        }
        return getValue().equals( ((ExaminationEntryType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	/**
	 * Категория записи осмотра
	 */
	private String category;
	
	/**
	 * Значение по умолчанию
	 */
	private String defaultValue;
	
	/**
	 * Базовая запись осмотра
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private ExaminationEntryType parentEntry;
	
	/**
	 * Необходимо ли вносить результат
	 */
	private boolean writable;
	
	/**
	 * Для корректной сортировки, возможно пригодится в будущем
	 */
	private int level;
	
	/**
	 * Тип поля для ввода:
	 * 0 - inputText, 1 - inputTextarea, 2 - inputSecret
	 * 3 - selectOneMenu, 4 - selectOneRadio
	 * 5 - selectManyCheckbox
	 */
	private int fieldType;

	
	private static final long serialVersionUID = 8235246788471406309L;


}