package ru.efive.medicine.niidg.trfu.dao;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import ru.efive.dao.sql.dao.GenericDAOHibernate;
import ru.efive.medicine.niidg.trfu.data.entity.integration.ExternalAnalysisResult;

import java.util.List;

@org.springframework.transaction.annotation.Transactional
public class ExternalAnalysisResultDAOImpl extends GenericDAOHibernate<ExternalAnalysisResult> {

    @Override
    protected Class<ExternalAnalysisResult> getPersistentClass() {
        return ExternalAnalysisResult.class;
    }

    @SuppressWarnings("unchecked")
    public List<ExternalAnalysisResult> findDocuments(int offset, int count, String orderBy, boolean orderAsc) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        String[] ords = orderBy == null ? null : orderBy.split(",");
        if (ords != null) {
            if (ords.length > 1) {
                addOrder(detachedCriteria, ords, orderAsc);
            } else {
                addOrder(detachedCriteria, orderBy, orderAsc);
            }
        }
        return (List<ExternalAnalysisResult>) getHibernateTemplate().findByCriteria(detachedCriteria, offset, count);
    }

    public long countDocument() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(getPersistentClass());
        detachedCriteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);

        return getCountOf(detachedCriteria);
    }

    public List<ExternalAnalysisResult> getResultsByAppointmentId(int appointmentId) {
        try (final Session session = getHibernateTemplate().getSessionFactory().openSession()) {
            final List<ExternalAnalysisResult> results = (List<ExternalAnalysisResult>)
                    session.createNativeQuery("CALL get_external_analysis_values(:appointmentId)")
                            .addEntity(ExternalAnalysisResult.class)
                            .setParameter("appointmentId", appointmentId)
                            .list();

            return results;
        }


    }

}