package ru.efive.medicine.niidg.trfu.data.dictionary;


import ru.efive.dao.sql.entity.DictionaryEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "trfu_blood_system_types")
public class BloodSystemType extends DictionaryEntity {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
