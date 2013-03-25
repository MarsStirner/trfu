package ru.efive.dao.sql.entity.document;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Документ
 */
@MappedSuperclass
public class Document extends IdentifiedEntity {
    
	/**
     * Конструктор, задающий дату создания нового документа и пользователя, создавшего документ.
     * 
     * @param dateCreated дата создания документа.
     * @param user пользователь, создавший документ.
     */
    public Document(Date created, User user) {
        this.created = created;
        this.author = user;
        deleted = false;
    }
    
    /**
     * Конструктор по умолчанию.
     */
    public Document() {
    	deleted = false;
    }
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }
    
    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
    
    /**
     * дата создания документа
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;
    
    /**
     * дата последнего редактирования документа
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date edited;
    
    /**
     * пользователь, создавший документ
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
    
    /**
     * пользователь, последний редактировавший документ
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User editor;
    
    /**
     * true - удалён, false или null - не удалён
     */
    private Boolean deleted;
    
    
    private static final long serialVersionUID = -5542939516927639639L;
}