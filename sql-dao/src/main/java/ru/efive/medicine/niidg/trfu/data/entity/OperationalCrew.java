package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;

/**
 * Бригада операционной
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_operational_crew")
public class OperationalCrew extends IdentifiedEntity {
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public User getAuthor() {
		return author;
	}

	public Date getRegistered() {
		return registered;
	}
	
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	
	public Set<User> getStaff() {
		return staff;
	}
	
	public void setStaff(Set<User> staff) {
		this.staff = staff;
	}
	
	@Transient
	public List<User> getStaffList() {
		List<User> result = new ArrayList<User>();
		if (staff != null) {
			result.addAll(staff);
		}
		Collections.sort(result, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getId() - o2.getId();
			}
		});
		return result;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public boolean isDeleted() {
		return deleted;
	}


	/**
	 * Автор документа
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User author;
	
	/**
	 * дата начала работы
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date registered;
	
	@ManyToMany
	@Cascade({ org.hibernate.annotations.CascadeType.REFRESH })
	@JoinTable(name = "trfu_operational_crew_members", 
			joinColumns = { @JoinColumn(name = "crew_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "member_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> staff;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	private static final long serialVersionUID = -2749690909846012707L;
}