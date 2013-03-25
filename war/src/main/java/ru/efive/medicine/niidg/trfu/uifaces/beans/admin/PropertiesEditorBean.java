package ru.efive.medicine.niidg.trfu.uifaces.beans.admin;

import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ApplicationPropertiesHolder;
import ru.efive.medicine.niidg.trfu.uifaces.beans.properties.ExtendedProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@Named("propertiesRedactorBean")
@SessionScoped
public class PropertiesEditorBean implements Serializable {
    @Inject
    @Named("propertiesHolder")
    private ApplicationPropertiesHolder propertiesHolder;

    private String selectedFile;
    private ExtendedProperties selectedProperties;
    private List<String> fileNames;

    @PostConstruct
    private void init(){
        fileNames = propertiesHolder.getFileNames();
        Collections.sort(fileNames);
        if (!fileNames.isEmpty()) {
            selectedFile = fileNames.get(0);
            selectedProperties = propertiesHolder.getExtendedProperties(selectedFile);
        }
    }

    public void processSelection(ActionEvent actionEvent){
        selectedProperties = propertiesHolder.getExtendedProperties(selectedFile);
    }

    public void processRedact(ActionEvent actionEvent) throws IOException {
        propertiesHolder.saveProperties(selectedFile);
    }


    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public ExtendedProperties getSelectedProperties() {
        return selectedProperties;
    }

    public void setSelectedProperties(ExtendedProperties selectedProperties) {
        this.selectedProperties = selectedProperties;
    }
}
