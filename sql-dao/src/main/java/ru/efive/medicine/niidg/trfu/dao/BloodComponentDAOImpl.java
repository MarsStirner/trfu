package ru.efive.medicine.niidg.trfu.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodGroup;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.Analysis;
import ru.efive.medicine.niidg.trfu.data.entity.BloodComponent;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.BloodComponentsFilter;
import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class BloodComponentDAOImpl extends GenericDAOHibernate<BloodComponent> {
	private static SRPDDao srpdDao;
	private static DonorHelper donorHelper;
	
	static {
		if (DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			donorHelper = new DonorHelper();
		}
	}
	
	@Override
	protected Class<BloodComponent> getPersistentClass() {
		return BloodComponent.class;
	}
	
	/**
     * Поиск документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
		return findDocuments(bloodComponentFilter, offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param filter          критерий поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(String filter, boolean showDeleted) {
        BloodComponentsFilter bloodComponentFilter = createBloodComponentsFilter(filter, showDeleted);
        bloodComponentFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
        return countDocument(bloodComponentFilter);
	}
	
	/**
     * Поиск документов
     *
     * @param in_map          параметры поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(Map<String, Object> in_map, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setMap(in_map);
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}

	/**
     * Кол-во документов
     *
     * @param in_map          параметры поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @return кол-во результатов
     */
	public long countDocument(Map<String, Object> in_map, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.setMap(in_map);
		return countDocument(bloodComponentsFilter);
	}
	
	
	/**
     * Поиск документов
     *
     * @param filter          фильтр поиска
     * @param statusId        статус
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (statusId != 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        else {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param statusId        статус
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, int statusId, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (statusId != 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        else {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
        }
      return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск документов
     *
     * @param filter          фильтр поиска
     * @param statusId        статус
     * @param showExpired     true - отображать просроченные, false - скрывать просроченные
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(String filter, int statusId, boolean showExpired, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (!showExpired) {
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
            disj.add(FieldFilterBean.init("expirationDate", calendar.getTime(),CompareType.GE));
            disj.add(FieldFilterBean.init("expirationDate", true, CompareType.NULL));
            bloodComponentsFilter.getListFielsDisjunction().add(disj);
        }
        if (statusId != 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        else {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов
	 * @param filter          фильтр поиска
	 * @param statusId        статус
	 * @param showExpired     true - отображать просроченные, false - скрывать просроченные
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countDocument(String filter, int statusId, boolean showExpired, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        if (!showExpired) {
        	Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
            disj.add(FieldFilterBean.init("expirationDate", calendar.getTime(),CompareType.GE));
            disj.add(FieldFilterBean.init("expirationDate", true, CompareType.NULL));
            bloodComponentsFilter.getListFielsDisjunction().add(disj);
        }
        if (statusId != 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        else {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск просроченных КК
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findExpiredDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, false);
       
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
        disj.add(FieldFilterBean.init("expirationDate", calendar.getTime(),CompareType.LT));
        disj.add(FieldFilterBean.init("expirationDate", true, CompareType.NULL));
        bloodComponentsFilter.getListFielsDisjunction().add(disj);
        
        disj = new ArrayList<FieldFilterBean>();
        disj.add(FieldFilterBean.init("statusId", 2));
        disj.add(FieldFilterBean.init("statusId", 3));
        bloodComponentsFilter.getListFielsDisjunction().add(disj);
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countExpiredDocument(String filter, boolean showDeleted) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
        disj.add(FieldFilterBean.init("expirationDate", calendar.getTime(),CompareType.LT));
        disj.add(FieldFilterBean.init("expirationDate", true, CompareType.NULL));
        bloodComponentsFilter.getListFielsDisjunction().add(disj);
        
        disj = new ArrayList<FieldFilterBean>();
        disj.add(FieldFilterBean.init("statusId", 2));
        disj.add(FieldFilterBean.init("statusId", 3));
        bloodComponentsFilter.getListFielsDisjunction().add(disj);

        return countDocument(bloodComponentsFilter);
	}
	
	protected DetachedCriteria getConjunctionSearchCriteria(DetachedCriteria criteria, Map<String, Object> in_map) {
		if (in_map != null && in_map.size() > 0) {
			Conjunction conjunction = Restrictions.conjunction();
			
			String in_key = "componentType";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((BloodComponentType) in_map.get(in_key)).getId()));
			}
			
			in_key = "statusId";
			if (in_map.get(in_key) != null) {
				if ((Integer) in_map.get(in_key) != 0) {
					conjunction.add(Restrictions.eq(in_key, in_map.get(in_key)));
				}
			}
			
			in_key = "maker";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "bloodGroup";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((BloodGroup) in_map.get(in_key)).getId()));
			}
			
			in_key = "rhesusFactor";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Classifier) in_map.get(in_key)).getId()));
			}
			/*
			in_key = "startReadyDate";
			if (in_map.get(in_key) != null) {
				conjunction.add(Restrictions.ge(in_key.substring(5, 6).toLowerCase()+in_key.substring(6), in_map.get(in_key)));
			}
			
			in_key = "endReadyDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "startDistributedDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			
			in_key = "endDistributedDate";
			if (in_map.get(in_key) != null) {
				criteria.createAlias(in_key, in_key, CriteriaSpecification.LEFT_JOIN);
				conjunction.add(Restrictions.eq(in_key + ".id", ((Contragent) in_map.get(in_key)).getId()));
			}
			*/
			criteria.add(conjunction);
		}
		return criteria;
	}
	
	
	/**
	 * Поиск компонентов по обращению на донацию
	 * @param donationId - идентификатор обращения на донацию
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByDonation(int donationId) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        
        if (donationId > 0) {
        	bloodComponentsFilter.setDonationId(donationId);
        }
        return findDocument(bloodComponentsFilter, -1, -1, "number", true);
	}
	
	/**
	 * Количество компонентов по обращению на донацию
	 * @param donationId - идентификатор обращения на донацию
	 * @return - количество компонентов
	 */
	public long countComponentsByDonation(int donationId) {
        BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        if (donationId > 0) {
        	bloodComponentsFilter.setDonationId(donationId);
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
	 * Поиск компонентов по обращению на заказ КК
	 * @param orderId - идентификатор обращения на заказ КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByOrder(int orderId) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        if (orderId > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", orderId));
        }
        return findDocument(bloodComponentsFilter, -1, -1, "parentNumber,number", true);
	}
	
	/**
	 * Поиск компонентов по статусу
	 * @param statusId - статус КК
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByStatusId(boolean showDeleted, int statusId, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
        if (statusId > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов по статусу
	 * @param statusId - статус КК
	 * @return - количество компонентов
	 */
	public long countComponentsByStatusId(boolean showDeleted, int statusId) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
        if (statusId > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        }
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск закупленных документов
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findPurchasedDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("purchased", true));
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество закупленных компонентов
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countPurchasedDocument(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 100));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("purchased", true));
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск компонентов с завершенным сроком карантинизации
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedDocuments(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 2));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("quarantineFinishDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime(), CompareType.LE));
        return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	/**
	 * Количество компонентов с завершенным сроком карантинизации
	 * 
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countQuarantinedDocument(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 2));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("quarantineFinishDate", Calendar.getInstance(ApplicationHelper.getLocale()).getTime(), CompareType.LE));
        return countDocument(bloodComponentsFilter);
	}
	
	/**
     * Поиск компонентов с завершенным сроком карантинизации по донору
     * 
     * @param donor           донор
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список документов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedDocumentsByDonor(Donor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
        	return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	/**
	 * Количество компонентов с завершенным сроком карантинизации по донору
	 * @param donor           донор
	 * @param filter          фильтр поиска
	 * @param showDeleted     true - show deleted, false - hide deleted
	 * @return количество компонентов
	 */
	public long countQuarantinedDocumentByDonor(Donor donor, String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and components.quarantineFinishDate <= sysdate() and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
            return countDocument(bloodComponentsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
        	return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countDocumentsInQuarantineByDonor(Donor donor, String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
        
        String query = "select components.id from trfu_blood_components components "
        	+ "inner join trfu_blood_donation_requests requests on components.donationId = requests.id "
        	+ "inner join trfu_donors donors on requests.donor_id = donors.id "
        	+ "where components.status_id = 2 and donors.id = " + donor.getId();
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
            return countDocument(bloodComponentsFilter);
        }
        else {
        	return 0;
        }
	}
	
	
	/**
     * Поиск компонентов по номеру донации и номеру компонента
     *
     * @param filter          фильтр поиска
     * @param showDeleted     true - show deleted, false - hide deleted
     * @param offset          смещение
     * @param count           кол-во результатов
     * @param orderBy         поле для сортировки
     * @param orderAsc        направление сортировки
     * @return список компонентов
     */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsByFullNumber(String parentNumber, String number, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("purchased", true));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("parentNumber", parentNumber, CompareType.ILIKE));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("number", parentNumber, CompareType.ILIKE));
		return findDocuments(bloodComponentsFilter, -1, -1, null, false);
	}
	
	/**
	 * Подбор компонентов по фенотипам
	 * @return - список компонентов
	 */
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsByPhenotypes(List<Analysis> phenotypes, boolean searchBloodGroup, String bloodGroup, boolean searchRhesus, String rhesusFactor, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
        
        StringBuilder concat = new StringBuilder();
        StringBuilder testsSubQuery = new StringBuilder();
        for (Analysis phenotype: phenotypes) {
        	if (!testsSubQuery.toString().equals("")) {
        		testsSubQuery.append(" or ");
        	}
        	testsSubQuery.append("binary value = '" + phenotype.getType().getValue() + "'");
        	concat.append(phenotype.getValue());
        }
        
        String query = "select components.id as id FROM trfu_blood_donation_request_tests_immuno immuno "
        	+ "inner join trfu_tests tests ON tests.id = immuno.testsImmuno_id "
        	+ "inner join trfu_blood_donation_requests donations on immuno.trfu_blood_donation_requests_id = donations.id "
        	+ "inner join trfu_blood_components components on donations.number = components.parentNumber "
        	+ (searchBloodGroup? "inner join trfu_blood_groups groups on components.bloodGroup_id = groups.id ": "")
        	+ (searchRhesus? "inner join trfu_classifiers classifiers on components.rhesusFactor_id = classifiers.id ": "")
        	+ "where components.status_id = 3 and (components.expirationDate > sysdate() or components.expirationDate is null) "
        	+ (searchBloodGroup? "and groups.value = '" + bloodGroup + "' ": "")
        	+ (searchRhesus? "and classifiers.value = '" + rhesusFactor + "' ": "")
        	+ "and tests.type_id in (select id from trfu_analysis_types where " + testsSubQuery + ") "
        	+ "group by components.id "
        	+ "having group_concat(concat('',tests.value) order by tests.id separator '')='" + concat + "'";
        
        List list = getSession().createSQLQuery(query).addScalar("id").list();
        
        if (list != null && list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocumentsInControl(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 3));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("inControl", true));
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
		
	}
	
	public long countDocumentsInControl(String filter, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", 3));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("inControl", true));
		return countDocument(bloodComponentsFilter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("inControl", false));
		return findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countControlledComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("inControl", false));
		return countDocument(bloodComponentsFilter);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, false);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("statusId", statusId));
        
		bloodComponentsFilter.getListAlias().add(AliasFilterBean.initAlias("history", CriteriaSpecification.LEFT_JOIN));
        List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
        disj.add(FieldFilterBean.init("history.toStatusId", 2));
        disj.add(FieldFilterBean.init("history.fromStatusId", 2));
        bloodComponentsFilter.getListFielsDisjunction().add(disj);
        
        List<BloodComponent> result = new ArrayList<BloodComponent>(
        		new LinkedHashSet<BloodComponent>(findDocument(bloodComponentsFilter, offset, count, orderBy, orderAsc)));
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public long countQuarantinedComponentsByStatusId(String filter, int statusId, boolean showDeleted) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(filter, showDeleted);
		
		bloodComponentsFilter.getListAlias().add(AliasFilterBean.initAlias("history", CriteriaSpecification.LEFT_JOIN));
		 List<FieldFilterBean> disj = new ArrayList<FieldFilterBean>();
	     disj.add(FieldFilterBean.init("history.toStatusId", 2));
	     disj.add(FieldFilterBean.init("history.fromStatusId", 2));
	     bloodComponentsFilter.getListFielsDisjunction().add(disj);
		
        List<BloodComponent> result = new ArrayList<BloodComponent>(new LinkedHashSet<BloodComponent>(
        		findDocument(bloodComponentsFilter, -1, -1, null, false)));
        
        return result.size() * 1l;
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory() {  
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, null);
        String query = "SELECT comp.id FROM trfu_blood_components comp INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE wf.to_status_id = 3 AND wf.startDate = (SELECT max(wf_in.startDate) FROM trfu_blood_component_history hist_in " +
        		"INNER JOIN wf_history wf_in ON hist_in.history_entry_id = wf_in.id WHERE hist_in.component_id = comp.id) " +
        		"AND wf.to_status_id != comp.status_id and comp.orderId=0 and comp.status_id=10";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findComponentsWrongHistory2() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, null);
        String query = "SELECT id from(SELECT sum(CASE WHEN wf.to_status_id = 3 THEN 1 ELSE 0 END) AS gotov, " +
        		"sum(CASE WHEN wf.to_status_id = 10 THEN 1 ELSE 0 END) AS vidan, comp.id, comp.volume FROM trfu_blood_components comp " +
        		"INNER JOIN trfu_blood_component_types comp_types ON comp.componentType_id = comp_types.id " +
        		"INNER JOIN trfu_blood_component_history hist ON comp.id = hist.component_id INNER JOIN wf_history wf ON hist.history_entry_id = wf.id " +
        		"WHERE comp.status_id = 10 GROUP BY comp.id) as table1 where table1.gotov = 0 AND table1.vidan = 1";
        List list = getSession().createSQLQuery(query).list();
        
        if (list.size() > 0) {
        	bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list,CompareType.IN));
        	return findDocument(bloodComponentsFilter, -1, -1, null, false);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findSplitComponents() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("split", true));
		return findDocument(bloodComponentsFilter, -1, -1, null, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<BloodComponent> findUniquieSplitComponents() {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		
		List list = getSession().createSQLQuery("select c.id from trfu_blood_components c where c.split = 1 and c.donationId <> 0 group by c.donationId").list();
		
		if (list.size() > 0) {
			bloodComponentsFilter.getListFields().add(FieldFilterBean.init("id", list, CompareType.IN));
			return findDocument(bloodComponentsFilter, -1, -1, null, false);
		}
		else {
			return Collections.emptyList();
		}
	}
	
	@SuppressWarnings("unchecked")
	public BloodComponent findParentComponent(BloodComponent splitComponent) {
		BloodComponentsFilter bloodComponentsFilter = createBloodComponentsFilter(null, false);
		
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("parentNumber", splitComponent.getParentNumber(), CompareType.ILIKE));
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("number", splitComponent.getNumber(), CompareType.ILIKE));
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		bloodComponentsFilter.getListFields().add(FieldFilterBean.init("donationId", splitComponent.getDonationId(), CompareType.ILIKE));
		List<BloodComponent> list = findDocument(bloodComponentsFilter, -1, -1, null, false);
		if (list.isEmpty()) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	public long countDocuments(BloodComponentsFilter filter) {
		filter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
		return countDocument(filter);
	}

	@SuppressWarnings("unchecked")
	public List<BloodComponent> findDocuments(BloodComponentsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
		filter.getListFields().add(FieldFilterBean.init("statusId", 100, CompareType.NE));
		return findDocument(filter, offset, count, orderBy, orderAsc);
	}
	private long countDocument(BloodComponentsFilter filter){
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		if (!DonorHelper.USE_SRPD && filter.isQueryToSRPD()) {
			Map<FieldsInMap, Object> paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			filter.setListSRPDIds(srpdDao.get(paramMap).keySet());
		}
		return getCountOf(getSearchCriteria(detachedCriteria, filter));
		
	}
	private List<BloodComponent> findDocument(BloodComponentsFilter filter, int offset, int count, String orderBy, boolean orderAsc ) {
		DetachedCriteria detachedCriteria = createDetachedCriteria();
		Map<String, Map<FieldsInMap,Object>> resMap = null;
		Map<FieldsInMap,Object> paramMap = null;
		List<BloodComponent> bloodComponents = null;
		addOrderCriteria(orderBy, orderAsc, detachedCriteria);
		/*if (DonorHelper.USE_SRPD && filter.isQueryToSRPD()) {
			donorHelper = new DonorHelper();
			paramMap = donorHelper.listIdsDonorsForFilter(filter);
			resMap = srpdDao.get(paramMap);
			filter.setListSRPDIds(resMap.keySet());
		}*/
		String query = "Select id from trfu.trfu_blood_donation_requests";
        List list = getSession().createSQLQuery(query).list();
		detachedCriteria.setFetchMode("donation", FetchMode.EAGER);
		detachedCriteria.add(Restrictions.in("donationId", list));
		bloodComponents =  getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter), offset,count);
		if (DonorHelper.USE_SRPD && resMap == null) {
			filter.setListSRPDIds(donorHelper.listIdsSRPDFromBloodComponent(bloodComponents));
			paramMap = donorHelper.listIdsSRPDFromFilter(filter);
			resMap = srpdDao.get(paramMap);
			bloodComponents = donorHelper.mergeBloodComponentsAndMap(bloodComponents, resMap);
		}
		return bloodComponents;
	}
	
	protected DetachedCriteria getSearchCriteria(DetachedCriteria criteria, BloodComponentsFilter filter) {
		if (filter != null) {
			Conjunction conjunction = Restrictions.conjunction();
			String number = filter.getNumber();
			int bloodComponentTypeId = filter.getBloodComponentTypeId();
			String donorCode = filter.getDonorCode();
			int makerId = filter.getMakerId();
			int bloodGroupId = filter.getBloodGroupId();
			int rhesusFactorId = filter.getRhesusFactorId();
			Date donationDate = filter.getDonationDate();
			Date expirationDate = filter.getExpirationDate();
			int statusId = filter.getStatusId();
			String firstName = filter.getFirstName();
			String lastName = filter.getLastName();
			String middleName = filter.getMiddleName();
			Collection<String> listSRPDIds = filter.getListSRPDIds();
			List<AliasFilterBean> listAlias = filter.getListAlias();
			List<List<FieldFilterBean>> listDisj = filter.getListFielsDisjunction();
			List<FieldFilterBean> listFields = filter.getListFields();
			for (AliasFilterBean i: listAlias) {
				criteria.createAlias(i.getAssociationPath(), i.getAlias(), i.getJoinType());
			}
			for (FieldFilterBean i: listFields) {
				conjunction.add(makeRestriction(i));
			}
			for(List<FieldFilterBean> j: listDisj) {
				Disjunction disj = Restrictions.disjunction();
				for(FieldFilterBean i: j) {
					disj.add(makeRestriction(i));
				}
				criteria.add(disj);
			}
			for (Junction i: filter.getListJunction()) {
				criteria.add(i);	
			}
			if (filter.getMap() != null) {
				getConjunctionSearchCriteria(criteria, filter.getMap());
			}
			if (StringUtils.isNotEmpty(number)) {
				String[] numbers = number.split("-");
				if (numbers.length == 1) {
					conjunction.add(Restrictions.ilike("number", numbers[0], MatchMode.ANYWHERE));
				} else {
					conjunction.add(Restrictions.ilike("parentNumber", numbers[0], MatchMode.ANYWHERE));
					conjunction.add(Restrictions.ilike("number", numbers[1], MatchMode.ANYWHERE));
				}
			}
			if (StringUtils.isNotEmpty(donorCode)) {
				conjunction.add(Restrictions.ilike("donorCode", donorCode, MatchMode.ANYWHERE));
			}
	        if (bloodGroupId != BloodComponentsFilter.BLOOD_GROUP_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("bloodGroup.id", bloodGroupId));
	        }
	        if (rhesusFactorId != BloodComponentsFilter.RHESUS_FACTOR_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("rhesusFactor.id", rhesusFactorId));
	        }
			if (donationDate != null) {
				addDateSearchCriteria(conjunction, donationDate, "donationDate");
			}
			if (expirationDate != null) {
				addDateSearchCriteria(conjunction, expirationDate, "expirationDate");
			}
			/* Проверка статуса: 1. CompareFlag = null_value или  eq -  Resrtictions.eq 
			 * 					 2. CompareFlag = ne - Restritions.ne */
	        if (statusId != BloodComponentsFilter.BLOOD_COMPONENT_STATUS_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("statusId", statusId));
	        }
	        if (bloodComponentTypeId != BloodComponentsFilter.BLOOD_COMPONENT_TYPE_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("componentType.id", bloodComponentTypeId));
	        }
	        if (makerId != BloodComponentsFilter.MAKER_NULL_VALUE) {
	        	conjunction.add(Restrictions.eq("maker.id", makerId));
	        }
	        if ((!DonorHelper.USE_SRPD && filter.isQueryToSRPD()) || listSRPDIds != null) {
	        	criteria.createAlias("donation", "donation", CriteriaSpecification.INNER_JOIN);
	        	criteria.createAlias("donation.donor", "donor", CriteriaSpecification.INNER_JOIN);
	        }
	        Disjunction disjunction = Restrictions.disjunction();
	        if (!DonorHelper.USE_SRPD) {
	        	if (StringUtils.isNotEmpty(lastName)) {
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
	        if (listSRPDIds != null) {
	        		disjunction.add(Restrictions.in("donor.tempStorageId", listSRPDIds));
	        		conjunction.add(disjunction);
	        }
	        if (!filter.isShowDeleted()) {
				criteria.add(Restrictions.eq("deleted", false));
			}
			criteria.add(conjunction);
		}
		return criteria;
	}
	private BloodComponentsFilter createBloodComponentsFilter(String pattern, Boolean showDeleted) {
		BloodComponentsFilter filter = new BloodComponentsFilter();
		if (StringUtils.isNotEmpty(pattern)) {
			filter.getListAlias().add(AliasFilterBean.initAlias("componentType", CriteriaSpecification.LEFT_JOIN));
			List<FieldFilterBean> listDisj = new ArrayList<FieldFilterBean>();
			listDisj.add(FieldFilterBean.init("componentType.value", pattern, CompareType.ILIKE));
			listDisj.add(FieldFilterBean.init("parentNumber", pattern, CompareType.ILIKE));
			listDisj.add(FieldFilterBean.init("number", pattern, CompareType.ILIKE));
			listDisj.add(FieldFilterBean.init("DATE_FORMAT(this_.donationDate, '%d.%m.%Y') like lower(?)", filter + "%", CompareType.SQL));
			/*Disjunction disjunction = Restrictions.disjunction();
	        disjunction.add(Restrictions.ilike("componentType.value", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("parentNumber", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.ilike("number", pattern, MatchMode.ANYWHERE));
	        disjunction.add(Restrictions.sqlRestriction("DATE_FORMAT(this_.donationDate, '%d.%m.%Y') like lower(?)", filter + "%", new StringType()));*/
	        if (pattern.length() > 8) {
	        	//disjunction.add(Restrictions.sqlRestriction("CONCAT(this_.parentNumber, this_.number) like RIGHT(?, 8)", filter + "%", new StringType()));
	        	listDisj.add(FieldFilterBean.init("CONCAT(this_.parentNumber, this_.number) like RIGHT(?, 8)", filter + "%", CompareType.SQL));
	        }
	        //filter.getListJunction().add(disjunction);
	        filter.getListFielsDisjunction().add(listDisj);
		}
		if (showDeleted != null) {
			filter.setShowDeleted(showDeleted);
		}
		return filter;
	}
	private List<BloodComponent> getDataFromSRPD(List<BloodComponent> components) {
		Map<FieldsInMap, Object> paramMap = new HashMap<DonorHelper.FieldsInMap, Object>();
		List<String> listStorageIds = donorHelper.listIdsSRPDFromBloodComponent(components);
		paramMap.put(FieldsInMap.LIST_STORAGE_IDS, listStorageIds);
		Map<String, Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
		components = donorHelper.mergeBloodComponentsAndMap(components, resMap);
		return components;
	}
}