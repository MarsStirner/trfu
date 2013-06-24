package ru.efive.wf.core;

import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;

public class StatusChangeVirusinactivationAction extends StatusChangeAction {

	public StatusChangeVirusinactivationAction(Process process) {
		super(process);
	}

	/**
	 * Определяет для каких КК доступно действие Вирусинактивация
	 */
	@Override
	public boolean isAvailable() {
		ProcessedData data = getProcess().getProcessedData();
		BloodComponent component = (BloodComponent) data;
		BloodComponentType componentType = component.getComponentType();
		if (!componentType.isLite()) {
			return !isRectification(component.getDonationId());
		}
		return false;
	}

	public boolean isRectification(int donationId) {
		try {
			if (donationId > 0) {
				BloodDonationRequestDAOImpl dao = (BloodDonationRequestDAOImpl) ApplicationContextHelper
						.getApplicationContext().getBean(
								ApplicationHelper.DONATION_DAO);
				BloodDonationRequest donation = dao.get(donationId);
				if (donation != null && donation.getStatusId() == 21) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}