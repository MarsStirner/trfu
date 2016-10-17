package ru.efive.medicine.niidg.trfu.util;

import java.util.Locale;

public final class ApplicationHelper {


	private ApplicationHelper() {
		
	}
	
    public static final Locale getLocale() {
    	return locale;
    }
    
    public static final String getFullTimeFormat() {
    	return "dd.MM.yyyy HH:mm:ss z";
    }
    
    public static String getStatusName(String type, int id) {
    	String result = "";
    	if (type.equals("Donor")) {
    		if (id == 1) {
        		result = "Кандидат";
        	}
    		else if (id == 2) {
        		result = "Донор";
        	}
        	else if (id == -1){
        		result = "Временный отвод";
        	}
        	else if (id == -2) {
        		result = "Абсолютный отвод";
        	}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("Examination")) {
    		if (id == 1) {
    			result = "Заполнение";
    		}
    		else if (id == 7) {
    			result = "Первичное исследование";
    		}
    		else if (id == 2) {
    			result = "Осмотр";
    		}
    		else if (id == 3) {
    			result = "Получение результатов анализов";
    		}
    		else if (id == 4) {
    			result = "Определение допуска к донорству";
    		}
    		else if (id == 5){
        		result = "Допущен";
        	}
    		else if (id == 6) {
    			result = "Направлен на дообследование";
    		}
    		else if (id == -1) {
    			result = "Отвод от донорства";
    		}
    		else if (id == -2) {
    			result = "Отменено";
    		}
    		else if (id == 9) {
    			result = "Запланировано";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodComponent")) {
    		if (id == 1) {
    			result = "Зарегистрирован";
    		}
    		else if (id == 2) {
        		result = "В карантине";
        	}
    		else if (id == 3) {
    			result = "Готов к выдаче";
    		}
    		else if (id == 4) {
    			result = "Задержка";
    		}
    		else if (id == 5) {
    			result = "Готов к выдаче из карантина";
    		}
    		else if (id == 6) {
    			result = "Брак из карантина";
    		}
    		else if (id == 10) {
    			result = "Выдан";
    		}
    		else if (id == 11) {
    			result = "Забронирован";
    		}
    		else if (id == -1) {
    			result = "Брак";
    		}
    		else if (id == -2) {
    			result = "Списан";
    		}
    		else if (id == -10) {
    			result = "Утилизирован";
    		}
    		else if (id == 100) {
    			result = "Разделен";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodComponentOrder")) {
    		if (id == 1) {
    			result = "Заполнение";
    		}
    		else if (id == 2) {
    			result = "Обработка";
    		}
    		else if (id == 3) {
    			result = "Компонент выдан";
    		}
    		else if (id == 4) {
    			result = "Забронировано";
    		}
    		else if (id == -1){
        		result = "Отказ";
        	}
    		else if (id == -2) {
    			result = "Утилизация";
    		}
    		else if (id == -3) {
    			result = "Отменена";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodDonation")) {
    		if (id == 1) {
    			result = "Заполнение";
    		}
    		else if (id == 2) {
        		result = "Донация";
        	}
    		else if (id == 21) {
    			result = "Фракционирование";
    		}
    		else if (id == 3) {
    			result = "Получение результатов анализов";
    		}
    		else if (id == 4) {
    			result = "Паспортизация";
    		}
    		else if (id == 9) {
    			result = "Запланировано";
    		}
    		else if (id == -1) {
    			result = "Отвод от донорства";
    		}
    		else if (id == -2) {
    			result = "Донация не состоялась";
    		}
    		else if (id == -3) {
    			result = "Донация отменена";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BiomaterialDonor")) {
    		if (id == 1) {
    			result = "Кандидат";
    		}
    		else {
        		result = "";
        	}
    	}
    	else if (type.equals("Operation")) {
    		if (id == 1) {
    			result = "Заполнение";
    		}
    		else {
        		result = "";
        	}
    	}
    	else if (type.equals("Biomaterial")) {
    		if (id == 1) {
    			result = "Зарегистрирован";
    		}
    		else if (id == 2) {
    			result = "Готов к обработке";
    		}
    		else if (id == 3) {
    			result = "Обработка";
    		}
    		else if (id == 4) {
    			result = "Выдан";
    		}
    		else if (id == 5) {
    			result = "Заморожен";
    		}
    		else if (id == 6) {
    			result = "Разморожен";
    		}
    		else if (id == 100) {
    			result = "Разделен";
    		}
    		else if (id == -10) {
    			result = "Утилизирован";
    		}
    		else {
        		result = "";
        	}
    	}
    	return result;
    }
    
    public static int getStatusId(String type, String name) {
    	int result = 0;
    	if (type.equals("BloodComponent")) {
    		if (name.equals("Зарегистрирован")) {
    			result = 1;
    		}
    		else if (name.equals("В карантине")) {
        		result = 2;
        	}
    		else if (name.equals("Готов к выдаче")) {
    			result = 3;
    		}
    		else if (name.equals("Задержка")) {
    			result = 4;
    		}
    		else if (name.equals("Готов к выдаче из карантина")) {
    			result = 5;
    		}
    		else if (name.equals("Брак из карантина")) {
    			result = 6;
    		}
    		else if (name.equals("Выдан")) {
    			result = 10;
    		}
    		else if (name.equals("Забронирован")) {
    			result = 11;
    		}
    		else if (name.equals("Брак")) {
    			result = -1;
    		}
    		else if (name.equals("Списан")) {
    			result = -2;
    		}
    		else if (name.equals("Утилизирован")) {
    			result = -10;
    		}
    		else if (name.equals("Разделен")) {
    			result = 100;
    		}
    	}
    	return result;
    }
    
    public static String getActionName(String type, int id) {
    	String result = "";
    	if (type.equals("Donor")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
        		result = "Направить на донорство";
        	}
    		else if (id == -1) {
        		result = "Допустить к донорству";
        	}
    		else if (id == -2) {
        		result = "Допустить к донорству";
        	}
    		else if (id == -3) {
        		result = "Отвести от донорства";
        	}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("Examination")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
    			result = "Направить на обследование";
    		}
    		else if (id == 2) {
    			result = "Внести результаты анализов";
    		}
    		else if (id == 3) {
    			result = "Результаты анализов получены";
    		}
    		else if (id == 4) {
    			result = "Допущен";
    		}
    		else if (id == 5){
        		result = "На дообследование";
        	}
    		else if (id == 6){
        		result = "На дообследование";
        	}
    		else if (id == 7){
        		result = "Допущен";
        	}
    		else if (id == 9) {
    			result = "Запланировать";
    		}
    		else if (id == 10) {
    			result = "Направить на обследование";
    		}
    		else if (id == 11) {
    			result = "Отправить на первичное исследование";
    		}
    		else if (id == 12) {
    			result = "Отправить на первичное исследование";
    		}
    		else if (id == 13) {
    			result = "Направить на обследование";
    		}
    		else if (id == -1) {
    			result = "Отвести от донорства";
    		}
    		else if (id == -2) {
    			result = "Отвести от донорства";
    		}
    		else if (id == -3) {
    			result = "Отвести от донорства";
    		}
    		else if (id == -4) {
    			result = "Отменить";
    		}
    		else if (id == -5) {
    			result = "Отменить";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodComponent")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
    			result = "В карантин";
    		}
    		else if (id == 2) {
        		result = "Готов к выдаче";
        	}
    		else if (id == 3) {
    			result = "Готов к выдаче";
    		}
    		else if (id == 4) {
    			result = "Задержка";
    		}
    		else if (id == 5) {
    			result = "Готов к выдаче";
    		}
    		else if (id == 6) {
    			result = "Выдан";
    		}
    		else if (id == 7) {
    			result = "Готов к выдаче";
    		}
    		else if (id == 11) {
    			result = "Забронировать";
    		}
    		else if (id == 12) {
    			result = "Отказ от бронирования";
    		}
    		else if (id == 13) {
    			result = "Отмена выдачи";
    		}
    		else if (id == VIRUSINAKTIVATION_ID) {
    			result = VIRUSINAKTIVATION;
    		}
    		else if (id == 99) {
    			// Готов к выдаче, отрицательная рекация
    			result = "Готов к выдаче";
    		}
    		else if (id == 100) {
    			result = "Разделен";
    		}
    		else if (id == 1001) {
    			result = "На контроле качества";
    		}
    		else if (id == 1002) {
    			result = "Контроль пройден";
    		}
    		else if (id == -1) {
    			result = "Брак";
    		}
    		else if (id == -2) {
    			result = "Брак";
    		}
    		else if (id == -3) {
    			result = "Брак";
    		}
    		else if (id == -4) {
    			result = "Списать";
    		}
    		else if (id == -5) {
    			result = "Списать";
    		}
    		else if (id == -6) {
    			result = "Списать";
    		}
    		else if (id == -7) {
    			result = "Брак";
    		}
    		else if (id == -8) {
    			result = "Списать";
    		}
    		else if (id == -9) {
    			result = "Брак";
    		}
    		else if (id == -10) {
    			result = "Утилизирован";
    		}
    		else if (id == -11) {
    			result = "Брак";
    		}
    		else if (id == -99) {
    			// Забраковка из карантина, положительная рекация
    			result = "Брак";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodComponentOrder")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
    			result = "Зарегистрировать";
    		}
    		else if (id == 2) {
    			result = "Компонент выдан";
    		}
    		else if (id == 3) {
    			result = "Забронировать";
    		}
    		else if (id == 4) {
    			result = "Компонент выдан";
    		}
    		else if (id == 5) {
    			result = "Отказ от бронирования";
    		}
    		else if (id == -1){
        		result = "Компонента нет в наличии";
        	}
    		else if (id == -2) {
    			result = "Отказать";
    		}
    		else if (id == -3) {
    			result = "Пробы не пройдены";
    		}
    		else if (id == -4) {
    			result = "КК нет применения";
    		}
    		else if (id == -5) {
    			result = "Возврат";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BloodDonation")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
    			result = "Направить на донацию";
    		}
    		else if (id == 3) {
        		result = "Результаты анализов получены";
        	}
    		else if (id == 11) {
    			result = "Отправить на фракционирование";
    		}
    		else if (id == 12) {
    			result = "Отправить на получение анализов";
    		}
    		else if (id == 21) {
    			result = "Разделение КК выполнено";
    		}
    		else if (id == 9) {
    			result = "Запланировать";
    		}
    		else if (id == 10) {
    			result = "Направить на донацию";
    		}
    		else if (id == -1) {
    			result = "Отвести от донорства";
    		}
    		else if (id == -2) {
    			result = "Донация не состоялась";
    		}
    		else if (id == -3) {
    			result = "Отменить донацию";
    		}
        	else {
        		result = "";
        	}
    	}
    	else if (type.equals("BiomaterialDonor")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else {
        		result = "";
        	}
    	}
    	else if (type.equals("Operation")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else {
        		result = "";
        	}
    	}
    	else if (type.equals("Biomaterial")) {
    		if (id == 0) {
        		result = "Документ создан";
        	}
    		else if (id == 1) {
    			result = "Готов к обработке";
    		}
    		else if (id == 2) {
    			result = "Начать обработку";
    		}
    		else if (id == 3) {
    			result = "Выдать";
    		}
    		else if (id == 4) {
    			result = "Выдать";
    		}
    		else if (id == 5) {
    			result = "Заморозить";
    		}
    		else if (id == 6) {
    			result = "Выдать";
    		}
    		else if (id == 7) {
    			result = "Разморозить";
    		}
    		else if (id == 8) {
    			result = "Выдать";
    		}
    		else if (id == 100) {
    			result = "Разделить";
    		}
    		else if (id == -1) {
    			result = "Утилизировать";
    		}
    		else if (id == -2) {
    			result = "Утилизировать";
    		}
    		else if (id == -3) {
    			result = "Утилизировать";
    		}
    		else if (id == -4) {
    			result = "Утилизировать";
    		}
    		else {
        		result = "";
        	}
    	}
    	
    	return result;
    }

    
    public static final String ANALYSIS_CATEGORY_PRIMARY = "Первичный";
    public static final String ANALYSIS_CATEGORY_SECONDARY = "Вторичный";
    public static final String ANALYSIS_CATEGORY_COMMON = "Общий";
    
    /**
     * Действие "Вирусинактивация" для компонента крови
     */
    public static final String VIRUSINAKTIVATION = "Вирусинактивация";
    public static final Integer VIRUSINAKTIVATION_ID = 14;
    
    public static final String STORE_NAME = "E5 TRANSFUSION";
	public static final String NAMESPACE = "http://www.alfresco.org/model/content/1.0";
	public static final String NAMESPACE_PREFIX = "cm";
	public static final String TYPE_CONTENT = "content";
    
    
    public static final int MAX_FRAGMENT_LEN = 180;
    
    private static final Locale locale = new Locale("ru", "RU");
    
}