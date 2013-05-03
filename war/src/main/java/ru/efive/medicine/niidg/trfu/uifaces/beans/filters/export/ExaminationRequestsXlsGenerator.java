package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationType;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.ExaminationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.ExaminationRequestFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class ExaminationRequestsXlsGenerator
		extends
		BaseXlsGenerator<ExaminationRequestFilterableListHolderBean, ExaminationsFilter> {

	public ExaminationRequestsXlsGenerator(Workbook wb, File logoFile,
			ExaminationRequestFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.getCurrentFilterCopy();
	}

	@Override
	protected void printTableContentPortion(int offset) {
		int rowOffset = getRowCount() + 1;
		List<ExaminationRequest> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (int index = 0; index < data.size(); index++) {
			ExaminationRequest examinationRequest = data.get(index);
			Row row = sheet.createRow(index + rowOffset);

			createDataCell(row, 0, examinationRequest.getNumber());

			Date createdDate = examinationRequest.getCreated();
			if (createdDate != null) {
				createDataCell(row, 1, DateHelper.formatDateByPattern(
						createdDate, "dd.MM.yyyy HH:mm"));
			}

			Donor donor = examinationRequest.getDonor();
			if (donor != null) {
				createDataCell(row, 2, donor.getDescriptionShort());
			}

			ExaminationType examinationType = bean
					.getExaminationType(examinationRequest.getExaminationType());
			if (examinationType != null) {
				createDataCell(row, 3, examinationType.getValue());
			}

			Date planDate = examinationRequest.getPlanDate();
			if (planDate != null) {
				createDataCell(row, 4, DateHelper.formatDateByPattern(planDate,
						"dd.MM.yyyy HH:mm"));
			}

			createDataCell(row, 5, examinationRequest.getStatusName());

			createDataCell(row, 6, examinationRequest.getCommentary());
		}
	}

	@Override
	protected void setWidths() {
		sheet.autoSizeColumn((short) 0);
		sheet.setColumnWidth((short) 1, 2900);
		sheet.setColumnWidth((short) 2, 5000);
		sheet.setColumnWidth((short) 3, 3800);
		sheet.setColumnWidth((short) 4, 3500);
		sheet.setColumnWidth((short) 5, 4200);
		sheet.setColumnWidth((short) 6, 4000);
	}
}