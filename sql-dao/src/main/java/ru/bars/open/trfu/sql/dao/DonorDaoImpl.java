package ru.bars.open.trfu.sql.dao;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;

import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 18.10.2016, 18:25 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class DonorDaoImpl extends GenericDAO<Donor> {

    public Logger logger = LoggerFactory.getLogger("DONOR_DAO");

    public static final String FILTER_MAP_KEY_NUMBER="NUMBER";

    @Autowired
    public void initSessionFactory(SessionFactory sessionFactory){
       setSessionFactory(sessionFactory);
    }

    /**
     * Получить самый простой критерий для отбора сущности, без лишних JOIN & FETCH
     *
     * @return критерий для сущностей с DISTINCT и нужными alias (with fetch strategy)
     */
    @Override
    public DetachedCriteria getSimplestCriteria() {
        return DetachedCriteria.forClass(Donor.class, "this").setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    }

    /**
     * Получить критерий для отбора сущности и их показа в расширенных списках
     *
     * @return критерий для сущностей с DISTINCT и нужными alias (with fetch strategy)
     */
    @Override
    public DetachedCriteria getListCriteria() {
        final DetachedCriteria result = getSimplestCriteria();
        result.createAlias("bloodGroup", "bloodGroup", CriteriaSpecification.LEFT_JOIN);
        result.createAlias("rhesusFactor", "rhesusFactor", CriteriaSpecification.LEFT_JOIN);
        result.createAlias("rejection", "rejection", CriteriaSpecification.LEFT_JOIN);
        result.createAlias("rejection.rejectionType", "rejection.rejectionType", CriteriaSpecification.LEFT_JOIN);
        result.createAlias("donorType", "donorType", CriteriaSpecification.LEFT_JOIN);
        return result;
    }

    /**
     * Получить критерий для отбора сущности с максимальным количеством JOIN & FETCH
     *
     * @return критерий для сущностей с DISTINCT и нужными alias (with fetch strategy)
     */
    @Override
    public DetachedCriteria getFullCriteria() {
        final DetachedCriteria result = getListCriteria();
        return result;
    }

    /**
     * Применитиь к текущим критериям огарничения сложного фильтра
     *
     * @param criteria текущий критерий, в который будут добавлены условия  (НЕ менее LIST_CRITERIA)
     * @param filters  сложный фильтр (карта)
     */
    @Override
    public void applyFilterMapCriteria(final DetachedCriteria criteria, final Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            logger.debug("FilterMapCriteria: null or empty. Skip.");
            return;
        }
        final Conjunction conjunction = Restrictions.conjunction();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (StringUtils.isEmpty(key)) {
                // Пропустить запись с пустым ключом
                logger.warn("FilterMapCriteria: skip empty key for \'{}\'", value);
                continue;
            } else if(value == null){
                // Пропустить запись с пустым значением
                logger.warn("FilterMapCriteria[{}]: skip empty value", key);
            }
            switch (key){
                case FILTER_MAP_KEY_NUMBER:
                    if(StringUtils.isNotEmpty((String) value)) {
                        conjunction.add(Restrictions.eq("this.number", StringUtils.leftPad((String) value, Donor.NUMBER_LENGTH, "0")));
                    }
                    break;
            }
        }
        criteria.add(conjunction);
    }

    /**
     * Производит поиск заданной строки в (по условию ИЛИ [дизъюнкция]):
     * заданных полях сущности
     *
     * @param criteria критерий отбора в который будет добавлено поисковое условие (НЕ менее LIST_CRITERIA)
     * @param filter   условие поиска
     */
    @Override
    public void applyFilterCriteria(final DetachedCriteria criteria, final String filter) {
        if (StringUtils.isNotEmpty(filter)) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.ilike("number", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("externalNumber", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("lastName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("middleName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("firstName", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("donorType.value", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("passportSeries", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("passportNumber", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("insuranceSeries", filter, MatchMode.ANYWHERE));
            disjunction.add(Restrictions.ilike("insuranceNumber", filter, MatchMode.ANYWHERE));
            criteria.add(disjunction);
        }
    }
}
