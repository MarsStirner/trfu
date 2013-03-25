package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComponentType", propOrder = {"id", "value", "code"} )
@XmlRootElement(name = "ComponentType")
public class ComponentType {
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "value")
    private String value;

    @XmlElement(name = "code")
    private String code;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
