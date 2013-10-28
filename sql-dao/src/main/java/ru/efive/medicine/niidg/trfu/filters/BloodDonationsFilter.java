package ru.efive.medicine.niidg.trfu.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;

/**
 * Фильтр для представления "Обращения на донацию".
 * 
 * @author Siarhei Ushanau
 */
public class BloodDonationsFilter extends AppendSRPDFilter<BloodDonationsFilter> {
	private static final long serialVersionUID = 8817277956915729553L;
	
	/**
	 * "Нулевое" значение для статуса обращения на донацию.
	 */
	public static final int BLOOD_DONATION_STATUS_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для типа донора.
	 */
	public static final int DONOR_TYPE_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для вида донации.
	 */
	public static final int BLOOD_DONATION_TYPE_NULL_VALUE = 0;

	/**
	 * Номер обращения на донацию.
	 */
	private String number;
	/**
	 * Имя, фамилия или отчетство донора.
	 */
	//private String donor;
	/**
	 * Дата создания записи об обращении.
	 */
	private Date created;
	/**
	 * Статус обращения на донацию (1 - Заполнение; 2 - Донация; 3 - Получение результатов анализов;
	 * 4 - Паспортизация; 9 - Запланировано; 21 - Фракционирование; -1 - Отвод от донорства;
	 * -2 - Донация не состоялась; -3 - Донация отменена;
	 * 0 - любой, значение по умолчанию, прописано в константе BLOOD_DONATION_STATUS_NULL_VALUE).
	 */
	private int statusId;
	/**
     * Тип донора. Используются значения из таблицы trfu_classifiers, категория "Тип донора" (Classifier тип),
     * к которым добавляется значение 0 - любой, значение по умолчанию, прописано в константе DONOR_TYPE_NULL_VALUE).
     */
	private int donorTypeId;
	/**
     * Вид донорства. Используются значения из таблицы trfu_blood_donation_types (BloodDonationType тип),
     * к которым добавляется значение 0 - любой, значение по умолчанию, прописано в константе BLOOD_DONATION_TYPE_NULL_VALUE).
     */
	private int donationTypeId;
	
	private String donorTypeValue;
	private String donationTypeValue;
	
	private List<AliasFilterBean> listAlias;
	private List<FieldFilterBean> listFilds;
	private List<FieldFilterBean> listFildsDisjunction;
	
	private int donorId;
	private int transfusiologistId;

	/**
     * Конструктор по умолчанию.
     */
	public BloodDonationsFilter() {
		super();
		setDefaultValues();
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDonor() {
		return getFirstName();
	}

	public void setDonor(String donor) {
		setFirstName(donor);
		setLastName(donor);
		setMiddleName(donor);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getDonorTypeId() {
		return donorTypeId;
	}

	public void setDonorTypeId(int donorTypeId) {
		this.donorTypeId = donorTypeId;
	}

	public int getDonationTypeId() {
		return donationTypeId;
	}

	public void setDonationTypeId(int donationTypeId) {
		this.donationTypeId = donationTypeId;
	}

	public int getBloodDonationStatusNullValue() {
		return BLOOD_DONATION_STATUS_NULL_VALUE;
	}

	public int getDonorTypeNullValue() {
		return DONOR_TYPE_NULL_VALUE;
	}

	public int getBloodDonationTypeNullValue() {
		return BLOOD_DONATION_TYPE_NULL_VALUE;
	}

	public List<AliasFilterBean> getListAlias() {
		return listAlias;
	}

	public void setListAlias(List<AliasFilterBean> listAlias) {
		this.listAlias = listAlias;
	}
	public List<FieldFilterBean> getListFilds() {
		return listFilds;
	}

	public void setListFilds(List<FieldFilterBean> listFilds) {
		this.listFilds = listFilds;
	}

	public List<FieldFilterBean> getListFildsDisjunction() {
		return listFildsDisjunction;
	}

	public void setListFildsDisjunction(List<FieldFilterBean> listFildsDijunction) {
		this.listFildsDisjunction = listFildsDijunction;
	}

	public String getDonorTypeValue() {
		return donorTypeValue;
	}

	public void setDonorTypeValue(String donorTypeValue) {
		this.donorTypeValue = donorTypeValue;
	}

	public String getDonationTypeValue() {
		return donationTypeValue;
	}

	public void setDonationTypeValue(String donationTypeValue) {
		this.donationTypeValue = donationTypeValue;
	}

	public int getDonorId() {
		return donorId;
	}

	public void setDonorId(int donorId) {
		this.donorId = donorId;
	}

	public int getTransfusiologistId() {
		return transfusiologistId;
	}

	public void setTransfusiologistId(int transfusiologistId) {
		this.transfusiologistId = transfusiologistId;
	}

	protected void setDefaultValues() {
		number = null;
		//donor = null;
		created = null;
		statusId = BLOOD_DONATION_STATUS_NULL_VALUE;
		donorTypeId = DONOR_TYPE_NULL_VALUE;
		donationTypeId = BLOOD_DONATION_TYPE_NULL_VALUE;
		donorTypeValue = null;
		donationTypeValue = null;
		listAlias = new ArrayList<AliasFilterBean>();
		listFilds = new ArrayList<FieldFilterBean>();
		listFildsDisjunction = new ArrayList<FieldFilterBean>();
		donorId = 0;
		transfusiologistId = 0;
	}

	@Override
	public void clear() {
		setDefaultValues();
	}

	@Override
	public void fillFrom(BloodDonationsFilter source) {
		setNumber(source.getNumber());
		//setDonor(source.getDonor());
		setCreated(source.getCreated());
		setStatusId(source.getStatusId());
		setDonorTypeId(source.getDonorTypeId());
		setDonationTypeId(source.getDonationTypeId());
		setDonorTypeValue(source.getDonorTypeValue());
		setDonationTypeValue(source.getDonationTypeValue());
		setListAlias(source.getListAlias());
		setListFilds(source.getListFilds());
		setListFildsDisjunction(source.getListFildsDisjunction());
		setDonorId(source.getDonorId());
		setTransfusiologistId(source.getTransfusiologistId());
	}
}