package ru.efive.medicine.niidg.trfu.uifaces.beans.properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.efive.medicine.niidg.trfu.uifaces.beans.IndexManagementBean;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyTypeNotSupported;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Singleton
@Startup
@Named("propertiesHolder")
@ApplicationScoped
public class ApplicationPropertiesHolder {
    private Map<String, ExtendedProperties> properties = new HashMap<String, ExtendedProperties>();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationPropertiesHolder.class);

    @Inject
    @Named("indexManagement")
    private transient IndexManagementBean contextManagementBean;

    @PostConstruct
    public void init() throws IllegalStateException {
        try {
            logger.info("CLASS LOADER find application.properties file in \'{}\'", getClass().getClassLoader().getResource("application.properties").getPath());
            properties.put("application", new ExtendedProperties(getClass().getClassLoader().getResource("application.properties").getPath()));
        }catch (Exception | PropertyTypeNotSupported e){
            throw new IllegalStateException(e);
        }

    }

    public Object getProperty(String fileAlias, String key) {
        return properties.get(fileAlias).getProperty(key);
    }

    public void setProperty(String fileAlias, String key, Object value) throws IOException {
        properties.get(fileAlias).setProperty(key, value);
        saveProperties(fileAlias);
    }

    public void saveProperties(String fileAlias) throws IOException {
            properties.get(fileAlias).save();
    }

    public List<String> getFileNames(){
        return new ArrayList<String>(properties.keySet());
    }

    public ExtendedProperties getExtendedProperties(String fileAlias) {
        return properties.get(fileAlias);
    }

}
