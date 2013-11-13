package ru.efive.medicine.niidg.trfu.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.BloodDonationRequest;
import ru.efive.medicine.niidg.trfu.data.entity.ExaminationRequest;
import ru.efive.medicine.niidg.trfu.filters.ExaminationsFilter;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.medicine.niidg.trfu.util.DateHelper;

public class ExaminationRequestDAOImpl extends GenericDAOHibernate<ExaminationRequest> {

    @Override
    protected Class<ExaminationRequest> getPersistentClass() {
        return ExaminationRequest.class;
    }

    /**
     * {@inheritDoc}
     */
    public ExaminationRequest get(Serializable id) {
        ExaminationRequest examination = (ExaminationRequest) getHibernateTemplate().get(getPersistentClass(), id);
        return examination;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExaminationRequest> findDocuments(boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        // Не отображаем запланированные
        detachedCriteria.add(Restrictions.ne("statusId", 9));

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }

        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countDocument(boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(
                DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        // Не отображаем запланированные
        detachedCriteria.add(Restrictions.ne("statusId", 9));

        return getCountOf(detachedCriteria);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        // Не отображаем запланированные
        detachedCriteria.add(Restrictions.ne("statusId", 9));

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }

        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
    }

    /**
     * Кол-во документов
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
    public long countDocument(String filter, boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(
                DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        // Не отображаем запланированные
        detachedCriteria.add(Restrictions.ne("statusId", 9));

        return getCountOf(getSearchCriteria(detachedCriteria, filter));
    }

    private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter) {
        if (StringUtils.isNotEmpty(filter)) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
            criteria.createAlias("donor", "donor", CriteriaSpecification.LEFT_JOIN);
            disjunction.add(Restrictions.ilike("donor.lastName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("donor.middleName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("donor.firstName", filter, MatchMode.ANYWHERE));
            criteria.createAlias("therapist", "therapist", CriteriaSpecification.LEFT_JOIN);
            disjunction.add(Restrictions.ilike("therapist.lastName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("therapist.middleName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("therapist.firstName", filter, MatchMode.ANYWHERE));
            criteria.add(disjunction);
        }
        return criteria;
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (personId > 0) {
            detachedCriteria.add(Restrictions.eq("therapist.id", personId));
            String[] ords = orderBy == null ? null : orderBy.split(",");
            if (ords != null) {
                if (ords.length > 1) {
                    addOrder(detachedCriteria, ords, orderAsc);
                } else {
                    addOrder(detachedCriteria, orderBy, orderAsc);
                }
            }
            return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (personId > 0) {
            detachedCriteria.add(Restrictions.eq("therapist.id", personId));
            return getCountOf(detachedCriteria);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        detachedCriteria.add(Restrictions.ge("planDate", start));
        detachedCriteria.add(Restrictions.le("planDate", finish));

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }
        return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);

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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (donorId > 0) {
            detachedCriteria.add(Restrictions.eq("donor.id", donorId));
            String[] ords = orderBy == null ? null : orderBy.split(",");
            if (ords != null) {
                if (ords.length > 1) {
                    addOrder(detachedCriteria, ords, orderAsc);
                } else {
                    addOrder(detachedCriteria, orderBy, orderAsc);
                }
            }
            return getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        if (donorId > 0) {
            detachedCriteria.add(Restrictions.eq("donor.id", donorId));
            return getCountOf(detachedCriteria);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 9));
        detachedCriteria.add(Restrictions.isNotNull("planDate"));

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }
        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
    }

    /**
     * Количество запланированных обращений на обследование
     *
     * @param filter      критерий поиска
     * @param showDeleted true - show deleted, false - hide deleted
     * @return - количество
     */
    public long countPlannedDocument(String filter, boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 9));

        return getCountOf(getSearchCriteria(detachedCriteria, filter));
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        if (statusId != 0) {
            detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }

        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
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
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        if (statusId != 0) {
            detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }

