package ru.efive.medicine.niidg.trfu.data.entity.medical;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ru.efive.dao.sql.entity.document.Document;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;

/**
 * Процедурный лист (лечебный сегмент)
 */
@Entity
@Table(name = "trfu_medical_operation_reports")
public class OperationReport extends Document {
	
	public String getContraindication() {
		return contraindication;
	}

	public void setContraindication(String contraindication) {
		this.contraindication = contraindication;
	}

	public Hemodynamics getHemodynamicsBefore() {
		return hemodynamicsBefore;
	}

	public void setHemodynamicsBefore(Hemodynamics hemodynamicsBefore) {
		this.hemodynamicsBefore = hemodynamicsBefore;
	}

	public Hemodynamics getHemodynamicsAfter() {
		return hemodynamicsAfter;
	}

	public void setHemodynamicsAfter(Hemodynamics hemodynamicsAfter) {
		this.hemodynamicsAfter = hemodynamicsAfter;
	}

	public Classifier getEquipment() {
		return equipment;
	}

	public void setEquipment(Classifier equipment) {
		this.equipment = equipment;
	}

	public boolean isBloodWarmer() {
		return bloodWarmer;
	}

	public void setBloodWarmer(boolean bloodWarmer) {
		this.bloodWarmer = bloodWarmer;
	}

	public List<Supply> getSupplyList() {
		return supplyList;
	}

	public void setSupplyList(List<Supply> supplyList) {
		this.supplyList = supplyList;
	}
	
	public void addSupply() {
		if (supplyList == null) {
			supplyList = new ArrayList<Supply>();
		}
		supplyList.add(new Supply());
	}
	
	public void addSupply(Supply supply) {
		if (supplyList == null) {
			supplyList = new ArrayList<Supply>();
		}
		supplyList.add(supply);
	}

	public void setEritrocyteMass(EritrocyteMass eritrocyteMass) {
		this.eritrocyteMass = eritrocyteMass;
	}

	public EritrocyteMass getEritrocyteMass() {
		return eritrocyteMass;
	}

	public boolean isCollectCentralAccess() {
		return collectCentralAccess;
	}

	public void setCollectCentralAccess(boolean collectCentralAccess) {
		this.collectCentralAccess = collectCentralAccess;
	}

	public boolean isCollectLeft() {
		return collectLeft;
	}

	public void setCollectLeft(boolean collectLeft) {
		this.collectLeft = collectLeft;
	}

	public String getCollectCentralCommentary() {
		return collectCentralCommentary;
	}

	public void setCollectCentralCommentary(String collectCentralCommentary) {
		this.collectCentralCommentary = collectCentralCommentary;
	}

	public boolean isReturnCentralAccess() {
		return returnCentralAccess;
	}

	public void setReturnCentralAccess(boolean returnCentralAccess) {
		this.returnCentralAccess = returnCentralAccess;
	}

	public boolean isReturnLeft() {
		return returnLeft;
	}

	public void setReturnLeft(boolean returnLeft) {
		this.returnLeft = returnLeft;
	}

	public String getReturnCentralCommentary() {
		return returnCentralCommentary;
	}

	public void setReturnCentralCommentary(String returnCentralCommentary) {
		this.returnCentralCommentary = returnCentralCommentary;
	}

	public OperationParameters getInitialParameters() {
		return initialParameters;
	}

	public void setInitialParameters(OperationParameters initialParameters) {
		this.initialParameters = initialParameters;
	}

	public OperationParameters getChangedParameters() {
		return changedParameters;
	}

	public void setChangedParameters(OperationParameters changedParameters) {
		this.changedParameters = changedParameters;
	}
	
	public void setMeasureList(List<LaboratoryMeasure> measureList) {
		this.measureList = measureList;
	}

	public List<LaboratoryMeasure> getMeasureList() {
		return measureList;
	}
	
	public void addLaboratoryMeasure() {
		if (measureList == null) {
			measureList = new ArrayList<LaboratoryMeasure>();
		}
		measureList.add(new LaboratoryMeasure());
	}
	
	public void addLaboratoryMeasure(LaboratoryMeasure laboratoryMeasure) {
		if (measureList == null) {
			measureList = new ArrayList<LaboratoryMeasure>();
		}
		measureList.add(laboratoryMeasure);
	}

	public void setLiquidBalance(LiquidBalance liquidBalance) {
		this.liquidBalance = liquidBalance;
	}

	public LiquidBalance getLiquidBalance() {
		return liquidBalance;
	}

