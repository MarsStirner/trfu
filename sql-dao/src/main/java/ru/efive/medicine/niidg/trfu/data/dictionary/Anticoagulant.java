package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - антикоагулянт
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_anticoagulants")
public class Anticoagulant extends DictionaryEntity {
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof Anticoagulant ) ) {
            return false;
        }
        return getValue().equals( ((Anticoagulant)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	private static final long serialVersionUID = -8054846507320303957L;
}