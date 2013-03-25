package ru.efive.medicine.niidg.trfu.data.entity;

import java.io.Serializable;

public class DonorFullTextEntry implements Serializable {
	
	public DonorFullTextEntry() {
		
	}
	
	public DonorFullTextEntry(int id, String content) {
		this.id = id;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	private int id;
	private String content;
	
	private static final long serialVersionUID = 1902088933112151046L;
}