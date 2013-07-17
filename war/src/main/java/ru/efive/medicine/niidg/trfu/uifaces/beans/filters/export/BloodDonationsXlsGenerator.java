package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.BloodDonationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodDonationFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodDonationsXlsGenerator
		extends
		BaseXlsGenerator<BloodDonationFilterableListHolderBean, BloodDonationsFilter> {

	public BloodDonationsXlsGenerator(Workbook wb, File logoFile,
			BloodDonationFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void setWidths() {
		sheet.autoSizeColumn((short) 0);
		sheet.setColumnWidth((short) 1, 2600);
		sheet.setColumnWidth((short) 2, 5000);
		sheet.setColumnWidth((short) 3, 4000);
		sheet.setColumnWidth((short) 4, 3500);
		sheet.setColumnWidth((short) 5, 4000);
		sheet.setColumnWidth((short) 6, 4300);
	}

	@Override
	protected void printTableContentPortion(int offset) {
		int rowOffset = getRowCount() + 1;
		List<BloodDonationRequest> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (int index = 0; index < data.size(); index++) {
			BloodDonationRequest donationRequest = data.get(index);
			Row row = sheet.createRow(index + rowOffset);

			createDataCell(row, 0, donationRequest.getNumber());

			Date createdDate = donationRequest.getCreated();
			if (createdDate != null) {
				createDataCell(row, 1, DateHelper.formatDateByPattern(
						createdDate, "dd.MM.yyyy HH:mm"));
			}

			Donor donor = donationRequest.getDonor();
			if (donor != null) {
				createDataCell(row, 2, donor.getDescriptionShort());
			}

			Classifier donorType = donationRequest.getDonorType();
			if (donorType != null) {
				createDataCell(row, 3, donorType.getValue());
			}

			List<BloodDonationType> donationTypes = donationRequest
					.getResultDonationTypeList();
			createDateCellForDonationType(row, 4, donationTypes);

			createDataCell(row, 5, donationRequest.getStatusName());

			createDataCell(row, 6, donationRequest.getCommentary());
		}
		printSummary();
	}
	
	private void printSummary() {
		int rowNumber = getRowCount() + 1;
		int count = bean.getTotalCount(bean.getCurrentFilter());
		Row row = sheet.createRow(rowNumber);
		Cell summaryCell = row.createCell(0);
		summaryCell.setCellValue("Итого: " + count);
		sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0,
				getColumnCount() - 1));
	}
}