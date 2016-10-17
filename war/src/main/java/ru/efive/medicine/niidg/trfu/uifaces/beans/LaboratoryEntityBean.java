package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.AnalysisDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.ExaminationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.wf.core.ActionResult;

@Named("laboratoryEntity")
@ConversationScoped
public class LaboratoryEntityBean extends AbstractDocumentHolderBean<ExaminationRequest, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		return false;
	}
	
	@Override
	protected Integer getDocumentId() {
		return getDocument().getId();
	}
	
	@Override
	protected FromStringConverter<Integer> getIdConverter() {
		return FromStringConverter.INTEGER_CONVERTER;
	}
	
	@Override
	protected void initDocument(Integer id) {
		setDocument(sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).get(id));
		if (getDocument() == null) {
			setState(STATE_NOT_FOUND);
		}
	}
	
	@Override
	protected void initNewDocument() {
		
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			ExaminationRequest examination = sessionManagement.getDAO(ExaminationRequestDAOImpl.class, EXAMINATION_DAO).save(getDocument());
			if (examination == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				AnalysisDAOImpl dao = sessionManagement.getDAO(AnalysisDAOImpl.class, ANALYSIS_DAO);
				for (int i = 0; i < getDocument().getTestList().size();i++) {
					dao.save(getDocument().getTestList().get(i));
				}
				result = true;
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected boolean saveNewDocument() {
		return false;
	}
	
	public boolean isAnamnesisEditable() {
		boolean result = false;
		int statusId = getDocument().getStatusId();
		if (statusId == 2 || statusId == 4 || statusId == 5) {
			result = true;
		}
		return result;
	}
	
    public ProcessorModalBean getProcessorModal() {
		return processorModal;
	}
    
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private ProcessorModalBean processorModal = new ProcessorModalBean() {
		@Override
		protected void doInit() {
			setProcessedData(getDocument());
			if (getDocumentId() == null || getDocumentId() == 0) saveNewDocument();
		}
		@Override
		protected void doPostProcess(ActionResult actionResult) {
			ExaminationRequest request = (ExaminationRequest) actionResult.getProcessedData();
			if (getSelectedAction().isHistoryAction()) {
				request.addToHistory(getHistoryEntry());
			}
			setDocument(request);
			LaboratoryEntityBean.this.save();
		}
	};
	
	
	private static final long serialVersionUID = 8070559401705301872L;
}