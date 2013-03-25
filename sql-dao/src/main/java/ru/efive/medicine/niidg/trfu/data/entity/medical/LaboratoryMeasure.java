package ru.efive.medicine.niidg.trfu.data.entity.medical;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Лабораторные измерения (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_laboratory_measures")
public class LaboratoryMeasure extends IdentifiedEntity {
	
	public LaboratoryMeasure() {
		
	}
	
	public LaboratoryMeasure(Classifier type) {
		setType(type);
	}
	
	public Classifier getType() {
		return type;
	}

	public void setType(Classifier type) {
		this.type = type;
	}

	public String getBeforeOperation() {
		return beforeOperation;
	}

	public void setBeforeOperation(String beforeOperation) {
		this.beforeOperation = beforeOperation;
	}

	public String getDuringOperation() {
		return duringOperation;
	}

	public void setDuringOperation(String duringOperation) {
		this.duringOperation = duringOperation;
	}

	public String getInProduct() {
		return inProduct;
	}

	public void setInProduct(String inProduct) {
		this.inProduct = inProduct;
	}

	public String getAfterOperation() {
		return afterOperation;
	}

	public void setAfterOperation(String afterOperation) {
		this.afterOperation = afterOperation;
	}
	
	
	/**
	 * Тип измерения
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier type;
	
	/**
	 * До процедуры
	 */
	private String beforeOperation;
	
	/**
	 * Во время процедуры
	 */
	private String duringOperation;
	
	/**
	 * В продукте афереза
	 */
	private String inProduct;
	
	/**
	 * После процедуры
	 */
	private String afterOperation;
	
	
	private static final long serialVersionUID = 774547757943469747L;
}