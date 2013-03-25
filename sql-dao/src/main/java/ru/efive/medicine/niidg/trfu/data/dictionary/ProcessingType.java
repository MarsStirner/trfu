package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - тип переработки
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_processing_types")
public class ProcessingType extends DictionaryEntity {
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof ProcessingType ) ) {
            return false;
        }
        return getValue().equals( ((ProcessingType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	private static final long serialVersionUID = 4977658185511767766L;
}