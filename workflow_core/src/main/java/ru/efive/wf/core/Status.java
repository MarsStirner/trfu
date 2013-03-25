package ru.efive.wf.core;

import java.util.ArrayList;
import java.util.List;

public final class Status<T extends ProcessedData> {
	
	protected Status() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocalActivities(List<IActivity> localActivities) {
		this.localActivities = localActivities;
	}
	
	public List<IActivity> getLocalActivities() {
		return localActivities;
	}

	public void setPreStatusActivities(List<IActivity> preStatusActivities) {
		this.preStatusActivities = preStatusActivities;
	}
	
	public List<IActivity> getPreStatusActivities() {
		return preStatusActivities;
	}
	
	
	public void setPostStatusActivities(List<IActivity> postStatusActivities) {
		this.postStatusActivities = postStatusActivities;
	}
	
	public List<IActivity> getPostStatusActivities() {
		return postStatusActivities;
	}
	
	
	public void setAvailableActions(List<StatusChangeAction> actions) {
		this.actions = actions;
	}
	
	public List<StatusChangeAction> getAvailableActions() {
		return actions;
	}
	
	public T getProcessedData() {
		return processedData;
	}
	
	public void setProcessedData(T processedData) {
		this.processedData = processedData;
	}
	
	
	
	private String name;
	private int id;
	
	private List<IActivity> localActivities = new ArrayList<IActivity>();
	private List<IActivity> preStatusActivities = new ArrayList<IActivity>();
	private List<IActivity> postStatusActivities = new ArrayList<IActivity>();
	
	private List<StatusChangeAction> actions = new ArrayList<StatusChangeAction>();
	
	private T processedData;
}