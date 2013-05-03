package ru.efive.medicine.niidg.trfu.uifaces.beans.filters.export;

import java.io.File;
import java.util.Date;
import java.util.List;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodDonationType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.dictionary.TransfusionType;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.filters.AbstractFilterableListHolderBean;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

@SuppressWarnings("rawtypes")
public abstract class BaseGenerator<T extends AbstractFilterableListHolderBean, F extends AbstractFilter<F>> {
	public static final String COMMON_LABEL = "Федеральный научно-клинический центр\nдетской гематологии, онкологии и иммунологии";
	public static final String INDIVIDUAL_LABEL = "Результаты фильтрации по форме";

	public static final int PAGE_SIZE = 100;

	protected T bean;
	protected F filter;
	protected File logoFile;

	public BaseGenerator(File logoFile, T bean) {
		this.bean = bean;
		this.logoFile = logoFile;
		this.filter = (F) bean.getCurrentFilterCopy();
	}

	protected String getIndividualLabel() {
		return INDIVIDUAL_LABEL + " \"" + bean.getFormTitle() + "\"";
	}

	protected String getCommonLabel() {
		return COMMON_LABEL;
	}

	protected String formatBloodInfo(BloodGroup bloodGroup,
			Classifier rhesusFactor) {
		StringBuffer bloodInfo = new StringBuffer();
		if (bloodGroup != null) {
			bloodInfo.append(bloodGroup.getValue());
		}
		if (rhesusFactor != null) {
			if (bloodInfo.length() > 0) {
				bloodInfo.append("\n");
			}
			bloodInfo.append(rhesusFactor.getValue());
		}
		return bloodInfo.toString();
	}

	protected String formatTransfusionType(TransfusionType transfusionType, Date planDate) {
		StringBuffer transfusionTypeInfo = new StringBuffer(
				transfusionType.getValue());
		if (transfusionType.getId() == 0 && planDate != null) {
			transfusionTypeInfo.append(" на дату ");
			transfusionTypeInfo.append(DateHelper.formatDateByPattern(planDate,
					"dd.MM.yyyy"));
		}
		return transfusionTypeInfo.toString();
	}

	protected String formatDonationTypes(
			List<BloodDonationType> donationTypes) {
		StringBuffer donationTypesInfo = new StringBuffer();
		for (BloodDonationType bloodDonationType : donationTypes) {
			if (donationTypesInfo.length() > 0) {
				donationTypesInfo.append("\n");
			}
			donationTypesInfo.append(bloodDonationType.getValue());
		}
		return donationTypesInfo.toString();
	}

	protected final void printTableContent() {
		int count = bean.getTotalCount(filter);
		int offset = 0;
		while (offset < count) {
			printTableContentPortion(offset);
			offset += PAGE_SIZE;
		}
	}

	protected abstract void printTableContentPortion(int offset);
}