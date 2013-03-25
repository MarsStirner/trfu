package ru.efive.wf.core.gateway;

import java.util.List;
import javax.ejb.Remote;

import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.IAction;
import ru.efive.wf.core.ProcessUser;
import ru.efive.wf.core.ProcessedData;

@Remote
public interface WorkFlowGateService {
	
	public boolean initialize(ProcessedData processedData, ProcessUser processUser);
	public List<? extends IAction> getAvailableActions();
	public List<? extends IAction> getAvailableActions(ProcessedData processedData, ProcessUser processUser);
	public ActionResult process(IAction action);
	
}