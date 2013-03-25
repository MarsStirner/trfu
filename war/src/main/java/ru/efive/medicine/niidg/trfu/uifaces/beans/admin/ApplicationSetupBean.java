package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisEntry;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAppointment;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

@Named("applicationSetup")
@SessionScoped
public class ApplicationSetupBean implements Serializable {
	
	public String getSmallLabelPrinterName() {
		return "ZebraTLP2824Plus";
	}
	
	public String getSmallLabelReportTemplateName() {
		return "Barcode4JReport.jrxml";
	}
	
	public String getBigLabelReportTemplateName() {
		return "BigBarcode4JReport.jrxml";
	}
	
	public class ExternalAnalysisHistoryModal extends ModalWindowHolderBean {
		
		public ExternalAppointment getAppointment() {
			return appointment;
		}
		
		public void setAppointment(ExternalAppointment appointment) {
			this.appointment = appointment;
		}
		
		public List<ExternalAnalysisResult> getEntries() {
			return entries;
		}
		
		public void setEntries(List<ExternalAnalysisResult> entries) {
			this.entries = entries;
		}
		
		@Override
		protected void doShow() {
			super.doShow();
			entries = new ArrayList<ExternalAnalysisResult>();
			if (appointment != null) {
				for (ExternalAnalysisEntry entry: appointment.getHistoryEntries()) {
					if (entry.getResults() != null) {
						entries.addAll(entry.getResults());
					}
				}
			}
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			appointment = null;
			entries = null;
		}
		
		private ExternalAppointment appointment;
		private List<ExternalAnalysisResult> entries;
		
		private static final long serialVersionUID = 1L;
	}
	
	public class AnalysisValueListModal extends ModalWindowHolderBean {
		
		public Analysis getAnalysis() {
			return analysis;
		}
		
		public void setAnalysis(Analysis analysis) {
			this.analysis = analysis;
		}
		
		@Override
		protected void doShow() {
			super.doShow();
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			setAnalysis(null);
		}
		
		@Override
		protected void doSave() {
			super.doSave();
		}
		
		private Analysis analysis;
		
		private static final long serialVersionUID = 1L;
	}
	
	public ExternalAnalysisHistoryModal getExternalAnalysisHistoryModal() {
		return externalAnalysisHistoryModal;
	}
	
	public void initializeExternalAnalysisHistoryModal(ExternalAppointment appointment) {
		externalAnalysisHistoryModal.setAppointment(appointment);
		externalAnalysisHistoryModal.setModalVisible(true);
	}
	
	public AnalysisValueListModal getAnalysisValueListModal() {
		return analysisValueListModal;
	}
	
	public void initializeAnalysisValueListModal(Analysis analysis) {
		analysisValueListModal.setAnalysis(analysis);
		analysisValueListModal.setModalVisible(true);
	}

    public ModalWindowHolderBean getAdditionalResultsModal() {
        return additionalResultsModal;
    }
    
    
    private ExternalAnalysisHistoryModal externalAnalysisHistoryModal = new ExternalAnalysisHistoryModal();
	private AnalysisValueListModal analysisValueListModal = new AnalysisValueListModal();
    private ModalWindowHolderBean additionalResultsModal = new ModalWindowHolderBean();

	private static final long serialVersionUID = 1L;
}