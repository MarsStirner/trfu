package ru.efive.medicine.niidg.trfu.data.dictionary;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Нерабочий день
 */
@Entity
@Table(name = "trfu_time_table_special_days")
public class TimeTableSpecialDay extends DictionaryEntity {
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
	}
	
	public boolean isRepeated() {
		return repeated;
	}
	
	
	/**
	 * Дата
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date date;
	
	/**
	 * Ежегодное событие
	 */
	private boolean repeated;
	
	private static final long serialVersionUID = -739050067706784903L;
}