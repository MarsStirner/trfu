package ru.efive.dao.sql.entity.user;

import ru.efive.dao.sql.entity.IdentifiedEntity;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Пользователь системы
 */
@Entity
@Table(name = "person")
public class User extends IdentifiedEntity {

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public List<Role> getRoleList() {
		List<Role> result = new ArrayList<Role>();
		if (roles != null) {
			result.addAll(roles);
		}
		Collections.sort(result, new Comparator<Role>() {
			public int compare(Role o1, Role o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return result;
	}
	
	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
	
	public Role getSelectedRole() {
		return selectedRole;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	/**
	 * полное имя
	 */
	@Transient
	public String getDescription() {
		return lastName + " " + (firstName != null && !firstName.equals("")? firstName + " ": "") + 
		(middleName != null && !middleName.equals("")? middleName: "");
	}
	
	/**
	 * краткая форма полного имени
	 */
	@Transient
	public String getDescriptionShort() {
		return lastName + " " + (firstName != null && !firstName.equals("")? firstName.substring(0, 1) + ". ": "") + 
		(middleName != null && !middleName.equals("")? middleName.substring(0, 1) + ".": "");
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getLastnameFM() {
		return ((StringUtils.isNotEmpty(lastName) ? lastName + " " : "") +
				(StringUtils.isNotEmpty(firstName) ? firstName.substring(0, 1) + "." : "") +
				(StringUtils.isNotEmpty(middleName) ? middleName.substring(0, 1) + "." : ""));
	}
	
	/**
	 * для использования в теге e5ui:column
	 */
	@Transient
	public Boolean getPropertyDeleted() {
		return isDeleted();
	}
	@Transient
	public void setPropertyDeleted(Boolean deleted) {
		setDeleted(deleted);
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
	 * учетная запись
	 */
	@Column(unique = true)
	private String login;

	/**
	 * пароль
	 */
	private String password;

	/**
	 * email
	 */
	//@Column(unique = true)
	private String email;

	/**
	 * роли
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "person_roles", 
			joinColumns = { @JoinColumn(name = "person_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	@IndexColumn(name="id")
	private Set<Role> roles;

	/**
	 * роль по умолчанию
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Role selectedRole;
	
	/**
	 * фамилия
	 */
	private String lastName;

	/**
	 * имя
	 */
	private String firstName;

	/**
	 * отчество
	 */
	private String middleName;
	
	/**
	 * должность
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Appointment appointment;
	
	/**
	 * дата создания учетной записи
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;

	/**
	 * true - пользователь удалён, false или null - не удалён
	 */
	private Boolean deleted;

    /**
     * рабочий телефон
     */
    private String phone;

    /**
     * мобильный телефон
     */
    private String mobilePhone;

	private static final long serialVersionUID = -7649892958713448678L;
}