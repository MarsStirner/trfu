package ru.efive.medicine.niidg.trfu.uifaces.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ru.efive.dao.sql.entity.enums.ConverterName;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.OperationalCrew;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalIndicator;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.UserListHolderBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;
import ru.efive.uifaces.bean.ModalWindowHolderBean;

@Named("bloodDonationOperationalList")
@SessionScoped
public class BloodDonationOperationalListHolderBean extends AbstractDocumentListHolderBean<BloodDonationRequest> {
    private String filter;
    private BloodSystemSelect bloodSystemTypeSelectModal = new BloodSystemSelect();
    private OperationalEditorModal operationalEditor = new OperationalEditorModal();
    @Inject @Named("sessionManagement")
    SessionManagementBean sessionManagement;
    @Inject @Named("operationalSession")
    private transient OperationalSessionBean operational;
    @Inject @Named("userList")
    private transient UserListHolderBean userList;

    private static final long serialVersionUID = 6546450615334686914L;

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}
	
	@Override
	protected Sorting initSorting() {
		return new Sorting("number", false);
    }
	
	@Override
	protected int getTotalCount() {
		int result = 0;
		try {
			return new Long(sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).countDocumentByStatus(2, filter, false)).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected List<BloodDonationRequest> loadDocuments() {
		List<BloodDonationRequest> result = new ArrayList<BloodDonationRequest>();
		try {
			result = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO).findDocumentsByStatus(2, filter, false,
				getPagination().getOffset(), getPagination().getPageSize(), getSorting().getColumnId(), getSorting().isAsc());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BloodDonationRequest> getDocuments() {
		return super.getDocuments();
	}
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public UserListSelectModalBean getOperationalCrewSelectModal() {
    	return operationalCrewSelectModal;
    }


	private UserListSelectModalBean operationalCrewSelectModal = new UserListSelectModalBean() {
		public UserListHolderBean getUserList() {
			return userList;
		}
		@Override
		protected void doSave() {
			super.doSave();
			OperationalCrew crew = operational.getCurrentCrew();
			Set<User> staff = new HashSet<User>();
			staff.addAll(getUsers());
			crew.setStaff(staff);
		}
		@Override
		protected void doHide() {
			super.doHide();
			userList.setFilter("");
			userList.markNeedRefresh();
			setUsers(new ArrayList<User>());
		}
	};


    public BloodSystemSelect getBloodSystemTypeSelectModal() {
        return bloodSystemTypeSelectModal;
    }

    public class BloodSystemSelect extends ModalWindowHolderBean {
        public List<BloodSystemType> getAllTypes(){
            return sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodSystemTypes(false);
        }

        private Set<BloodSystemType> selected = new HashSet<BloodSystemType>();

        public void select(BloodSystemType selected) {
            this.selected.add(selected);
        }

        public void unSelect(BloodSystemType selected) {
            this.selected.remove(selected);
        }

        public boolean selected(BloodSystemType row) {
            return this.selected.contains(row);
        }

        @Override
        protected void doSave() {
            operational.getCurrentOperationalSetup().setSystemTypes(new ArrayList<BloodSystemType>(selected));
            super.doSave();
        }

        @Override
        protected void doHide() {
            selected.clear();
            super.doHide();
        }
    }

    public OperationalEditorModal getOperationalEditor() {
        return operationalEditor;
    }

    public class OperationalEditorModal extends ModalWindowHolderBean{
        private String message;

        public OperationalEditorModal() {
            setModalVisible(true);
        }

        @Override
        protected void doHide() {
            super.doHide();
        }

        public String getMessage() {
            return message;
        }
    }
}