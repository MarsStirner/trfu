package ru.efive.medicine.niidg.trfu.uifaces.beans.properties;

import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.util.PropertyTypeNotSupported;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class ExtendedProperties {
    private Properties properties;
    private String path;
    private Map<String, ExtendedProperty> extendedProperties;

    public ExtendedProperties(String path) throws IOException, PropertyTypeNotSupported, ParseException {
        this.path = path;
        properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        extendedProperties = new LinkedHashMap<String, ExtendedProperty>();
        for (Map.Entry entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            int separator = key.lastIndexOf(".");
            String keyPrefix = null;
            String keySufix = null;
            if (separator != -1) {
                keyPrefix = key.substring(0, separator);
                keySufix = key.substring(separator + 1);
            }
            Boolean isSimple = !((keyPrefix != null) && (keySufix != null) && (keySufix.equals("value") || keySufix.equals("type") || keySufix.equals("pattern")|| keySufix.equals("alias")));
            if (!isSimple) {
                if (!extendedProperties.containsKey(keyPrefix)) {
                    extendedProperties.put(keyPrefix, new ExtendedProperty());
                    extendedProperties.get(keyPrefix).setSimple(false);
                    extendedProperties.get(keyPrefix).setName(keyPrefix);
                }

                if (keySufix.equals("value")) {
                    extendedProperties.get(keyPrefix).setStringValue(value);
                } else if (keySufix.equals("type")) {
                    extendedProperties.get(keyPrefix).setType(value);
                } else if (keySufix.equals("pattern")) {
                    extendedProperties.get(keyPrefix).setPattern(value);
                } else if (keySufix.equals("alias")) {
                    extendedProperties.get(keyPrefix).setAlias(value);
                }
            } else {
                if (!extendedProperties.containsKey(key)){
                    extendedProperties.put(key, new ExtendedProperty());
                    extendedProperties.get(key).setName(key);
                }
                extendedProperties.get(key).setStringValue(value);
                extendedProperties.get(key).setSimple(true);
            }
        }
        for (ExtendedProperty extendedProperty: extendedProperties.values()){
            if (extendedProperty.getType()==null)
                extendedProperty.setType("String");
        }
    }



    public String getPath() {
        return path;
    }

    public void save() throws IOException {
        for (String key : extendedProperties.keySet()) {
            if (!extendedProperties.get(key).getSimple())
                properties.setProperty(key + ".value", extendedProperties.get(key).getStringValue());
            else
                properties.setProperty(key, extendedProperties.get(key).getStringValue());
        }
        store();
    }

    public void store() throws IOException {
        properties.store(new OutputStreamWriter(new FileOutputStream(path),"UTF-8"), "");
    }

    public Object getProperty(String key) {
        return extendedProperties.get(key).getObjectValue();
    }

    public void setProperty(String key, Object value) {
        extendedProperties.get(key).setObjectValue(value);
        properties.setProperty(key, extendedProperties.get(key).getStringValue());
    }

    public List<String> getKeys() {
        return new ArrayList<String>(extendedProperties.keySet());
    }

    public Map<String, ExtendedProperty> getExtendedProperties() {
        return extendedProperties;
    }
}
