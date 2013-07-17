package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodComponentFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodComponentsXlsGenerator
		extends
		BaseXlsGenerator<BloodComponentFilterableListHolderBean, BloodComponentsFilter> {
	
	private long totalVolume = 0;

	public BloodComponentsXlsGenerator(Workbook wb, File logoFile,
			BloodComponentFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void setWidths() {
		sheet.setColumnWidth((short) 0, 2200);
		sheet.setColumnWidth((short) 1, 6000);
		sheet.autoSizeColumn((short) 2);
		sheet.setColumnWidth((short) 3, 3800);
		sheet.autoSizeColumn((short) 4);
		sheet.setColumnWidth((short) 5, 2700);
		sheet.setColumnWidth((short) 6, 4400);
	}

	@Override
	protected void printTableContentPortion(int offset) {
		int rowOffset = getRowCount() + 1;
		List<BloodComponent> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (int index = 0; index < data.size(); index++) {
			BloodComponent bloodComponent = data.get(index);
			Row row = sheet.createRow(index + rowOffset);

			createDataCell(row, 0, bloodComponent.getFullNumber());

			BloodComponentType bloodComponentType = bloodComponent
					.getComponentType();
			if (bloodComponentType != null) {
				createDataCell(row, 1, bloodComponentType.getCodeAndValue());
			}

			createDataCell(row, 2, String.valueOf(bloodComponent.getVolume()));

			Anticoagulant anticoagulant = bloodComponent.getAnticoagulant();
			if (anticoagulant != null) {
				createDataCell(row, 3, anticoagulant.getValue());
			}

			BloodGroup bloodGroup = bloodComponent.getBloodGroup();
			Classifier rhesusFactor = bloodComponent.getRhesusFactor();
			createDataCellForBloodGroup(row, 4, bloodGroup, rhesusFactor);

			Date expirationDate = bloodComponent.getExpirationDate();
			if (expirationDate != null) {
				createDataCell(row, 5, DateHelper.formatDateByPattern(
						expirationDate, "dd.MM.yyyy"));
			}

			createDataCell(row, 6, bloodComponent.getStatusName());
			
			totalVolume += bloodComponent.getVolume();
		}
		printSummary();
		printTotalVolume();
	}
	
	private void printTotalVolume() {
		int rowNumber = getRowCount() + 1;
		Row row = sheet.createRow(rowNumber);
		Cell summaryCell = row.createCell(0);
		summaryCell.setCellValue("Общий объем: " + totalVolume);
		sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0,
				getColumnCount() - 1));
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