package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorCategory;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.DonorFilterableListHolderBean;
          /*
public class DonorsXlsGenerator extends
		BaseXlsGenerator<DonorFilterableListHolderBean, DonorsFilter> {

	public DonorsXlsGenerator(Workbook wb, File logoFile,
			DonorFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void setWidths() {
		sheet.setColumnWidth((short) 0, 6000);
		sheet.autoSizeColumn((short) 1);
		sheet.autoSizeColumn((short) 2);
		sheet.autoSizeColumn((short) 3);
		sheet.setColumnWidth((short) 4, 5000);
		sheet.setColumnWidth((short) 5, 5200);
	}

	@Override
	protected void printTableContentPortion(int offset) {
		int rowOffset = getRowCount() + 1;
		List<Donor> data = bean.loadDocuments(offset, PAGE_SIZE, filter);
		for (int index = 0; index < data.size(); index++) {
			Donor donor = data.get(index);
			Row row = sheet.createRow(index + rowOffset);

			createDataCell(row, 0, donor.getDescriptionShort());

			createDataCell(row, 1, donor.getNumber());

			DonorCategory donorCategory = bean.getDonorCategory(donor
					.getCategory());
			if (donorCategory != null) {
				createDataCell(row, 2, donorCategory.getValue());
			}

			BloodGroup bloodGroup = donor.getBloodGroup();
			Classifier rhesusFactor = donor.getRhesusFactor();
			createDataCellForBloodGroup(row, 3, bloodGroup, rhesusFactor);

			createDataCell(row, 4, donor.getStatusName());

			createDataCell(row, 5, donor.getCommentary());
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

		  		  */