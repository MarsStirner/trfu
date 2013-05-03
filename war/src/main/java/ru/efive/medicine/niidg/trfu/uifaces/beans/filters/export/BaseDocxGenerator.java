package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.Ind;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcMar;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.AbstractFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.FilterParameter;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseDocxGenerator<T extends AbstractFilterableListHolderBean, F extends AbstractFilter<F>>
		extends BaseGenerator<T, F> {
	protected WordprocessingMLPackage pkg;
	protected MainDocumentPart refBody;
	protected ObjectFactory factory = org.docx4j.jaxb.Context
			.getWmlObjectFactory();
	protected Tbl contentTable;

	public BaseDocxGenerator(WordprocessingMLPackage pkg, File logoFile, T bean) {
		super(logoFile, bean);
		this.pkg = pkg;
		refBody = pkg.getMainDocumentPart();
	}

	public void formContent() throws Exception {
		clearContent();
		printLogo();
		printCommonLabel();
		printEmptyPara();
		printIndividualLabel();
		printEmptyPara();
		printFilterParameters();
		printEmptyPara();
		printTable();
		printEmptyPara();
		printDateTime();
	}

	protected void clearContent() {
		refBody.getContent().clear();
	}

	protected void printDateTime() {
		P p = createParagraphOfText(JcEnumeration.RIGHT, false,
				DateHelper.getCurrentDate());
		refBody.addObject(p);
	}

	protected void printIndividualLabel() {
		P p = createParagraphOfText(JcEnumeration.CENTER, true,
				getIndividualLabel());
		refBody.addObject(p);
	}

	protected void printCommonLabel() {
		P p = createParagraphOfText(JcEnumeration.CENTER, false,
				getCommonLabel());
		refBody.addObject(p);
	}

	protected void printEmptyPara() {
		P p = createParagraphOfText(JcEnumeration.LEFT, false, null);
		refBody.addObject(p);
	}

	protected void printFilterParameters() {
		Tbl tbl = factory.createTbl();

		List<FilterParameter> parameters = bean.getNotNullFilterParameters();
		for (FilterParameter parameter : parameters) {
			Tr tr = factory.createTr();
			String parameterLabel = parameter.getTitle();
			parameterLabel = parameterLabel.replaceAll("<br/>", "\n");
			createCell(tr, true, parameterLabel);
			createCell(tr, false, parameter.getValue());
			tbl.getEGContentRowContent().add(tr);
		}

		refBody.addObject(tbl);
	}

	protected void printLogo() throws Exception {
		P p = factory.createP();
		refBody.addObject(p);

		R r = factory.createR();
		p.getContent().add(r);

		Drawing drawing = factory.createDrawing();
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
				.createImagePart(pkg, logoFile);
		Inline inline = imagePart.createImageInline(null, null, 0, 1, false);
		drawing.getAnchorOrInline().add(inline);
		r.getContent().add(drawing);

		PPr ppr = factory.createPPr();
		p.setPPr(ppr);
		setAlignment(ppr, JcEnumeration.CENTER);
		setIndentionLeft(ppr, new BigInteger("0"));
	}

	protected void printTable() {
		contentTable = factory.createTbl();
		printTableHeader();
		printTableContent();
		addBorders(contentTable);
		refBody.addObject(contentTable);
	}

	protected void printTableHeader() {
		Tr tr = factory.createTr();
		List<String> columns = bean.getFormColumns();
		for (String columnTitle : columns) {
			createCell(tr, true, columnTitle);
		}
		contentTable.getEGContentRowContent().add(tr);
	}

	protected P createParagraphOfText(JcEnumeration alignment, boolean bold,
			String text) {
		P p = factory.createP();
		R r = factory.createR();
		p.getContent().add(r);

		RPr rpr = factory.createRPr();
		r.setRPr(rpr);

		if (bold) {
			rpr.setB(factory.createBooleanDefaultTrue());
		}

		HpsMeasure size = factory.createHpsMeasure();
		size.setVal(getDefaultFontSize());
		rpr.setSz(size);
		rpr.setSzCs(size);

		RFonts rFonts = factory.createRFonts();
		rFonts.setAscii(getDefaultFont());
		rFonts.setHAnsi(getDefaultFont());
		rFonts.setCs(getDefaultFont());
		rpr.setRFonts(rFonts);

		PPr ppr = factory.createPPr();
		p.setPPr(ppr);
		setAlignment(ppr, alignment);
		setIndentionLeft(ppr, new BigInteger("0"));

		addTextToParagraph(r, text);

		return p;
	}

	protected void addTextToParagraph(R r, String text) {
		if (text == null) {
			text = "";
		}

		boolean firstToken = true;
		String[] tokens = text.split("\n");
		for (String token : tokens) {
			if (!firstToken) {
				r.getContent().add(factory.createBr());
			}

			Text t = factory.createText();
			t.setValue(token);
			r.getContent().add(t);

			firstToken = false;
		}
	}

	protected void setIndentionLeft(PPr ppr, BigInteger leftIndention) {
		Ind indention = factory.createPPrBaseInd();
		indention.setLeft(leftIndention);
		ppr.setInd(indention);
	}

	protected void setAlignment(PPr ppr, JcEnumeration alignment) {
		Jc textAlignment = factory.createJc();
		textAlignment.setVal(alignment);
		ppr.setJc(textAlignment);
	}

	protected String getDefaultFont() {
		return "Times New Roman";
	}

	protected BigInteger getDefaultFontSize() {
		return new BigInteger("20");
	}

	protected void addBorders(Tbl tbl) {
		TblPr tblpr = factory.createTblPr();
		tbl.setTblPr(tblpr);

		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("1"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);

		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);

		tblpr.setTblBorders(borders);
	}

	protected void createCellForBloodGroup(Tr tr, BloodGroup bloodGroup,
			Classifier rhesusFactor) {
		createCell(tr, formatBloodInfo(bloodGroup, rhesusFactor));
	}

	protected void createCell(Tr tr, String text) {
		createCell(tr, false, text);
	}

	protected void createCell(Tr tr, boolean bold, String text) {
		P p = createParagraphOfText(JcEnumeration.LEFT, bold, text);

		Tc tc = factory.createTc();
		tc.getEGBlockLevelElts().add(p);
		tr.getEGContentCellContent().add(tc);

		TcPr tcpr = factory.createTcPr();
		tc.setTcPr(tcpr);

		CTVerticalJc vAlignment = new CTVerticalJc();
		vAlignment.setVal(STVerticalJc.CENTER);
		tcpr.setVAlign(vAlignment);

		TblWidth tblWidth = factory.createTblWidth();
		tblWidth.setType("dxa");
		tblWidth.setW(new BigInteger("57"));

		TcMar tcMar = factory.createTcMar();
		tcMar.setLeft(tblWidth);
		tcMar.setRight(tblWidth);
		tcMar.setTop(tblWidth);
		tcpr.setTcMar(tcMar);
	}

	protected void createCellForTransfusionType(Tr tr,
			TransfusionType transfusionType, Date planDate) {
		String transfusionTypeInfo = formatTransfusionType(transfusionType,
				planDate);
		createCell(tr, transfusionTypeInfo);
	}

	protected void createCellForDonationType(Tr tr,
			List<BloodDonationType> donationTypes) {
		String donationTypesInfo = formatDonationTypes(donationTypes);
		createCell(tr, donationTypesInfo);
	}

	// public abstract void printTableContent(Tbl tbl);
}