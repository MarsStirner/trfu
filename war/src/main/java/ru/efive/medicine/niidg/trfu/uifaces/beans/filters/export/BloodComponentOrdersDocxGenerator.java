package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.Tr;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentOrderRequest;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentOrdersFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.BloodComponentOrderFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class BloodComponentOrdersDocxGenerator
		extends
		BaseDocxGenerator<BloodComponentOrderFilterableListHolderBean, BloodComponentOrdersFilter> {

	public BloodComponentOrdersDocxGenerator(WordprocessingMLPackage wb,
			File logoFile, BloodComponentOrderFilterableListHolderBean bean) {
		super(wb, logoFile, bean);
//		filter = bean.createFilterInstance();
//		filter.fillFrom(bean.getCurrentFilter());
	}

	@Override
	protected void printTableContentPortion(int offset) {
		List<BloodComponentOrderRequest> data = bean.loadDocuments(offset,
				PAGE_SIZE, filter);
		for (BloodComponentOrderRequest orderRequest : data) {
			Tr tr = factory.createTr();

			createCell(tr, orderRequest.getNumber());

			Date createdDate = orderRequest.getCreated();
			String cellValue = null;
			if (createdDate != null) {
				cellValue = DateHelper.formatDateByPattern(createdDate,
						"dd.MM.yyyy HH:mm");
			}
			createCell(tr, cellValue);

			createCell(tr, orderRequest.getRecipient());

			BloodComponentType componentType = orderRequest.getComponentType();
			cellValue = null;
			if (componentType != null) {
				cellValue = componentType.getCodeAndValue();
			}
			createCell(tr, cellValue);

			int transfusionTypeId = orderRequest.getTransfusionType();
			TransfusionType transfusionType = bean
					.getTransfusionType(transfusionTypeId);
			Date planDate = orderRequest.getPlanDate();
			createCellForTransfusionType(tr, transfusionType, planDate);

			createCell(tr, orderRequest.getDivision());

			createCell(tr, orderRequest.getStatusName());

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