package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.efive.crm.data.Contragent;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodComponentsDocxGenerator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodComponentsXlsGenerator;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@Named("bloodComponentFilterableList")
@SessionScoped
public class BloodComponentFilterableListHolderBean extends
		AbstractFilterableListHolderBean<BloodComponent, BloodComponentsFilter> {
	private static final long serialVersionUID = -1208938476641901049L;

	@Override
	public Sorting initSorting() {
		return new Sorting("parentNumber,number", true);
	}

	@Override
	public List<BloodComponent> loadDocuments(int offset, int pageSize,
			BloodComponentsFilter filter) {
		try {
			return sessionManagement.getDAO(BloodComponentDAOImpl.class,
					ApplicationHelper.BLOOD_COMPONENT_DAO).findDocuments(
					filter, offset, pageSize, getSorting().getColumnId(),
					getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<BloodComponent>();
	}

	@Override
	public List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<FilterParameter>();
		String number = storedFilter.getNumber();
		int bloodComponentTypeId = storedFilter.getBloodComponentTypeId();
		String donorCode = storedFilter.getDonorCode();
		int makerId = storedFilter.getMakerId();
		int bloodGroupId = storedFilter.getBloodGroupId();
		int rhesusFactorId = storedFilter.getRhesusFactorId();
		Date donationDate = storedFilter.getDonationDate();
		Date expirationDate = storedFilter.getExpirationDate();
		int statusId = storedFilter.getStatusId();
		String fio = storedFilter.getFio();

		if (StringUtils.isNotEmpty(number)) {
			parameters.add(new FilterParameter(
					BloodComponentsFilter.NUMBER_TITLE, number));
		}
		if (StringUtils.isNotEmpty(donorCode)) {
			parameters.add(new FilterParameter(
					BloodComponentsFilter.DONOR_CODE_TITLE, donorCode));
		}
		if (StringUtils.isNotEmpty(fio)) {
			parameters.add(new FilterParameter(BloodComponentsFilter.FIO_TITLE,
					fio));
		}
		if (bloodGroupId != AbstractFilter.BLOOD_GROUP_NULL_VALUE) {
			BloodGroup bloodGroup = getDictionaryDAO().get(BloodGroup.class,
					bloodGroupId);
			if (bloodGroup != null) {
				parameters
						.add(new FilterParameter(
								AbstractFilter.BLOOD_GROUP_TITLE, bloodGroup
										.getValue()));
			}
		}
		if (rhesusFactorId != AbstractFilter.RHESUS_FACTOR_NULL_VALUE) {
			Classifier rhesusFactor = getDictionaryDAO().get(Classifier.class,
					rhesusFactorId);
			if (rhesusFactor != null) {
				parameters.add(new FilterParameter(
						AbstractFilter.RHESUS_FACTOR_TITLE, rhesusFactor
								.getValue()));
			}
		}
		if (donationDate != null) {
			parameters.add(new FilterParameter(
					AbstractFilter.DONATION_DATE_TITLE, DateHelper
							.formatDateByPattern(donationDate,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (expirationDate != null) {
			parameters.add(new FilterParameter(
					AbstractFilter.EXPIRATION_DATE_TITLE, DateHelper
							.formatDateByPattern(expirationDate,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (statusId != BloodComponentsFilter.BLOOD_COMPONENT_STATUS_NULL_VALUE) {
			String statusName = ApplicationHelper.getStatusName(
					"BloodComponent", statusId);
			parameters.add(new FilterParameter(AbstractFilter.STATUS_TITLE,
					statusName));
		}
		if (bloodComponentTypeId != BloodComponentsFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
			BloodComponentType bloodComponentType = getDictionaryDAO().get(
					BloodComponentType.class, bloodComponentTypeId);
			if (bloodComponentType != null) {
				parameters.add(new FilterParameter(
						AbstractFilter.BLOOD_COMPONENT_TYPE_TITLE,
						bloodComponentType.getValue()));
			}
		}
		if (makerId != BloodComponentsFilter.MAKER_NULL_VALUE) {
			Contragent contragent = getDictionaryDAO().get(Contragent.class,
					makerId);
			if (contragent != null) {
				parameters.add(new FilterParameter(AbstractFilter.MAKER_TITLE,
						contragent.getFullName()));
			}
		}

		return parameters;
	}

	@Override
	public void formXlsContent(Workbook wb, File logoFile) throws Exception {
		new BloodComponentsXlsGenerator(wb, logoFile, this).formContent();
	}

	@Override
	public String getFormTitle() {
		return "Компоненты крови";
	}

	@Override
	public void formDocxContent(WordprocessingMLPackage pkg, File logoFile)
			throws Exception {
		new BloodComponentsDocxGenerator(pkg, logoFile, this).formContent();
	}

	@Override
	public List<String> getFormColumns() {
		return Arrays.asList(new String[] { "Номер", "Компонент", "Объем",
				"Антикоагулянт", "Группа крови", "Срок\nхранения", "Статус" });
	}

	@Override
	public BloodComponentsFilter createFilterInstance() {
		return new BloodComponentsFilter();
	}

	@Override
	public int getTotalCount(BloodComponentsFilter filter) {
		try {
			long count = sessionManagement.getDAO(BloodComponentDAOImpl.class,
					ApplicationHelper.BLOOD_COMPONENT_DAO).countDocuments(
					filter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}