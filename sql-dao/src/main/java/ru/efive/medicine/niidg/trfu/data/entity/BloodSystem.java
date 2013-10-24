package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;

/**
 * Система крови
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_systems")
public class BloodSystem extends IdentifiedEntity {
	public BloodSystemType getType() {
        return type;
    }

    public void setType(BloodSystemType type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public String getSystemLot() {
		return systemLot;
	}

	public void setSystemLot(String systemLot) {
		this.systemLot = systemLot;
	}

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private BloodSystemType type;

    private Integer count = 1;
    
    private Date expirationDate;
    
    private Integer userId;
    
    private Integer roleId;
    
    private String systemLot;
	
	private static final long serialVersionUID = 1L;
}