package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.ExaminationsFilter;
import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class ExaminationRequestDAOImpl extends GenericDAOHibernate<ExaminationRequest> {
	private static SRPDDao srpdDao;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}

    @Override
    protected Class<ExaminationRequest> getPersistentClass() {
        return ExaminationRequest.class;
    }

    /**
     * {@inheritDoc}
     */
    public ExaminationRequest get(Serializable id) {
        ExaminationRequest examination = (ExaminationRequest) getHibernateTemplate().get(getPersistentClass(), id);
        if(DonorHelper.USE_SRPD) {
        	Map<FieldsInMap, Object> paramMap = donorHelper.makeMapForGet(examination.getDonor().getTempStorageId());
        	Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
        	examination = donorHelper.mergeRequestAndMap(examination, resMap);
        }
        return examination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExaminationRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
        return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countDocument(boolean showDeleted) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
    	return countDocuments(examinationsFilter);
    }

    /**
     * Поиск документов
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @param offset      смещение
     * @param count       кол-во результатов
     * @param orderBy     поле для сортировки
     * @param orderAsc    направление сортировки
     * @return список документов
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
        examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
        return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
    }

    /**
     * Кол-во документов
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
    public long countDocument(String filter, boolean showDeleted) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
        return countDocuments(examinationsFilter);
    }
    /**
     * Поиск обращений на обследование
     *
     * @param showDeleted true - show deleted, false - hide deleted
     * @param personId    - идентификатор пользователя
     * @return - список обращений на обследование
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocumentsByPerson(boolean showDeleted, int personId, int offset, int count, String orderBy, boolean orderAsc) {
        if (personId > 0) {
        	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        	examinationsFilter.getListFields().add(FieldFilterBean.init("therapist.id", personId, CompareType.EQ));
        	return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Количество обращений на обследование
     *
     * @param showDeleted true - show deleted, false - hide deleted
     * @param personId    - идентификатор пользователя
     * @return - количество
     */
    public long countDocumentsByPerson(boolean showDeleted, int personId) {
        if (personId > 0) {
        	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        	examinationsFilter.getListFields().add(FieldFilterBean.init("therapist.id", personId, CompareType.EQ));
        	return countDocument(examinationsFilter);
        } else {
            return 0;
        }
    }

    /**
     * Поиск обращений на обследование
     *
     * @param showDeleted true - show deleted, false - hide deleted
     * @return - список обращений на обследование
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocumentsByByTimeBounds(boolean showDeleted, Date start, Date finish, int offset, int count, String orderBy, boolean orderAsc) {
       	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
    	examinationsFilter.getListFields().add(FieldFilterBean.init("planDate", start, CompareType.GE));
    	examinationsFilter.getListFields().add(FieldFilterBean.init("planDate", finish, CompareType.LE));
    	return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
        

    }

    /**
     * Поиск обращений на обследование
     *
     * @param showDeleted true - show deleted, false - hide deleted
     * @param donorId     - идентификатор донора
     * @return - список обращений на обследование
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocumentsByDonor(boolean showDeleted, int donorId, int offset, int count, String orderBy, boolean orderAsc) {
        if (donorId > 0) {
        	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        	examinationsFilter.getListFields().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
        	return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * Количество обращений на обследование
     *
     * @param showDeleted true - show deleted, false - hide deleted
     * @param donorId     - идентификатор пользователя
     * @return - количество
     */
    public long countDocumentsByDonor(boolean showDeleted, int donorId) {
        if (donorId > 0) {
        	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
        	examinationsFilter.getListFields().add(FieldFilterBean.init("donor.id", donorId, CompareType.EQ));
        	return countDocuments(examinationsFilter);
        } else {
            return 0;
        }
    }

    /**
     * Поиск обращений на обследование
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return - список обращений на обследование
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findPlannedDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
    	examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.EQ));
    	examinationsFilter.getListFields().add(FieldFilterBean.init("planDate", null, CompareType.NOT_NULL));
    	return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
    	
    }

    /**
     * Количество запланированных обращений на обследование
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return - количество
     */
    public long countPlannedDocument(String filter, boolean showDeleted) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
    	examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.EQ));
    	/* в начальном коде не было этого параметра, но для выборки документов он используется. !!нужно уточнить!! */
    	examinationsFilter.getListFields().add(FieldFilterBean.init("planDate", null, CompareType.NOT_NULL));
    	return countDocuments(examinationsFilter);
    }

    /**
     * Поиск документов
     *
     * @param statusId    идентификатор статуса
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @param offset      смещение
     * @param count       кол-во результатов
     * @param orderBy     поле для сортировки
     * @param orderAsc    направление сортировки
     * @return список документов
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocumentsByStatus(int statusId, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
        if (statusId != 0) {
        	examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId, CompareType.EQ));
        }
        return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
    }

    /**
     * Кол-во документов
     *
     * @param statusId    идентификатор статуса
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
    public long countDocumentByStatus(int statusId, String filter, boolean showDeleted) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
        if (statusId != 0) {
            examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId, CompareType.EQ));
        }
        return countDocuments(examinationsFilter);
    }


    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findRequestsForLaboratory(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
    	examinationsFilter.getListFieldsDisjunction().add(FieldFilterBean.init("statusId", 2, CompareType.EQ));
    	examinationsFilter.getListFieldsDisjunction().add(FieldFilterBean.init("statusId", 3, CompareType.EQ));
        return findDocument(examinationsFilter, offset, count, orderBy, orderAsc);
    }

    public long countRequestsForLaboratory(String filter, boolean showDeleted) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(filter, showDeleted);
    	examinationsFilter.getListFieldsDisjunction().add(FieldFilterBean.init("statusId", 2, CompareType.EQ));
    	examinationsFilter.getListFieldsDisjunction().add(FieldFilterBean.init("statusId", 3, CompareType.EQ));
    	return countDocuments(examinationsFilter);
    }


    /**
     * Поиск пересекающихся по времени обращений на обследование
     */
    public long countPlannedDocument(Date planDate, boolean showDeleted) {
        ExaminationsFilter examinationsFilter = createExaminationsFilter(null, showDeleted);
    	examinationsFilter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.EQ));

        if (planDate != null) {
            Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.setTime(planDate);
            String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
            examinationsFilter.getSqlRestrictions().add("this_.planDate >= date_sub('" + formattedDate + "',interval 10 minute) and this_.planDate <= date_add('" + formattedDate + "',interval 10 minute)");
            return countDocuments(examinationsFilter);
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public ExaminationRequest findDocumentByAppointmentId(int appointmentId) {
    	ExaminationsFilter examinationsFilter = createExaminationsFilter(null, null);
        if (appointmentId > 0) {
        	examinationsFilter.getListFields().add(FieldFilterBean.init("appointment.id", appointmentId, CompareType.EQ));
        	List<ExaminationRequest> list = findDocument(examinationsFilter, -1, -1, null, false);
            if ((list != null) && !list.isEmpty()) {
                return list.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

	@SuppressWarnings("unchecked")
	public List<ExaminationRequest> findDocuments(ExaminationsFilter filter, int offset, int count,	String orderBy, boolean orderAsc) {
		filter.setShowDeleted(false);
		filter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}

	private DetachedCriteria getSearchCriteria(
			DetachedCriteria criteria, ExaminationsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			String firstName = filter.getFirstName();
			String lastName = filter.getLastName();
			String middleName = filter.getMiddleName(); 
			Date created = filter.getCreated();
			Date planDate = filter.getPlanDate();
			int statusId = filter.getStatusId();
			int examinationTypeId = filter.getExaminationTypeId();
			
			for (AliasFilterBean i: filter.getListAlias()) {
				 criteria.createAlias(i.getAssociationPath(), i.getAlias(), i.getJoinType());
			}
			for (FieldFilterBean i: filter.getListFields()) {
				switch (i.getCompareType()) {
				case EQ:
					conjunction.add(Restrictions.eq(i.getFieldName(), i.getValue()));
					break;
				case GE:
					conjunction.add(Restrictions.ge(i.getFieldName(), i.getValue()));
					break;
				case LE:
					conjunction.add(Restrictions.le(i.getFieldName(), i.getValue()));
					break;
				case NOT_NULL:
					conjunction.add(Restrictions.isNotNull(i.getFieldName()));
					break;
				case NE:
					conjunction.add(Restrictions.ne(i.getFieldName(), i.getValue()));
					break;
				}
			}
			for (String i: filter.getSqlRestrictions()) {
				conjunction.add(Restrictions.sqlRestriction(i));
			}
			
			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(firstName) 
					|| StringUtils.isNotEmpty(lastName) 
					|| StringUtils.isNotEmpty(middleName)) {
				if (!DonorHelper.USE_SRPD) {
					Disjunction disjunction = Restrictions.disjunction(); 
					if(StringUtils.isNotEmpty(lastName)) {
						disjunction.add(Restrictions.ilike("donor.lastName", lastName, MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotEmpty(middleName)) {
						disjunction.add(Restrictions.ilike("donor.middleName", middleName, MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotEmpty(firstName)) {
						disjunction.add(Restrictions.ilike("donor.firstName", firstName, MatchMode.ANYWHERE));
					}
					conjunction.add(disjunction);
				}
			}
			if (created != null) {
				addDateSearchCriteria(conjunction, created, "created");
			}
			if (planDate != null) {
				addDateSearchCriteria(conjunction, planDate, "planDate");
			}
	        if (statusId != ExaminationsFilter.EXAMINATION_STATUS_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("statusId", statusId));
	        }
	        if (examinationTypeId != ExaminationsFilter.EXAMINATION_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("examinationType", examinationTypeId));
	        }
			
			criteria.add(conjunction);
		}
        return criteria;
	}

	public long countDocument(ExaminationsFilter filter) {
		filter.setShowDeleted(false);
		filter.getListFields().add(FieldFilterBean.init("statusId", 9, CompareType.NE));
		return countDocuments(filter);
	}

	private long countDocuments(ExaminationsFilter filter) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
	private List<ExaminationRequest> findDocument(ExaminationsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
		List<ExaminationRequest> examinationaRequests = null;
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		examinationaRequests = getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset,count);
		if (DonorHelper.USE_SRPD) {
			filter.setListSRPDIds(donorHelper.listIdsSRPDFromRequest(examinationaRequests));
			Map<FieldsInMap, Object> paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
			examinationaRequests = donorHelper.mergeRequestsAndMap(examinationaRequests, resMap);
		}
		return examinationaRequests;
	}
	private ExaminationsFilter createExaminationsFilter(String pattern, Boolean showDeleted) {
		ExaminationsFilter filter = new ExaminationsFilter();
		if (StringUtils.isNotEmpty(pattern)) {
			 filter.getListAlias().add(AliasFilterBean.initAlias("donor", CriteriaSpecification.LEFT_JOIN));
			 filter.getListAlias().add(AliasFilterBean.initAlias("therapist", CriteriaSpecification.LEFT_JOIN));
			 
			 filter.getListFieldsDisjunction().add(FieldFilterBean.init("number", filter, CompareType.ILIKE));
			 filter.getListFieldsDisjunction().add(FieldFilterBean.init("therapist.lastName", filter, CompareType.ILIKE));
			 filter.getListFieldsDisjunction().add(FieldFilterBean.init("therapist.middleName", filter, CompareType.ILIKE));
			 filter.getListFieldsDisjunction().add(FieldFilterBean.init("therapist.firstName", filter, CompareType.ILIKE));
			 
			 filter.setFirstName(pattern);
			 filter.setMiddleName(pattern);
			 filter.setLastName(pattern);
		}
		if (showDeleted != null && !showDeleted) {
			filter.setShowDeleted(showDeleted);
		}
		return filter;
	}
}