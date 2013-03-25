package ru.efive.medicine.niidg.trfu.data.entity.medical;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ru.efive.dao.sql.entity.document.Document;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Обработка - Процессинг (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_processings")
public class Processing extends Document {
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	public void setTransfusiologist(User transfusiologist) {
		this.transfusiologist = transfusiologist;
	}

	public User getTransfusiologist() {
		return transfusiologist;
	}

	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}

	public Date getProcessingDate() {
		return processingDate;
	}

	public void setProcessingType(Classifier processingType) {
		this.processingType = processingType;
	}

	public Classifier getProcessingType() {
		return processingType;
	}
	
	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	@Transient
	public String getType() {
		return "Processing";
	}

	public int getGrouping() {
		return grouping;
	}

	public void setGrouping(int grouping) {
		this.grouping = grouping;
	}
	
	
	/**
	 * Номер
	 */
	private String number;
	
	/**
	 * Врач-трансфузиолог
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User transfusiologist;
	
	/**
	 * Дата обработки
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date processingDate;
	
	/**
	 * Тип обработки
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier processingType;
	
	/**
	 * Примечание
	 */
	@Column(columnDefinition="text")
	private String commentary;
	
	/**
	 * Для группировок в представлениях
	 */
	@Transient
	private int grouping = 100;
	
	
	private static final long serialVersionUID = -5278594518122260015L;
}