package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

//import ru.efive.medicine.niidg.trfu.dao.BloodComponentMatchDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentMatch;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentMatchCriteria;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

@Named("bloodComponentMatch")
@ConversationScoped
public class BloodComponentMatchBean extends AbstractDocumentHolderBean<BloodComponentMatch, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			/*result = sessionManagement.getDAO(BloodComponentMatchDAOImpl.class, ApplicationHelper.MATCH_DAO).delete(getDocumentId());
			if (!result) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно удалить документ. Попробуйте повторить позже.", ""));
			}*/
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Внутренняя ошибка.", ""));
		}
		return result;
	}
	
	@Override
	protected Integer getDocumentId() {
		return getDocument() == null? null: getDocument().getId();
	}
	
	@Override
	protected FromStringConverter<Integer> getIdConverter() {
		return FromStringConverter.INTEGER_CONVERTER;
	}
	
	@Override
	protected void initDocument(Integer id) {
		/*try {
			setDocument(sessionManagement.getDAO(BloodComponentMatchDAOImpl.class, ApplicationHelper.MATCH_DAO).get(id));
			if (getDocument() == null) {
				setState(STATE_NOT_FOUND);
			}
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Внутренняя ошибка.", ""));
			e.printStackTrace();
		}*/
	}
	
	@Override
	protected void initNewDocument() {
		BloodComponentMatch match = new BloodComponentMatch();
		match.setAuthor(sessionManagement.getLoggedUser());
		match.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
		match.setDeleted(false);
		
		try {
			List<AnalysisType> types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Иммуносерология", false);
			List<BloodComponentMatchCriteria> criteriaList = new ArrayList<BloodComponentMatchCriteria>(types.size());
			
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis.setValue("-");
				BloodComponentMatchCriteria criteria = new BloodComponentMatchCriteria();
				criteria.setPhenotype(analysis);
				criteria.setNecessary(true);
				criteriaList.add(criteria);
			}
			
			match.setSearchBloodGroup(true);
			match.setSearchRhesus(true);
			
			match.setCriteriaList(criteriaList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
        setDocument(match);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			/*BloodComponentMatch match = sessionManagement.getDAO(BloodComponentMatchDAOImpl.class, ApplicationHelper.MATCH_DAO).save(getDocument());
			if (match == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Документ не может быть сохранен. Попробуйте повторить позже.", ""));
			}*/
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Внутренняя ошибка.", ""));
		}
		return result;
	}
	
	@Override
	protected boolean saveNewDocument() {
		boolean result = false;
		try {
			/*BloodComponentMatch match = sessionManagement.getDAO(BloodComponentMatchDAOImpl.class, ApplicationHelper.MATCH_DAO).save(getDocument());
			if (match == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Документ не может быть сохранен. Попробуйте повторить позже.", ""));
			}*/
			result = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка.", ""));
		}
		return result;
	}
	
	public void setMatchResultList(List<BloodComponent> matchResultList) {
		this.matchResultList = matchResultList;
	}
	
	public List<BloodComponent> getMatchResultList() {
		return matchResultList;
	}
	
	public void select(BloodComponent bloodComponent) {
		if (getDocument().getComponents() == null) {
			getDocument().setComponents(new HashSet<BloodComponent>());
		}
		getDocument().getComponents().add(bloodComponent);
	}
	
	public void unselect(BloodComponent bloodComponent) {
		getDocument().getComponents().remove(bloodComponent);
	}

	public boolean selected(BloodComponent bloodComponent) {
		return getDocument().getComponents() == null || getDocument().getComponents().size() == 0? false: getDocument().getComponents().contains(bloodComponent);
	}
	
	public void search() {
		try {
			matchResultList = bloodComponentList.getDocumentsByPhenotypes(getDocument().getPhenotypeList(), getDocument().isSearchBloodGroup(), 
					getDocument().getBloodGroup().getValue(), getDocument().isSearchRhesus(), getDocument().getRhesusFactor().getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при поиске", ""));
		}
	}
	
	
	private List<BloodComponent> matchResultList;
	
	@Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement;
	@Inject @Named("bloodComponentList")
    private transient BloodComponentListHolderBean bloodComponentList;
	
	
	private static final long serialVersionUID = 3978250113633115607L;
}