package ru.efive.medicine.niidg.trfu.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;

@Entity
@Table(name = "trfu_blood_donation_entries")
public class BloodDonationEntry extends IdentifiedEntity {
	
	public BloodDonationEntry() {
		
	}
	
	public void setDonationType(BloodDonationType donationType) {
		this.donationType = donationType;
	}
	
	public BloodDonationType getDonationType() {
		return donationType;
	}
	
	public void setDose(int dose) {
		this.dose = dose;
	}
	
	public int getDose() {
		return dose;
	}
	
	@Override
    public int hashCode() {
        if (getUuid() != null)
            return getUuid().hashCode();
        final int prime = 31;
		int result = 1;
		result = prime * result + getId();
		return result;
    }

    @Override
    public boolean equals(Object obj) {
    	if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiedEntity other = (IdentifiedEntity) obj;
		if (StringUtils.isNotEmpty(getUuid()) && StringUtils.isNotEmpty(other.getUuid())) {
			return getUuid().equals(other.getUuid());
		}
		if (getId() != other.getId())
			return false;
		return true;
    }

	/**
	 * Тип донации
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodDonationType donationType;
	
	/**
	 * Объем донации
	 */
	private int dose;
	
	
	private static final long serialVersionUID = 2690921094013363882L;
}