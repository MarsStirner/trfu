package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.DictionaryEntity;

/**
 * Элемент справочника - виды отвода
 * 
 * @author Alexey Vagizov
 */
@Entity
@Table(name = "trfu_donor_rejection_types")
public class DonorRejectionType extends DictionaryEntity {
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	public String getDisplayedType() {
		StringBuffer buffer = new StringBuffer();
		switch (type) {
		case 0:
			boolean addSpace = false;
			if (years != 0) {
				buffer.append(years).append(" ").append(years%10==1 && years%100!=11 ? "год" : 
					years%10>=2 && years%10<=4 && (years%100<10 || years%100>=20) ? "года" : "лет");
				addSpace = true;
			}
			if (months != 0) {
				if (addSpace) {
					buffer.append(" ");
				}
				buffer.append(months).append(" ").append("мес.");
				addSpace = true;
			}
			if (days != 0) {
				if (addSpace) {
					buffer.append(" ");
				}
				buffer.append(days).append(" ").append("дн.");
			}
			break;
		case 1:
			buffer.append("Временный до выясн.");
			break;
		case 2:
			buffer.append("Временный до закл.");
			break;
		case 3:
			buffer.append("Абсолютный до закл.");
			if (commentary != null && !commentary.equals("")) buffer.append(" <br />").append(commentary);
			break;
		case 4:
			buffer.append("Абсолютный");
			break;
		}
		return buffer.toString();
	}

	
	/**
	 * Категория отвода
	 */
	private String category;
	
	/**
	 * Тип отвода:
	 * 0 - временный, 1 - временный до выяснения, 2 - временный до заключения
	 * 3 - абсолютный до заключения, 4 - абсолютный
	 */
	private byte type;
	
	/**
	 * Код ЕДЦ
	 */
	private String code;
	
	/**
	 * комментарий для отводов 1, 2 и 3
	 */
	private String commentary;
	
	/**
	 * срок отвода (для временного)
	 */
	private int years; 
	private int months;
	private int days;
	
	private static final long serialVersionUID = 6700644845173345530L;
}