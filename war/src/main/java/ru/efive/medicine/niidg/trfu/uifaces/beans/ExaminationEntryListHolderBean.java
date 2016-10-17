package ru.efive.medicine.niidg.trfu.uifaces.beans;

import ru.efive.medicine.niidg.trfu.dao.ExaminationEntryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationEntry;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static ru.bars.open.sql.dao.util.ApplicationDAONames.EXAMINATION_ENTRY_DAO;

@Named("examinationEntryList")
@SessionScoped
public class ExaminationEntryListHolderBean extends AbstractDocumentListHolderBean<ExaminationEntry> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 10);
	}

	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			result = new Long(sessionManagement.getDAO(ExaminationEntryDAOImpl.class, EXAMINATION_ENTRY_DAO).countDocument(false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<ExaminationEntry> loadDocuments() {
		List<ExaminationEntry> result = new ArrayList<>();
		try {
			result = sessionManagement.getDAO(ExaminationEntryDAOImpl.class, EXAMINATION_ENTRY_DAO).findDocuments(false,
					getPagination().getOffset(), getPagination().getPageSize(), "id", true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ExaminationEntry> getDocuments() {
		return super.getDocuments();
	}
	
	public List<ExaminationEntry> getEntriesByExaminationRequest(int examinationRequestId) {
		List<ExaminationEntry> result = new ArrayList<>();
		try {
			ExaminationEntryDAOImpl dao = sessionManagement.getDAO(ExaminationEntryDAOImpl.class, EXAMINATION_ENTRY_DAO);
			List<ExaminationEntry> baseList = dao.findBaseEntries(examinationRequestId);
			for (ExaminationEntry entry: baseList) {
				entry.setGrouping(0);
				result.add(entry);
				List<ExaminationEntry> childList = dao.findChildEntries(examinationRequestId, entry.getId());
				for (ExaminationEntry child: childList) {
					if (!entry.isParent()) {
						entry.setParent(true);
					}
					child.setGrouping(1);
					result.add(child);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Inject @Named("sessionManagement")
	SessionManagementBean sessionManagement = new SessionManagementBean();
	
	private static final long serialVersionUID = -2187772824856576385L;
}