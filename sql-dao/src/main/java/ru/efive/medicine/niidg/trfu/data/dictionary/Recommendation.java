package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Рекоммендация
 */
@Entity
@Table(name = "trfu_examination_recommendations")
public class Recommendation extends DictionaryEntity {
	
	public Classifier getType() {
		return type;
	}
	
	public void setType(Classifier type) {
		this.type = type;
	}
	
	public boolean isPrescripted() {
		return prescripted;
	}
	
	public void setPrescripted(boolean prescripted) {
		this.prescripted = prescripted;
	}
	
	public boolean isDecisionReceived() {
		return decisionReceived;
	}
	
	public void setDecisionReceived(boolean decisionReceived) {
		this.decisionReceived = decisionReceived;
	}
	
	
	/**
	 * Тип рекомендации
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier type;
	
	/**
	 * Назначена
	 */
	private boolean prescripted;
	
	/**
	 * Получено заключение
	 */
	private boolean decisionReceived;
	
	
	private static final long serialVersionUID = 1L;
}