package ru.efive.medicine.niidg.trfu.filters;

import java.io.Serializable;

/**
 * Базовый класс реализации фильтров. Для удобства реализации и повторного использования
 * в класс включены константы с подписями для параметров фильтров а также некоторые 
 * константы со значениями по умолчанию для тех параметров, которые используются в нескольких
 * фильтрах.
 * 
 * @author Siarhei Ushanau
 */
public abstract class AbstractFilter<T> implements Serializable {
	private static final long serialVersionUID = 2699612886836430997L;

	/**
	 * Подпись для параметра "Номер".
	 */
	public static final String NUMBER_TITLE = "Номер";
	/**
	 * Подпись для параметра "Имя".
	 */
	public static final String FIRST_NAME_TITLE = "Имя";
	/**
	 * Подпись для параметра "Фамилия".
	 */
	public static final String LAST_NAME_TITLE = "Фамилия";
	/**
	 * Подпись для параметра "Отчество".
	 */
	public static final String MIDDLE_NAME_TITLE = "Отчество";
	/**
	 * Подпись для параметра "Дата создания".
	 */
	public static final String CREATED_TITLE = "Дата создания";
	/**
	 * Подпись для параметра "Дата рождения".
	 */
	public static final String BIRTH_TITLE = "Дата рождения";
	/**
	 * Подпись для параметра "Пол".
	 */
	public static final String GENDER_TITLE = "Пол";
	/**
	 * Подпись для параметра "Документ".
	 */
	public static final String DOCUMENT_TYPE_TITLE = "Документ";
	/**
	 * Подпись для параметра "Номер".
	 */
	public static final String DOCUMENT_NUMBER_TITLE = "Номер";
	/**
	 * Подпись для параметра "Серия".
	 */
	public static final String DOCUMENT_SERIES_TITLE = "Серия";
	/**
	 * Подпись для параметра "Номер в ЕДЦ".
	 */
	public static final String EXTERNAL_NUMBER_TITLE = "Номер в ЕДЦ";
	/**
	 * Подпись для параметра "Фактический адрес проживания".
	 */
	public static final String FACT_ADDRESS_TITLE = "Фактический <br/>адрес проживания";
	/**
	 * Подпись для параметра "Категория донора".
	 */
	public static final String DONOR_CATEGORY_TITLE = "Категория донора";
	/**
	 * Подпись для параметра "Статус донора".
	 */
	public static final String DONOR_STATUS_TITLE = "Статус донора";
	/**
	 * Подпись для параметра "Статус".
	 */
	public static final String STATUS_TITLE = "Статус";
	/**
	 * Подпись для параметра "Группа крови".
	 */
	public static final String BLOOD_GROUP_TITLE = "Группа крови";
	/**
	 * Подпись для параметра "Резус-фактор".
	 */
	public static final String RHESUS_FACTOR_TITLE = "Резус-фактор";
	/**
	 * Подпись для параметра "Прошедшие карантинизацию".
	 */
	public static final String PAST_QUARANTINE_TITLE = "Прошедшие <br/>карантинизацию";
	/**
	 * Подпись для параметра "Компонент" (тип компонента крови).
	 */
	public static final String BLOOD_COMPONENT_TYPE_TITLE = "Компонент";
	/**
	 * Подпись для параметра "Код донора".
	 */
	public static final String DONOR_CODE_TITLE = "Код донора";
	/**
	 * Подпись для параметра "Изготовитель".
	 */
	public static final String MAKER_TITLE = "Изготовитель";
	/**
	 * Подпись для параметра "Дата донации".
	 */
	public static final String DONATION_DATE_TITLE = "Дата донации";
	/**
	 * Подпись для параметра "Дата окончания срока хранения".
	 */
	public static final String EXPIRATION_DATE_TITLE = "Дата окончания <br/>срока хранения";
	/**
	 * Подпись для параметра "Донор".
	 */
	public static final String DONOR_TITLE = "Донор";
	/**
	 * Подпись для параметра "Планируемая дата обследования".
	 */
	public static final String EXAMINATION_PLAN_DATE_TITLE = "Планируемая дата <br/>обследования";
	/**
	 * Подпись для параметра "Тип обследования".
	 */
	public static final String EXAMINATION_TYPE_TITLE = "Тип обследования";
	/**
	 * Подпись для параметра "Тип донора".
	 */
	public static final String DONOR_TYPE_TITLE = "Тип донора";
	/**
	 * Подпись для параметра "Вид донорства".
	 */
	public static final String DONATION_TYPE_TITLE = "Вид донорства";
	/**
	 * Подпись для параметра "Отделение".
	 */
	public static final String DIVISION_TITLE = "Отделение";
	/**
	 * Подпись для параметра "Имя реципиента".
	 */
	public static final String RECIPIENT_FIRST_NAME_TITLE = "Имя реципиента";
	/**
	 * Подпись для параметра "Фамилия реципиента".
	 */
	public static final String RECIPIENT_LAST_NAME_TITLE = "Фамилия <br/>реципиента";
	/**
	 * Подпись для параметра "Отчество реципиента".
	 */
	public static final String RECIPIENT_MIDDLE_NAME_TITLE = "Отчество <br/>реципиента";
	/**
	 * Подпись для параметра "Вид трансфузии".
	 */
	public static final String TRANSFUSION_TYPE_TITLE = "Вид трансфузии";
	/**
	 * Подпись для параметра "Дата выдачи".
	 */
	public static final String ISSUE_DATE_TITLE = "Дата выдачи";
	/**
	 * Подпись для параметра "Дата регистрации".
	 */
	public static final String REGISTRATION_DATE_TITLE = "Дата регистрации";
	/**
	 * Подпись для параметра "ФИО".
	 */
	public static final String FIO_TITLE = "ФИО";
	
