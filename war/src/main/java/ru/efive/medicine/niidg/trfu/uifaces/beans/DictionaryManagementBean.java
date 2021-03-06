package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.crm.data.ContragentNomenclature;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentStatus;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationStatus;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorCategory;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorStatus;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorType;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationEntryType;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationStatus;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Gender;
import ru.efive.medicine.niidg.trfu.data.dictionary.ProcessingType;
import ru.efive.medicine.niidg.trfu.data.dictionary.QualityControlMappingEntry;
import ru.efive.medicine.niidg.trfu.data.dictionary.Recommendation;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

@Named("dictionaryManagement")
@RequestScoped
public class DictionaryManagementBean implements Serializable {
	
	public List<BloodGroup> getBloodGroups() {
		List<BloodGroup> result = new ArrayList<BloodGroup>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodGroups(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodGroup> getBloodGroupsWithEmpty() {
		List<BloodGroup> result = new ArrayList<BloodGroup>();
		BloodGroup bloodGroup = new BloodGroup();
		bloodGroup.setValue("");
		result.add(bloodGroup);
		try {
			result.addAll(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodGroups(false, null, false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public BloodGroup getBloodGroupByNumber(int number) {
		BloodGroup result = null;
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodGroupByNumber(number);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Anticoagulant> getAnticoagulants() {
		List<Anticoagulant> result = new ArrayList<Anticoagulant>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnticoagulants(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<ContragentNomenclature> getContragentNomenclature() {
		List<ContragentNomenclature> result = new ArrayList<ContragentNomenclature>();
		try {
			result = sessionManagement.getDictionaryDAO(ru.efive.crm.dao.NomenclatureDAOImpl.class, "contragentNomenclatureDao").findDocuments();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<ExaminationEntryType> getExaminationEntryTypes() {
		List<ExaminationEntryType> result = new ArrayList<ExaminationEntryType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findExaminationEntryTypes(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<ExaminationEntryType> getExaminationEntryTypesWithEmpty() {
		List<ExaminationEntryType> result = new ArrayList<ExaminationEntryType>();
		ExaminationEntryType entryType = new ExaminationEntryType();
		entryType.setValue("");
		result.add(entryType);
		try {
			result.addAll(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findExaminationEntryTypes(false, null, false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponentType> getBloodComponentTypes() {
		List<BloodComponentType> result = new ArrayList<BloodComponentType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodComponentTypes(false, "code", true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponentType> getBloodComponentTypes(String filter) {
		List<BloodComponentType> result = new ArrayList<BloodComponentType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodComponentTypes(filter, false, "code", true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponentType> getUsedBloodComponentTypes() {
		List<BloodComponentType> result = new ArrayList<BloodComponentType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findUsedBloodComponentTypes(false, "code", true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodComponentType> getUsedBloodComponentTypesWithEmpty() {
		List<BloodComponentType> result = new ArrayList<BloodComponentType>();
		BloodComponentType componentType = new BloodComponentType();
		componentType.setCode("");
		componentType.setValue("");
		result.add(componentType);
		try {
			result.addAll(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findUsedBloodComponentTypes(false, "code", true));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<ProcessingType> getProcessingTypes() {
		List<ProcessingType> result = new ArrayList<ProcessingType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findProcessingTypes(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BloodDonationType> getBloodDonationTypes() {
		List<BloodDonationType> result = new ArrayList<BloodDonationType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodDonationTypes(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<DonorType> getDonorTypes() {
		List<DonorType> result = new ArrayList<DonorType>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findDonorTypes(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public QualityControlMappingEntry getQualityControlMappingEntry(BloodComponentType componentType) {
		QualityControlMappingEntry result = null;
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findQualityControlMappingEntries(componentType, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Recommendation> getRecommendations() {
		List<Recommendation> result = new ArrayList<Recommendation>();
		try {
			result = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findRecommendations(false, null, false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Classifier> getByCategory(String category) {
		return sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByCategory(Classifier.class, category, false);
	}
	
	public List<Classifier> getByCategoryWithEmpty(String category) {
		List<Classifier> result = new ArrayList<Classifier>();
		Classifier classifier = new Classifier();
		classifier.setValue("");
		result.add(classifier);
		try {
			result.addAll(sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByCategory(Classifier.class, category, false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getValuesByCategory(String category) {
		List<String> result = new ArrayList<String>();
		try {
			List<Classifier> list =  sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByCategory(Classifier.class, category, false);
			for (Classifier classifier: list) {
				result.add(classifier.getValue());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getValuesByCategoryWithEmpty(String category) {
		List<String> result = new ArrayList<String>();
		String empty = "";
		result.add(empty);
		try {
			result.addAll(getValuesByCategory(category));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getClassifierCategories() {
		List<String> result = new ArrayList<String>();
		try {
			result =  sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findClassifierCategories();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getAvailableStatusName() {
		List<String> result = new ArrayList<String>();
		result.add("Брак");
		result.add("В карантине");
		result.add("Выдан");
		result.add("Готов к выдаче");
		result.add("Забронирован");
		result.add("Задержка");
		result.add("Зарегистрирован");
		result.add("Разделен");
		result.add("Списан");
		result.add("Утилизирован");
		return result;
	}
	
	public List<String> getAvailableStatusNameWithEmpty() {
		List<String> result = new ArrayList<String>();
		result.add("");
		result.add("Брак");
		result.add("В карантине");
		result.add("Выдан");
		result.add("Готов к выдаче");
		result.add("Забронирован");
		result.add("Задержка");
		result.add("Зарегистрирован");
		result.add("Разделен");
		result.add("Списан");
		result.add("Утилизирован");
		return result;
	}
	
	
	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement = new SessionManagementBean();
	
	
	private static final long serialVersionUID = -7887415733134653639L;
	
	/**
	 * Возвращает список полов (женский, мужской).
	 * 
	 * @return список полов.
	 */
	public List<Gender> getGenders() {
		return getDictionaryDAO().getGenders();
	}

	/**
	 * Возвращает список статусов доноров.
	 * 
	 * @return список статусов доноров.
	 */
	public List<DonorStatus> getDonorStatuses() {
		return getDictionaryDAO().getDonorStatuses();
	}

	/**
	 * Возвращает список статусов компонентов крови.
	 * 
	 * @return список статусов компонентов крови.
	 */
	public List<BloodComponentStatus> getBloodComponentStatuses() {
		return getDictionaryDAO().getBloodComponentStatuses();
	}

	/**
	 * Возвращает список статусов обращений на обследование.
	 * 
	 * @return список статусов обращений на обследование.
	 */
	public List<ExaminationStatus> getExaminationStatuses() {
		return getDictionaryDAO().getExaminationStatuses();
	}

	/**
	 * Возвращает список типов обращений на обследование.
	 * 
	 * @return список типов обращений на обследование.
	 */
	public List<ExaminationType> getExaminationTypes() {
		return getDictionaryDAO().getExaminationTypes();
	}

	/**
	 * Возвращает список статусов донаций крови.
	 * 
	 * @return список статусов донаций крови.
	 */
	public List<BloodDonationStatus> getBloodDonationStatuses() {
		return getDictionaryDAO().getBloodDonationStatuses();
	}

	/**
	 * Возвращает список видов трансфузии.
	 * 
	 * @return список видов трансфузии.
	 */
	public List<TransfusionType> getTransfusionTypes() {
		return getDictionaryDAO().getTransfusionTypes();
	}

	/**
	 * Возвращает список категорий доноров.
	 * 
	 * @return список видов категорий доноров.
	 */
	public List<DonorCategory> getDonorCategories() {
		return getDictionaryDAO().getDonorCategories();
	}

	/**
	 * Метод для удобного получения DAO по работе со справочной информацией.
	 * @return DAO для работы со справочной информацией.
	 */
	protected DictionaryDAOImpl getDictionaryDAO() {
		return sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO);
	}

	/**
	 * Возвращает список контрагентов.
	 * 
	 * @return список контрагентов.
	 */
	public List<Contragent> getContragents() {
		ContragentDAOHibernate dao = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO);
		return dao.getContragents();
	}
}