package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - тип донора
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_donor_types")
public class DonorType extends DictionaryEntity {
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof DonorType ) ) {
            return false;
        }
        return getValue().equals( ((DonorType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	private static final long serialVersionUID = -5037837516543584886L;
}