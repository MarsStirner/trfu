package ru.efive.wf.core.gateway;

import ru.efive.wf.core.IAction;

public final class EngineAction {
	
	public EngineAction(IAction action) {
		this.action = action;
	}
	
	public int getId() {
		return action.getId();
	}
	
	public String getName() {
		return action == null ? null : action.getName();
	}
	
	public IAction getAction() {
		return action;
	}
	
	
	private IAction action;
}