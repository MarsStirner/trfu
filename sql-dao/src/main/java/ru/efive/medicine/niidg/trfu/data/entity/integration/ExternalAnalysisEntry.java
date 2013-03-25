package ru.efive.medicine.niidg.trfu.data.entity.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * Запись в журнале получения результатов исследований
 */
@Entity
@Table(name = "trfu_external_analysis_entries")
public class ExternalAnalysisEntry extends IdentifiedEntity {
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public int getLaboratoryPhysicianId() {
		return laboratoryPhysicianId;
	}
	
	public void setLaboratoryPhysicianId(int laboratoryPhysicianId) {
		this.laboratoryPhysicianId = laboratoryPhysicianId;
	}
	
	public String getBiomaterialDefects() {
		return biomaterialDefects;
	}
	
	public void setBiomaterialDefects(String biomaterialDefects) {
		this.biomaterialDefects = biomaterialDefects;
	}
	
	public List<ExternalAnalysisResult> getResults() {
		return results;
	}
	
	public void setResults(List<ExternalAnalysisResult> results) {
		this.results = results;
	}
	
	public void addResult(ExternalAnalysisResult result) {
		if (results == null) {
			results = new ArrayList<ExternalAnalysisResult>();
		}
		results.add(result);
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	/**
	 * дата создания документа
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
    /**
     * уникальный идентификационный номер врача лаборатории подписавшего результаты исследования
     */
    private int laboratoryPhysicianId;
    
    /**
     * Дефекты биоматерала
     */
    private String biomaterialDefects;
    
    /**
	 * результаты анализов для показателей, в том числе, обнаруженные микроорганизмы
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_external_analysis_entry_results", 
			joinColumns = { @JoinColumn(name = "entry_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "result_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExternalAnalysisResult> results;
    
    /**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1L;
}