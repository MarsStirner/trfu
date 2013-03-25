package ru.efive.medicine.niidg.trfu.uifaces.ws.laboratory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * список изображений
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ImageValue")
@XmlType(name = "ImageValue", propOrder = {"imageString", "imageData"} )
public class ImageValue implements java.io.Serializable {
	
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
	@XmlElement(name = "imageString")
	private String imageString;
	
	/**
	 * картинка, закодированная в Base64
	 */
	@XmlElement(name = "imageData")
	private String imageData;
	
	
	private static final long serialVersionUID = 1L;
}