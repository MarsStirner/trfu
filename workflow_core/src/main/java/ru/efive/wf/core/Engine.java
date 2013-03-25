package ru.efive.wf.core;

import java.util.ArrayList;
import java.util.List;

public final class Engine {
	
	public Engine() {
		
	}
	
	public void initialize(ProcessedData processedData, ProcessUser processUser) throws Exception {
		System.out.println("initialize engine");
		process = ProcessFactory.getProcessByType(processedData);
		process.setProcessUser(processUser);
		
		List <IAction> actions = new ArrayList<IAction>();
		
		List<StatusChangeAction> statusActions = process.getCurrentStatus().getAvailableActions();
		for (StatusChangeAction action:statusActions) {
			if (action.isAvailable()) actions.add(action);
		}
		List<NoStatusAction> noStatusActions = process.getNoStatusActions();
		for (NoStatusAction action:noStatusActions) {
			if (action.isAvailable()) actions.add(action);
		}
		currentActions = actions;
	}
	
	public List<? extends IAction> getActions() {
		return currentActions;
	}
	
	public ActionResult process(IAction selectedAction) {
		ActionResult result = new ActionResult();
		try {
			System.out.println("Processing action " + selectedAction.getId() + " - " + selectedAction.getName());
			result = selectedAction.run();
			System.out.println("current status in process is: " + process.getCurrentStatus().getId());
			System.out.println("after processing processedData status id is: " + process.getProcessedData().getStatusId());
			System.out.println("transaction result processedData status id is: " + result.getProcessedData().getStatusId());
			process.setProcessedData(result.getProcessedData());
		}
		catch (Exception e) {
			result = new ActionResult();
			result.setProcessed(false);
			result.setProcessedData(process.getProcessedData());
			e.printStackTrace();
		}
		return result;
	}
	
	
	public ProcessedData getProcessedData() {
		return process.getProcessedData();
	}
	
	private Process process;
	
	private List<IAction> currentActions;
}