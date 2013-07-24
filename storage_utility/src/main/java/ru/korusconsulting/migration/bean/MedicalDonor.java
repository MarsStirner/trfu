package ru.korusconsulting.migration.bean;

import javax.persistence.*;

/**
 * @author vkastsiuchenka
 *
 */
@Entity
@Table(name="trfu_medical_donors")
public class MedicalDonor extends CommonDonor {

	@Override
	public String toString() {
		return "MedicalDonor [" + super.toString() + "]";
	}

}
