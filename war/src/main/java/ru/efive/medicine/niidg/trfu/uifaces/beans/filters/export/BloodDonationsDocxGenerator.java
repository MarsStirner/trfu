package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.BloodDonationsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodDonationFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodDonationsDocxGenerator
		extends
		BaseDocxGenerator<BloodDonationFilterableListHolderBean, BloodDonationsFilter> {

	public BloodDonationsDocxGenerator(WordprocessingMLPackage wb,
			File logoFile, BloodDonationFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void printTableContentPortion(int offset) {
		List<BloodDonationRequest> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (BloodDonationRequest donationRequest : data) {
			Tr tr = factory.createTr();

			createCell(tr, donationRequest.getNumber());

			Date createdDate = donationRequest.getCreated();
			String cellValue = null;
			if (createdDate != null) {
				cellValue = DateHelper.formatDateByPattern(createdDate,
						"dd.MM.yyyy HH:mm");
			}
			createCell(tr, cellValue);

			Donor donor = donationRequest.getDonor();
			cellValue = null;
			if (donor != null) {
				cellValue = donor.getDescriptionShort();
			}
			createCell(tr, cellValue);

			Classifier donorType = donationRequest.getDonorType();
			cellValue = null;
			if (donorType != null) {
				cellValue = donorType.getValue();
			}
			createCell(tr, cellValue);

			List<BloodDonationType> donationTypes = donationRequest
					.getResultDonationTypeList();
			createCellForDonationType(tr, donationTypes);

			createCell(tr, donationRequest.getStatusName());

			createCell(tr, donationRequest.getCommentary());

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