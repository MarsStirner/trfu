package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.*;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - группа крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_groups")
public class BloodGroup extends DictionaryEntity {
	
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof BloodGroup ) ) {
            return false;
        }
        return getValue().equals( ((BloodGroup)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	private int number;

	private static final long serialVersionUID = -5985527921853123963L;
}