package ru.efive.medicine.niidg.trfu.data.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.AbstractRequest;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.medicine.niidg.trfu.data.entity.operational.OperationalRoom;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.*;

/**
 * Донация
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_blood_donation_requests")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BloodDonationRequest extends AbstractRequest implements ProcessedData {
	//TODO
	//TODO Удалить поле "Врач-Трансфузиолог", наследуемое из AbstractRequest
	//TODO Удалить поле "Медицинская сестра", наследуемое из AbstractRequest

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}
	
	@Transient
	public String getPrintableNumber() {
		return String.valueOf(getDonor().getBloodGroup().getNumber()) + StringUtils.right(getNumber(), 5);
	}

	public Date getFactDate() {
		return factDate;
	}

	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}

	@Transient
	public List<BloodDonationType> getResultDonationTypeList() {
		List<BloodDonationType> result = new ArrayList<BloodDonationType>();
		try {
			if (getFactEntryList().size() > 0) {
				for (BloodDonationEntry entry: getFactEntryList()) {
					result.add(entry.getDonationType());
				}
			}
			else {
				for (BloodDonationEntry entry: getEntryList()) {
					result.add(entry.getDonationType());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public User getTransfusiologist() {
		return transfusiologist;
	}

	public void setTransfusiologist(User transfusiologist) {
		this.transfusiologist = transfusiologist;
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
	
	public void setTestsImmuno(Set<Analysis> testsImmuno) {
		this.testsImmuno = testsImmuno;
	}
	
	public Set<Analysis> getTestsImmuno() {
		return testsImmuno;
	}
	
	@Transient
	public List<Analysis> getTestImmunoList() {
		List<Analysis> result = new ArrayList<Analysis>();
		if (testsImmuno != null) {
			result.addAll(testsImmuno);
		}
		Collections.sort(result, new Comparator<Analysis>() {
			public int compare(Analysis o1, Analysis o2) {
				int result = o1.getType().getLevel() - o2.getType().getLevel();
				return result == 0? o1.getId() - o2.getId(): result;
			}
		});
		return result;
	}

	public void setRepetition(int repetition) {
		this.repetition = repetition;
	}

	public int getRepetition() {
		return repetition;
	}

	public void setDonorType(Classifier donorType) {
		this.donorType = donorType;
	}

	public Classifier getDonorType() {
		return donorType;
	}

	public void setReportBlankNumber(String reportBlankNumber) {
		this.reportBlankNumber = reportBlankNumber;
	}

	public String getReportBlankNumber() {
		return reportBlankNumber;
	}

	public void setEntries(Set<BloodDonationEntry> entries) {
		this.entries = entries;
	}

	public Set<BloodDonationEntry> getEntries() {
		return entries;
	}

	public void addEntry() {
		if (entries == null || entries.isEmpty()) {
			entries = new java.util.HashSet<BloodDonationEntry>();
		}
		entries.add(new BloodDonationEntry());
	}
	
	public void removeEntry(BloodDonationEntry entry) {
		entries.remove(entry);
	}

	@Transient
	public List<BloodDonationEntry> getEntryList() {
		List<BloodDonationEntry> result = new ArrayList<BloodDonationEntry>();
		if (entries != null) {
			result.addAll(entries);
		}
		Collections.sort(result, new Comparator<BloodDonationEntry>() {
			public int compare(BloodDonationEntry o1, BloodDonationEntry o2) {
				return o2.getId() - o1.getId();
			}
		});
		return result;
	}

	public void setFactEntries(Set<BloodDonationEntry> factEntries) {
		this.factEntries = factEntries;
	}

	public Set<BloodDonationEntry> getFactEntries() {
		return factEntries;
	}

	public void addToFactEntry(BloodDonationEntry newEntry) {
		if (factEntries == null || factEntries.isEmpty()) {
			factEntries = new java.util.HashSet<BloodDonationEntry>(1);
		}
		factEntries.add(newEntry);
	}
	
	public void removeFactEntry(BloodDonationEntry entry) {
		factEntries.remove(entry);
	}

	@Transient
	public List<BloodDonationEntry> getFactEntryList() {
		List<BloodDonationEntry> result = new ArrayList<BloodDonationEntry>();
		if (factEntries != null) {
			result.addAll(factEntries);
		}
		Collections.sort(result, new Comparator<BloodDonationEntry>() {
			public int compare(BloodDonationEntry o1, BloodDonationEntry o2) {
				return o2.getId() - o1.getId();
			}
		});
		return result;
	}
	
	@Transient
	public boolean isPheresisNecessary() {
		boolean result = false;
		if (getReport() == null) {
			if (getFactEntries() != null && getFactEntries().size() > 0) {
				for (BloodDonationEntry entry: getFactEntries()) {
					if (entry.getDonationType() != null && StringUtils.contains(entry.getDonationType().getValue(), "ферез")) {
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	public void addPheresis() {
		if (getReport() == null) {
			setReport(new PheresisReport());
		}
	}
	
	public void setAnalysisCount(int analysisCount) {
		this.analysisCount = analysisCount;
	}
	
	public int getAnalysisCount() {
		return analysisCount;
	}
	
	public void setExamination(ExaminationRequest examination) {
		this.examination = examination;
	}
	
	public ExaminationRequest getExamination() {
		return examination;
	}
	
	public void setReport(PheresisReport report) {
		this.report = report;
	}
	
	public PheresisReport getReport() {
		return report;
	}
	
	public void setTechProcesses(Set<Classifier> techProcesses) {
		this.techProcesses = techProcesses;
	}
	
	public Set<Classifier> getTechProcesses() {
		return techProcesses;
	}
	
	@Transient
	public String getType() {
		return "BloodDonation";
	}

	@Transient
	public String getStatusName() {
		return ApplicationHelper.getStatusName("BloodDonation", getStatusId());
	}
	
	/**
	 * Донация на цитоферез
	 */
	@Transient
	public boolean isPheresis() {
		boolean result = false;
		try {
			for (BloodDonationEntry entry: getEntryList()) {
				if (StringUtils.contains(entry.getDonationType().getValue(), "ферез")) {
					result = true;
				}
			}
			if (!result) {
				for (BloodDonationEntry entry: getFactEntryList()) {
					if (StringUtils.contains(entry.getDonationType().getValue(), "ферез")) {
						result = true;
					}
				}
			}
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
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

    //TODO 1) Выделить в интерфейс ?
    //TODO 2) Поменять везде добавление записи в историю на вызов метода, а не на получение + модификацию списка + установку
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

	public void setBloodSystems(List<BloodSystem> bloodSystems) {
		this.bloodSystems = bloodSystems;
	}
	
	public List<BloodSystem> getBloodSystems() {
		return bloodSystems;
	}
	
	public void setComplications(String complications) {
		this.complications = complications;
	}
	
	public String getComplications() {
		return complications;
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

	public OperationalRoom getOperationalRoom() {
		return operationalRoom;
	}

	public void setOperationalRoom(OperationalRoom operationalRoom) {
		this.operationalRoom = operationalRoom;
	}

	/**
	 * Номер донации
	 */
	private String number;

	/**
	 * Фактическая дата
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date factDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="operationalRoom_id")
	private OperationalRoom operationalRoom;

	/**
	 * Врач-трансфузиолог
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private User transfusiologist;

	/**
	 * Анализы
	 */
	@OneToMany(cascade = CascadeType.REFRESH)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Analysis> tests;
	
	/*@Transient
	private List<Analysis> testList;*/
	
	/**
	 * Иммуносерология - анализы
	 */
	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "trfu_blood_donation_request_tests_immuno")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Analysis> testsImmuno;
	
	/**
	 * Кратность
	 */
	private int repetition;

	/**
	 * Тип донора (для донации)
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier donorType;

	/**
	 * Номер бланка строгой отчетности
	 */
	private String reportBlankNumber;

	/**
	 * Рекомендуемые донации
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "trfu_blood_donation_request_entries", 
			joinColumns = { @JoinColumn(name = "donation_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<BloodDonationEntry> entries;

	/**
	 * Фактические донации
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "trfu_blood_donation_request_fact_entries", 
			joinColumns = { @JoinColumn(name = "donation_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<BloodDonationEntry> factEntries;
	
	/**
	 * Отправлено на анализы, мл
	 */
	private int analysisCount;
	
	/**
	 * Обращение на обследование - основание донации
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private ExaminationRequest examination;
	
	/**
	 * Протокол цитофереза
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private PheresisReport report;
	
	/**
	 * Технологические процессы
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trfu_blood_donation_request_processes", 
			joinColumns = { @JoinColumn(name = "donation_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "process_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Classifier> techProcesses;
	
	/**
	 * История
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_blood_donation_request_history", 
			joinColumns = { @JoinColumn(name = "request_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "history_entry_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HistoryEntry> history;
	

	/**
	 * Системы крови
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private List<BloodSystem> bloodSystems;

	
	/**
	 * Осложнения
	 */
	private String complications;
	
	/**
	 * Направление на анализы
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ExternalAppointment appointment;
	
	@Transient
    private List<ExternalAnalysisResult> additionalResults;

	private static final long serialVersionUID = -4072023462849191731L;
}
