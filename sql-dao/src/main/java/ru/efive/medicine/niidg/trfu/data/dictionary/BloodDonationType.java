package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - тип донации
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_donation_types")
public class BloodDonationType extends DictionaryEntity {
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof BloodDonationType ) ) {
            return false;
        }
        return getValue().equals( ((BloodDonationType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	private static final long serialVersionUID = -5576480075650551897L;
}