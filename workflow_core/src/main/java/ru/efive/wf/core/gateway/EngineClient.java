package ru.efive.wf.core.gateway;

import java.util.ArrayList;
import java.util.List;

import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.Engine;
import ru.efive.wf.core.IAction;
import ru.efive.wf.core.ProcessUser;
import ru.efive.wf.core.ProcessedData;

public class EngineClient {
	
	public EngineClient(ProcessedData processedData, ProcessUser processUser) throws Exception {
		try {
			processor = new Engine();
			System.out.println("engine initialization, processeddata: " + processedData.getType() + ", processuser: " + processUser);
			processor.initialize(processedData, processUser);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<EngineAction> getActions() throws Exception {
		List<EngineAction> result = new ArrayList<EngineAction>();
		try {
			if (processor != null) {
				System.out.println("get available actions");
				List<? extends IAction> actions = processor.getActions();
				for (IAction action : actions) {
					EngineAction engineAction = new EngineAction(action);
					result.add(engineAction);
				}
			}
		}
		catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	
	public ActionResult process(EngineAction action) throws Exception {
		ActionResult result = new ActionResult();
		try {
			if (processor != null && processor.getProcessedData() != null) {
				System.out.println("process action: id = " + action.getId() + ", name = " + action.getName());
				result = processor.process(action.getAction());
				System.out.println("result is " + result);
				System.out.println("new status id is " + processor.getProcessedData().getStatusId());
			}
		}
		catch (Exception e) {
			result.setProcessed(false);
			e.printStackTrace();
		}
		return result;
	}
	
	
	private Engine processor;
}