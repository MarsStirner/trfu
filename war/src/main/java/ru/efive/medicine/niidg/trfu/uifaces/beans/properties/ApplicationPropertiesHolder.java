package ru.efive.medicine.niidg.trfu.uifaces.beans.properties;

import ru.efive.medicine.niidg.trfu.uifaces.beans.IndexManagementBean;

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
    private Map<String, Boolean> isStatic = new HashMap<String, Boolean>();
    private String storagePath;

    @Inject
    @Named("indexManagement")
    private transient IndexManagementBean contextManagementBean;

    @PostConstruct
    public void init(){

//        File dir = new File(storagePath+"static/");
//        List<String> fileNames = Arrays.asList(dir.list(filter));
//        for (String fileName: fileNames) {
//            properties.put(fileName.split(".properties")[0], new ExtendedProperties(storagePath + "static/" + fileName));
//            isStatic.put(fileName.split(".properties")[0], true);
//        }
//
//        dir = new File(storagePath+"dynamic/");
//        fileNames = Arrays.asList(dir.list(filter));
//
//        for (String fileName: fileNames) {
//            properties.put(fileName.split(".properties")[0], new ExtendedProperties(storagePath + "dynamic/" + fileName));
//            isStatic.put(fileName.split(".properties")[0], false);
//        }
    }

    public Object getProperty(String fileAlias, String key) {
        return properties.get(fileAlias).getProperty(key);
    }

    public void setProperty(String fileAlias, String key, Object value) throws IOException {
        properties.get(fileAlias).setProperty(key, value);
        saveProperties(fileAlias);
    }

    public void saveProperties(String fileAlias) throws IOException {
        if (isStatic.get(fileAlias)){
            properties.get(fileAlias).save();
        } else {
            properties.get(fileAlias).save();
        }
    }

    public List<String> getFileNames(){
        return new ArrayList<String>(properties.keySet());
    }

    public ExtendedProperties getExtendedProperties(String fileAlias) {
        return properties.get(fileAlias);
    }

}
