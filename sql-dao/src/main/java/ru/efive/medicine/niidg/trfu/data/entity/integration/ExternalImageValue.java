package ru.efive.medicine.niidg.trfu.data.entity.integration;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

/**
 * список изображений
 */
@Entity
@Table(name = "trfu_external_image_values")
public class ExternalImageValue extends IdentifiedEntity {
	
	public String getImageString() {
		return imageString;
	}
	
	public void setImageString(String imageString) {
		this.imageString = imageString;
	}
	
	public String getImageData() {
		return imageData;
	}
	
	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
	
	
	/**
	 * строка описания
	 */
	private String imageString;
	
	/**
	 * картинка, закодированная в Base64
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576)
	private String imageData;
	
	
	private static final long serialVersionUID = 1L;
}