	public void setFinalVolumeList(List<FinalVolume> finalVolumeList) {
		this.finalVolumeList = finalVolumeList;
	}

	public List<FinalVolume> getFinalVolumeList() {
		return finalVolumeList;
	}
	
	public void addFinalVolume() {
		if (finalVolumeList == null) {
			finalVolumeList = new ArrayList<FinalVolume>();
		}
		finalVolumeList.add(new FinalVolume());
	}
	
	public void addFinalVolume(FinalVolume finalVolume) {
		if (finalVolumeList == null) {
			finalVolumeList = new ArrayList<FinalVolume>();
		}
		finalVolumeList.add(finalVolume);
	}

	public void setExtendedParameterList(List<ExtendedOperationParameters> extendedParameterList) {
		this.extendedParameterList = extendedParameterList;
	}

	public List<ExtendedOperationParameters> getExtendedParameterList() {
		return extendedParameterList;
	}
	
	public void addExtendedParameters() {
		if (extendedParameterList == null) {
			extendedParameterList = new ArrayList<ExtendedOperationParameters>();
		}
		extendedParameterList.add(new ExtendedOperationParameters());
	}
	
	public void addExtendedParameters(ExtendedOperationParameters extendedOperationParameters) {
		if (extendedParameterList == null) {
			extendedParameterList = new ArrayList<ExtendedOperationParameters>();
		}
		extendedParameterList.add(extendedOperationParameters);
	}

	public void setComplications(String complications) {
		this.complications = complications;
	}

	public String getComplications() {
		return complications;
	}

	public void setSalineVolumePhoto(double salineVolumePhoto) {
		this.salineVolumePhoto = salineVolumePhoto;
	}

	public double getSalineVolumePhoto() {
		return salineVolumePhoto;
	}

	public void setMetoxipsoralenVolume(double metoxipsoralenVolume) {
		this.metoxipsoralenVolume = metoxipsoralenVolume;
	}

	public double getMetoxipsoralenVolume() {
		return metoxipsoralenVolume;
	}

	/**
	 * Противопоказания
	 */
	private String contraindication;
	
	/**
	 * Гемодинамика и температура до процедуры
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Hemodynamics hemodynamicsBefore;
	
	/**
	 * Гемодинамика и температура после процедуры
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Hemodynamics hemodynamicsAfter;
	
	/**
	 * Оборудование
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Classifier equipment;
	
	/**
	 * Подогреватель крови
	 */
	private boolean bloodWarmer;
	
	/**
	 * Расходные материалы
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_medical_operation_report_supplies", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "supply_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Supply> supplyList;
	
	/**
	 * Эр. масса
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private EritrocyteMass eritrocyteMass;
	
	/**
	 * Линия забора
	 * true - центральный доступ, false - периферический доступ
	 */
	private boolean collectCentralAccess;
	
	/**
	 * Линия забора
	 * false - правая, true - левая рука
	 */
	private boolean collectLeft;
	
	/**
	 * Описание линии забора (центральный доступ)
	 */
	private String collectCentralCommentary;
	
	/**
	 * Линия возврата
	 * true - центральный доступ, false - периферический доступ
	 */
	private boolean returnCentralAccess;
	
	/**
	 * Линия возврата
	 * true - правая, false - левая рука
	 */
	private boolean returnLeft;
	
	/**
	 * Описание линии возврата (центральный доступ)
	 */
	private String returnCentralCommentary;
	
	/**
	 * Инициальные параметры процедуры
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private OperationParameters initialParameters;
	
	/**
	 * Изменения параметров процедуры
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private OperationParameters changedParameters;
	
	/**
	 * Лабораторные измерения
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_medical_operation_report_measures", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "measure_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<LaboratoryMeasure> measureList;
	
	/**
	 * Баланс жидкостей
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private LiquidBalance liquidBalance;
	
	/**
	 * Финальные объемы
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_medical_operation_report_final_volumes", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "volume_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<FinalVolume> finalVolumeList;
	
	/**
	 * Расширенные параметры процедуры
	 */
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinTable(name = "trfu_medical_operation_report_extended_parameters", 
			joinColumns = { @JoinColumn(name = "report_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "volume_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExtendedOperationParameters> extendedParameterList;
	
	/**
	 * Осложнения
	 */
	private String complications;
	
	/**
	 * Фотоферез: объем физиологического раствора
	 */
	private double salineVolumePhoto;
	
	/**
	 * Фотоферез: объем раствора метоксипсоралена (20 мкг/мл)
	 */
	private double metoxipsoralenVolume;
	
	
	private static final long serialVersionUID = -6552085533420571878L;
}