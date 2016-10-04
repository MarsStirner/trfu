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

import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.filters.BloodDonationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodDonationsDocxGenerator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.BloodDonationsXlsGenerator;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@Named("bloodDonationFilterableList")
@SessionScoped
public class BloodDonationFilterableListHolderBean
		extends
		AbstractFilterableListHolderBean<BloodDonationRequest, BloodDonationsFilter> {
	private static final long serialVersionUID = 8575687867519785975L;

	@Override
	protected Sorting initSorting() {
		return new Sorting("number", false);
	}

	@Override
	public List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<>();
		String number = storedFilter.getNumber();
		String donor = storedFilter.getDonor();
		Date created = storedFilter.getCreated();
		int statusId = storedFilter.getStatusId();
		int donorTypeId = storedFilter.getDonorTypeId();
		int donationTypeId = storedFilter.getDonationTypeId();

		if (StringUtils.isNotEmpty(number)) {
			parameters.add(new FilterParameter(
					BloodDonationsFilter.NUMBER_TITLE, number));
		}
		if (StringUtils.isNotEmpty(donor)) {
			parameters.add(new FilterParameter(
					BloodDonationsFilter.DONOR_TITLE, donor));
		}
		if (created != null) {
			parameters.add(new FilterParameter(
					BloodDonationsFilter.CREATED_TITLE, DateHelper
							.formatDateByPattern(created,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (statusId != BloodDonationsFilter.BLOOD_DONATION_STATUS_NULL_VALUE) {
			String statusName = ApplicationHelper.getStatusName(
					"BloodDonation", statusId);
			parameters.add(new FilterParameter(
					BloodDonationsFilter.STATUS_TITLE, statusName));
		}
		if (donorTypeId != BloodDonationsFilter.DONOR_TYPE_NULL_VALUE) {
			Classifier donorType = getDictionaryDAO().get(Classifier.class,
					donorTypeId);
			if (donorType != null) {
				parameters.add(new FilterParameter(
						BloodDonationsFilter.DONOR_TYPE_TITLE, donorType
								.getValue()));
			}
		}
		if (donationTypeId != BloodDonationsFilter.DONOR_TYPE_NULL_VALUE) {
			BloodDonationType donationType = getDictionaryDAO().get(
					BloodDonationType.class, donationTypeId);
			if (donationType != null) {
				parameters.add(new FilterParameter(
						BloodDonationsFilter.DONATION_TYPE_TITLE, donationType
								.getValue()));
			}
		}

		return parameters;
	}

	@Override
	public List<BloodDonationRequest> loadDocuments(int offset, int pageSize,
			BloodDonationsFilter filter) {
		try {
			return sessionManagement.getDAO(BloodDonationRequestDAOImpl.class,
					ApplicationHelper.DONATION_DAO).findDocuments(filter,
					offset, pageSize, getSorting().getColumnId(),
					getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>(0);
	}

	@Override
	public void formXlsContent(Workbook wb, File logoFile) throws Exception {
		new BloodDonationsXlsGenerator(wb, logoFile, this).formContent();
	}

	@Override
	public String getFormTitle() {
		return "Обращения на донацию";
	}

	@Override
	public void formDocxContent(WordprocessingMLPackage pkg, File logoFile)
			throws Exception {
		new BloodDonationsDocxGenerator(pkg, logoFile, this).formContent();
	}

	@Override
	public List<String> getFormColumns() {
		return Arrays.asList(
				"Номер", "Дата\nсоздания", "Донор", "Тип донора", "Вид донорства", "Статус", "Комментарии"
		);
	}

	@Override
	public BloodDonationsFilter createFilterInstance() {
		return new BloodDonationsFilter();
	}

	@Override
	public int getTotalCount(BloodDonationsFilter filter) {
		try {
			long count = sessionManagement.getDAO(
					BloodDonationRequestDAOImpl.class,
					ApplicationHelper.DONATION_DAO).countDocument(filter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}