	/**
	 * "Нулевое" значение для группы крови.
	 */
	public static final int BLOOD_GROUP_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для резус-фактора.
	 */
	public static final int RHESUS_FACTOR_NULL_VALUE = 0;
	/**
	 * "Нулевое" значение для типа компонента крови.
	 */
	public static final int BLOOD_COMPONENT_TYPE_NULL_VALUE = 0;

	public int getBloodComponentTypeNullValue() {
		return BLOOD_COMPONENT_TYPE_NULL_VALUE;
	}
	
	public String getNumberTitle() {
		return NUMBER_TITLE;
	}

	public String getFirstNameTitle() {
		return FIRST_NAME_TITLE;
	}

	public String getLastNameTitle() {
		return LAST_NAME_TITLE;
	}

	public String getMiddleNameTitle() {
		return MIDDLE_NAME_TITLE;
	}

	public String getCreatedTitle() {
		return CREATED_TITLE;
	}

	public String getBirthTitle() {
		return BIRTH_TITLE;
	}

	public String getGenderTitle() {
		return GENDER_TITLE;
	}

	public String getDocumentTypeTitle() {
		return DOCUMENT_TYPE_TITLE;
	}

	public String getDocumentNumberTitle() {
		return DOCUMENT_NUMBER_TITLE;
	}

	public String getDocumentSeriesTitle() {
		return DOCUMENT_SERIES_TITLE;
	}

	public String getExternalNumberTitle() {
		return EXTERNAL_NUMBER_TITLE;
	}

	public String getFactAddressTitle() {
		return FACT_ADDRESS_TITLE;
	}

	public String getDonorStatusTitle() {
		return DONOR_STATUS_TITLE;
	}

	public String getDonorCategoryTitle() {
		return DONOR_CATEGORY_TITLE;
	}

	public String getStatusTitle() {
		return STATUS_TITLE;
	}

	public String getBloodGroupTitle() {
		return BLOOD_GROUP_TITLE;
	}

	public String getRhesusFactorTitle() {
		return RHESUS_FACTOR_TITLE;
	}

	public String getPastQuarantineTitle() {
		return PAST_QUARANTINE_TITLE;
	}

	public String getBloodComponentTypeTitle() {
		return BLOOD_COMPONENT_TYPE_TITLE;
	}

	public String getDonorCodeTitle() {
		return DONOR_CODE_TITLE;
	}

	public String getMakerTitle() {
		return MAKER_TITLE;
	}

	public String getDonationDateTitle() {
		return DONATION_DATE_TITLE;
	}

	public String getExpirationDateTitle() {
		return EXPIRATION_DATE_TITLE;
	}

	public String getDonorTitle() {
		return DONOR_TITLE;
	}

	public String getExaminationPlanDateTitle() {
		return EXAMINATION_PLAN_DATE_TITLE;
	}

	public String getExaminationTypeTitle() {
		return EXAMINATION_TYPE_TITLE;
	}

	public String getDonorTypeTitle() {
		return DONOR_TYPE_TITLE;
	}

	public String getDonationTypeTitle() {
		return DONATION_TYPE_TITLE;
	}

	public String getDivisionTitle() {
		return DIVISION_TITLE;
	}

	public String getRecipientFirstNameTitle() {
		return RECIPIENT_FIRST_NAME_TITLE;
	}

	public String getRecipientLastNameTitle() {
		return RECIPIENT_LAST_NAME_TITLE;
	}

	public String getRecipientMiddleNameTitle() {
		return RECIPIENT_MIDDLE_NAME_TITLE;
	}

	public String getTransfusionTypeTitle() {
		return TRANSFUSION_TYPE_TITLE;
	}

	public String getIssueDateTitle() {
		return ISSUE_DATE_TITLE;
	}

	public String getRegistrationDateTitle() {
		return REGISTRATION_DATE_TITLE;
	}

	public String getFioTitle() {
		return FIO_TITLE;
	}

	public int getBloodGroupNullValue() {
		return BLOOD_GROUP_NULL_VALUE;
	}

	public int getRhesusFactorNullValue() {
		return RHESUS_FACTOR_NULL_VALUE;
	}

	/**
	 * Перевод фильтра в начальное нулевое состояние.
	 */
	public abstract void clear();
	
	/**
	 * Заполнение параметров фильтра параметрами из другого объекта-источника. 
	 * 
	 * @param source объект-источник.
	 */
	public abstract void fillFrom(T source);
}