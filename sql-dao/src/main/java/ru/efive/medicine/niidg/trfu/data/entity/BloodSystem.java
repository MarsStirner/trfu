package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;

/**
 * Система крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_systems")
public class BloodSystem extends IdentifiedEntity {
	public BloodSystemType getType() {
        return type;
    }

    public void setType(BloodSystemType type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodSystemType type;

    private Integer count = 1;
	
	private static final long serialVersionUID = 1L;
}