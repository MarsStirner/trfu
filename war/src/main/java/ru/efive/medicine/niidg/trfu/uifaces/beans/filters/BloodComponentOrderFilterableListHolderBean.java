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

import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentOrdersFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodComponentOrdersDocxGenerator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodComponentOrdersXlsGenerator;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@Named("bloodComponentOrderFilterableList")
@SessionScoped
public class BloodComponentOrderFilterableListHolderBean
		extends
		AbstractFilterableListHolderBean<BloodComponentOrderRequest, BloodComponentOrdersFilter> {
	private static final long serialVersionUID = -4038404875054468250L;

	@Override
	public Sorting initSorting() {
		return new Sorting("number", false);
	}

	@Override
	public List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<FilterParameter>();
		String number = storedFilter.getNumber();
		String ibNumber = storedFilter.getIbNumber();
		Date createdFrom = storedFilter.getCreatedFrom();
		Date createdTo = storedFilter.getCreatedTo();
		Date factDateFrom = storedFilter.getFactDateFrom();
		Date factDateTo = storedFilter.getFactDateTo();
		Date recipientBirthFrom = storedFilter.getRecipientBirthFrom();
		Date recipientBirthTo = storedFilter.getRecipientBirthTo();
		String recipientFirstName = storedFilter.getRecipientFirstName();
		String recipient = storedFilter.getRecipient();
		String recipientMiddleName = storedFilter.getRecipientMiddleName();
		int bloodGroupId = storedFilter.getBloodGroupId();
		int rhesusFactorId = storedFilter.getRhesusFactorId();
		int bloodComponentTypeId = storedFilter.getBloodComponentTypeId();
		String division = storedFilter.getDivision();
		int transfusionTypeId = storedFilter.getTransfusionTypeId();

		if (StringUtils.isNotEmpty(number)) {
			parameters.add(new FilterParameter(AbstractFilter.NUMBER_TITLE,
					number));
		}
		if (StringUtils.isNotEmpty(ibNumber)) {
			parameters.add(new FilterParameter(AbstractFilter.IB_NUMBER_TITLE,
					ibNumber));
		}
		if (createdFrom != null) {
			parameters.add(new FilterParameter(
					AbstractFilter.DATE_FROM_TITLE, DateHelper
							.formatDateByPattern(createdFrom,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (createdTo != null) {
			parameters.add(new FilterParameter(
					AbstractFilter.DATE_TO_TITLE, DateHelper
							.formatDateByPattern(createdTo,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (StringUtils.isNotEmpty(division)) {
			parameters.add(new FilterParameter(AbstractFilter.DIVISION_TITLE,
					division));
		}
		if (StringUtils.isNotEmpty(recipient)) {
			parameters.add(new FilterParameter(
					AbstractFilter.RECIPIENT_LAST_NAME_TITLE, recipient));
		}
		if (StringUtils.isNotEmpty(recipientFirstName)) {
			parameters.add(new FilterParameter(
					AbstractFilter.RECIPIENT_FIRST_NAME_TITLE,
					recipientFirstName));
		}
		if (StringUtils.isNotEmpty(recipientMiddleName)) {
			parameters.add(new FilterParameter(
					AbstractFilter.RECIPIENT_MIDDLE_NAME_TITLE,
					recipientMiddleName));
		}
		if (recipientBirthFrom != null) {
			parameters.add(new FilterParameter(AbstractFilter.DATE_FROM_TITLE,
					DateHelper.formatDateByPattern(recipientBirthFrom,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (recipientBirthTo != null) {
			parameters.add(new FilterParameter(AbstractFilter.DATE_TO_TITLE,
					DateHelper.formatDateByPattern(recipientBirthTo,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
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
		if (bloodComponentTypeId != AbstractFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
			BloodComponentType bloodComponentType = getDictionaryDAO().get(
					BloodComponentType.class, bloodComponentTypeId);
			if (bloodComponentType != null) {
				parameters.add(new FilterParameter(
						AbstractFilter.BLOOD_COMPONENT_TYPE_TITLE,
						bloodComponentType.getValue()));
			}
		}
		if (transfusionTypeId != BloodComponentOrdersFilter.TRANSFUSION_TYPE_NULL_VALUE) {
			TransfusionType transfusionType = getTransfusionType(transfusionTypeId);
			if (transfusionType != null) {
				parameters.add(new FilterParameter(
						AbstractFilter.TRANSFUSION_TYPE_TITLE, transfusionType
								.getValue()));
			}
		}
		if (factDateFrom != null) {
			parameters.add(new FilterParameter(AbstractFilter.DATE_FROM_TITLE,
					DateHelper.formatDateByPattern(factDateFrom,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (factDateTo != null) {
			parameters.add(new FilterParameter(AbstractFilter.DATE_TO_TITLE,
					DateHelper.formatDateByPattern(factDateTo,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}

		return parameters;
	}

	public TransfusionType getTransfusionType(int transfusionTypeId) {
		return getDictionaryDAO().getTransfusionType(transfusionTypeId);
	}

	@Override
	public List<BloodComponentOrderRequest> loadDocuments(int offset,
			int pageSize, BloodComponentOrdersFilter filter) {
		try {
			return sessionManagement.getDAO(
					BloodComponentOrderRequestDAOImpl.class,
					ApplicationHelper.COMPONENT_ORDER_DAO).findDocuments(
					filter, offset, pageSize, getSorting().getColumnId(),
					getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<BloodComponentOrderRequest>();
	}

	@Override
	public void formXlsContent(Workbook wb, File logoFile) throws Exception {
		new BloodComponentOrdersXlsGenerator(wb, logoFile, this).formContent();
	}

	@Override
	public String getFormTitle() {
		return "Заявки на выдачу КК";
	}

	@Override
	public void formDocxContent(WordprocessingMLPackage pkg, File logoFile)
			throws Exception {
		new BloodComponentOrdersDocxGenerator(pkg, logoFile, this)
				.formContent();
	}

	@Override
	public List<String> getFormColumns() {
		return Arrays.asList(new String[] { "Номер", "Дата\nрегистрации",
				"Реципиент", "Компонент", "Вид трансфузии", "Отделение",
				"Статус" });
	}

	@Override
	public BloodComponentOrdersFilter createFilterInstance() {
		return new BloodComponentOrdersFilter();
	}

	@Override
	public int getTotalCount(BloodComponentOrdersFilter filter) {
		try {
			long count = sessionManagement.getDAO(
					BloodComponentOrderRequestDAOImpl.class,
					ApplicationHelper.COMPONENT_ORDER_DAO).countDocument(
					filter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}