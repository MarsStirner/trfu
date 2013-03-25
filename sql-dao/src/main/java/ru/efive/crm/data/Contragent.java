package ru.efive.crm.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Контрагент
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "crm_contragents")
public class Contragent extends IdentifiedEntity {
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public ContragentNomenclature getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(ContragentNomenclature nomenclature) {
		this.nomenclature = nomenclature;
	}
	
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
	
	public int getStateCode() {
		return stateCode;
	}
	
	public void setOrganizationCode(int organizationCode) {
		this.organizationCode = organizationCode;
	}
	
	public int getOrganizationCode() {
		return organizationCode;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	@Override
	public String toString() {
		return StringUtils.isNotEmpty(fullName)? fullName: shortName;
	}
	
	
	/**
	 * Полное наименование
	 */
	private String fullName;
	
	/**
	 * Краткое наименование
	 */
	private String shortName;
	
	/**
	 * Номенклатура
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name= "nomenclature_id")
	private ContragentNomenclature nomenclature;
	
	// Код объекта республиканского подчинения, на территории которого находится организация
	private int stateCode;
	
	// код организации, проводившей донацию, уникальный на территории объекта республиканского подчинения
	private int organizationCode;
	
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	private static final long serialVersionUID = 1123233260758669450L;
}