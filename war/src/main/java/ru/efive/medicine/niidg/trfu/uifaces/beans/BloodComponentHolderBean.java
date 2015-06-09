package ru.efive.medicine.niidg.trfu.uifaces.beans;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import ru.efive.crm.dao.ContragentDAOHibernate;
import ru.efive.crm.data.Contragent;
import ru.efive.dao.sql.entity.enums.RoleType;
import ru.efive.dao.sql.wf.entity.HistoryEntry;
import ru.efive.medicine.niidg.trfu.dao.BloodComponentDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.BloodSystemDAOImpl;
import ru.efive.medicine.niidg.trfu.dao.DictionaryDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Anticoagulant;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodComponentType;
import ru.efive.medicine.niidg.trfu.data.dictionary.Classifier;
import ru.efive.medicine.niidg.trfu.data.entity.*;
import ru.efive.medicine.niidg.trfu.uifaces.beans.admin.PropertiesEditorBean;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;
import ru.efive.uifaces.bean.AbstractDocumentHolderBean;
import ru.efive.uifaces.bean.FromStringConverter;
import ru.efive.uifaces.bean.ModalWindowHolderBean;
import ru.efive.wf.core.ActionResult;
import ru.efive.wf.core.activity.EditableProperty;
import ru.efive.wf.core.util.EngineHelper;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;

@Named("bloodComponent")
@ConversationScoped
public class BloodComponentHolderBean extends AbstractDocumentHolderBean<BloodComponent, Integer> {

    private static final long serialVersionUID = 1L;
    private Donor donor;
    private BloodDonationRequest donation;
    private int expirationDays;
    private boolean haveConsumableMaterial = false;
    private boolean needRefreshVirusinactivationList = false;
    @Inject
    @Named("virusinactivationComponentList")
    private VirusinactivationBloodComponetsBean virusinactivationComponentList;
    @Inject
    @Named("propertiesRedactorBean")
    private transient PropertiesEditorBean propertiesEditorBean;
    @Inject
    @Named("sessionManagement")
    private transient SessionManagementBean sessionManagement = new SessionManagementBean();

    @Inject
    @Named("purchasedBloodComponentRegistration")
    private transient PurchasedBloodComponentRegistrationBean purchasedConfig;

