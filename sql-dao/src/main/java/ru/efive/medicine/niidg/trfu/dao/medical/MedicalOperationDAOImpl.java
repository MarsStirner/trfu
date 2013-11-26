package ru.efive.medicine.niidg.trfu.dao.medical;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.dao.sql.entity.document.Document;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Biomaterial;
import ru.efive.medicine.niidg.trfu.data.entity.medical.BiomaterialDonor;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Operation;
import ru.efive.medicine.niidg.trfu.data.entity.medical.Processing;
import ru.efive.medicine.niidg.trfu.filters.AppendSRPDFilter.CompareType;
import ru.efive.medicine.niidg.trfu.filters.BiomaterialDonorsFilter;
import ru.efive.medicine.niidg.trfu.filters.BiomaterialsFilter;
import ru.efive.medicine.niidg.trfu.filters.MedicalOperationFilter;
import ru.efive.medicine.niidg.trfu.filters.OperationsFilter;
import ru.efive.medicine.niidg.trfu.filters.bean.AliasFilterBean;
import ru.efive.medicine.niidg.trfu.filters.bean.FieldFilterBean;
import ru.korusconsulting.SRPD.DonorHelper;
import ru.korusconsulting.SRPD.DonorHelper.FieldsInMap;
import ru.korusconsulting.SRPD.SRPDDao;

public class MedicalOperationDAOImpl extends GenericDAOHibernate<Document> {
	private static SRPDDao srpdDao;
	private static DonorHelper helper;
	
	static {
		if(DonorHelper.USE_SRPD) {
			srpdDao = new SRPDDao();
			helper = new DonorHelper();
		}
	}
	
