package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureType", propOrder = {"id", "value"} )
@XmlRootElement(name = "ProcedureType")
public class ProcedureType {
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "value")
    private String value;

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
}
