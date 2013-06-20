package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.AbstractFilterableListHolderBean;

public class FilterResultsExportGenerator {

	protected Workbook createWorkbook() throws IOException,
			InvalidFormatException {
		return createWorkbook("export_template.xls");
	}

	protected Workbook createWorkbook(String templateFilename)
			throws IOException, InvalidFormatException {
		InputStream is = null;
		Workbook wb = null;
		try {
//			is = getClass().getResourceAsStream(templateFilename);
			is = this.getClass().getClassLoader().getResourceAsStream("templates/" + templateFilename);
			wb = WorkbookFactory.create(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return wb;
	}

	protected void saveWorkbook(Workbook wb, File file) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			wb.write(fos);
			fos.flush();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	protected WordprocessingMLPackage createWordprocessingMLPackage()
			throws IOException, Docx4JException {
		InputStream is = null;
		WordprocessingMLPackage pkg = null;
		try {
//			is = getClass().getResourceAsStream("export_template.docx");
			is = this.getClass().getClassLoader().getResourceAsStream("templates/export_template.docx");
			pkg = WordprocessingMLPackage.load(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return pkg;
	}

	@SuppressWarnings("rawtypes")
	public void generateXls(File file, File logoFile,
			AbstractFilterableListHolderBean bean) throws Exception {
		Workbook wb = createWorkbook();
		bean.formXlsContent(wb, logoFile);
		saveWorkbook(wb, file);
	}

	@SuppressWarnings("rawtypes")
	public void generateXlsx(File file, File logoFile,
			AbstractFilterableListHolderBean bean) throws Exception {
		Workbook wb = createWorkbook("export_template.xlsx");
		bean.formXlsContent(wb, logoFile);
		saveWorkbook(wb, file);
	}

	@SuppressWarnings("rawtypes")
	public void generateDocx(File file, File logoFile,
			AbstractFilterableListHolderBean bean) throws Exception {
		WordprocessingMLPackage pkg = createWordprocessingMLPackage();
		bean.formDocxContent(pkg, logoFile);
		new SaveToZipFile(pkg).save(file);
	}
}