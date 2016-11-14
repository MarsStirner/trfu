package ru.efive.medicine.niidg.trfu.uifaces.beans.filters;

import org.apache.poi.ss.usermodel.Workbook;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.filters.AbstractFilter;
import ru.efive.medicine.niidg.trfu.uifaces.beans.SessionManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.print.FilterParametersPair;
import ru.efive.medicine.niidg.trfu.uifaces.beans.print.PrintHelper;
import ru.efive.uifaces.bean.AbstractDocumentListHolderBean;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static ru.bars.open.trfu.sql.dao.util.ApplicationDAONames.*;
public abstract class AbstractFilterableListHolderBean<T extends Serializable, F extends AbstractFilter<F>>
		extends AbstractDocumentListHolderBean<T> {
	private static final long serialVersionUID = 4901127386797242470L;
	
	/**
	 * Конструктор по умолчанию.
	 */
	public AbstractFilterableListHolderBean() {
		super();
		initFilters();
	}
	
	public void openResetFilterPanel() throws IOException {
		hideFilterPanel();
		if (getPagination() != null) {
			resetFilter();
		}
		String url = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("url");
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}

	@Override
	public Pagination initPagination() {
		return new Pagination(0, getTotalCount(), 100);
	}

	@Override
	protected List<T> loadDocuments() {
		return loadDocuments(getPagination().getOffset(), getPagination()
				.getPageSize());
	}

	@Inject
	@Named("sessionManagement")
	protected SessionManagementBean sessionManagement = new SessionManagementBean();

	/**
	 * Флаг предназначен для запоминания состояния панели с фильтром. Если флаг
	 * установлен в true, то панель отображается. Если флаг выставлен в false,
	 * то панель скрыта на странице.
	 */
	protected boolean filterPanelActive = false;

	/**
	 * Поле служит для хранения текущей информации введенной в панель с
	 * фильтром.
	 */
	protected F currentFilter = null;
	/**
	 * Поле служит для хранения фильтра, по которому осуществлялась последняя
	 * фильтрация данных.
	 */
	protected F storedFilter = null;

	public boolean isFilterPanelActive() {
		return filterPanelActive;
	}

	/**
	 * Обработчик нажатия кнопки "Скрыть фильтр".
	 */
	public void hideFilterPanel() {
		filterPanelActive = false;
	}

	/**
	 * Обработчик нажатия кнопки "Показать фильтр".
	 */
	public void showFilterPanel() {
		filterPanelActive = true;
	}

	/**
	 * Обработчик нажатия кнопки "Применить фильтр".
	 */
	public void applyFilter() {
		storedFilter.fillFrom(currentFilter);
		refresh();
	}

	/**
	 * Обработчик нажатия кнопки "Очистить фильтр".
	 */
	public void resetFilter() {
		clearFilter();
		refresh();
	}

	/**
	 * Обработчик нажатия кнопки "Обновить".
	 */
	public void pressRefresh() {
		currentFilter.fillFrom(storedFilter);
		refresh();
	}

	/**
	 * Перевод фильтра в начальное нулевое состояние.
	 */
	public void clearFilter() {
		currentFilter.clear();
		storedFilter.clear();
	}

	public F getCurrentFilter() {
		return currentFilter;
	}

	public F getCurrentFilterCopy() {
		F filter = createFilterInstance();
		filter.fillFrom(currentFilter);
		return filter;
	}
	
	public F getStoredFilter() {
		return storedFilter;
	}

	/**
	 * Получение набора непустых параметров в удобном виде для печати в две
	 * колонки.
	 * 
	 * @return набор непустых параметров для печати.
	 */
	public List<FilterParametersPair> getFilterParametersForPrinting() {
		List<FilterParameter> parameters = getNotNullFilterParameters();
		return PrintHelper.prepareParametersForPrinting(parameters);
	}

	/**
	 * Метод для удобного получения DAO по работе со справочной информацией.
	 * 
	 * @return DAO для работы со справочной ифнормацией.
	 */
	protected DictionaryDAOImpl getDictionaryDAO() {
		return sessionManagement.getDAO(DictionaryDAOImpl.class,
				DICTIONARY_DAO);
	}

	public abstract List<FilterParameter> getNotNullFilterParameters();

	/**
	 * Возвращает набор элементов по заданному смещению и количеству элементов
	 * на странице.
	 * 
	 * @param offset
	 *            смещение до первого элемента.
	 * @param pageSize
	 *            количество элементов на странице.
	 * @return подходящий набор элементов.
	 */
	public abstract List<T> loadDocuments(int offset, int pageSize, F filter);

	public List<T> loadDocuments(int offset, int pageSize) {
		return loadDocuments(offset, pageSize, storedFilter);
	}

	/**
	 * Инициация фильтров.
	 */
	protected void initFilters() {
		currentFilter = createFilterInstance();
		storedFilter = createFilterInstance();
	}
	
	/**
	 * Создание экземпляра фильтра.
	 * @return экземпляр фильтра.
	 */
	public abstract F createFilterInstance();

	/**
	 * Метод возвращает полный набор найденных элементов. Используется при
	 * печати списка элементов.
	 * 
	 * @return полный список доноров.
	 */
	public List<T> getDocumentsForPrinting() {
		return loadDocuments(-1, -1);
	}

	/**
	 * Формирование контента для excel.
	 * 
	 * @throws Exception
	 */
	public abstract void formXlsContent(Workbook wb, File logoFile)
			throws Exception;

	/**
	 * Имя представления.
	 * 
	 * @return
	 */
	public abstract String getFormTitle();

	/**
	 * Состав колонок таблицы.
	 * 
	 * @return
	 */
	public abstract List<String> getFormColumns();

	/**
	 * Формирование контента для word.
	 * 
	 * @throws Exception
	 */
	public abstract void formDocxContent(WordprocessingMLPackage pkg,
			File logoFile) throws Exception;

	public abstract int getTotalCount(F filter);
	
	@Override
	protected int getTotalCount() {
		return getTotalCount(storedFilter);
	}

}