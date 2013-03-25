package ru.efive.medicine.niidg.trfu.dao.medical;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

public class MedicalOperationDAOImpl extends GenericDAOHibernate<Document> {
	
	@Override
	protected Class<Document> getPersistentClass() {
		return Document.class;
	}
	
	public <T extends Document> T get(Class<T> clazz, Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Document> T save(Class<T> clazz, T document) {
		return (T) save(document);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Document> T update(Class<T> clazz, T document) {
		return (T) update(document);
	}
	
	public BiomaterialDonor getDonorByExternalId(Serializable externalId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BiomaterialDonor.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        List<BiomaterialDonor> list = getHibernateTemplate().findByCriteria(detachedCriteria, -1, -1);
        
        if (list != null && !list.isEmpty()) {
        	return list.get(0);
        }
        else {
        	return null;
        }
	}
	
	/*
	 * Доноры (Лечебный сегмент)
	 */
	
	@SuppressWarnings("unchecked")
	public List<BiomaterialDonor> findDonors(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BiomaterialDonor.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "BiomaterialDonor"), offset, count);
	}
	
	public long countDonors(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BiomaterialDonor.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "BiomaterialDonor"));
	}
	
	/*
	 * Лечебные процедуры
	 */
	
	@SuppressWarnings("unchecked")
	public List<Operation> findOperations(String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Operation.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Operation"), offset, count);
	}
	
	public long countOperations(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Operation.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "Operation"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Operation> findOperationsByDonor(BiomaterialDonor donor, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Operation.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (donor != null && donor.getId() > 0) {
        	detachedCriteria.add(Restrictions.eq("donor.id", donor.getId()));
        	
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Operation"), offset, count);
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
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Biomaterial"), offset, count);
	}
	
	public long countBiomaterials(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "Biomaterial"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Biomaterial> findBiomaterials(String filter, int statusId, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
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
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Biomaterial"), offset, count);
	}
	
	public long countBiomaterials(String filter, int statusId, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (statusId != 0) {
        	detachedCriteria.add(Restrictions.eq("statusId", statusId));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "Biomaterial"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Biomaterial> findBiomaterialsByOperation(Operation operation, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
        if (operation != null && operation.getId() > 0) {
        	detachedCriteria.add(Restrictions.eq("operation.id", operation.getId()));
        	
        	String[] ords = orderBy == null ? null : orderBy.split(",");
    		if (ords != null) {
    			if (ords.length > 1) {
    				addOrder(detachedCriteria, ords, orderAsc);
    			} else {
    				addOrder(detachedCriteria, orderBy, orderAsc);
    			}
    		}
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Biomaterial"), offset, count);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countBiomaterialsByOperation(Operation operation, String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Biomaterial.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (!showDeleted) {
			detachedCriteria.add(Restrictions.eq("deleted", false));
		}
		
		if (operation != null && operation.getId() > 0) {
			detachedCriteria.add(Restrictions.eq("operation.id", operation.getId()));
			
			return getCountOf(getSearchCriteria(detachedCriteria, filter, "Biomaterial"));
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
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }

		String[] ords = orderBy == null ? null : orderBy.split(",");
		if (ords != null) {
			if (ords.length > 1) {
				addOrder(detachedCriteria, ords, orderAsc);
			} else {
				addOrder(detachedCriteria, orderBy, orderAsc);
			}
		}
		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Processing"), offset, count);
	}
	
	public long countProcessings(String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
		return getCountOf(getSearchCriteria(detachedCriteria, filter, "Processing"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Processing> findProcessingsByBiomaterial(Biomaterial biomaterial, String filter, boolean showDeleted, int offset, int count, String orderBy, boolean orderAsc) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
        
        if (!showDeleted) {
            detachedCriteria.add(Restrictions.eq("deleted", false));
        }
        
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
    		return getHibernateTemplate().findByCriteria(getSearchCriteria(detachedCriteria, filter, "Processing"), offset, count);
        }
        else {
        	return Collections.emptyList();
        }
	}
	
	public long countProcessingsByBiomaterial(Biomaterial biomaterial, String filter, boolean showDeleted) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processing.class);
		detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		if (!showDeleted) {
			detachedCriteria.add(Restrictions.eq("deleted", false));
		}
		
		if (biomaterial != null && biomaterial.getId() > 0) {
			detachedCriteria.add(Restrictions.eq("biomaterial.id", biomaterial.getId()));
			
			return getCountOf(getSearchCriteria(detachedCriteria, filter, "Processing"));
		}
		else {
			return 0;
		}
	}
	
	
	private DetachedCriteria getSearchCriteria(DetachedCriteria criteria, String filter, String type) {
		if (StringUtils.isNotEmpty(filter)) {
			Disjunction disjunction = Restrictions.disjunction();
			if (type.equals("BiomaterialDonor")) {
				disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("lastName", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("middleName", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("firstName", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("passportSeries", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("passportNumber", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("insuranceSeries", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("insuranceNumber", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("employment", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("workPhone", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("phone", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("registrationAddress", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("factAddress", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("infectiousStatus", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("pregnancy", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("commentary", filter, MatchMode.ANYWHERE));
			}
			else if (type.equals("Operation")) {
				disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.ilike("recipient", filter, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.ilike("ibNumber", filter, MatchMode.ANYWHERE));
				criteria.createAlias("donor", "donor", CriteriaSpecification.LEFT_JOIN);
		        disjunction.add(Restrictions.ilike("donor.lastName", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("donor.middleName", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("donor.firstName", filter, MatchMode.ANYWHERE));
			}
			else if (type.equals("Biomaterial")) {
				disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
				criteria.createAlias("operation", "operation", CriteriaSpecification.LEFT_JOIN);
		        disjunction.add(Restrictions.ilike("operation.number", filter, MatchMode.ANYWHERE));
		        disjunction.add(Restrictions.ilike("operation.recipient", filter, MatchMode.ANYWHERE));
			}
			else if (type.equals("Processing")) {
				disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
			}
	        criteria.add(disjunction);
		}
        return criteria;
	}
}