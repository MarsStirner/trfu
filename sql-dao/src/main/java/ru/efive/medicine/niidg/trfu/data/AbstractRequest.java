package ru.efive.medicine.niidg.trfu.data;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.DonorRejection;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;

/**
 * Базовый класс - обращение
 * 
 * @author Alexey Vagizov
 */
@MappedSuperclass
public class AbstractRequest extends IdentifiedEntity {
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public User getRegistrator() {
		return registrator;
	}

	public void setRegistrator(User registrator) {
		this.registrator = registrator;
	}

	public User getTherapist() {
		return therapist;
	}

	public void setTherapist(User therapist) {
		this.therapist = therapist;
	}
	
	public User getStaffNurse() {
		return staffNurse;
	}

	public void setStaffNurse(User staffNurse) {
		this.staffNurse = staffNurse;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	@Transient
	public String getUrl() {
		StringBuffer result = new StringBuffer(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()).append("/component/");
		if (this instanceof BloodDonationRequest) {
			result.append("blood_donation");
		}
		else {
			result.append("examination");
		}
		result.append(".xhtml?docId=").append(getId());
		return result.toString();
	}
	
	@Transient
	public String getRequestType() {
		String result = "";
		if (this instanceof BloodDonationRequest) {
			result = "На донацию";
		}
		else if (this instanceof ExaminationRequest) {
			result = new StringBuffer().append(((ExaminationRequest) this).getExaminationType() == 0? "Первичное ": "Повторное ").append("обследование").toString();
		}
		return result;
	}
	
	public void setRejection(DonorRejection rejection) {
		this.rejection = rejection;
	}
	
	public DonorRejection getRejection() {
		return rejection;
	}


	/**
	 * дата создания документа
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * Донор
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Donor donor;
	
	/**
     * Медицинский регистратор
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User registrator;
	
    /**
     * Врач-терапевт
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User therapist;
	
    /**
     * Медицинская сестра/Операционная
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User staffNurse;
	
	/**
	 * Комментарий
	 */
    @Column(columnDefinition="text")
	private String commentary;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	/**
	 * Текущий статус документа в процессе
	 */
	@Column(name="status_id")
	private int statusId;
	
	/**
	 * Причина отвода (временная переменная)
	 */
	@Transient
	private DonorRejection rejection;
	
	private static final long serialVersionUID = -4072023462849191731L;
}