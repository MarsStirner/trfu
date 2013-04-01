package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DonorDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.Gender;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@Named("donorFilterableList")
@SessionScoped
public class DonorFilterableListHolderBean extends AbstractFilterableListHolderBean<Donor, DonorsFilter> {
	private static final long serialVersionUID = -1125733962957391017L;
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("lastName,firstName,middleName", true);
    }

	@Override
	protected int getTotalCount() {
		try {
			long count = sessionManagement.getDAO(DonorDAOImpl.class, "donorDao").countDocument(storedFilter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected List<Donor> loadDocuments(int offset, int pageSize) {
		try {
			return sessionManagement.getDAO(DonorDAOImpl.class, "donorDao")
					.findDocuments(storedFilter, offset, pageSize,
							getSorting().getColumnId(), getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Donor>();
	}
	
	@Override
	protected List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<FilterParameter>();
		String number = storedFilter.getNumber();
		String lastName = storedFilter.getLastName();
		String firstName = storedFilter.getFirstName();
		String middleName = storedFilter.getMiddleName();
		Date created = storedFilter.getCreated();
		Date birth = storedFilter.getBirth();
		int genderId = storedFilter.getGenderId();
		int documentTypeId = storedFilter.getDocumentTypeId();
		String documentNumber = storedFilter.getDocumentNumber();
		String documentSeries = storedFilter.getDocumentSeries();
		String externalNumber = storedFilter.getExternalNumber();
		String factAddress = storedFilter.getFactAddress();
		int statusId = storedFilter.getStatusId();
		int bloodGroupId = storedFilter.getBloodGroupId();
		int rhesusFactorId = storedFilter.getRhesusFactorId();
		int pastQuarantineId = storedFilter.getPastQuarantineId();

		if (StringUtils.isNotEmpty(number)) {
			parameters.add(new FilterParameter(DonorsFilter.NUMBER_TITLE, number));
		}
		if (created != null) {
			parameters.add(new FilterParameter(DonorsFilter.CREATED_TITLE,
					DateHelper.formatDateByPattern(created,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (StringUtils.isNotEmpty(lastName)) {
			parameters.add(new FilterParameter(DonorsFilter.LAST_NAME_TITLE, lastName));
		}
		if (StringUtils.isNotEmpty(firstName)) {
			parameters.add(new FilterParameter(DonorsFilter.FIRST_NAME_TITLE, firstName));
		}
		if (StringUtils.isNotEmpty(middleName)) {
			parameters.add(new FilterParameter(DonorsFilter.MIDDLE_NAME_TITLE, middleName));
		}
		if (birth != null) {
			parameters.add(new FilterParameter(DonorsFilter.BIRTH_TITLE,
					DateHelper.formatDateByPattern(birth,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
        if (genderId != DonorsFilter.GENDER_NULL_VALUE) {
        	Gender gender = getDictionaryDAO().getGender(genderId);
        	if (gender != null) {
				parameters.add(new FilterParameter(DonorsFilter.GENDER_TITLE,
						gender.getValue()));
        	}
        }
        if (documentTypeId != DonorsFilter.DOCUMENT_TYPE_NULL_VALUE) {
        	DocumentType documentType = getDocumentType(documentTypeId);
        	if (documentType != null) {
				parameters.add(new FilterParameter(
						DonorsFilter.DOCUMENT_TYPE_TITLE, documentType
								.getValue()));
        	}
        }
		if (StringUtils.isNotEmpty(documentNumber)) {
			parameters.add(new FilterParameter(DonorsFilter.DOCUMENT_NUMBER_TITLE, documentNumber));
		}
        if (StringUtils.isNotEmpty(documentSeries)) {
        	parameters.add(new FilterParameter(DonorsFilter.DOCUMENT_SERIES_TITLE, documentSeries));
        }
		if (StringUtils.isNotEmpty(externalNumber)) {
			parameters.add(new FilterParameter(DonorsFilter.EXTERNAL_NUMBER_TITLE, externalNumber));
		}
		if (StringUtils.isNotEmpty(factAddress)) {
			parameters.add(new FilterParameter(DonorsFilter.FACT_ADDRESS_TITLE, factAddress));
		}
        if (statusId != DonorsFilter.DONOR_STATUS_NULL_VALUE) {
    		String statusName = ApplicationHelper.getStatusName("Donor", statusId);
    		parameters.add(new FilterParameter(DonorsFilter.DONOR_STATUS_TITLE, statusName));
        }
        if (bloodGroupId != DonorsFilter.BLOOD_GROUP_NULL_VALUE) {
        	BloodGroup bloodGroup = getDictionaryDAO().get(BloodGroup.class, bloodGroupId);
        	if (bloodGroup != null) {
				parameters.add(new FilterParameter(
						DonorsFilter.BLOOD_GROUP_TITLE, bloodGroup.getValue()));
        	}
        }
        if (rhesusFactorId != DonorsFilter.RHESUS_FACTOR_NULL_VALUE) {
        	Classifier rhesusFactor = getDictionaryDAO().get(Classifier.class, rhesusFactorId);
        	if (rhesusFactor != null) {
				parameters.add(new FilterParameter(
						DonorsFilter.RHESUS_FACTOR_TITLE, rhesusFactor
								.getValue()));
        	}
        }
        if (pastQuarantineId != DonorsFilter.PAST_QUARANTINE_NULL_VALUE) {
        	PastQuarantine pastQuarantine = getPastQuarantineObject(pastQuarantineId);
        	if (pastQuarantine != null) {
        		parameters.add(new FilterParameter(DonorsFilter.PAST_QUARANTINE_TITLE, pastQuarantine.getValue()));
        	}
        }
        return parameters;
	}

	/**
	 * Список типов документов.
	 */
	protected List<DocumentType> documentTypes = null;
	/**
	 * Список значений для признака "Прошедшие карантинизацию".
	 */
	protected List<PastQuarantine> pastQuarantineObjects = null;

	/**
	 * Получение списка типов документов (паспорт, полис).
	 * @return список типов документов.
	 */
	public List<DocumentType> getDocumentTypes() {
		if (documentTypes == null) {
			documentTypes = new ArrayList<DocumentType>();
			documentTypes.add(new DocumentType(1, "Паспорт"));
			documentTypes.add(new DocumentType(2, "Полис"));
		}		
		return documentTypes;
	}
	/**
	 * Получение списка объектов для признака "Прошедние карантинизацию" (да, нет).
	 * @return список объектов.
	 */
	public List<PastQuarantine> getPastQuarantineObjects() {
		if (pastQuarantineObjects == null) {
			pastQuarantineObjects = new ArrayList<PastQuarantine>();
			pastQuarantineObjects.add(new PastQuarantine(1, "да"));
			pastQuarantineObjects.add(new PastQuarantine(2, "нет"));
		}		
		return pastQuarantineObjects;
	}
	/**
	 * Получение типа документа по идентификатору.
	 * 
	 * @param id идентификатор.
	 * @return найденный по идентификатору объект.
	 */
	public DocumentType getDocumentType(int id) {
		return DictionaryDAOImpl.findObject(id, getDocumentTypes());
	}
	/**
	 * Получение значения признака "Прошедшие карантинизацию" по идентификатору.
	 * 
	 * @param id идентификатор.
	 * @return найденный по идентификатору объект.
	 */
	public PastQuarantine getPastQuarantineObject(int id) {
		return DictionaryDAOImpl.findObject(id, getPastQuarantineObjects());
	}

	@Override
	protected void initFilters() {
		currentFilter = new DonorsFilter();
		storedFilter = new DonorsFilter();
	}
}