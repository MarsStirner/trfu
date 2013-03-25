package ru.efive.medicine.niidg.trfu.data.entity.integration;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;

/**
 * Новость в АРМ информатор
 */
@Entity
@Table(name = "trfu_information_entries")
public class InformationEntry extends IdentifiedEntity {
	
	public User getAuthor() {
		return author;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public boolean isPublished() {
		return published;
	}
	
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}
	
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * Автор новости
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * Дата создания новости
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * Опубликована ли новость
	 */
	private boolean published;
	
	/**
	 * дата публикации новости
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date publishDate;
	
    /**
     * Заголовок новости
     */
    private String title;
    
    /**
     * Краткое содержание
     */
    private String shortDescription;
    
    /**
     * Текст новости
     */
    @Column(columnDefinition="text")
    private String description;
    
	/**
	 * Удалена ли новость
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1L;
}