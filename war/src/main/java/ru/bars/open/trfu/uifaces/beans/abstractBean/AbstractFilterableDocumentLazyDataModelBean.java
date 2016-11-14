package ru.bars.open.trfu.uifaces.beans.abstractBean;



import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bars.open.trfu.sql.dao.GenericDAO;
import ru.bars.open.trfu.sql.dao.util.AuthorizationData;
import ru.efive.dao.sql.entity.IdentifiedEntity;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 31.03.2015, 16:35 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
public abstract class AbstractFilterableDocumentLazyDataModelBean<T extends IdentifiedEntity> extends LazyDataModel<T> implements Serializable {
    private String filterString;
    private Map<String, Object> filter;
    private AuthorizationData authData;
    private GenericDAO<T> dao;
    private Logger logger;
    private int logID;

    protected void initLazyModel(final GenericDAO<T> dao, final AuthorizationData authData, final Logger logger, final int logID) {
        this.dao = dao;
        this.authData = authData;
        this.logger = logger != null ? logger : LoggerFactory.getLogger("LAZY_DM_FILTERABLE");
        this.logID = logID;
        this.filter = new HashMap<>(0);
    }


    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(final String filterString) {
        this.filterString = filterString;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(final Map<String, Object> filter) {
        this.filter = filter;
    }

    public void clearFilters() {
        filter.clear();
        filterString = null;
    }

    @Override
    public List<T> load(
            int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters
    ) {
        removeEmptyEntries(filter);
        logger.debug(
                "#{} Call load interval[{}-{}], order[{}-{}], filter[{}], filterMap={}",
                logID,
                first,
                first + pageSize,
                sortField,
                sortOrder,
                filterString,
                filter
        );
        final long totalCount = dao.countByFilters(authData, filterString, filter, false);
        logger.debug("#{} Total count = {}", logID, totalCount);
        setRowCount((int) totalCount);
        if (totalCount < first) {
            first = 0;
        }
        final List<T> resultList = dao.getByFilters(
                authData, filterString, filter, sortField, SortOrder.ASCENDING.equals(sortOrder), first, pageSize, false
        );
        logger.debug("#{} End. Founded {} records", logID, resultList.size());
        return resultList;
    }

    private void removeEmptyEntries(final Map<String, Object> filter) {
        final Iterator<Map.Entry<String, Object>> iter = filter.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = iter.next();
            final Object value = entry.getValue();
            if(value == null || StringUtils.EMPTY.equals(value)){
                iter.remove();
            }
        }
    }

    @Override
    public T getRowData(String rowKey) {
        if (StringUtils.isEmpty(rowKey)) {
            logger.error("#{} Try to get Item by empty ROW_KEY. Return NULL", logID);
            return null;
        }
        try {
            return dao.getForListView(NumberUtils.createInteger(rowKey));
        } catch (NumberFormatException e) {
            logger.error("#{} Try to get Item by non-integer ROW_KEY \'{}\'. Return NULL", logID, rowKey);
            return null;
        }
    }

    @Override
    public Object getRowKey(T item) {
        return item.getId();
    }


}
