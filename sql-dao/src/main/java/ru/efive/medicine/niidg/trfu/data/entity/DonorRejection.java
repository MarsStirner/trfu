package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorRejectionType;

/**
 * Записи отвода от донорства
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_donor_rejectios")
public class DonorRejection extends IdentifiedEntity {
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public DonorRejectionType getRejectionType() {
		return rejectionType;
	}

	public void setRejectionType(DonorRejectionType rejectionType) {
		this.rejectionType = rejectionType;
	}

	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public Date getExpiration() {
		return expiration;
	}


	/**
	 * Дата отвода
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * Направивший на отвод
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * Тип отвода
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private DonorRejectionType rejectionType;
	
	/**
	 * Донор
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Donor donor;
	
	/**
	 * Идентификатор обращения: e_id - обследование, d_id - донация
	 */
	private String request;
	
	/**
	 * Дата окончания отвода (для временных)
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date expiration;
	
	private static final long serialVersionUID = -2714902401307427438L;
}