	@Override
	protected Class<Document> getPersistentClass() {
		return Document.class;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Document> T get(Class<T> clazz, Serializable id) {
		T document = (T) getHibernateTemplate().get(clazz, id); 
		if (DonorHelper.USE_SRPD && BiomaterialDonor.class.getName().equals(clazz.getName())) {
			Map<FieldsInMap, Object> paramMap = helper.makeMapForGet(((BiomaterialDonor)document).getTempStorageId());
			document = (T)helper.mergeDonorAndMap((BiomaterialDonor)document, (Map<FieldsInMap, Object>)srpdDao.get(paramMap).values().toArray()[0]);
		} 
		return document;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Document> T save(Class<T> clazz, T document) {
		if (DonorHelper.USE_SRPD && BiomaterialDonor.class.getName().equals(clazz.getName())) {
			Session session = null;
			try {
				session = getSession();
				session.beginTransaction();
				session.save(document);
				Map<FieldsInMap, Object> query = helper.makeMapFromDonor((BiomaterialDonor)document);
				/* commit for saving information to TRFU, without temp_stogate_id */
				Map<DonorHelper.FieldsInMap, Object> answer = srpdDao.addPDToSRPD(query);
				BiomaterialDonor newDonor = helper.mergeDonorAndMap((BiomaterialDonor)document, answer);
				/* update for writing temp_stogate_id to DB of TRFU */
				session.update(newDonor);
				session.getTransaction().commit();
				return (T) get(BiomaterialDonor.class, ((BiomaterialDonor)document).getId());
			} catch(Exception e) {
				session.getTransaction().rollback();
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return null;
		} else {
			return (T) save(document);
		}
			
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Document> T update(Class<T> clazz, T document) {
		if (DonorHelper.USE_SRPD && BiomaterialDonor.class.getName().equals(clazz.getName())) {
			Session session = null;
			try {
				session = getSession();
				session.beginTransaction();
				session.update(document);
				session.getTransaction().commit();
				srpdDao.updateToSRPD(helper.makeMapFromDonor((BiomaterialDonor)document));
				return (T) get(clazz,((BiomaterialDonor)document).getId());
			} catch(Exception e) {
				session.getTransaction().rollback();
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return null;
		} else {
			return (T) update(document);
		}
	}
	
	public BiomaterialDonor getDonorByExternalId(Serializable externalId, boolean showDeleted) {
		BiomaterialDonorsFilter filter = createBiomaterialDonorsFilter(null, showDeleted);
		//filter.setExternalId(externalId);
		filter.getListFields().add(FieldFilterBean.init("externalId", externalId));
		List<BiomaterialDonor> list = findDonors(filter, -1, -1, null, true);
        if (list != null && !list.isEmpty()) {
        	return list.get(0);
        }
        return null;
	}
	
	/*
	 * Доноры (Лечебный сегмент)
	 */
	@SuppressWarnings("unchecked")
	public List<BiomaterialDonor> findDonors(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BiomaterialDonorsFilter biomaterialFilter = createBiomaterialDonorsFilter(filter, showDeleted);
		return findDonors(biomaterialFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countDonors(String filter, boolean showDeleted) {
        BiomaterialDonorsFilter biomaterialFilter = createBiomaterialDonorsFilter(filter, showDeleted);
        return countDonors(biomaterialFilter);
	}
	
	/*
	 * Лечебные процедуры
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findOperations(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		OperationsFilter operationFilter = createOperationsFilter(filter, showDeleted);
		return findOperations(operationFilter, offset, count, orderBy, orderAsc);	
	}
	
	public long countOperations(String filter, boolean showDeleted) {
        OperationsFilter operationFilter = createOperationsFilter(filter, showDeleted);
        return countOperations(operationFilter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Operation> findOperationsByDonor(BiomaterialDonor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        if (donor != null && donor.getId() > 0) {
        	OperationsFilter operationfilterFilter = createOperationsFilter(filter, showDeleted);
        	operationfilterFilter.getListFields().add(FieldFilterBean.init("donor.id", donor.getId()));
    		return findOperations(operationfilterFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	/*
	 * Биоматериалы
	 */
	@SuppressWarnings("unchecked")
	public List<Biomaterial> findBiomaterials(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BiomaterialsFilter biomaterialsFilter = createBiomaterialsFilter(filter, showDeleted);
		return findBiomaterials(biomaterialsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countBiomaterials(String filter, boolean showDeleted) {
		BiomaterialsFilter biomaterialsFilter = createBiomaterialsFilter(filter, showDeleted);
		return getCountOf(getSearchCriteria(biomaterialsFilter));
	}
	
	@SuppressWarnings("unchecked")
	public List<Biomaterial> findBiomaterials(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		BiomaterialsFilter biomaterialsFilter = createBiomaterialsFilter(filter, showDeleted);
        if (statusId != 0) {
        	biomaterialsFilter.getListFields().add(FieldFilterBean.init("statusId", filter));
        }
        return findBiomaterials(biomaterialsFilter, offset, count, orderBy, orderAsc);
	}
	
	public long countBiomaterials(String filter, int statusId, boolean showDeleted) {
		BiomaterialsFilter biomaterialFilter = createBiomaterialsFilter(filter, showDeleted);
        if (statusId != 0) {
        	biomaterialFilter.getListFields().add(FieldFilterBean.init("statusId", filter));
        }
        return countBiomaterials(biomaterialFilter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Biomaterial> findBiomaterialsByOperation(Operation operation, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
        if (operation != null && operation.getId() > 0) {
        	BiomaterialsFilter biomaterialsFilter = createBiomaterialsFilter(filter, showDeleted);
        	biomaterialsFilter.getListFields().add(FieldFilterBean.init("operation.id", operation.getId()));
        	return findBiomaterials(biomaterialsFilter, offset, count, orderBy, orderAsc);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countBiomaterialsByOperation(Operation operation, String filter, boolean showDeleted) {
		if (operation != null && operation.getId() > 0) {
			BiomaterialsFilter biomaterialsFilter = createBiomaterialsFilter(filter, showDeleted);
			biomaterialsFilter.getListFields().add(FieldFilterBean.init("operation.id", operation.getId()));
			return countBiomaterials(biomaterialsFilter);
		}
		else {
			return 0;
		}
	}
	
	/*
	 * Обработка
	 */
	@SuppressWarnings("unchecked")
	public List<Processing> findProcessings(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
 
		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Processing", showDeleted), offset, count);
	}
	
	public long countProcessings(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "Processing", showDeleted));
	}
	
	@SuppressWarnings("unchecked")
	public List<Processing> findProcessingsByBiomaterial(Biomaterial biomaterial, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (biomaterial != null && biomaterial.getId() > 0) {
        	detachedCriteria.add(Restrictions.eq("biomaterial.id", biomaterial.getId()));
        	
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Processing", showDeleted), offset, count);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countProcessingsByBiomaterial(Biomaterial biomaterial, String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (biomaterial != null && biomaterial.getId() > 0) {
			detachedCriteria.add(Restrictions.eq("biomaterial.id", biomaterial.getId()));
			
			return getCountOf(getSearchCriteria(detachedCriteria, filter, "Processing", showDeleted));
		}
		else {
			return 0;
		}
	}
	/**
	 * BiomaterialDonor - универсальный метод использующий фильтр
	 */
	
	private List<BiomaterialDonor> findDonors(BiomaterialDonorsFilter filter,int offset, int count, String orderBy, boolean orderAsc) {
			DetachedCriteria detachedCriteria = getSearchCriteria(filter);
			List<BiomaterialDonor> donors;
			addOrderCriteria(orderBy, orderAsc, detachedCriteria);
			if (DonorHelper.USE_SRPD) {
				Map<String,Map<FieldsInMap, Object>> resMap = null;
				Map<FieldsInMap, Object> paramMap = null;
				/*this logic will be executed, when some of 'parameters for search in SRPD' will be 'not Null'*/
				/*if(filter.isQueryToSRPD()) {
					/* параметры для предварительного поиска данных в ЗХПД */
					/*paramMap = helper.listIdsDonorsForFilter(filter);
					resMap = srpdDao.get(paramMap);
					filter.setListSRPDIds(resMap.keySet());
				}*/
				donors = getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
				/* logic used, when query to SRPD were not execute*/
				if (resMap == null) {
					/* количество необходимых данных из ЗХПД могло увеличится на основе выборки из базы ТРФУ,
					 *  поэтому переотправляем запрос к ЗХПД */
					filter.setListSRPDIds(helper.listIdsSRPDFromBiomaterialDonors(donors));
					paramMap = helper.listIdsSRPDFromFilter(filter);
					resMap = srpdDao.get(paramMap);
				}
				donors = helper.mergeBiomaterialDonorsAndMap(donors, resMap);
			} else {
				donors = getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
			}
			return donors;
		}
		private long countDonors(BiomaterialDonorsFilter filter) {
			if (DonorHelper.USE_SRPD) {
				if(filter.isQueryToSRPD()) {
					Map<FieldsInMap, Object> paramMap = helper.listIdsSRPDFromFilter(filter);
					Map<String,Map<FieldsInMap, Object>> resMap = srpdDao.get(paramMap);
					filter.setListSRPDIds(resMap.keySet());
				}
			} 
			return getCountOf(getSearchCriteria(filter));
			
		}
		/**
		 * Operation - универсальный метод для поиска и подсчёта количества
		 */
		private List<Operation> findOperations(OperationsFilter filter, int offset, int count, String orderBy, boolean orderAsc) {
			List<Operation> operations = null;
			DetachedCriteria detachedCriteria = getSearchCriteria(filter);
			operations =  getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
			return operations;
		}
		private long countOperations(OperationsFilter filter) {
			DetachedCriteria detachedCriteria = getSearchCriteria(filter);
			return getCountOf(detachedCriteria);
		}
		/**
		 * Biomaterial - универсальные методы для нахождения и подсчётак количества
		 */
		private List<Biomaterial> findBiomaterials(BiomaterialsFilter filter,int offset, int count, String orderBy, boolean orderAsc) {
			DetachedCriteria detachedCriteria = getSearchCriteria(filter);
			addOrderCriteria(orderBy, orderAsc, detachedCriteria);
			@SuppressWarnings("unchecked")
			List<Biomaterial> biomaterials = getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
			if (DonorHelper.USE_SRPD) {
				if (filter.getListFieldsDisjunction().size() < 1) {
					filter.getListFieldsDisjunction().add(new ArrayList<FieldFilterBean>());
				}
				filter.getListFieldsDisjunction().get(0).add(FieldFilterBean.init("temp_storage_id", helper.listIdsSRPDFromBiomaterial(biomaterials), CompareType.IN));
				Map<FieldsInMap, Object> map = helper.listIdsSRPDFromFilter(filter);
				Map<String,Map<FieldsInMap, Object>> resMap = srpdDao.get(map);
				biomaterials = helper.mergeBiomaterialsAndMap(biomaterials, resMap);
			}
			return biomaterials;
		}
		private long countBiomaterials(BiomaterialsFilter filter) {
			long res = getCountOf(getSearchCriteria(filter)); 
			return res;
		}
		/**
		 * Creation DetachedCriteria for BiomaterialDonorsFilter
		 */
		private DetachedCriteria getSearchCriteria(MedicalOperationFilter filter) {
			DetachedCriteria criteria = createDetachedCriteria(filter.getCurrentEntity());
			if (filter != null) {
				/*Disjunction disjunction = Restrictions.disjunction();
				Serializable externalId = filter.getExternalId();
				String number = filter.getNumber();
				String infectiousStatus = filter.getInfectiousStatus();
				String pregnancy = filter.getPregnancy();
				String commentary = filter.getCommentary();
				String factAdress = filter.getFactAdress();*/
				Collection<FieldFilterBean> fields = filter.getListFields();
				Collection<List<FieldFilterBean>> disjFields = filter.getListFieldsDisjunction();
				Collection<String> listIdsSRPD = filter.getListSRPDIds();
				for(FieldFilterBean i: fields) {
					criteria.add(makeRestriction(i));
				}
				for (List<FieldFilterBean> j: disjFields) {
					Disjunction disj = Restrictions.disjunction();
					for (FieldFilterBean i: j) {
						disj.add(makeRestriction(i));
					}
					criteria.add(disj);
				}
				/*if (externalId != null) {
					disjunction.add(Restrictions.eq("externalId", externalId));
					
				}
				if (StringUtils.isNotEmpty(number)) {
					disjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(infectiousStatus)) {
					disjunction.add(Restrictions.ilike("infectiousStatus", infectiousStatus, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(pregnancy)) {
					disjunction.add(Restrictions.ilike("pregnancy", pregnancy, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(commentary)) {
					disjunction.add(Restrictions.ilike("commentary", commentary, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(factAdress)) {
					disjunction.add(Restrictions.ilike("factAddress", factAdress, MatchMode.ANYWHERE));
				}*/
				if (listIdsSRPD != null) {
						//disjunction.add(Restrictions.in("tempStorageId", listIdsSRPD));
				}
				//criteria.add(disjunction);	
				if (!filter.isShowDeleted()) {
					criteria.add(Restrictions.eq("deleted", false));
				}
			}
			return criteria;
		}
		/**
		 * Creation DetachedCriteria for OperationsFilter
		 */
		/*private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, OperationsFilter filter) {
			if (filter != null) {
				Disjunction disjunction = Restrictions.disjunction();
				String number = filter.getNumber();
				String recepient = filter.getRecepient();
				String idNumber = filter.getIdNumber();
				Collection<String> listIdsSRPD = filter.getListSRPDIds();
				Integer donorId = filter.getDonorId();
				if (StringUtils.isNotEmpty(number)) {
					disjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(recepient)) {
					disjunction.add(Restrictions.ilike("recipient", recepient, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(idNumber)) {
					disjunction.add(Restrictions.ilike("ibNumber", idNumber, MatchMode.ANYWHERE));
				}
				criteria.createAlias("donor", "donor", CriteriaSpecification.LEFT_JOIN);
				if (!DonorHelper.USE_SRPD) {
					String firstName = filter.getFirstName();
					String lastName = filter.getLastName();
					String middleName = filter.getMiddleName();
					if (StringUtils.isNotEmpty(lastName)) {
						disjunction.add(Restrictions.ilike("donor.lastName", firstName, MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotEmpty(middleName)) {
						disjunction.add(Restrictions.ilike("donor.middleName", middleName, MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotEmpty(firstName)) {
						disjunction.add(Restrictions.ilike("donor.firstName", firstName, MatchMode.ANYWHERE));
					}
				} else if (listIdsSRPD != null) {
					disjunction.add(Restrictions.in("donor.tempStorageId", listIdsSRPD));
				}
				criteria.add(disjunction);
				if (!filter.isShowDeleted()) {
					criteria.add(Restrictions.eq("deleted", false));
				}
				if (donorId != null) {
					criteria.add(Restrictions.eq("donor.id", donorId));
				}
			}
			return criteria;
		}
		/**
		 * Create DetachedCriteria for search Biomaterials
		 */
		/*private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, BiomaterialsFilter filter) {
			if (filter != null) {
				Disjunction disjunction = Restrictions.disjunction();
				String number = filter.getNumber();
				String operationNumber = filter.getOperationNumber();
				String operationRecepient = filter.getOperationRecepient();
				Integer statusId = filter.getStatusId();
				if (StringUtils.isNotEmpty(number)) {
					disjunction.add(Restrictions.ilike("number", number, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(operationNumber) || StringUtils.isNotEmpty(operationRecepient)) {
					criteria.createAlias("operation", "operation", CriteriaSpecification.LEFT_JOIN);
				}
				if (StringUtils.isNotEmpty(operationNumber)) {
					disjunction.add(Restrictions.ilike("operation.number", operationNumber, MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotEmpty(operationRecepient)) {
					disjunction.add(Restrictions.ilike("operation.recipient", operationRecepient, MatchMode.ANYWHERE));
				}
				criteria.add(disjunction);
				if (!filter.isShowDeleted()) {
					criteria.add(Restrictions.eq("deleted", false));
				}
				if (statusId != null) {
					criteria.add(Restrictions.eq("statusId", statusId));
				}
			}
			return criteria;
		}
		/**
		 * Старая версия создания DetachedCriteria
		 */
		private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter, String type, boolean showDeleted) {
			if (!showDeleted) {
	            criteria.add(Restrictions.eq("deleted", false));
	        }
			if (StringUtils.isNotEmpty(filter)) {
				Disjunction disjunction = Restrictions.disjunction();
				if (type.equals("Processing")) {
					disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
				}
		        criteria.add(disjunction);
			}
	        return criteria;
		}
		/**
		 * Create filter for creation DetachedCriteria for BiomaterialDonor
		 */
		private BiomaterialDonorsFilter createBiomaterialDonorsFilter(String pattern, boolean showDeleted) {
			BiomaterialDonorsFilter filter = new BiomaterialDonorsFilter();
			if (!showDeleted) {
				filter.setShowDeleted(showDeleted);
			}
 			if (StringUtils.isNotEmpty(pattern)) {
 				List<FieldFilterBean> fields = new ArrayList<FieldFilterBean>();
 				fields.add(FieldFilterBean.init("number", filter, CompareType.ILIKE));
 				fields.add(FieldFilterBean.init("factAddress", filter, CompareType.ILIKE));
 				fields.add(FieldFilterBean.init("infectiousStatus", filter, CompareType.ILIKE));
 				fields.add(FieldFilterBean.init("pregnancy", filter, CompareType.ILIKE));
 				fields.add(FieldFilterBean.init("commentary", filter, CompareType.ILIKE));
 				if (DonorHelper.USE_SRPD) {
 					filter.setFirstName(pattern);
 					filter.setMiddleName(pattern);
 					filter.setPassport(pattern);
 					filter.setInsuranceNumber(pattern);
 					filter.setInsuranceSeries(pattern);
 					filter.setEmployment(pattern);
 					filter.setWorkPhone(pattern);
 					filter.setPhone(pattern);
 					filter.setRegistrationAdress(pattern);
 				} else {
 					fields.add(FieldFilterBean.init("lastName", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("middleName", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("firstName", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("passportSeries", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("passportNumber", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("insuranceSeries", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("insuranceNumber", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("employment", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("workPhone", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("phone", filter, CompareType.ILIKE));
 					fields.add(FieldFilterBean.init("registrationAddress", filter, CompareType.ILIKE));
 				}
 				filter.getListFieldsDisjunction().add(fields);
			}
			return filter;
		}
		/**
		 * Create filter for creation DetachedCriteria for Operation
		 */
		private OperationsFilter createOperationsFilter(String pattern, boolean showDeleted) {
			OperationsFilter filter = new OperationsFilter();
			filter.setShowDeleted(showDeleted);
			if (StringUtils.isNotEmpty(pattern)) {
				List<FieldFilterBean> disjFields = new ArrayList<FieldFilterBean>();
				disjFields.add(FieldFilterBean.init("number", pattern, CompareType.ILIKE));
				disjFields.add(FieldFilterBean.init("recipient", pattern, CompareType.ILIKE));
				disjFields.add(FieldFilterBean.init("idNumber", pattern, CompareType.ILIKE));
				if (DonorHelper.USE_SRPD) {
					filter.setLastName(pattern);
					filter.setFirstName(pattern);
					filter.setMiddleName(pattern);
				} else {
					filter.getListAlias().add(AliasFilterBean.initAlias("donor", CriteriaSpecification.LEFT_JOIN));
					disjFields.add(FieldFilterBean.init("donor.lastName", filter, CompareType.ILIKE));
 					disjFields.add(FieldFilterBean.init("donor.middleName", filter, CompareType.ILIKE));
 					disjFields.add(FieldFilterBean.init("donor.firstName", filter, CompareType.ILIKE));
				}
				filter.getListFieldsDisjunction().add(disjFields);
			}
			return filter;
		}
		/**
		 * Create filter for creation DetachedCriteria for Biomaterial
		 */
		private BiomaterialsFilter createBiomaterialsFilter(String pattern, boolean showDeleted) {
			BiomaterialsFilter filter = new BiomaterialsFilter();
			if (!showDeleted) {
				filter.setShowDeleted(showDeleted);
			}
			filter.setShowDeleted(showDeleted);
			if (StringUtils.isNotEmpty(pattern)) {
				filter.getListAlias().add(AliasFilterBean.initAlias("operation", CriteriaSpecification.LEFT_JOIN));
				List<FieldFilterBean> disjFields = new ArrayList<FieldFilterBean>();
				disjFields.add(FieldFilterBean.init("number", pattern, CompareType.ILIKE));
				disjFields.add(FieldFilterBean.init("operation.recipient", pattern, CompareType.ILIKE));
				disjFields.add(FieldFilterBean.init("operation.number", pattern, CompareType.ILIKE));
			}
			return filter;
		}
}