    private ProcessorModalBean processorModal = new ProcessorModalBean() {
        @Override
        protected void doInit() {
            if (getDocumentId() == null || getDocumentId() == 0) {
                saveNewDocument();
            }
            setProcessedData(getDocument());
        }

        @Override
        protected void doPostProcess(ActionResult actionResult) {
            BloodComponent component = (BloodComponent) actionResult.getProcessedData();
            if (getSelectedAction().isHistoryAction()) {
                Set<HistoryEntry> history = component.getHistory();
                if (history == null) {
                    history = new HashSet<HistoryEntry>();
                }
                history.add(getHistoryEntry());
                component.setHistory(history);
            }
            System.out.println("commentary: " + getHistoryEntry().getCommentary());
            setDocument(component);
            BloodComponentHolderBean.this.save();
        }

        @Override
        protected void doProcessException(ActionResult actionResult) {
            if (getSelectedAction() != null) {
                for (EditableProperty property : getSelectedAction().getProperties()) {
                    if (property.getName().equals(EngineHelper.PROP_WF_RESULT_DESCRIPTION) && property.getValue() != null) {
                        setActionResult(property.getValue().toString());
                    }
                }
            }
        }
    };
    private VirusinaktivationModalBean virusinaktivationModal = new VirusinaktivationModalBean() {

        public void perfomVirusinaktivation() {
            System.out.println("VirusInactivation");
            if (isValid()) {
                BloodComponentDAOImpl dao = sessionManagement.getDAO(
                        BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO
                );
                BloodComponent bloodComponent = getDocument();
                bloodComponent.setPreInactivatedVolume(bloodComponent.getVolume());
                bloodComponent.setVolume(Integer.parseInt(getVolume()));
                bloodComponent.setInactivated(true);

                Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
                HistoryEntry historyEntry = new HistoryEntry();
                historyEntry.setCreated(created);
                historyEntry.setStartDate(created);
                historyEntry.setOwner(sessionManagement.getLoggedUser());
                historyEntry.setDocType(bloodComponent.getType());
                historyEntry.setParentId(bloodComponent.getId());
                historyEntry.setActionId(ApplicationHelper.VIRUSINAKTIVATION_ID);
                historyEntry.setFromStatusId(bloodComponent.getStatusId());
                historyEntry.setToStatusId(bloodComponent.getStatusId());
                historyEntry.setCommentary("");
                bloodComponent.addToHistory(historyEntry);
                bloodComponent = dao.save(bloodComponent);
                setDocument(bloodComponent);
                doHide();
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Введите целое число!", ""
                        )
                );
            }
        }

        public void doHide() {
            virusinaktivationModal.setModalVisible(false);
        }
    };
    @Inject
    @Named("contragentList")
    private transient ContragentListHolderBean contragentList;
    @Inject
    @Named("dictionaryManagement")
    private transient DictionaryManagementBean dictionaryManagement;
    @Inject
    @Named("reports")
    private transient ReportsManagmentBean reportsManagement = new ReportsManagmentBean();
    private VirusinWithResuspensionSolutionModalBean virusinWithResuspensionSolutionModal = new VirusinWithResuspensionSolutionModalBean();
    private ContragentSelectModalHolder contragentSelectModal = new ContragentSelectModalHolder();
    private SplitModalHolder splitModal = new SplitModalHolder();

    @Override
    protected boolean deleteDocument() {
        boolean result = false;
        try {
            sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).delete(getDocument());
            result = true;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Невозможно удалить документ. Попробуйте повторить позже.", ""
                    )
            );
        }
        return result;
    }

    @Override
    protected Integer getDocumentId() {
        return getDocument().getId();
    }

    @Override
    protected void initNewDocument() {
        BloodComponent bloodComponent = new BloodComponent();
        bloodComponent.setStatusId(1);
        Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
        bloodComponent.setCreated(created);
        bloodComponent.setDoseCount(1);
        bloodComponent.setAuthor(sessionManagement.getLoggedUser());
        try {
            final String externalNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("purchasedNumber");
            //Закупленный ли КК
            if (StringUtils.isNotEmpty(externalNumber)) {
                try {
                    bloodComponent.setPurchased(true);
                    bloodComponent.setIrradiated(true);

                    final List<Classifier> preservativeTypes = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByValueAndCategory(Classifier.class, "SAGM", "Ресуспендирующий раствор");
                    if(!preservativeTypes.isEmpty()){
                        bloodComponent.setPreservative(preservativeTypes.get(0));
                        bloodComponent.setPreservativeVolume(100);
                    }

                    final List<Anticoagulant> anticoagulants =  sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findByValueAndCategory(Anticoagulant.class, "CPD", null);
                    if(!anticoagulants.isEmpty()){
                       bloodComponent.setAnticoagulant(anticoagulants.get(0));
                        bloodComponent.setAnticoagulantVolume(63.0d);
                    }

                    if(purchasedConfig != null){
                        bloodComponent.setInvoiceNumber(purchasedConfig.getInvoiceNumber());
                        bloodComponent.setReceivedDate(purchasedConfig.getReceivedDate());
                    }
                    // 2 символа - НОМЕР КК
                    final String componentNumber = StringUtils.right(externalNumber, 2);
                    bloodComponent.setNumber(componentNumber);
                    // 6 символов - номер донации
                    final String donationNumber = StringUtils.right(externalNumber, 8).substring(0, 6);
                    bloodComponent.setParentNumber(donationNumber);
                    // 2 символа ПРОПУСКАЕМ (год производства КК)
                    // 2 символа organisationCode
                    final String organizationCode  = StringUtils.right(externalNumber, 12).substring(0, 2);
                    // 2 символа stateCode
                    final String stateCode  = StringUtils.right(externalNumber, 14).substring(0, 2);
                    try {
                        if (StringUtils.isNotEmpty(stateCode) && StringUtils.isNotEmpty(organizationCode)) {
                            System.out.println("stateCode: " + stateCode + ", organizationCode: " + organizationCode);
                            Contragent contragent = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO)
                                    .getByStateAndOrganizationCode(Integer.parseInt(stateCode), Integer.parseInt(organizationCode));
                            if (contragent != null) {
                                bloodComponent.setMaker(contragent);
                            }
                        }
                    } catch (NumberFormatException e){
                        System.out.println("Cannot parse externalNumber Organisation part to Integers : "+e.getMessage());
                    }
                    //Добавление результатов тестов (иммуносерология)
                    final List<AnalysisType> allAnalysisTypes = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO)
                            .findAnalysisTypes("Иммуносерология", false);

                    final List<Analysis> bctl = bloodComponent.getTestList();
                    if (bctl == null || bctl.isEmpty()) {
                        bloodComponent.setTestList(new ArrayList<Analysis>(6));
                    }
                    for (AnalysisType currentType : allAnalysisTypes) {
                        boolean exists = false;
                        for (Analysis currentTest : bloodComponent.getTestList()) {
                            if (currentTest.getType().equals(currentType)) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            final Analysis analysis = new Analysis();
                            analysis.setType(currentType);
                            analysis.setValue("-");
                            bloodComponent.getTestList().add(analysis);
                        }
                    }
                    if(bloodComponent.getFactEntry()== null){
                        final BloodDonationEntry currentFactEntry = new BloodDonationEntry();
                        currentFactEntry.setDonationType(sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodDonationTypes(false,null,false).get(0));
                        currentFactEntry.setDose(450);
                        bloodComponent.setFactEntry(currentFactEntry);
                    }

                } catch (Exception e) {
                    System.out.println("Некорректный номер закупленного компонента");
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Некорректный номер закупленного компонента", ""
                            )
                    );
                }
            } else {
                String fullNameContragent = (String) propertiesEditorBean.getSelectedProperties().getProperty("reports.institution.name");
                Contragent currentContragent = sessionManagement.getDAO(ContragentDAOHibernate.class, ApplicationHelper.CONTRAGENT_DAO).getByFullName(
                        fullNameContragent
                );
                if (currentContragent != null) {
                    bloodComponent.setMaker(currentContragent);
                }
                final String donationId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("donationId");
                if (StringUtils.isNotEmpty(donationId)) {
                    int id = Integer.parseInt(donationId);
                    bloodComponent.setDonationId(id);
                    BloodDonationRequest donation = sessionManagement.getDAO(BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO)
                            .get(id);
                    if (donation != null && donation.getId() == id) {
                        bloodComponent.setBloodGroup(donation.getDonor().getBloodGroup());
                        bloodComponent.setRhesusFactor(donation.getDonor().getRhesusFactor());
                        bloodComponent.setParentNumber(donation.getNumber());
                        bloodComponent.setDonationDate(donation.getFactDate());
                        bloodComponent.setProductionDate(donation.getCreated());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setCreated(created);
        historyEntry.setStartDate(created);
        historyEntry.setOwner(sessionManagement.getLoggedUser());
        historyEntry.setDocType(bloodComponent.getType());
        historyEntry.setParentId(bloodComponent.getId());
        historyEntry.setActionId(0);
        historyEntry.setFromStatusId(0);
        historyEntry.setToStatusId(1);
        historyEntry.setEndDate(created);
        historyEntry.setProcessed(true);
        historyEntry.setCommentary("");
        Set<HistoryEntry> history = new HashSet<HistoryEntry>();
        history.add(historyEntry);
        bloodComponent.setHistory(history);

        setDocument(bloodComponent);
    }

    public void handleDateSelect(SelectEvent event){
        final Date nd = (Date) event.getObject();
        final BloodComponent document = getDocument();
        document.setDonationDate(nd);
        document.setProductionDate(nd);
        Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
        if (document.getProductionDate() != null) {
            calendar.setTime(document.getProductionDate());
        }
        calendar.add(Calendar.DATE, 41);
        document.setExpirationDate(calendar.getTime());
    }

    @Override
    protected void initDocument(Integer id) {
        try {
            needRefreshVirusinactivationList = false;
            setDocument(sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO).get(id));
            if (getDocument() == null) {
                setState(STATE_NOT_FOUND);
            }

            // блок выполняется только при открытии КК из АРМ Вирусинактивация
            if (StringUtils.isNotEmpty(getBloodSystemId())) {
                haveConsumableMaterial = true;
                Integer bloodSystemId = Integer.valueOf(getBloodSystemId());
                BloodSystemDAOImpl bloodSystemDAO = sessionManagement.getDAO(
                        BloodSystemDAOImpl.class, ApplicationHelper.BLOOD_SYSTEM_DAO
                );
                BloodSystem bloodSystem = bloodSystemDAO.get(bloodSystemId);
                getDocument().setBloodSystem(bloodSystem);
                saveDocument();
                if (getDocument().getAdditionalVolume() == 0) {
                    if (getDocument().getDirAdditionalLiquor() != null) {
                        int additionalVolume = getDocument().getDirAdditionalLiquor().getAdditionalVolume();
                        getDocument().setAdditionalVolume(additionalVolume);
                    }
                }

                if (getDocument().getAdditionalLiquor() == null) {
                    if (getDocument().getDirAdditionalLiquor() != null) {
                        Classifier additionalLiquor = getDocument().getDirAdditionalLiquor().getAdditionalLiquor();
                        getDocument().setAdditionalLiquor(additionalLiquor);
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка при загрузке", ""));
            e.printStackTrace();
        }
    }

    @Override
    protected boolean saveNewDocument() {
        boolean result = false;
        try {
            BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
            BloodComponent bloodComponent = getDocument();

            if (bloodComponent.getExpirationDate() == null && expirationDays > 1) {
                Calendar calendar = Calendar.getInstance(ApplicationHelper.getLocale());
                if (bloodComponent.getProductionDate() != null) {
                    calendar.setTime(bloodComponent.getProductionDate());
                }
                calendar.add(Calendar.DATE, expirationDays - 1);
                bloodComponent.setExpirationDate(calendar.getTime());
                expirationDays = 0;
            }

            if (!validate()) {
                return false;
            }

            if (bloodComponent.getDonationId() != 0) {
                int count = new Long(dao.countComponentsByDonation(bloodComponent.getDonationId())).intValue() + 1;
                bloodComponent.setNumber(StringUtils.right("0" + count, 2));
                BloodDonationRequest donation = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(bloodComponent.getDonationId());
                if (donation != null) {
                    bloodComponent.setParentNumber(donation.getNumber());
                }
            }
            bloodComponent = dao.save(getDocument());
            if (bloodComponent == null) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""
                        )
                );
            } else {
                setDocument(bloodComponent);
                result = true;
            }
        } catch (Exception e) {
            result = false;
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Ошибка " + "при сохранении документа.", ""
                    )
            );
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected boolean saveDocument() {
        boolean result = false;
        try {
            BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);

            if (!validate()) {
                return false;
            }

            final BloodComponent bloodComponent = dao.update(getDocument());
            if (bloodComponent == null) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Невозможно сохранить документ. Попробуйте повторить позже.", ""
                        )
                );
            } else {
                setDocument(bloodComponent);
                result = true;
            }
        } catch (Exception e) {
            result = false;
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Ошибка " + "при сохранении документа.", ""
                    )
            );
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected FromStringConverter<Integer> getIdConverter() {
        return FromStringConverter.INTEGER_CONVERTER;
    }

    @Override
    public String edit() {
        final String result = super.edit();
        BloodComponent bloodComponent = getDocument();
        if (bloodComponent.isPurchased()) {
            //Добавление результатов тестов (иммуносерология + 5 тестов на вич, сифилис, гепатиты)
            final List<AnalysisType> allAnalysisTypes = sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO)
                    .findAnalysisTypes("Иммуносерология", false);
            allAnalysisTypes.addAll(
                    sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes(
                            "Гепатит B", "Донация", false
                    )
            );
            allAnalysisTypes.addAll(
                    sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes(
                            "Гепатит C", "Донация", false
                    )
            );
            allAnalysisTypes.addAll(
                    sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("ВИЧ 1", "Донация", false)
            );
            allAnalysisTypes.addAll(
                    sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes("ВИЧ 2", "Донация", false)
            );
            allAnalysisTypes.addAll(
                    sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findAnalysisTypes(
                            "Сифилис (RW)", "Донация", false
                    )
            );
            final List<Analysis> bctl = bloodComponent.getTestList();
            if (bctl == null || bctl.isEmpty()) {
                bloodComponent.setTestList(new ArrayList<Analysis>(11));
            }
            for (AnalysisType currentType : allAnalysisTypes) {
                boolean exists = false;
                for (Analysis currentTest : bloodComponent.getTestList()) {
                    if (currentTest.getType().equals(currentType)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    final Analysis analysis = new Analysis();
                    analysis.setType(currentType);
                    if (currentType.getCategory().equals("Донация")) {
                        analysis.setValue("Отрицательный");
                    } else {
                        analysis.setValue("-");
                    }
                    bloodComponent.getTestList().add(analysis);
                }
            }
            if(bloodComponent.getFactEntry()== null){
                final BloodDonationEntry currentFactEntry = new BloodDonationEntry();
                currentFactEntry.setDonationType(sessionManagement.getDAO(DictionaryDAOImpl.class, ApplicationHelper.DICTIONARY_DAO).findBloodDonationTypes(false,null,false).get(0));
                currentFactEntry.setDose(450);
                bloodComponent.setFactEntry(currentFactEntry);
            }
        }
        return result;
    }

    public boolean validate() {
        boolean result = true;
        try {
            BloodComponent bloodComponent = getDocument();
            if (bloodComponent != null) {
                if (bloodComponent.isPurchased() && StringUtils.isEmpty(bloodComponent.getNumber())) {
                    result = false;
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Не указан номер компонента", ""
                            )
                    );
                }
                if (bloodComponent.getComponentType() == null) {
                    result = false;
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Не указан тип компонента", ""
                            )
                    );
                }
                if (bloodComponent.getComponentType() != null && !bloodComponent.getComponentType().isLite() &&
                        bloodComponent.getExpirationDate() == null) {
                    result = false;
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR, "Не указана дата окончания срока хранения", ""
                            )
                    );
                }

            }
        } catch (Exception e) {
            result = false;
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Внутренняя ошибка", ""
                    )
            );
            e.printStackTrace();
        }
        return result;
    }

    public Donor getDonor() {
        try {
            if (getDocument().getDonationId() > 0 && donor == null) {
                donation = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(getDocument().getDonationId());
                if (donation != null) {
                    donor = donation.getDonor();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donor;
    }

    public BloodDonationRequest getDonation() {
        try {
            if (getDocument().getDonationId() > 0 && donation == null) {
                donation = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(getDocument().getDonationId());
                if (donation != null) {
                    donor = donation.getDonor();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donation;
    }

    public boolean isShowDonor() {
        return !getDocument().isPurchased() && (getDocument().getStatusId() == 2);
    }

    public boolean isLeukoConcentrate() {
        try {
            if (getDocument().getComponentType() != null && getDocument().getComponentType().getValue() != null) {
                if (StringUtils.containsIgnoreCase(
                        getDocument().getComponentType().getValue(), "Лейкоцитный " + "концентрат"
                )) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isInControl() {
        boolean result = false;
        try {
            result = getDocument().getAppointment() != null && getDocument().getAppointment().getTests() != null &&
                    getDocument().getAppointment().getTests().size() > 0 && sessionManagement.getCurrentRole().getRoleType()
                    .equals(RoleType.IN_CONTROL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void componentTypeListener(AjaxBehaviorEvent event) {
        final BloodComponent document = getDocument();
        final BloodComponentType componentType = document.getComponentType();
        if(componentType != null) {
            if(componentType.getHigherStorageTemperature() != null) {
                document.setHigherStorageTemperature(componentType.getHigherStorageTemperature().byteValue());
            }
            if(componentType.getLowerStorageTemperature() != null){
                document.setLowerStorageTemperature(componentType.getLowerStorageTemperature().byteValue());
            }
        }
    }

    public void inactivatedListener(AjaxBehaviorEvent event) {
        System.out.println("inactivated state change listener");
        if (getDocument().getPreInactivatedVolume() == 0) {
            System.out.println("Pre inactivated volume - " + getDocument().getVolume());
            getDocument().setPreInactivatedVolume(getDocument().getVolume());
        }
    }

    public ProcessorModalBean getProcessorModal() {
        return processorModal;
    }

    public boolean isProcessingAvailable() {
        boolean result = true;
        try {
            if (getDocument().getDonationId() != 0) {
                BloodDonationRequest request = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(getDocument().getDonationId());
                if (request != null && request.getStatusId() == 21 && getDocument().getComponentType() != null &&
                        !getDocument().getComponentType().isLite()) {
                    result = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isErComponent() {
        boolean result = false;
        try {
            if (getDocument().getDonationId() != 0) {
                BloodDonationRequest request = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(getDocument().getDonationId());
                if (request != null && request.getStatusId() > 3 && getDocument().getComponentType() != null &&
                        (StringUtils.contains(getDocument().getComponentType().getValue(), "Эритроцитная") || StringUtils.contains(
                                getDocument().getComponentType().getValue(), "Эритроцитарная"
                        ))) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isPersonificationAvailable() {
        boolean result = false;
        try {
            if (getDocument().getComponentType() != null && !getDocument().getComponentType().getValue().toLowerCase().contains("ауто")) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isRectification() {
        boolean result = false;
        try {
            if (getDocument().getDonationId() > 0) {
                BloodDonationRequest donation = sessionManagement.getDAO(
                        BloodDonationRequestDAOImpl.class, ApplicationHelper.DONATION_DAO
                ).get(getDocument().getDonationId());
                if (donation != null && donation.getStatusId() == 21) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isHaveConsumableMaterial() {
        return haveConsumableMaterial;
    }

    public String getConsumableMaterialLot() {
        return getDocument().getBloodSystem().getSystemLot();
    }

    public String getConsumableMaterialType() {
        return getDocument().getBloodSystem().getType().getValue();
    }

    public Date getConsumableMaterialExpirationDate() {
        return getDocument().getBloodSystem().getExpirationDate();
    }

    private String getBloodSystemId() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bloodSystemId");
    }

    public void previewBigLabel() {
        try {
            if (getDocument().getBloodGroup() == null || StringUtils.isEmpty(
                    getDocument().getBloodGroup().getValue()
            ) ||
                    StringUtils.containsIgnoreCase(getDocument().getBloodGroup().getValue(), "не определен")) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Не " + "указана группа крови", ""
                        )
                );
                return;
            }
            if (getDocument().getRhesusFactor() == null || StringUtils.isEmpty(
                    getDocument().getRhesusFactor().getValue()
            ) ||
                    StringUtils.containsIgnoreCase(getDocument().getRhesusFactor().getValue(), "не определен")) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_ERROR, "Не " + "указан резус-фактор", ""
                        )
                );
                return;
            }
            Map<String, String> requestProperties = new HashMap<String, String>();
            if(getDocument().isPurchased() && getDocument().getMaker().getId() == 13){
                requestProperties.put("reportName", "BigBarcode4JReport_fmba.jrxml");
            } else {
                requestProperties.put("reportName", "BigBarcode4JReport.jrxml");
            }
            requestProperties.put("docId", Integer.toString(getDocument().getId()));
            requestProperties.put("docType", getDocument().getType());
            if (donor != null) {
                requestProperties.put("donorId", Integer.toString(donor.getId()));
            }
            requestProperties.put(
                    "transfusiologistFullName", sessionManagement.getLoggedUser() == null ? "" : sessionManagement.getLoggedUser().getDescription()
            );
            reportsManagement.sqlPrintReportByRequestParams(requestProperties);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Ошибка " + "при формировании этикетки компонента", ""
                    )
            );
            e.printStackTrace();
        }
    }

    public boolean isExpeditionEditAvailable() {
        boolean result = true;
        try {
            if (sessionManagement.getCurrentRole().getRoleType().equals(RoleType.EXPEDITION) && getDocument().getStatusId() != 1) {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isRefreshVirusinactivationList() {
        return needRefreshVirusinactivationList;
    }

    /**
     * Метод возвращает модальное окно Вирусинактивации
     *
     * @return VirusinaktivationModalBean
     */
    public VirusinaktivationModalBean getVirusinaktivationModal() {
        return virusinaktivationModal;
    }

    public VirusinWithResuspensionSolutionModalBean getVirusinWithResuspensionSolutionModal() {
        return virusinWithResuspensionSolutionModal;
    }

    public boolean isVirusinaktivation() {
        boolean result = false;
        if (processorModal.getSelectedAction() != null) {
            if (processorModal.getSelectedAction().getName().equals(ApplicationHelper.VIRUSINAKTIVATION)) {
                result = true;
            }
        }
        return result;
    }

    public ContragentSelectModalHolder getContragentSelectModal() {
        return contragentSelectModal;
    }

    public SplitModalHolder getSplitModal() {
        return splitModal;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(int expirationDays) {
        this.expirationDays = expirationDays;
    }

    public String getInstitutionName() {
        String result;
        result = (String) propertiesEditorBean.getSelectedProperties().getExtendedProperties().get("reports.institution.name").getObjectValue();
        return result;
    }

    public class ContragentSelectModalHolder extends ModalWindowHolderBean {
        private static final long serialVersionUID = 686982749044631899L;
        private Contragent contragent;

        public ContragentListHolderBean getContragentList() {
            return contragentList;
        }

        public void setContragentList(ContragentListHolderBean contragentList_) {
            contragentList = contragentList_;
        }

        public void select(Contragent contragent) {
            this.contragent = contragent;
        }

        public boolean selected(Contragent contragent) {
            return this.contragent != null && this.contragent.equals(contragent);
        }        @Override
        protected void doShow() {
            List<Contragent> contragents = contragentList.getDocuments();
            for (Contragent contragent : contragents) {
                if (contragent.getFullName().equals(getInstitutionName())) {
                    contragents.remove(contragent);
                    break;
                }
            }
        }

        @Override
        protected void doSave() {
            super.doSave();
            if (contragent != null) {
                getDocument().setMaker(contragent);
            }
        }

        @Override
        protected void doHide() {
            contragentList.setFilter("");
            contragentList.markNeedRefresh();
            contragent = null;
        }




    }

    public class SplitModalHolder extends ModalWindowHolderBean {

        private static final long serialVersionUID = -5600215387042330919L;
        private List<VolumeEntry> volumeList = new ArrayList<VolumeEntry>();
        private boolean invalid = false;
        private String message = "";

        public List<VolumeEntry> getVolumeList() {
            return volumeList;
        }        @Override
        protected void doShow() {
            super.doShow();
            invalid = false;
            message = "";
            volumeList = new ArrayList<VolumeEntry>();
            volumeList.add(new VolumeEntry(0));
            volumeList.add(new VolumeEntry(0));
        }

        public void setVolumeList(List<VolumeEntry> volumeList) {
            this.volumeList = volumeList;
        }

        public void addVolumeEntry() {
            volumeList.add(new VolumeEntry(0));
        }

        @Override
        protected void doSave() {
            super.doSave();
        }

        public void validateAndCompose() {
            int sum = 0;
            for (VolumeEntry volume : volumeList) {
                if (volume.getVolume() == 0) {
                    invalid = true;
                    message = "Объем нового компонента не может быть 0";
                    return;
                } else if (volume.getVolume() < 0) {
                    invalid = true;
                    message = "Объем нового компонента не может быть отрицательным";
                    return;
                } else {
                    sum += volume.getVolume();
                }
            }
            if (sum == getDocument().getVolume()) {
                //TODO тикет на дубликаты номеров при разделении
                final Date splitDate = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();
                BloodComponentDAOImpl dao = sessionManagement.getDAO(
                        BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO
                );
                for (VolumeEntry volume : volumeList) {
                    BloodComponent component = getDocument().cloneComponent();
                    component.setId(0);
                    final double proportion = ((double) volume.getVolume()) / getDocument().getVolume();
                    if (getDocument().getAnticoagulantVolume() > 0) {
                        double v = roundTwoDecimals(2, getDocument().getAnticoagulantVolume() * proportion);
                        System.out.println("Объем антикоагулянта разделенного компонента: " + v);
                        component.setAnticoagulantVolume(v);
                    }
                    if (getDocument().getDoseCount() > 0) {
                        double v = roundTwoDecimals(2, getDocument().getDoseCount() * proportion);
                        System.out.println("Количество доз разделенного компонента: " + v);
                        component.setDoseCount(v);
                    }
                    if (getDocument().getCellCount() > 0) {
                        double v = roundTwoDecimals(2, getDocument().getCellCount() * proportion);
                        System.out.println("Количество клеток разделенного компонента: " + v);
                        component.setCellCount(v);
                    }
                    if (getDocument().getPreservativeVolume() > 0) {
                        int v = roundToInteger(getDocument().getPreservativeVolume() * proportion);
                        System.out.println("Объем ресуспендирующего раствора разделенного компонента: " + v);
                        component.setPreservativeVolume(v);
                    }
                    component.setVolume(volume.getVolume());
                    component.setSplit(true);
                    component.setSplitVolume(getDocument().getVolume());
                    component.setSplitDate(splitDate);
                    component.setPreInactivatedVolume(0);

                    HistoryEntry newEntry = new HistoryEntry();
                    newEntry.setActionId(3);
                    newEntry.setFromStatusId(1);
                    newEntry.setToStatusId(component.getStatusId());
                    newEntry.setCreated(splitDate);
                    newEntry.setStartDate(splitDate);
                    newEntry.setOwner(sessionManagement.getLoggedUser());
                    newEntry.setDocType(component.getType());
                    newEntry.setParentId(component.getId());
                    newEntry.setEndDate(splitDate);
                    newEntry.setProcessed(true);
                    StringBuilder sb = new StringBuilder("Получено путем разделения из компонента объемом ").append(component.getSplitVolume())
                            .append(" ").append(
                                    new java.text.SimpleDateFormat(
                                            "dd.MM" +
                                                    ".yyyy" + " HH:mm"
                                    ).format(splitDate)
                            );
                    newEntry.setCommentary(sb.toString());

                    Set<HistoryEntry> history = new HashSet<HistoryEntry>();
                    history.add(newEntry);
                    component.setHistory(history);

                    dao.save(component);
                }

                final BloodComponent component = getDocument();
                component.setSplitDate(splitDate);

                //Создание записи в истории о разделении  исходного КК
                HistoryEntry newEntry = new HistoryEntry();
                newEntry.setActionId(100);
                newEntry.setFromStatusId(component.getStatusId());
                newEntry.setToStatusId(100);
                newEntry.setCreated(splitDate);
                newEntry.setStartDate(splitDate);
                newEntry.setOwner(sessionManagement.getLoggedUser());
                newEntry.setDocType(component.getType());
                newEntry.setParentId(component.getId());
                newEntry.setEndDate(splitDate);
                newEntry.setProcessed(true);
                StringBuilder sb = new StringBuilder("Компонент разделен ").append(
                        new java.text.SimpleDateFormat(
                                "dd" + ".MM.yyyy HH:mm"
                        ).format(splitDate)
                ).append(" ").append("на ").append(volumeList.size()).append(" КК (");
                final Iterator<VolumeEntry> volumeEntryIterator = volumeList.iterator();
                while (volumeEntryIterator.hasNext()) {
                    sb.append(volumeEntryIterator.next().getVolume());
                    if (volumeEntryIterator.hasNext()) {
                        sb.append(" мл, ");
                    } else {
                        sb.append(" мл)");
                    }
                }
                newEntry.setCommentary(sb.toString());
                //Добавление записи в историю
                component.addToHistory(newEntry);
                component.setStatusId(100);
                setDocument(dao.save(component));

                hide();
            } else {
                invalid = true;
                message = "Итоговый объем компонентов не совпадает с исходным";
            }
        }

        private double roundTwoDecimals(int decimalPlaces, double d) {
            BigDecimal bdTest = new BigDecimal(d);
            bdTest = bdTest.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
            return bdTest.doubleValue();
        }

        private int roundToInteger(double d) {
            return new BigDecimal(d).intValue();
        }

        public boolean isInvalid() {
            return invalid;
        }

        public String getMessage() {
            return message;
        }






    }

    public class VolumeEntry {

        private int volume;

        public VolumeEntry(int volume) {
            this.volume = volume;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }
    }

    public class VirusinWithResuspensionSolutionModalBean extends ModalWindowHolderBean {
        public VirusinWithResuspensionSolutionModalBean() {
            if (StringUtils.isNotEmpty(getBloodSystemId())) {
                setModalVisible(true);
            }
        }

        public void perfomVirusinaktivation() {
            BloodComponentDAOImpl dao = sessionManagement.getDAO(BloodComponentDAOImpl.class, ApplicationHelper.BLOOD_COMPONENT_DAO);
            BloodComponent bloodComponent = getDocument();

            Date created = Calendar.getInstance(ApplicationHelper.getLocale()).getTime();

            bloodComponent.setPreInactivatedVolume(bloodComponent.getVolume());
            bloodComponent.setVolume(getVirusinWithResuspensionSolutionVolume());
            bloodComponent.setInactivated(true);
            bloodComponent.setVirusInactivationDate(created);

            HistoryEntry historyEntry = new HistoryEntry();
            historyEntry.setCreated(created);
            historyEntry.setStartDate(created);
            historyEntry.setOwner(sessionManagement.getLoggedUser());
            historyEntry.setDocType(bloodComponent.getType());
            historyEntry.setParentId(bloodComponent.getId());
            historyEntry.setActionId(ApplicationHelper.VIRUSINAKTIVATION_ID);
            historyEntry.setFromStatusId(bloodComponent.getStatusId());
            historyEntry.setToStatusId(bloodComponent.getStatusId());
            historyEntry.setCommentary("");
            bloodComponent.addToHistory(historyEntry);
            bloodComponent = dao.save(bloodComponent);
            setDocument(bloodComponent);

            setModalVisible(false);
            needRefreshVirusinactivationList = true;
            virusinactivationComponentList.refresh();
        }

        public int getVirusinWithResuspensionSolutionVolume() {
            int preInactivatedVolume = getDocument().getVolume();
            int additionalVolume = getDocument().getAdditionalVolume();
            int result = preInactivatedVolume + additionalVolume;
            result = roundResult(result);
            return result;
        }

        private int roundResult(int value) {
            String str = String.valueOf(value);
            if (str.length() > 0) {
                str = str.substring(0, str.length() - 1) + "0";
            }
            value = Integer.parseInt(str);
            return value;
        }
    }
}