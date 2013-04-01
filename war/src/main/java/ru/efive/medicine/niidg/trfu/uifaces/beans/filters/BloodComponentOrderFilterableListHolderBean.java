package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import ru.efive.medicine.niidg.trfu.dao.BloodComponentOrderRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentOrdersFilter;
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
	protected List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<FilterParameter>();
		String number = storedFilter.getNumber();
		Date created = storedFilter.getCreated();
		Date factDate = storedFilter.getFactDate();
		Date recipientBirth = storedFilter.getRecipientBirth();
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
		if (created != null) {
			parameters.add(new FilterParameter(
					AbstractFilter.REGISTRATION_DATE_TITLE, DateHelper
							.formatDateByPattern(created,
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
		if (recipientBirth != null) {
			parameters.add(new FilterParameter(AbstractFilter.BIRTH_TITLE,
					DateHelper.formatDateByPattern(recipientBirth,
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
			TransfusionType transfusionType = getDictionaryDAO()
					.getTransfusionType(transfusionTypeId);
			if (transfusionType != null) {
				parameters.add(new FilterParameter(
						AbstractFilter.TRANSFUSION_TYPE_TITLE, transfusionType
								.getValue()));
			}
		}
		if (factDate != null) {
			parameters.add(new FilterParameter(AbstractFilter.ISSUE_DATE_TITLE,
					DateHelper.formatDateByPattern(factDate,
							DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}

		return parameters;
	}

	@Override
	protected List<BloodComponentOrderRequest> loadDocuments(int offset,
			int pageSize) {
		try {
			return sessionManagement.getDAO(
					BloodComponentOrderRequestDAOImpl.class,
					ApplicationHelper.COMPONENT_ORDER_DAO).findDocuments(
					storedFilter, offset, pageSize, getSorting().getColumnId(),
					getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<BloodComponentOrderRequest>();
	}

	@Override
	protected void initFilters() {
		currentFilter = new BloodComponentOrdersFilter();
		storedFilter = new BloodComponentOrdersFilter();
	}

	@Override
	protected int getTotalCount() {
		try {
			long count = sessionManagement.getDAO(
					BloodComponentOrderRequestDAOImpl.class,
					ApplicationHelper.COMPONENT_ORDER_DAO).countDocument(
					storedFilter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}