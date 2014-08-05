package ru.efive.medicine.niidg.trfu.data.entity.medical;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.document.Document;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.wf.core.ProcessedData;

/**
 * Биоматериал (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_biomaterials")
public class Biomaterial extends Document implements ProcessedData, Cloneable {

    @Transient
    public String getFullNumber() {
        String result = "";
        if (operation != null && operation.getNumber() != null && !operation.getNumber().equals("")) {
            result = operation.getNumber() + (getNumber() == null ? "" : "-" + getNumber());
        } else {
            result = (getNumber() == null ? "" : getNumber());
        }
        return result;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setBiomaterialType(Classifier biomaterialType) {
        this.biomaterialType = biomaterialType;
    }

    public Classifier getBiomaterialType() {
        return biomaterialType;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public double getInitialVolume() {
        return initialVolume;
    }

    public void setInitialVolume(double initialVolume) {
        this.initialVolume = initialVolume;
    }

    public List<BiomaterialAdditionalSubstance> getAdditionalSubstances() {
        return additionalSubstances;
    }

    public void setAdditionalSubstances(List<BiomaterialAdditionalSubstance> additionalSubstances) {
        this.additionalSubstances = additionalSubstances;
    }

    public void addSubstance() {
        if (additionalSubstances == null) {
            additionalSubstances = new ArrayList<BiomaterialAdditionalSubstance>(0);
        }
        additionalSubstances.add(new BiomaterialAdditionalSubstance());
    }

    public void removeSubstance(BiomaterialAdditionalSubstance substance) {
        additionalSubstances.remove(substance);
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Anticoagulant getAnticoagulant() {
        return anticoagulant;
    }

    public void setAnticoagulant(Anticoagulant anticoagulant) {
        this.anticoagulant = anticoagulant;
    }

    public double getAnticoagulantVolume() {
        return anticoagulantVolume;
    }

    public void setAnticoagulantVolume(double anticoagulantVolume) {
        this.anticoagulantVolume = anticoagulantVolume;
    }

    public double getCellCount() {
        return cellCount;
    }

    public void setCellCount(double cellCount) {
        this.cellCount = cellCount;
    }

    public double getTnc() {
        return tnc;
    }

    public void setTnc(double tnc) {
        this.tnc = tnc;
    }

    public double getNcPerKg() {
        return ncPerKg;
    }

    public void setNcPerKg(double ncPerKg) {
        this.ncPerKg = ncPerKg;
    }

    public double getCd34() {
        return cd34;
    }

    public void setCd34(double cd34) {
        this.cd34 = cd34;
    }

    public double getCd34X6() {
        return cd34X6;
    }

    public void setCd34X6(double cd34x6) {
        cd34X6 = cd34x6;
    }

    public double getCd34X6PerKg() {
        return cd34X6PerKg;
    }

    public void setCd34X6PerKg(double cd34x6PerKg) {
        cd34X6PerKg = cd34x6PerKg;
    }

    public double getCd3() {
        return cd3;
    }

    public void setCd3(double cd3) {
        this.cd3 = cd3;
    }

    public double getCd3X8() {
        return cd3X8;
    }

    public void setCd3X8(double cd3x8) {
        cd3X8 = cd3x8;
    }

    public double getCd3x8PerKg() {
        return cd3x8PerKg;
    }

    public void setCd3x8PerKg(double cd3x8PerKg) {
        this.cd3x8PerKg = cd3x8PerKg;
    }

    public double getMncPercentage() {
        return mncPercentage;
    }

    public void setMncPercentage(double mncPercentage) {
        this.mncPercentage = mncPercentage;
    }

    public double getMncx9() {
        return mncx9;
    }

    public void setMncx9(double mncx9) {
        this.mncx9 = mncx9;
    }

    public void setProcessings(List<Processing> processings) {
        this.processings = processings;
    }

    public List<Processing> getProcessings() {
        /*if (processings != null) {
			Collections.sort(processings, new Comparator<Processing>() {
				
				@Override
				public int compare(Processing o1, Processing o2) {
					Calendar c1 = Calendar.getInstance(ApplicationHelper.getLocale());
					c1.setTime(o1.getProcessingDate());
					Calendar c2 = Calendar.getInstance(ApplicationHelper.getLocale());
					c2.setTime(o2.getProcessingDate());
					return c2.compareTo(c1);
				}
				
			});
		}*/
        return processings;
    }

    public void addProcessing(Processing processing) {
        if (processings == null) {
            processings = new ArrayList<Processing>();
        }
        processings.add(processing);
    }

    public void setParentBiomaterial(Biomaterial parentBiomaterial) {
        this.parentBiomaterial = parentBiomaterial;
    }

    public Biomaterial getParentBiomaterial() {
        return parentBiomaterial;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplitDate(Date splitDate) {
        this.splitDate = splitDate;
    }

    public Date getSplitDate() {
        return splitDate;
    }

    @Transient
    public String getType() {
        return "Biomaterial";
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Transient
    public String getStatusName() {
        return ApplicationHelper.getStatusName("Biomaterial", getStatusId());
    }

    public int getGrouping() {
        return grouping;
    }

    public void setGrouping(int grouping) {
        this.grouping = grouping;
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
     *
     * @param historyEntry Запись в истории компонента, которую надо добавить
     * @return статус добавления (true - успех)
     */
    public boolean addToHistory(final HistoryEntry historyEntry) {
        if (history == null) {
            this.history = new HashSet<HistoryEntry>(1);
        }
        return this.history.add(historyEntry);
    }

    public Biomaterial cloneComponent() {
        try {
            return (Biomaterial) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning not allowed.");
            return this;
        }
    }


    /**
     * Номер
     */
    private String number;

    /**
     * Дата/время получения
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date received;

    /**
     * Срок годности
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expirationDate;

    /**
     * Тип биоматериала
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Classifier biomaterialType;

    /**
     * Операция, в рамках которой получен биоматериал
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Operation operation;

    /**
     * Исходный объем
     */
    private double initialVolume;

    /**
     * Добавленный объём
     */
    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(name = "trfu_medical_biomaterial_substances",
            joinColumns = {@JoinColumn(name = "biomaterial_id")},
            inverseJoinColumns = {@JoinColumn(name = "substance_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BiomaterialAdditionalSubstance> additionalSubstances;

    /**
     * Объем биоматериала
     */
    private double volume;

    /**
     * Антикоагулянт
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Anticoagulant anticoagulant;

    /**
     * Объем антикоагулянта
     */
    private double anticoagulantVolume;

    /**
     * Количество клеток, х1011
     */
    private double cellCount;

    /**
     * TNC (x10 в 9 степени)
     */
    private double tnc;

    /**
     * NC/кг (x10 в 8 степени)
     */
    private double ncPerKg;

    /**
     * CD34
     */
    private double cd34;

    /**
     * CD34 (x10 в 6 степени)
     */
    private double cd34X6;

    /**
     * CD34 (x10 в 6 степени/кг)
     */
    private double cd34X6PerKg;

    /**
     * CD3
     */
    private double cd3;

    /**
     * CD3 (x10 в 8 степени)
     */
    private double cd3X8;

    /**
     * CD3 (x10 в 8 степени/кг)
     */
    private double cd3x8PerKg;

    /**
     * MNC,%
     */
    private double mncPercentage;

    /**
     * MNC (x10 в 9 степени)
     */
    private double mncx9;

    /**
     * История обработки
     */
    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(name = "trfu_medical_biomaterial_processings",
            joinColumns = {@JoinColumn(name = "biomaterial_id")},
            inverseJoinColumns = {@JoinColumn(name = "processing_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Processing> processings;

    /**
     * Получен путем разделения
     */
    private boolean split;

    /**
     * Биоматериал, из которого был получен
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Biomaterial parentBiomaterial;

    /**
     * Дата разделения
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date splitDate;

    /**
     * Текущий статус документа в процессе
     */
    @Column(name = "status_id")
    private int statusId;

    /**
     * Для группировок в представлениях
     */
    @Transient
    private int grouping = 100;

    /**
     * История
     */
    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JoinTable(name = "trfu_medical_biomaterial_history",
            joinColumns = {@JoinColumn(name = "biomaterial_id")},
            inverseJoinColumns = {@JoinColumn(name = "history_entry_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<HistoryEntry> history;


    private static final long serialVersionUID = 4774560475228785259L;
}