package ru.efive.medicine.niidg.trfu.data.entity.integration;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.enums.ConverterName;

/**
 * Внешние исследования
 */
@Entity
@Table(name = "trfu_external_indicators")
public class ExternalIndicator extends IdentifiedEntity {
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

    public ConverterName getConverterName() {
        return converterName;
    }

    public void setConverterName(ConverterName converterName) {
        this.converterName = converterName;
    }

    /**
	 * код показателя/метода/микроорганизма (из справочника ЛИС)
	 */
	private String code;
	
	/**
	 * наименование показателя/метода/микроорганизма (из справочника ЛИС)
	 */
	private String name;

    @Enumerated(value = EnumType.STRING)
    private ConverterName converterName;

	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1L;
}