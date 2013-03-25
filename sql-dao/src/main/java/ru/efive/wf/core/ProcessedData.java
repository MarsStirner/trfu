package ru.efive.wf.core;

public interface ProcessedData {
	
	public int getId();
	public String getType();
	public int getStatusId();
	public void setStatusId(int statusId);
	
}