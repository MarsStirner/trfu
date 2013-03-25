package ru.efive.medicine.niidg.trfu.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Протокол цитофереза
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_pheresis_reports")
public class PheresisReport extends IdentifiedEntity {
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}

	public double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public double getTotalAcVolume() {
		return totalAcVolume;
	}

	public void setTotalAcVolume(double totalAcVolume) {
		this.totalAcVolume = totalAcVolume;
	}

	public double getRecievedAcVolume() {
		return recievedAcVolume;
	}

	public void setRecievedAcVolume(double recievedAcVolume) {
		this.recievedAcVolume = recievedAcVolume;
	}

	public double getTotalPltVolume() {
		return totalPltVolume;
	}

	public void setTotalPltVolume(double totalPltVolume) {
		this.totalPltVolume = totalPltVolume;
	}

	public double getAcPltVolume() {
		return acPltVolume;
	}

	public void setAcPltVolume(double acPltVolume) {
		this.acPltVolume = acPltVolume;
	}

	public double getCollectedPlt() {
		return collectedPlt;
	}

	public void setCollectedPlt(double collectedPlt) {
		this.collectedPlt = collectedPlt;
	}

	public double getLabPltVolume() {
		return labPltVolume;
	}

	public void setLabPltVolume(double labPltVolume) {
		this.labPltVolume = labPltVolume;
	}

	public double getCorrelationPlt() {
		return correlationPlt;
	}

	public void setCorrelationPlt(double correlationPlt) {
		this.correlationPlt = correlationPlt;
	}

	public double getDonorPltBefore() {
		return donorPltBefore;
	}

	public void setDonorPltBefore(double donorPltBefore) {
		this.donorPltBefore = donorPltBefore;
	}

	public double getDonorPltAfter() {
		return donorPltAfter;
	}

	public void setDonorPltAfter(double donorPltAfter) {
		this.donorPltAfter = donorPltAfter;
	}

	public double getPlasmaVolume() {
		return plasmaVolume;
	}

	public void setPlasmaVolume(double plasmaVolume) {
		this.plasmaVolume = plasmaVolume;
	}

	public double getAcPlasmaVolume() {
		return acPlasmaVolume;
	}

	public void setAcPlasmaVolume(double acPlasmaVolume) {
		this.acPlasmaVolume = acPlasmaVolume;
	}

	public double getErVolume() {
		return erVolume;
	}

	public void setErVolume(double erVolume) {
		this.erVolume = erVolume;
	}
	
	public void setAcErVolume(double acErVolume) {
		this.acErVolume = acErVolume;
	}
	
	public double getAcErVolume() {
		return acErVolume;
	}

	public double getDonorHtBefore() {
		return donorHtBefore;
	}

	public void setDonorHtBefore(double donorHtBefore) {
		this.donorHtBefore = donorHtBefore;
	}

	public double getDonorHtAfter() {
		return donorHtAfter;
	}

	public void setDonorHtAfter(double donorHtAfter) {
		this.donorHtAfter = donorHtAfter;
	}
	
	public void setComplications(String complications) {
		this.complications = complications;
	}
	
	public String getComplications() {
		return complications;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}


	public void setPmn(double pmn) {
		this.pmn = pmn;
	}

	public double getPmn() {
		return pmn;
	}


	public void setGcsf(double gcsf) {
		this.gcsf = gcsf;
	}

	public double getGcsf() {
		return gcsf;
	}


	/**
	 * Длительность сеанса, мин
	 */
	private String time;
	
	/**
	 * Время завершения сеанса
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date endTime;
	
	/**
	 * Общий объем обработанной крови
	 */
	private double totalVolume;
	
	/**
	 * Общий объем использованного АК
	 */
	private double totalAcVolume;
	
	/**
	 * Объем АК, поступивший донору
	 */
	private double recievedAcVolume;
	
	/**
	 * Общий объем PLT-конц.
	 */
	private double totalPltVolume;
	
	/**
	 * Объем АК в PLT-конц.
	 */
	private double acPltVolume;
	
	/**
	 * Кол-во собр. PLT(х1011)
	 */
	private double collectedPlt;
	
	/**
	 * Лабораторноизмеренный выход PLT
	 */
	private double labPltVolume;
	
	/**
	 * Соотношение лабораторно-отчет по PLT
	 */
	private double correlationPlt;
	
	/**
	 * Кол-во PLT донора до процедуры
	 */
	private double donorPltBefore;
	
	/**
	 * Кол-во PLT донора после процедуры
	 */
	private double donorPltAfter;
	
	/**
	 * Объем собранной плазмы (мл)
	 */
	private double plasmaVolume;
	
	/**
	 * Объем АК в плазме (мл)
	 */
	private double acPlasmaVolume;
	
	/**
	 * Объем собранной эр.массы+консервир/раствор (мл)
	 */
	private double erVolume;
	
	/**
	 * Объем АК в эр.массе, мл
	 */
	private double acErVolume;
	
	/**
	 * Ht донора до процедуры
	 */
	private double donorHtBefore;
	
	/**
	 * Ht донора после процедуры
	 */
	private double donorHtAfter;
	
	/**
	 * Осложнения
	 */
	private String complications;
	
	/**
	 * Примечание
	 */
	private String commentary;
	
	/**
	 * PMN, %
	 */
	private double pmn;
	
	/**
	 * Доза Г-КСФ, мкг
	 */
	private double gcsf;
	
	
	private static final long serialVersionUID = 5811138468024242162L;
}