package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

@Entity
@Table(name = "trfu_blood_component_types")
public class BloodComponentType extends DictionaryEntity {
	
	public void setLite(boolean lite) {
		this.lite = lite;
	}
	
	public boolean isLite() {
		return lite;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	@Override
	public String toString() {
		return getCode() + " " + getValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
        if( !( obj instanceof BloodComponentType ) ) {
            return false;
        }
        return getValue().equals( ((BloodComponentType)obj).getValue() );
    }
	
	@Override
    public int hashCode() {
        return getValue().hashCode();
    }
	
	
	/**
	 * Легковесный компонент (упрощенная форма)
	 */
	private boolean lite;
	
	/**
	 * Код компонента
	 */
	private String code;
	
	/**
	 * Используется ли компонент
	 */
	private boolean used;
	
	private static final long serialVersionUID = 6086994767559295755L;
	
	public String getCodeAndValue() {
		return toString();
	} 	
}