package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Классификатор
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_classifiers")
public class Classifier extends DictionaryEntity {
	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof Classifier ) ) {
            return false;
        }
        return getValue().equals( ((Classifier)obj).getValue() ) && getCategory().equals( ((Classifier)obj).getCategory() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode() + getCategory().hashCode();
    }
	
	
	/**
	 * Категория классификатора
	 */
	private String category;
	
	private static final long serialVersionUID = 7107661137634082274L;
}