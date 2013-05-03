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

import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationType;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.ExaminationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.ExaminationRequestsDocxGenerator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export.ExaminationRequestsXlsGenerator;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@Named("examinationRequestFilterableList")
@SessionScoped
public class ExaminationRequestFilterableListHolderBean
		extends
		AbstractFilterableListHolderBean<ExaminationRequest, ExaminationsFilter> {
	private static final long serialVersionUID = -6252505703763890115L;

	@Override
	protected Sorting initSorting() {
		return new Sorting("number", false);
	}

	@Override
	public List<ExaminationRequest> loadDocuments(int offset, int pageSize,
			ExaminationsFilter filter) {
		try {
			return sessionManagement.getDAO(ExaminationRequestDAOImpl.class,
					ApplicationHelper.EXAMINATION_DAO).findDocuments(filter,
					offset, pageSize, getSorting().getColumnId(),
					getSorting().isAsc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ExaminationRequest>();
	}

	@Override
	public List<FilterParameter> getNotNullFilterParameters() {
		List<FilterParameter> parameters = new ArrayList<FilterParameter>();
		String number = storedFilter.getNumber();
		String donor = storedFilter.getDonor();
		Date created = storedFilter.getCreated();
		Date planDate = storedFilter.getPlanDate();
		int statusId = storedFilter.getStatusId();
		int examinationTypeId = storedFilter.getExaminationTypeId();

		if (StringUtils.isNotEmpty(number)) {
			parameters.add(new FilterParameter(ExaminationsFilter.NUMBER_TITLE,
					number));
		}
		if (StringUtils.isNotEmpty(donor)) {
			parameters.add(new FilterParameter(ExaminationsFilter.DONOR_TITLE,
					donor));
		}
		if (created != null) {
			parameters.add(new FilterParameter(
					ExaminationsFilter.CREATED_TITLE, DateHelper
							.formatDateByPattern(created,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (planDate != null) {
			parameters.add(new FilterParameter(
					ExaminationsFilter.EXAMINATION_PLAN_DATE_TITLE, DateHelper
							.formatDateByPattern(planDate,
									DateHelper.DATE_WITHOUT_TIME_PATTERN)));
		}
		if (statusId != ExaminationsFilter.EXAMINATION_STATUS_NULL_VALUE) {
			String statusName = ApplicationHelper.getStatusName("Examination",
					statusId);
			parameters.add(new FilterParameter(ExaminationsFilter.STATUS_TITLE,
					statusName));
		}
		if (examinationTypeId != ExaminationsFilter.EXAMINATION_TYPE_NULL_VALUE) {
			ExaminationType examinationType = getExaminationType(examinationTypeId);
			if (examinationType != null) {
				parameters.add(new FilterParameter(
						ExaminationsFilter.EXAMINATION_TYPE_TITLE,
						examinationType.getValue()));
			}
		}

		return parameters;
	}

	public ExaminationType getExaminationType(int examinationTypeId) {
		ExaminationType examinationType = getDictionaryDAO()
				.getExaminationType(examinationTypeId);
		return examinationType;
	}

	@Override
	public void formXlsContent(Workbook wb, File logoFile) throws Exception {
		new ExaminationRequestsXlsGenerator(wb, logoFile, this).formContent();
	}

	@Override
	public String getFormTitle() {
		return "Обращения на обследования";
	}

	@Override
	public void formDocxContent(WordprocessingMLPackage pkg, File logoFile)
			throws Exception {
		new ExaminationRequestsDocxGenerator(pkg, logoFile, this).formContent();
	}

	@Override
	public List<String> getFormColumns() {
		return Arrays.asList(new String[] { "Номер", "Дата\nсоздания", "Донор",
				"Обследование", "Планируемая\nдата", "Статус", "Комментарии" });
	}

	@Override
	public ExaminationsFilter createFilterInstance() {
		return new ExaminationsFilter();
	}

	@Override
	public int getTotalCount(ExaminationsFilter filter) {
		try {
			long count = sessionManagement.getDAO(
					ExaminationRequestDAOImpl.class,
					ApplicationHelper.EXAMINATION_DAO).countDocument(
					filter);
			return new Long(count).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}