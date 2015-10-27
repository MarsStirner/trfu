package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import org.apache.commons.lang.StringUtils;
import ru.efive.dao.sql.entity.DictionaryEntity;
import ru.efive.dao.sql.entity.enums.ConverterName;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.*;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.DictionaryManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("settings")
@SessionScoped
public class SettingsListHolderBean extends AbstractDocumentListHolderBean<DictionaryEntity> {
	
	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	public Sorting initSorting() {
		return new Sorting("id", false);
	}
	
	/**
	 Удаление обьектов
	 */
	public void newDelete() {
		document.setDeleted(true);
		sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).save(document);
		refresh();
		setDocument(getDocuments().get(0));
	}
	
	/*Создание нового документа*/
	protected void newDocument() {
		if (filterAnalysisType) {
			document = new AnalysisType();
		}
		else if (filterBloodGroup) {
			document = new BloodGroup();
		}
		else if (filterBloodComponentType) {
			document = new BloodComponentType();
			((BloodComponentType) document).setUsed(true);
		}
		else if (filterBloodDonationType) {
			document = new BloodDonationType();
		}
		else if (filterClassifier) {
			document = new Classifier();
		}
		else if (filterDonorRejectionType) {
			document = new DonorRejectionType();
		}
		else if (filterQualityControl) {
			document = new QualityControlMappingEntry();
		}
		else {
			document = new ExaminationEntryType();
		}
		document.setValue("");
	}
	
	/* Сохрание элементов */
	public boolean saveNewDocument() {
		boolean result = false;
		try {
			DictionaryEntity document = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).save(getDocument());
			if (document == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно сохранить документ. Попробуйте повторить позже.", ""));
			}
			else {
				setDocument(document);
				result = true;
				refresh();
			}
		}
		catch (Exception e) {
			result = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ошибка при сохранении документа.", ""));
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			long count;
			if (filterAnalysisType) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countAnalysisTypes(false);
			}
			else if (filterBloodGroup) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countBloodGroups(false);
			}
			else if (filterBloodComponentType) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countBloodComponentTypes(false);
			}
			else if (filterBloodDonationType) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countBloodDonationTypes(false);
			}
			else if (filterClassifier) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countClassifier(filter, false);
			}
			else if (filterDonorRejectionType) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countDonorRejectionTypes(false);
			}
			else if (filterQualityControl) {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countQualityControlMappingEntries(false);
			}
			else {
				count = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).countExaminationEntryTypes(false);
			}
			result = new Long(count).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected List<DictionaryEntity> loadDocuments() {
		List<DictionaryEntity> result = new ArrayList<DictionaryEntity>();
		try {
			if (filterAnalysisType) {
				List<AnalysisType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes(
						false, "id", false);
				for (AnalysisType type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new AnalysisType();
				}
			}
			else if (filterBloodGroup) { 
				List<BloodGroup> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodGroups(
						false, "id", false);
				for (BloodGroup type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new BloodGroup();
				}
			}
			else if (filterBloodComponentType) { 
				List<BloodComponentType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodComponentTypes(
						false, "id", false);
				for (BloodComponentType type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new BloodComponentType();
				}
			}
			else if (filterBloodDonationType) { 
				List<BloodDonationType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodDonationTypes(
						false, "id", false);
				for (BloodDonationType type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new BloodDonationType();
				}
			}
			else if (filterClassifier) { 
				List<Classifier> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findClassifier(filter,
						false, "id", false);
				for (Classifier type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new Classifier();
				}
			}
			else if (filterDonorRejectionType) { 
				List<DonorRejectionType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findDonorRejectionTypes(
						false, "id", false);
				for (DonorRejectionType type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new DonorRejectionType();
				}
			}
			else if (filterQualityControl) {
				List<QualityControlMappingEntry> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findQualityControlMappingEntries(
						false, "id", false);
				for (QualityControlMappingEntry type: list) {
					result.add(type);
				}
				if (result.size() == 0) {
					document = new QualityControlMappingEntry();
				}
			}
			else {
				List<ExaminationEntryType> list = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findExaminationEntryTypes(
						false, getSorting().getColumnId(), getSorting().isAsc());
				for (ExaminationEntryType type: list) {
					result.add(type);
				}
			}
			if (document == null) {
				document = result.get(0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<DictionaryEntity> getDocuments() {
		return super.getDocuments();
	}
	
	public boolean isFilterAnalysisType() {
		return filterAnalysisType;
	}

	public boolean isFilterBloodGroup() {
		return filterBloodGroup;
	}

	public boolean isFilterBloodDonationType() {
		return filterBloodDonationType;
	}
	
	public boolean isFilterBloodComponentType() {
		return filterBloodComponentType;
	}
	
	public boolean isFilterClassifier() {
		return filterClassifier;
	}
	
	public boolean isFilterDonorRejectionType() {
		return filterDonorRejectionType;
	}
	
	public boolean isFilterExaminationEntryType() {
		return filterExaminationEntryType;
	}
	
	public boolean isFilterQualityControl() {
		return filterQualityControl;
	}
	
	public void doFilterAnalysisType() {
		filterAnalysisType = true;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}
	
	public void doFilterBloodGroup() {
		filterAnalysisType = false;
		filterBloodGroup = true;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}
	
	public void doFilterExaminationEntryType() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = true;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}

	public void doFilterBloodComponentType() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = true;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}

	public void doFilterBloodDonationType() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = true;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}

	public void doFilterClassifier() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = true;
		filterDonorRejectionType = false;
		filterQualityControl = false;
		document = null;
		refresh();
	}

	public void doFilterDonorRejectionType() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = true;
		filterQualityControl = false;
		document = null;
		refresh();
	}
	
	public void doFilterQualityControl() {
		filterAnalysisType = false;
		filterBloodGroup = false;
		filterExaminationEntryType = false;
		filterBloodComponentType = false;
		filterBloodDonationType = false;
		filterClassifier = false;
		filterDonorRejectionType = false;
		filterQualityControl = true;
		document = null;
		refresh();
	}
	
	public String getFilterName() {
		String result = "";
		if (filterAnalysisType) {
			result = "виды анализов";
		}
		else if (filterBloodGroup) {
			result = "группы крови";
		}
		else if (filterBloodComponentType) {
			result = "группы крови";
		}
		else if (filterBloodDonationType) {
			result = "виды донации";
		}
		else if (filterClassifier) {
			result = "классификаторы";
		}
		else if (filterDonorRejectionType) {
			result = "виды отказов";
		}
		else if (filterQualityControl) {
			result = "показатели контроля качества";
		}
		else {
			result = "типы записей осмотра";
		}
		return result;
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public DictionaryEntity getDocument() {
		return document;
	}
	
	public void setDocument(DictionaryEntity document) {
		this.document = document;
	}
	
	public void select(DictionaryEntity document){
		this.document = document;
	}
	
	public boolean selected(DictionaryEntity document){
		return this.document == null ? false: this.document.equals(document);
	}
	
	public QualityControlBloodComponentTypeSelectModalBean getComponentTypeSelect() {
		return componentTypeSelect;
	}
	
	public QualityControlAnalysisTypeSelectModalBean getAnalysisTypeSelect() {
		return analysisTypeSelect;
	}
	
	
	public class QualityControlBloodComponentTypeSelectModalBean extends ModalWindowHolderBean {
		
		public List<BloodComponentType> getBloodComponentTypeList() {
			return componentTypeList;
		}
		
		public List<BloodComponentType> getAvailableBloodComponentTypeList() {
			return availableComponentTypeList;
		}
		
		public void setBloodComponentTypeList(List<BloodComponentType> componentTypeList) {
			if (componentTypeList == null) {
				this.componentTypeList = new ArrayList<BloodComponentType>();
			}
			else{
				this.componentTypeList = componentTypeList;
			}
		}
		
		public void setFilter(String filter) {
			this.filter = filter;
		}

		public String getFilter() {
			return filter;
		}

		public void select(BloodComponentType componentType) {
			componentTypeList.add(componentType);		
		}
		
		public void unselect(BloodComponentType componentType) {
			componentTypeList.remove(componentType);
		}
		
		public boolean selected(BloodComponentType componentType) {
			return componentTypeList.contains(componentType);
		}
		
		public void search() {
			availableComponentTypeList = dictionaryManagement.getBloodComponentTypes(filter);
		}
		
		@Override
		protected void doSave() {
			super.doSave();
			if (getDocument() instanceof QualityControlMappingEntry) {
				((QualityControlMappingEntry) getDocument()).setComponentTypes(getBloodComponentTypeList());
			}
		}
		
		@Override
		protected void doShow() {
			super.doShow();
			componentTypeList = new ArrayList<BloodComponentType>();
			availableComponentTypeList = dictionaryManagement.getBloodComponentTypes();
			if (getDocument() instanceof QualityControlMappingEntry) {
				setBloodComponentTypeList(((QualityControlMappingEntry) getDocument()).getComponentTypes());
			}
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			componentTypeList = null;
			availableComponentTypeList = null;
			filter = null;
		}
		
		private List<BloodComponentType> componentTypeList;
		private List<BloodComponentType> availableComponentTypeList;
		
		private String filter;
		
		private static final long serialVersionUID = -2965379270415093952L;
	}
	
	public class QualityControlAnalysisTypeSelectModalBean extends ModalWindowHolderBean {
		
		public List<AnalysisType> getAvailableAnalysisTypeList() {
			return availableAnalysisTypeList;
		}
		
		public List<AnalysisType> getAnalysisTypeList() {
			return analysisTypeList;
		}
		
		public void setAnalysisTypeList(List<AnalysisType> analysisTypeList) {
			if (analysisTypeList == null) {
				this.analysisTypeList = new ArrayList<AnalysisType>();
			}
			else{
				this.analysisTypeList = analysisTypeList;
			}
		}
		
		public void setFilter(String filter) {
			this.filter = filter;
		}

		public String getFilter() {
			return filter;
		}
		
		public void select(AnalysisType analysisType) {
			analysisTypeList.add(analysisType);		
		}
		
		public void unselect(AnalysisType analysisType) {
			analysisTypeList.remove(analysisType);
		}
		
		public boolean selected(AnalysisType analysisType) {
			return analysisTypeList.contains(analysisType);
		}
		
		public void search() {
			availableAnalysisTypeList = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes(filter, "Контроль качества", false);
		}
		
		@Override
		protected void doSave() {
			super.doSave();
			if (getDocument() instanceof QualityControlMappingEntry) {
				((QualityControlMappingEntry) getDocument()).setAnalysisTypes(getAnalysisTypeList());
			}
		}
		
		@Override
		protected void doShow() {
			super.doShow();
			analysisTypeList = new ArrayList<AnalysisType>();
			availableAnalysisTypeList = sessionManagement.getDictionaryDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("Контроль качества", false);
			if (getDocument() instanceof QualityControlMappingEntry) {
				setAnalysisTypeList(((QualityControlMappingEntry) getDocument()).getAnalysisTypes());
			}
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			analysisTypeList = null;
			availableAnalysisTypeList = null;
		}
		
		private List<AnalysisType> analysisTypeList;
		private List<AnalysisType> availableAnalysisTypeList;
		
		private String filter;
		
		private static final long serialVersionUID = -4006910775892712093L;
	}
	
	
	public class ClassifierCategorySelect extends ModalWindowHolderBean {
		
		public List<String> getCategories() {
			return categories;
		}
		
		public void select(String category) {
			this.category = category;
		}
		
		public boolean selected(String category) {
			return this.category == null? false: this.category.equals(category);
		}
		
		@Override
		protected void doSave() {
			super.doSave();
			if (StringUtils.isNotEmpty(category)) {
				((Classifier) getDocument()).setCategory(category);
			}
		}
		
		@Override
		protected void doShow() {
			super.doShow();
			categories = dictionaryManagement.getClassifierCategories();
		}
		
		@Override
		protected void doHide() {
			super.doHide();
			categories = null;
		}
		
		private String category;
		private List<String> categories;
		
		private static final long serialVersionUID = 1L;
	}
	
	public ClassifierCategorySelect getClassifierCategorySelect() {
		return classifierCategorySelect;
	}

    public class ConverterNameSelect extends ModalWindowHolderBean{
        public List<String> getAllNames(){
            List<String> names = new ArrayList<String>();
            for (ConverterName cn:ConverterName.values())
                names.add(cn.alias);
            return names;
        }

        private String selected;

        private ExternalIndicator externalIndicator;

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public ExternalIndicator getExternalIndicator() {
            return externalIndicator;
        }

        public void setExternalIndicator(ExternalIndicator externalIndicator) {
            this.externalIndicator = externalIndicator;
        }

        public boolean selected(String row) {
            return this.selected != null && this.selected.equals(row);
        }

        @Override
        protected void doSave() {
            if(selected!=null)
                externalIndicator.setConverterName(ConverterName.getConverterName(selected));
            super.doSave();
        }

        @Override
        protected void doHide() {
            selected = null;
            super.doHide();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    public ConverterNameSelect getConverterNameSelect() {
        return converterNameSelect;
    }

    public void initConverterNameSelect(ExternalIndicator externalIndicator){
        converterNameSelect.setExternalIndicator(externalIndicator);
        converterNameSelect.setModalVisible(true);
    }
	
	private String filter;
	
	private DictionaryEntity document;
	
	private boolean filterAnalysisType = true;
    private boolean filterBloodGroup = false;
    private boolean filterExaminationEntryType = false;
    private boolean filterBloodComponentType = false;
    private boolean filterBloodDonationType = false;
    private boolean filterClassifier = false;
    private boolean filterDonorRejectionType = false;
    private boolean filterQualityControl = false;
    
    private QualityControlBloodComponentTypeSelectModalBean componentTypeSelect = new QualityControlBloodComponentTypeSelectModalBean();
    private QualityControlAnalysisTypeSelectModalBean analysisTypeSelect = new QualityControlAnalysisTypeSelectModalBean();
    private ClassifierCategorySelect classifierCategorySelect = new ClassifierCategorySelect();
    private ConverterNameSelect converterNameSelect = new ConverterNameSelect();

	@Inject @Named("sessionManagement")
	private transient SessionManagementBean sessionManagement;
	@Inject @Named("dictionaryManagement")
	private transient DictionaryManagementBean dictionaryManagement;
	
	
	private static final long serialVersionUID = 1L;
}