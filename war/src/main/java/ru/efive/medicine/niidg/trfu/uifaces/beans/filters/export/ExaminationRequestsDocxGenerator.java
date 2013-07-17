package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.ExaminationType;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.ExaminationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.ExaminationRequestFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class ExaminationRequestsDocxGenerator
		extends
		BaseDocxGenerator<ExaminationRequestFilterableListHolderBean, ExaminationsFilter> {

	public ExaminationRequestsDocxGenerator(WordprocessingMLPackage wb,
			File logoFile, ExaminationRequestFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void printTableContentPortion(int offset) {
		List<ExaminationRequest> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (ExaminationRequest examinationRequest : data) {
			Tr tr = factory.createTr();

			createCell(tr, examinationRequest.getNumber());

			Date createdDate = examinationRequest.getCreated();
			String cellValue = null;
			if (createdDate != null) {
				cellValue = DateHelper.formatDateByPattern(createdDate,
						"dd.MM.yyyy HH:mm");
			}
			createCell(tr, cellValue);

			Donor donor = examinationRequest.getDonor();
			cellValue = null;
			if (donor != null) {
				cellValue = donor.getDescriptionShort();
			}
			createCell(tr, cellValue);

			ExaminationType examinationType = bean
					.getExaminationType(examinationRequest.getExaminationType());
			cellValue = null;
			if (examinationType != null) {
				cellValue = examinationType.getValue();
			}
			createCell(tr, cellValue);

			Date planDate = examinationRequest.getPlanDate();
			cellValue = null;
			if (planDate != null) {
				cellValue = DateHelper.formatDateByPattern(planDate,
						"dd.MM.yyyy HH:mm");
			}
			createCell(tr, cellValue);

			createCell(tr, examinationRequest.getStatusName());

			createCell(tr, examinationRequest.getCommentary());

			contentTable.getEGContentRowContent().add(tr);
		}
		printSummary();
	}
	
	private void printSummary() {
		int count = bean.getTotalCount(bean.getCurrentFilter());
		P p = createParagraphOfText(JcEnumeration.LEFT, false,
				"Итого: " + count);
		contentTable.getEGContentRowContent().add(p);
	}
}