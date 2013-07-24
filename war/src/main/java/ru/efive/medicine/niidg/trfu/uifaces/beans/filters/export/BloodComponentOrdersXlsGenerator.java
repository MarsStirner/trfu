package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentOrdersFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodComponentOrderFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodComponentOrdersXlsGenerator
		extends
		BaseXlsGenerator<BloodComponentOrderFilterableListHolderBean, BloodComponentOrdersFilter> {

	public BloodComponentOrdersXlsGenerator(Workbook wb, File logoFile,
			BloodComponentOrderFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void setWidths() {
		sheet.autoSizeColumn((short) 0);
		sheet.setColumnWidth((short) 1, 3200);
		sheet.setColumnWidth((short) 2, 4600);
		sheet.setColumnWidth((short) 3, 5400);
		sheet.setColumnWidth((short) 4, 3100);
		sheet.setColumnWidth((short) 5, 4300);
		sheet.setColumnWidth((short) 6, 2800);
	}

	@Override
	protected void printTableContentPortion(int offset) {
		int rowOffset = getRowCount() + 1;
		List<BloodComponentOrderRequest> data = bean.loadDocuments(offset,
				PAGE_SIZE, filter);
		for (int index = 0; index < data.size(); index++) {
			BloodComponentOrderRequest orderRequest = data.get(index);
			Row row = sheet.createRow(index + rowOffset);

			createDataCell(row, 0, orderRequest.getNumber());

			Date createdDate = orderRequest.getCreated();
			if (createdDate != null) {
				createDataCell(row, 1, DateHelper.formatDateByPattern(
						createdDate, "dd.MM.yyyy HH:mm"));
			}

			createDataCell(row, 2, orderRequest.getRecipient());

			BloodComponentType componentType = orderRequest.getComponentType();
			if (componentType != null) {
				createDataCell(row, 3, componentType.getCodeAndValue());
			}

			int transfusionTypeId = orderRequest.getTransfusionType();
			TransfusionType transfusionType = bean
					.getTransfusionType(transfusionTypeId);
			Date planDate = orderRequest.getPlanDate();
			createDataCellForTransfusionType(row, 4, transfusionType, planDate);

			createDataCell(row, 5, orderRequest.getDivision());

			createDataCell(row, 6, orderRequest.getStatusName());
		}
	}
	
	@Override
	protected void printSummary() {
		int rowNumber = getRowCount() + 1;
		int count = bean.getTotalCount(bean.getCurrentFilter());
		Row row = sheet.createRow(rowNumber);
		Cell summaryCell = row.createCell(0);
		summaryCell.setCellValue("Итого: " + count);
		sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0,
				getColumnCount() - 1));
	}
}