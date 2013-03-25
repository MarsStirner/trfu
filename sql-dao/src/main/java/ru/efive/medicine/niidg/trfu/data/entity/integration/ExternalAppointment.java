package ru.efive.medicine.niidg.trfu.data.entity.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;

/**
 * Направление на исследования
 */
@Entity
@Table(name = "trfu_external_appointments")
public class ExternalAppointment extends IdentifiedEntity {
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public List<Analysis> getTests() {
		return tests;
	}
	
	public void setTests(List<Analysis> tests) {
		this.tests = tests;
	}
	
	public Date getRegistrationDate() {
		return registrationDate;
	}
	
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public List<ExternalAnalysisEntry> getHistoryEntries() {
		return historyEntries;
	}
	
	public void setHistoryEntries(List<ExternalAnalysisEntry> historyEntries) {
		this.historyEntries = historyEntries;
	}
	
	public void addHistoryEntry(ExternalAnalysisEntry historyEntry) {
		if (historyEntries == null) {
			historyEntries = new ArrayList<ExternalAnalysisEntry>();
		}
		historyEntries.add(historyEntry);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Transient
	public List<ExternalIndicator> getIndicators() {
		List<ExternalIndicator> list = new ArrayList<ExternalIndicator>();
		try {
			for (Analysis analysis: getTests()) {
				if (analysis.getType().isLaboratoryTest() && analysis.getType().getIndicators() != null && analysis.getType().getIndicators().size() > 0) {
					list.addAll(analysis.getType().getIndicators());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * дата создания документа
	*/
    @Temporal(value = TemporalType.TIMESTAMP)
	private Date created;
	
    /**
     * Автор
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private User author;
    
    /**
	 * Анализы
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_external_appointment_tests", 
			joinColumns = { @JoinColumn(name = "appointment_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "test_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Analysis> tests;
    
    /**
     * дата/время регистрации биоматериала в лаборатории
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date registrationDate;
    
    /**
	 * Анализы
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_external_appointment_analysis_entries", 
			joinColumns = { @JoinColumn(name = "appointment_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id") })
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<ExternalAnalysisEntry> historyEntries;
    
    /**
     * Запись активна
     */
    private boolean active;
    
	/**
	 * Удален ли документ
	 */
	private boolean deleted;
	
	
	private static final long serialVersionUID = 1L;
}