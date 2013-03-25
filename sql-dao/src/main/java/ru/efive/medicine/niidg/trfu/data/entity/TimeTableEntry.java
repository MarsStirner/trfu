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

/**
 * Запись в расписании (рабочий день)
 */
@Entity
@Table(name = "trfu_time_table_entries")
public class TimeTableEntry extends IdentifiedEntity {
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	public String getCommentary() {
		return commentary;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	
	/**
	 * дата создания документа
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
     * Автор
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * Дата/время начала работы
	 */
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date startDate;
	
	/**
	 * Дата/время завершения работы
	 */
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date endDate;
	
	/**
	 * Сотрудник
	 */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User user;
	
    /**
     * Комментарий
     */
    private String commentary;
    
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 7167749458451438312L;
}