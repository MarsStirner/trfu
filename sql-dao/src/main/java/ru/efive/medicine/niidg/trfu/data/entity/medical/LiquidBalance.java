package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Гемодинамика и температура (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_liquid_balance")
public class LiquidBalance extends IdentifiedEntity {
	
	public String getAcdLoad() {
		return acdLoad;
	}

	public void setAcdLoad(String acdLoad) {
		this.acdLoad = acdLoad;
	}

	public String getNaClLoad() {
		return naClLoad;
	}

	public void setNaClLoad(String naClLoad) {
		this.naClLoad = naClLoad;
	}

	public String getCaLoad() {
		return caLoad;
	}

	public void setCaLoad(String caLoad) {
		this.caLoad = caLoad;
	}

	public String getOtherLoad() {
		return otherLoad;
	}

	public void setOtherLoad(String otherLoad) {
		this.otherLoad = otherLoad;
	}

	public String getTotalLoad() {
		return totalLoad;
	}

	public void setTotalLoad(String totalLoad) {
		this.totalLoad = totalLoad;
	}

	public String getPackRemove() {
		return packRemove;
	}

	public void setPackRemove(String packRemove) {
		this.packRemove = packRemove;
	}

	public String getOtherRemove() {
		return otherRemove;
	}

	public void setOtherRemove(String otherRemove) {
		this.otherRemove = otherRemove;
	}

	public String getTotalRemove() {
		return totalRemove;
	}

	public void setTotalRemove(String totalRemove) {
		this.totalRemove = totalRemove;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
	public String getErCurcuitFill() {
		return erCurcuitFill;
	}

	public void setErCurcuitFill(String erCurcuitFill) {
		this.erCurcuitFill = erCurcuitFill;
	}


	public String getBloodReturn() {
		return bloodReturn;
	}

	public void setBloodReturn(String bloodReturn) {
		this.bloodReturn = bloodReturn;
	}


	/**
	 * Введено ACD
	 */
	private String acdLoad;
	
	/**
	 * Введено NaCl
	 */
	private String naClLoad;
	
	/**
	 * Введено Ca++
	 */
	private String caLoad;
	
	/**
	 * Введено другое
	 */
	private String otherLoad;
	
	/**
	 * Введено всего
	 */
	private String totalLoad;
	
	/**
	 * Удалено в пакете
	 */
	private String packRemove;
	
	/**
	 * Удалено другое
	 */
	private String otherRemove;
	
	/**
	 * Удалено всего
	 */
	private String totalRemove;
	
	/**
	 * Баланс
	 */
	private String balance;
	
	/**
	 * Заполнение контура эр.массой
	 */
	private String erCurcuitFill;
	
	/**
	 * Возврат крови
	 */
	private String bloodReturn;
	
	private static final long serialVersionUID = -3408663464733786038L;
}