package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.AbstractFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.FilterParameter;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseXlsGenerator<T extends AbstractFilterableListHolderBean, F extends AbstractFilter<F>>
		extends BaseGenerator<T, F> {
	private static final int COMMON_LABEL_ROW_NUMBER = 5;
	protected Workbook wb;
	protected Sheet sheet;
	protected CellStyle tableContentCellStyle;

	public BaseXlsGenerator(Workbook wb, File logoFile, T bean) {
		super(logoFile, bean);
		this.wb = wb;
		sheet = wb.getSheetAt(0);
	}

	public void formContent() throws Exception {
		setSheetName();
		printCommonLabel();
		printIndividualLabel();
		printFilterParameters();
		printTableHeader();
		printTableContent();
		setWidths();
		printSummary();
		printTotalVolume();
		printDateTime();
		printLogo();
	}
	
	protected void printTotalVolume() {
		
	}
	
	protected void printSummary() {
		
	}

	public void setSheetName() {
		wb.setSheetName(0, bean.getFormTitle());
	}

	protected void printDateTime() {
		int rowNumber = getRowCount() + 2;
		Row row = sheet.createRow(rowNumber);
		Cell dateTimeCell = row.createCell(0);
		dateTimeCell.setCellStyle(getDateTimeCellStyle());
		dateTimeCell.setCellValue(DateHelper.getCurrentDate());
		sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0,
				getLastColumnNumber()));
	}

	protected CellStyle getDateTimeCellStyle() {
		CellStyle cs = wb.createCellStyle();
		cs.setAlignment(CellStyle.ALIGN_RIGHT);
		return cs;
	}

	protected void printCommonLabel() {
		Row row = sheet.createRow(COMMON_LABEL_ROW_NUMBER);
		row.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
		Cell commonLabelCell = row.createCell(0);
		commonLabelCell.setCellStyle(getCommonLabelCellStyle());
		commonLabelCell.setCellValue(getCommonLabel());
		sheet.addMergedRegion(new CellRangeAddress(COMMON_LABEL_ROW_NUMBER,
				COMMON_LABEL_ROW_NUMBER, 0, getLastColumnNumber()));
	}

	protected void printIndividualLabel() {
		Row row = sheet.createRow(getIndividualLabelRowNumber());
		Cell individualLabelCell = row.createCell(0);
		individualLabelCell.setCellStyle(getIndividualLabelCellStyle());
		individualLabelCell.setCellValue(getIndividualLabel());
		sheet.addMergedRegion(new CellRangeAddress(
				getIndividualLabelRowNumber(), getIndividualLabelRowNumber(),
				0, getLastColumnNumber()));
	}

	protected void printLogo() throws Exception {
		if (logoFile != null && logoFile.exists()) {
			InputStream is = new FileInputStream(logoFile);
			byte[] bytes = IOUtils.toByteArray(is);
			int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			is.close();

			CreationHelper helper = wb.getCreationHelper();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(getLogoColumnMargin());
			anchor.setRow1(0);
			Drawing drawing = sheet.createDrawingPatriarch();
			drawing.createPicture(anchor, pictureIdx).resize();
		}
	}

	protected void printFilterParameters() {
		CellStyle labelCellStyle = getFilterParameterLabelCellStyle();
		CellStyle valueCellStyle = getFilterParameterValueCellStyle();
		int rowOffset = getRowCount() + 2;
		List<FilterParameter> filterParameters = bean
				.getNotNullFilterParameters();
		for (int index = 0; index < filterParameters.size(); index++) {
			FilterParameter filterParameter = filterParameters.get(index);
			int rowNumber = index + rowOffset;
			Row row = sheet.createRow(rowNumber);

			String parameterLabel = filterParameter.getTitle();
			if (parameterLabel.contains("<br/>")) {
				parameterLabel = parameterLabel.replaceAll("<br/>", "\n");
				row.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
			}
			Cell cell = row.createCell(0);
			cell.setCellStyle(labelCellStyle);
			cell.setCellValue(parameterLabel);

			cell = row.createCell(2);
			cell.setCellStyle(valueCellStyle);
			cell.setCellValue(filterParameter.getValue());
			sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0,
					1));
			sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 2,
					getLastColumnNumber()));
		}
	}

	protected void printTableHeader() {
		CellStyle cs = getTableHeaderCellStyle();
		Row row = sheet.createRow(getRowCount() + 2);

		boolean doubleHeight = false;
		List<String> columns = bean.getFormColumns();
		for (int index = 0; index < columns.size(); index++) {
			String columnTitle = columns.get(index);
			Cell сell = row.createCell(index);
			сell.setCellStyle(cs);
			сell.setCellValue(columnTitle);

			if (columnTitle.indexOf('\n') != -1) {
				doubleHeight = true;
			}
		}

		if (doubleHeight) {
			row.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
		}
	}

	protected void createDataCell(Row row, int columnIndex, String value) {
		createDataCell(row, columnIndex, value, getTableContentCellStyle());
	}

	protected void createDataCell(Row row, int columnIndex, String value,
			CellStyle cellStyle) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	protected CellStyle getTableContentCellStyle() {
		if (tableContentCellStyle == null) {
			tableContentCellStyle = wb.createCellStyle();
			tableContentCellStyle.setWrapText(true);
			tableContentCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
			tableContentCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		}
		return tableContentCellStyle;
	}

	private CellStyle getTableHeaderCellStyle() {
		CellStyle cs = wb.createCellStyle();
		Font fnt = wb.createFont();
		fnt.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(fnt);
		cs.setWrapText(true);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cs;
	}

	private CellStyle getFilterParameterLabelCellStyle() {
		CellStyle cs = wb.createCellStyle();
		Font fnt = wb.createFont();
		fnt.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(fnt);
		cs.setWrapText(true);
		return cs;
	}

	protected CellStyle getFilterParameterValueCellStyle() {
		CellStyle cs = wb.createCellStyle();
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cs;
	}

	private CellStyle getIndividualLabelCellStyle() {
		CellStyle cs = wb.createCellStyle();
		Font fnt = wb.createFont();
		fnt.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(fnt);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		return cs;
	}

	private int getIndividualLabelRowNumber() {
		return COMMON_LABEL_ROW_NUMBER + 2;
	}

	private CellStyle getCommonLabelCellStyle() {
		CellStyle cs = wb.createCellStyle();
		cs.setWrapText(true);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		return cs;
	}

	private int getLastColumnNumber() {
		return getColumnCount() - 1;
	}

	protected int getRowCount() {
		return sheet.getLastRowNum();
	}

	protected int getColumnCount() {
		return bean.getFormColumns().size();
	}

	protected void createDataCellForTransfusionType(Row row, int columnIndex,
			TransfusionType transfusionType, Date planDate) {
		String transfusionTypeInfo = formatTransfusionType(transfusionType, planDate);
		createDataCell(row, columnIndex, transfusionTypeInfo);
	}

	protected void createDateCellForDonationType(Row row, int columnIndex,
			List<BloodDonationType> donationTypes) {
		String donationTypesInfo = formatDonationTypes(donationTypes);
		createDataCell(row, columnIndex, donationTypesInfo);
	}

	protected void createDataCellForBloodGroup(Row row, int columnIndex,
			BloodGroup bloodGroup, Classifier rhesusFactor) {
		createDataCell(row, columnIndex, formatBloodInfo(bloodGroup, rhesusFactor));
	}

	protected int getLogoColumnMargin() {
		return 3;
	}

	protected abstract void setWidths();
}