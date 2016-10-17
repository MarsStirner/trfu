package ru.efive.medicine.niidg.trfu.data.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.dictionary.Recommendation;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.*;

/**
 * Обращение на обследование
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_examination_requests")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExaminationRequest extends AbstractRequest implements ProcessedData {
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	@Transient
	public String getPrintableNumber() {
		return new StringBuffer().append(getDonor().getBloodGroup().getNumber()).append(StringUtils.right(getNumber(), 5)).toString();
	}
	
	public int getExaminationType() {
		return examinationType;
	}
	
	public void setExaminationType(int examinationType) {
		this.examinationType = examinationType;
	}
	
	public void setAnamnesis(Anamnesis anamnesis) {
		this.anamnesis = anamnesis;
	}

	public Anamnesis getAnamnesis() {
		return anamnesis;
	}
	
	public Set<Analysis> getTests() {
		return tests;
	}
	
	public void setTests(Set<Analysis> tests) {
		this.tests = tests;
	}
	
	@Transient
	public List<Analysis> getTestList() {
		List<Analysis> result = new ArrayList<Analysis>();
		if (tests != null && !tests.isEmpty()) {
			result.addAll(tests);
		}
		else {
			if (appointment != null && appointment.getTests() != null) {
				result = appointment.getTests();
			}
		}
		Collections.sort(result, new Comparator<Analysis>() {
			public int compare(Analysis o1, Analysis o2) {
				int result = o1.getType().getLevel() - o2.getType().getLevel();
				return result == 0? o1.getId() - o2.getId(): result;
			}
		});
		return result;
	}
	
	public void setExaminationEntries(Set<ExaminationEntry> examinationEntries) {
		this.examinationEntries = examinationEntries;
	}
	
	public Set<ExaminationEntry> getExaminationEntries() {
		return examinationEntries;
	}

	public void setExaminationEntryList(List<ExaminationEntry> examinationEntryList) {
		this.examinationEntryList = examinationEntryList;
	}
	
	public List<ExaminationEntry> getExaminationEntryList() {
		return examinationEntryList == null? new ArrayList<ExaminationEntry>(): examinationEntryList;
	}
	
	public List<Recommendation> getRecommendations() {
		return recommendations;
	}
	
	@Transient
	public List<Recommendation> getPrescriptedRecommendations() {
		List<Recommendation> result = new ArrayList<Recommendation>();
		if (recommendations != null) {
			for (Recommendation recommendation: recommendations) {
				if (recommendation.isPrescripted()) {
					result.add(recommendation);
				}
			}
		}
		return result;
	}

	public void setRecommendations(List<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}
	
	public void addRecommendation(Recommendation recommendation) {
		if (recommendations == null) {
			recommendations = new ArrayList<Recommendation>();
		}
		recommendations.add(recommendation);
	}
	
	public void setHistory(Set<HistoryEntry> history) {
		this.history = history;
	}
	
	public Set<HistoryEntry> getHistory() {
		return history;
	}
	
	@Transient
	public List<HistoryEntry> getHistoryList() {
		List<HistoryEntry> result = new ArrayList<HistoryEntry>();
		if (history != null) {
			result.addAll(history);
		}
		Collections.sort(result);
		return result;
	}
    /**
     * Добавление в историю компонента еще одной записи, если история пуста, то она создается
     * @param historyEntry Запись в истории компонента, которую надо добавить
     * @return  статус добавления (true - успех)
     */
    public boolean addToHistory(final HistoryEntry historyEntry) {
        if(history == null){
            this.history = new HashSet<HistoryEntry>(1);
        }
        return this.history.add(historyEntry);
    }
	
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	
	public Date getPlanDate() {
		return planDate;
	}
	
	@Transient
	public String getType() {
		return "Examination";
	}
	
	@Transient
	public String getStatusName() {
		String statusName = ApplicationHelper.getStatusName("Examination", getStatusId());
		if (getStatusId() == 9) {
			statusName = statusName + (planDate == null? "": " на " + new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(planDate));
		}
		return statusName;
	}

	private void initEntryValues() {
		List<ExaminationEntry> entryList = new ArrayList<ExaminationEntry>();
		entryList.addAll(examinationEntries);
		for (ExaminationEntry entry:entryList) {
			if(entry.getType().getValue().equals("Рост")) {
				donorHeight = entry.getValue();
			}
			else if(entry.getType().getValue().equals("Вес")) {
				donorWeight = entry.getValue();
			}
			else if(entry.getType().getValue().equals("Пульс")) {
				donorPulse = entry.getValue();
			}
			else if(entry.getType().getValue().equals("АД мм.рт.ст.")) {
				donorAD = entry.getValue();
			}
			else if(entry.getType().getValue().equals("Температура, t° C")) {
				donorTemperature = entry.getValue();
			}
		}
	}
	
	private void initAnalysisValues() {
		List<Analysis> analysisList = getTestList();
		for (Analysis analysis:analysisList) {
			if (analysis.getType().getValue().equals("Rh - фактор")) {
				donorRhesusFactor = analysis.getValue();
			}
			else if (analysis.getType().getValue().equals("Группа крови(экспресс-диагностика)")) {
				donorBloodGroup = analysis.getValue();
			}
		}
	}


	/**
	 * Набор методов для печати в результатах обследования
	 */
	@Transient
	public String getDonorHeight() {
		if (donorHeight == null) initEntryValues();
		return donorHeight;
	}
	@Transient
	public String getDonorWeight() {
		if (donorWeight == null) initEntryValues();
		return donorWeight;
	}
	@Transient
	public String getDonorPulse() {
		if (donorPulse == null) initEntryValues();
		return donorPulse;
	}
	@Transient
	public String getDonorAD() {
		if (donorAD == null) initEntryValues();
		return donorAD;
	}
	@Transient
	public String getDonorTemperature() {
		if (donorTemperature == null) initEntryValues();
		return donorTemperature;
	}
	
	public String getDonorRhesusFactor() {
		if (donorRhesusFactor == null) initAnalysisValues();
		return donorRhesusFactor;
	}
	
	public String getDonorBloodGroup() {
		if (donorBloodGroup == null) initAnalysisValues();
		return donorBloodGroup;
	}
	
	public String getAnalysisResultByCategory(String category) {
		List<Analysis> testList = getTestList();
		if (testList.size() > 0) {
			for (Analysis analysis: testList) {
				if (analysis.getType().getValue().equals(category)) {
					return analysis.getValue();
				}
			}
		}
		return "";
	}
	
	public ExternalAppointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(ExternalAppointment appointment) {
		this.appointment = appointment;
	}

    public List<ExternalAnalysisResult> getAdditionalResults() {
        return additionalResults;
    }

    public void setAdditionalResults(List<ExternalAnalysisResult> additionalResults) {
        this.additionalResults = additionalResults;
    }

    /**
	 * Номер обращения
	 */
	private String number;
	
	/**
	 * Тип обращения на обследование: 0 - первичный, 1 - вторичный
	 */
	private int examinationType;
	
	/**
	 * Анамнез
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Anamnesis anamnesis;
	
	/**
	 * Анализы
	 */
	@OneToMany(cascade = CascadeType.REFRESH)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Analysis> tests;
	
	@Transient
	private List<ExaminationEntry> examinationEntryList;
	
	/**
	 * Записи осмотра
	 */
	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "trfu_examination_request_entries", 
			joinColumns = { @JoinColumn(name = "request_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<ExaminationEntry> examinationEntries;
	
	/**
	 * Рекомендации
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "trfu_examination_request_recommendations", 
			joinColumns = { @JoinColumn(name = "request_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "recommendation_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Recommendation> recommendations;
	
	/**
	 * История
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_examination_request_history", 
			joinColumns = { @JoinColumn(name = "request_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	
	/**
	 * Запланировано на
	*/
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date planDate;
	
	@Transient
	private String donorHeight;
	@Transient
	private String donorWeight;
	@Transient
	private String donorPulse;
	@Transient
	private String donorAD;
	@Transient
	private String donorTemperature;
	@Transient
	private String donorRhesusFactor;
	@Transient
	private String donorBloodGroup;
	
	/**
	 * Направление на анализы
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ExternalAppointment appointment;

    @Transient
    private List<ExternalAnalysisResult> additionalResults;
	
	private static final long serialVersionUID = 1680946284293102300L;
}