        return getCountOf(getSearchCriteria(detachedCriteria, filter));
    }
    
    /**
     * Поиск документов с расширенным фильтром
     *
     * @param statusId    идентификатор статуса
     * @param filterNumberExamination      критерий поиска по номеру обследования 
     * @param filterDonorLastName      критерий поиска по фамилии
     * @param filterDonorFirstName      критерий поиска по имени
     * @param filterDonorMiddleName      критерий поиска по отчеству
     * @param showDeleted true - show deleted, false - hide deleted
     * @param offset      смещение
     * @param count       кол-во результатов
     * @param orderBy     поле для сортировки
     * @param orderAsc    направление сортировки
     * @return список документов
     */
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findDocumentsByStatus(int statusId, String filterNumberExamination, String filterDonorLastName, String filterDonorFirstName, String filterDonorMiddleName,
    		boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        detachedCriteria = getCriteriaForAdmittedExaminations(detachedCriteria, statusId, showDeleted);

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }

        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filterNumberExamination, filterDonorLastName, filterDonorFirstName, filterDonorMiddleName), offset, count);
    }

    /**
     * Кол-во документов с расширенным фильтром
     *
     * @param statusId    идентификатор статуса
     * @param filterNumberExamination      критерий поиска по номеру обследования 
     * @param filterDonorLastName      критерий поиска по фамилии
     * @param filterDonorFirstName      критерий поиска по имени
     * @param filterDonorMiddleName      критерий поиска по отчеству
     * @param showDeleted true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
    public long countDocumentByStatus(int statusId, String filterNumberExamination, String filterDonorLastName, String filterDonorFirstName, String filterDonorMiddleName,
    		boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        detachedCriteria = getCriteriaForAdmittedExaminations(detachedCriteria, statusId, showDeleted);

        return getCountOf(getSearchCriteria(detachedCriteria, filterNumberExamination, filterDonorLastName, filterDonorFirstName, filterDonorMiddleName));
    }
    
    private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filterNumberExamination, String filterDonorLastName, String filterDonorFirstName, String filterDonorMiddleName) {
    	if (StringUtils.isNotEmpty(filterNumberExamination) || StringUtils.isNotEmpty(filterDonorLastName) || StringUtils.isNotEmpty(filterDonorFirstName) || StringUtils.isNotEmpty(filterDonorMiddleName)) {
    		Disjunction disjunction = Restrictions.disjunction();
    		criteria.createAlias("donor", "donor", CriteriaSpecification.LEFT_JOIN);
    		if (StringUtils.isNotEmpty(filterNumberExamination)) {
    			disjunction.add(Restrictions.ilike("number", filterNumberExamination, MatchMode.ANYWHERE));
    		}
    		if (StringUtils.isNotEmpty(filterDonorLastName)) {
    			disjunction.add(Restrictions.ilike("donor.lastName", filterDonorLastName, MatchMode.ANYWHERE));
    		}
    		if (StringUtils.isNotEmpty(filterDonorFirstName)) {
    			disjunction.add(Restrictions.ilike("donor.firstName", filterDonorFirstName, MatchMode.ANYWHERE));
    		}
    		if (StringUtils.isNotEmpty(filterDonorMiddleName)) {
    			disjunction.add(Restrictions.ilike("donor.middleName", filterDonorMiddleName, MatchMode.ANYWHERE));
    		}
    		criteria.add(disjunction);
    	}
        
        return criteria;
    }
    
    private DetachedCriteria getCriteriaForAdmittedExaminations(DetachedCriteria detachedCriteria, int statusId, boolean showDeleted) {
    	if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

        if (statusId != 0) {
            detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        
        Date currentDate = new Date();
        Date fromDate = DateHelper.getDateWithoutTime(currentDate);
        detachedCriteria.add(Restrictions.ge("created", fromDate));
		Date toDate = DateHelper.getDateWithoutTime(DateHelper.getTomorrowDate(currentDate));
		detachedCriteria.add(Restrictions.lt("created", toDate));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(BloodDonationRequest.class);
		subCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		subCriteria.add(Restrictions.isNotNull("examination.id"));
	    subCriteria.setProjection(Projections.property("examination.id"));
	    
	    detachedCriteria.add(Subqueries.propertyNotIn("id", subCriteria));
    	
    	return detachedCriteria;
    }
    
    @SuppressWarnings("unchecked")
    public List<ExaminationRequest> findRequestsForLaboratory(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(disjunction);
        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }
        return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset, count);
    }

    public long countRequestsForLaboratory(String filter, boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ExaminationRequest.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("statusId", 2));
        disjunction.add(Restrictions.eq("statusId", 3));
        detachedCriteria.add(disjunction);
        return getCountOf(getSearchCriteria(detachedCriteria, filter));
    }


    /**
     * Поиск пересекающихся по времени обращений на обследование
     */
    public long countPlannedDocument(Date planDate, boolean showDeleted) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        detachedCriteria.add(Restrictions.eq("statusId", 9));

        if (planDate != null) {
            Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.setTime(planDate);
            String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
            detachedCriteria.add(Restrictions.sqlRestriction("this_.planDate >= date_sub('" + formattedDate + "',interval 10 minute) and this_.planDate <= date_add('" + formattedDate + "',interval 10 minute)"));

            return getCountOf(detachedCriteria);
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public ExaminationRequest findDocumentByAppointmentId(int appointmentId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass()).setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        if (appointmentId > 0) {
            detachedCriteria.add(Restrictions.eq("appointment.id", appointmentId));
            List<ExaminationRequest> list = getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
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
	public List<ExaminationRequest> findDocuments(
			ExaminationsFilter filter, int offset, int count,
			String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		addNotDeletedCriteria(detachedCriteria);
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		return getHibernateTemplate().findByCriteria(
				getSearchCriteria(detachedCriteria, filter), offset,
				count);
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
			
			
			if (StringUtils.isNotEmpty(number)) {
				conjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotEmpty(firstName) || StringUtils.isNotEmpty(lastName) || StringUtils.isNotEmpty(middleName)) {
				criteria.createAlias("donor", "donor", CriteriaSpecification.INNER_JOIN);
				if (StringUtils.isNotEmpty(firstName)) {
					conjunction.add(Restrictions.ilike("donor.firstName", firstName, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(lastName)) {
					conjunction.add(Restrictions.ilike("donor.lastName", lastName, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(middleName)) {
					conjunction.add(Restrictions.ilike("donor.middleName", middleName, MatchMode.ANYWHERE));
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
		DetachedCriteria detachedCriteria = createDetachedCriteria();
        addNotDeletedCriteria(detachedCriteria);
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
	}
}