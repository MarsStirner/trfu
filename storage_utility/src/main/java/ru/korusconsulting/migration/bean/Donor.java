package ru.korusconsulting.migration.bean;

import javax.persistence.*;

/**
 * 
 * @author vkastsiuchenka
 *
 */

@Entity
@Table(name="trfu_donors")
public class Donor extends CommonDonor {
	private String mail;
	@Column
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
