package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodComponentFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodComponentsDocxGenerator
		extends
		BaseDocxGenerator<BloodComponentFilterableListHolderBean, BloodComponentsFilter> {
	private int totalCount = 0;
	private long totalVolume = 0;

	public BloodComponentsDocxGenerator(WordprocessingMLPackage wb,
			File logoFile, BloodComponentFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void printTableContentPortion(int offset) {
		List<BloodComponent> data = bean.loadDocuments(offset, PAGE_SIZE,
				filter);
		for (BloodComponent bloodComponent : data) {
			Tr tr = factory.createTr();

			createCell(tr, bloodComponent.getFullNumber());

			BloodComponentType bloodComponentType = bloodComponent
					.getComponentType();
			String cellValue = null;
			if (bloodComponentType != null) {
				cellValue = bloodComponentType.getCodeAndValue();
			}
			createCell(tr, cellValue);

			createCell(tr, String.valueOf(bloodComponent.getVolume()));

			Anticoagulant anticoagulant = bloodComponent.getAnticoagulant();
			cellValue = null;
			if (anticoagulant != null) {
				cellValue = anticoagulant.getValue();
			}
			createCell(tr, cellValue);

			BloodGroup bloodGroup = bloodComponent.getBloodGroup();
			Classifier rhesusFactor = bloodComponent.getRhesusFactor();
			createCellForBloodGroup(tr, bloodGroup, rhesusFactor);

			Date expirationDate = bloodComponent.getExpirationDate();
			cellValue = null;
			if (expirationDate != null) {
				cellValue = DateHelper.formatDateByPattern(expirationDate,
						"dd.MM.yyyy");
			}
			createCell(tr, cellValue);

			createCell(tr, bloodComponent.getStatusName());

			contentTable.getEGContentRowContent().add(tr);
			
			totalVolume += bloodComponent.getVolume();
		}
		totalCount += data.size();
	}
	
	@Override
	protected void printTotalVolume() {
		P p = createParagraphOfText(JcEnumeration.LEFT, false,
				"Общий объем: " + totalVolume);
		contentTable.getEGContentRowContent().add(p);
	}
		
	@Override
	protected void printSummary() {
		int count = totalCount;
		P p = createParagraphOfText(JcEnumeration.LEFT, false,
				"Итого: " + count);
		contentTable.getEGContentRowContent().add(p);
	}
}