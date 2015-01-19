package ru.efive.medicine.niidg.trfu.data.dictionary;


import ru.efive.dao.sql.entity.DictionaryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "trfu_blood_system_types")
public class BloodSystemType extends DictionaryEntity {
    private String code;

    private String unit;

    @Column(name="mnem")
    private String mnem;

    @Column(name="shortName")
    private String shortName;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

    public String getMnem() {
        return mnem;
    }

    public void setMnem(String mnem) {
        this.mnem = mnem;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}