package ru.efive.dao.sql.wf.entity;

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

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

/**
 * Запись истории workflow
 */
@Entity
@Table(name = "wf_history")
public class HistoryEntry extends IdentifiedEntity implements Comparable<HistoryEntry> {
	
	public HistoryEntry() {
		
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	public String getDocType() {
		return docType;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public int getFromStatusId() {
		return fromStatusId;
	}

	public void setFromStatusId(int fromStatusId) {
		this.fromStatusId = fromStatusId;
	}
	
	@Transient
	public String getFromStatusName() {
		return fromStatusId == 0? "": ApplicationHelper.getStatusName(getDocType(), fromStatusId);
	}

	public int getToStatusId() {
		return toStatusId;
	}

	public void setToStatusId(int toStatusId) {
		this.toStatusId = toStatusId;
	}
	
	@Transient
	public String getToStatusName() {
		return toStatusId == 0? "": ApplicationHelper.getStatusName(getDocType(), toStatusId);
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	
	@Transient
	public String getActionName() {
		return ApplicationHelper.getActionName(getDocType(), actionId);
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	
	/**
	 * Дата создания
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
	/**
	 * id документа в формате "type_id"
	 */
	@Column(name="parent_id")
	private int parentId;
	
	/**
	 * Тип документа
	 */
	private String docType;
	
	/**
	 * Время начала
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date startDate;
	
	/**
	 * Время завершения
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date endDate;
	
	/**
	 * Пользователь, выполнивший действие
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User owner;
	
	/**
	 * Идентификатор начального статуса
	 */
	@Column(name="from_status_id")
	private int fromStatusId;
	
	/**
	 * Идентификатор конечного статуса
	 */
	@Column(name="to_status_id")
	private int toStatusId;
	
	/**
	 * Идентификатор действия
	 */
	@Column(name="action_id")
	private int actionId;
	
	/**
	 * Было ли выполнено действие: 0 - нет, 1 - да
	 */
	private boolean processed;
	
	/**
	 * Комментарий
	 */
    @Column(columnDefinition="text")
	private String commentary;

    @Override
    public int hashCode() {
        if (getUuid()!=null)
            return getUuid().hashCode();
        return (getCreated()!=null ? getCreated().hashCode() + getId(): getId());
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode()==obj.hashCode();
    }


    private static final long serialVersionUID = -67429605560038386L;


    @Override
    public int compareTo(HistoryEntry o) {
        if(created == null){
            return o.getCreated() == null ?  0 :  -1;
        } else {
            return o.getCreated() == null ? 1 : created.compareTo(o.getCreated());
        }
    }
}