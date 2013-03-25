package ru.efive.medicine.niidg.trfu.data.entity;

import ru.efive.dao.sql.entity.IdentifiedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "trfu_text_templates")
public class TextTemplate extends IdentifiedEntity {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
