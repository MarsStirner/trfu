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
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponentMatchCriteria;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.data.entity.DonorMatch;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;

@Named("donorMatch")
@ConversationScoped
public class DonorMatchBean extends AbstractDocumentHolderBean<DonorMatch, Integer> {
	
	@Override
	protected boolean deleteDocument() {
		return true;
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
	}
	
	@Override
	protected void initNewDocument() {
		DonorMatch match = new DonorMatch();
		match.setAuthor(sessionManagement.getLoggedUser());
		match.setCreated(Calendar.getInstance(ApplicationHelper.getLocale()).getTime());
		match.setDeleted(false);
		
		try {
			List<AnalysisType> types = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Иммуносерология", false);
			List<BloodComponentMatchCriteria> criteriaList = new ArrayList<BloodComponentMatchCriteria>();
			
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
		return true;
	}
	
	@Override
	protected boolean saveNewDocument() {
		return true;
	}
	
	public void setMatchResultList(List<Donor> matchResultList) {
		this.matchResultList = matchResultList;
	}
	
	public List<Donor> getMatchResultList() {
		return matchResultList;
	}
	
	public void select(Donor donor) {
		if (getDocument().getDonors() == null) {
			getDocument().setDonors(new HashSet<Donor>());
		}
		getDocument().getDonors().add(donor);
	}
	
	public void unselect(Donor donor) {
		getDocument().getDonors().remove(donor);
	}

	public boolean selected(Donor donor) {
		return !(getDocument().getDonors() == null || getDocument().getDonors().size() == 0) && getDocument()
				.getDonors().contains(donor);
	}
	
	public void search() {
		try {
			matchResultList = donorList.getDocumentsByPhenotypes(getDocument().getPhenotypeList(), getDocument().isSearchBloodGroup(), 
					getDocument().getBloodGroup().getValue(), getDocument().isSearchRhesus(), getDocument().getRhesusFactor().getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при поиске", ""));
		}
	}
	
	
	private List<Donor> matchResultList;
	
	@Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement;
	@Inject @Named("donorList")
    private transient DonorListHolderBean donorList;
	
	
	private static final long serialVersionUID = 3978250113633115607L;
}