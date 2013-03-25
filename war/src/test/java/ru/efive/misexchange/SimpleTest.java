package ru.efive.misexchange;

import misexchange.PharmacyExchangeUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.efive.medicine.niidg.trfu.context.NoPropertyStoragePathException;
import ru.efive.medicine.niidg.trfu.dao.BloodDonationRequestDAOImpl;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyTypeNotSupported;
import ru.efive.medicine.niidg.trfu.util.ApplicationHelper;


import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class SimpleTest {
    private PharmacyExchangeUtils utils;
    private ClassPathXmlApplicationContext ctx;

    @Before
    public void init() throws IOException, PropertyTypeNotSupported, ParseException, NoPropertyStoragePathException, JAXBException {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        utils = new PharmacyExchangeUtils();
        ApplicationPropertiesHolder propertiesHolder = new ApplicationPropertiesHolder();
        propertiesHolder.init();
        utils.setPropertiesHolder(propertiesHolder);
    }
    
    //@Test
    public void testDrugLIstDownload() throws IOException, JAXBException {
        utils.downloadDrugList("drugs.txt");
    }

    public void testStorageList() throws IOException {
        utils.downloadStorageList("storages.txt");
    }

    //@Test
    public void testSystemCountChecking() {
        BloodSystemType systemType = new BloodSystemType();
        systemType.setCode("2450bf0b-8a08-11e1-a073-005056a41f97");
        systemType.setValue("антикоагулянт стерильный ,д/проведения процедур автомат. афереза: ACD-A (натрия цитрат 2,2%, глюкоза 2,45%, лимонная к-та 0,8%) в пластиковом контейнере объемом 500 мл, исполнения 426С");
        System.out.println(utils.checkBloodSystemCount(systemType, 1));
    }

    //@Test
    public void testCountDecrease() throws UnsupportedEncodingException, JAXBException {
        BloodDonationRequestDAOImpl donationRequestDAO = (BloodDonationRequestDAOImpl) ctx.getBean(ApplicationHelper.DONATION_DAO);

        //BloodSystemType systemType = new BloodSystemType();
        //systemType.setCode("2450bf0b-8a08-11e1-a073-005056a41f97");
        //systemType.setValue("антикоагулянт стерильный ,д/проведения процедур автомат. афереза: ACD-A (натрия цитрат 2,2%, глюкоза 2,45%, лимонная к-та 0,8%) в пластиковом контейнере объемом 500 мл, исполнения 426С");
        utils.decreaseBloodSystemsCount(donationRequestDAO.get(779));
    }
}
