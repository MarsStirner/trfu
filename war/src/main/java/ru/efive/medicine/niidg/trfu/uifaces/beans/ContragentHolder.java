package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.crm.data.ContragentNomenclature;
import static ru.bars.open.sql.dao.util.ApplicationDAONames.*;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

@Named("contragent")
@ConversationScoped
public class ContragentHolder extends AbstractDocumentHolderBean<Contragent, Integer> implements Serializable {
	
	@Override
	protected boolean deleteDocument() {
		boolean result = false;
		try {
			result = sessionManagement.getDAO(ContragentDAOHibernate.class, CONTRAGENT_DAO).delete(getDocumentId());
			if (!result) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Невозможно удалить документ. Попробуйте повторить позже.", ""));
			}
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
		try {
			setDocument(sessionManagement.getDAO(ContragentDAOHibernate.class, CONTRAGENT_DAO).get(id));
			if (getDocument() == null) {
				setState(STATE_NOT_FOUND);
			}
		}
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Внутренняя ошибка.", ""));
			e.printStackTrace();
		}
	}
	
	@Override
	protected void initNewDocument() {
		Contragent contragent = new Contragent();
		contragent.setDeleted(false);
		
        setDocument(contragent);
	}
	
	@Override
	protected boolean saveDocument() {
		boolean result = false;
		try {
			Contragent contragent = sessionManagement.getDAO(ContragentDAOHibernate.class, CONTRAGENT_DAO).save(getDocument());
			if (contragent == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Документ не может быть сохранен. Попробуйте повторить позже.", ""));
			}
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
			Contragent contragent = sessionManagement.getDAO(ContragentDAOHibernate.class, CONTRAGENT_DAO).save(getDocument());
			if (contragent == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Документ не может быть сохранен. Попробуйте повторить позже.", ""));
			}
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
	
	
	public class NomenclatureSelectModal extends ModalWindowHolderBean {
		
		public ContragentNomenclature getValue() {
			return value;
		}

		public void setValue(ContragentNomenclature value) {
			this.value = value;
		}

		public boolean selected(ContragentNomenclature value) {
			return this.value != null ? this.value.getValue().equals(value.getValue()) : false;
		}

		public void select(ContragentNomenclature value) {
			this.value = value;
		}

		@Override
		protected void doSave() {
			super.doSave();
			getDocument().setNomenclature(getValue());
		}

		@Override
		protected void doShow() {
			super.doShow();
		}

		private ContragentNomenclature value;
		private static final long serialVersionUID = 3204083909477490577L;
	}
	public NomenclatureSelectModal getNomenclatureIndexSelectModal() {
		return	nomenclatureSelectModal;
	}
	
	
	@Override
    protected String doAfterCreate() {
		contragentList.markNeedRefresh();
        return super.doAfterCreate();
    }
    
    @Override
    protected String doAfterEdit() {
    	contragentList.markNeedRefresh();
        return super.doAfterEdit();
    }
    
    @Override
    protected String doAfterDelete() {
    	contragentList.markNeedRefresh();
        return super.doAfterDelete();
    }
    
    @Override
    protected String doAfterSave() {
    	contragentList.markNeedRefresh();
        return super.doAfterSave();
    }
    
    
    private NomenclatureSelectModal nomenclatureSelectModal = new NomenclatureSelectModal();
	
	@Inject @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement;
	@Inject @Named("contragentList")
    private transient ContragentListHolderBean contragentList;
	
	private static final long serialVersionUID = 4716264614655470705L;
}