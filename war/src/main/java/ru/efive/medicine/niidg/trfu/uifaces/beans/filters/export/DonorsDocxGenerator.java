package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.List;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.DonorCategory;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.DonorsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.DonorFilterableListHolderBean;

public class DonorsDocxGenerator extends
		BaseDocxGenerator<DonorFilterableListHolderBean, DonorsFilter> {

	public DonorsDocxGenerator(WordprocessingMLPackage wb, File logoFile,
			DonorFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void printTableContentPortion(int offset) {
		List<Donor> data = bean.loadDocuments(offset, PAGE_SIZE, filter);
		for (Donor donor : data) {
			Tr tr = factory.createTr();

			createCell(tr, donor.getDescriptionShort());

			createCell(tr, donor.getNumber());

			DonorCategory donorCategory = bean.getDonorCategory(donor
					.getCategory());
			String cellValue = null;
			if (donorCategory != null) {
				cellValue = donorCategory.getValue();
			}
			createCell(tr, cellValue);

			BloodGroup bloodGroup = donor.getBloodGroup();
			Classifier rhesusFactor = donor.getRhesusFactor();
			createCellForBloodGroup(tr, bloodGroup, rhesusFactor);

			createCell(tr, donor.getStatusName());

			createCell(tr, donor.getCommentary());

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