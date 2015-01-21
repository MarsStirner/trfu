package ru.efive.dao.sql.entity.enums;

/**
 * Набор ролей
 */
public enum RoleType {
	
	/** АРМ Регистратура */
	REGISTRATOR,
	
	/** АРМ Терапевт */
	THERAPIST,
	
	/** АРМ Операционная */
	OPERATIONAL,
	
	/** АРМ Фракционирование */
	RECTIFICATION,
	
	/** АРМ Выбраковка */
	DRAFT_OUT,
	
	/** АРМ Этикетировка */
	LABELING,
	
	/** АРМ Экспедиция */
	EXPEDITION,
	
	/** АРМ Карантинизация */
	QUARANTINE,
	
	/** АРМ Лаборатория */
	LABORATORY,
	
	/** АРМ Вирусинактивация */
	//INACTIVATION,
	
	/** АРМ Лечебные процедуры */
	MEDICAL,
	
	/** АРМ Контроль качества */
	IN_CONTROL,
	
	/** Старшая медицинская сестра */
	HEAD_NURSE,
	
	/** АРМ Заведующий отделением */
	DIVISION_SUPERINTENDENT,
	
	/** предметное администрирование */
	ENTERPRISE_ADMINISTRATION,
	
	/** АРМ Вторичная переработка */
	SECONDARY_PROCESSING
	
}