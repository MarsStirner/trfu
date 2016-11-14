package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bars.open.trfu.sql.dao.DonorDaoImpl;
import ru.bars.open.trfu.uifaces.beans.abstractBean.AbstractFilterableDocumentLazyDataModelBean;
import ru.efive.medicine.niidg.trfu.data.entity.Donor;
import ru.efive.medicine.niidg.trfu.uifaces.beans.IndexManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.atomic.AtomicInteger;

@Named("donorFilterableList")
@ViewScoped
public class DonorFilterableListHolderBean extends AbstractFilterableDocumentLazyDataModelBean<Donor> {

    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(DonorFilterableListHolderBean.class);

	@Inject
	@Named("sessionManagement")
	private SessionManagementBean sessionManagement;

    @Inject
    @Named("indexManagement")
    private IndexManagementBean indexManagementBean;

    private int currentNumber;

	@PostConstruct
	public void init() {
        currentNumber = counter.incrementAndGet();
        logger.debug("#{} Created by [{}]", currentNumber, sessionManagement.getAuthData().getAuthorized().getDescriptionShort());
		initLazyModel(indexManagementBean.getContext().getBean(DonorDaoImpl.class), sessionManagement.getAuthData(), logger, currentNumber);
	}

    @PreDestroy
    public void destroy(){
        logger.debug("#{} Destroyed", currentNumber);
    }
}