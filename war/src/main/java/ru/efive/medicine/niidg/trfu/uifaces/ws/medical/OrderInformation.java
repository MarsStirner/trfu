package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;


import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInformation", propOrder = {"id", "number","divisionId","ibNumber","diagnosis","componentTypeId","volume","doseCount","indication","transfusionType",
                                                 "planDate","registrationDate","attendingPhysicianId","attendingPhysicianLastName","attendingPhysicianFirstName", "attendingPhysicianMiddleName"} )
@XmlRootElement(name = "OrderInformation")
public class  OrderInformation implements java.io.Serializable{
    @XmlElement(name = "id")
    private Integer id;
    @XmlElement(name = "number")
    private String number;
    @XmlElement(name = "divisionId")
    private Integer divisionId;
    @XmlElement(name = "ibNumber")
    private String ibNumber;
    @XmlElement(name = "diagnosis")
    private String diagnosis;
    @XmlElement(name = "componentTypeId")
    private Integer componentTypeId;
    @XmlElement(name = "volume")
    private Integer volume;
    @XmlElement(name = "doseCount")
    private Double doseCount;
    @XmlElement(name = "indication")
    private String indication;
    @XmlElement(name = "transfusionType")
    private Integer transfusionType;
    @XmlElement(name = "planDate")
    private Date planDate;
    @XmlElement(name = "registrationDate")
    private Calendar registrationDate;
    @XmlElement(name = "attendingPhysicianId")
    private Integer attendingPhysicianId;
    @XmlElement(name = "attendingPhysicianLastName")
    private String attendingPhysicianLastName;
    @XmlElement(name = "attendingPhysicianFirstName")
    private String attendingPhysicianFirstName;
    @XmlElement(name = "attendingPhysicianMiddleName")
    private String attendingPhysicianMiddleName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public String getIbNumber() {
        return ibNumber;
    }

    public void setIbNumber(String ibNumber) {
        this.ibNumber = ibNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getComponentTypeId() {
        return componentTypeId;
    }

    public void setComponentTypeId(Integer componentTypeId) {
        this.componentTypeId = componentTypeId;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getDoseCount() {
        return doseCount;
    }

    public void setDoseCount(Double doseCount) {
        this.doseCount = doseCount;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public Integer getTransfusionType() {
        return transfusionType;
    }

    public void setTransfusionType(Integer transfusionType) {
        this.transfusionType = transfusionType;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Calendar registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getAttendingPhysicianId() {
        return attendingPhysicianId;
    }

    public void setAttendingPhysicianId(Integer attendingPhysicianId) {
        this.attendingPhysicianId = attendingPhysicianId;
    }

    public String getAttendingPhysicianLastName() {
        return attendingPhysicianLastName;
    }

    public void setAttendingPhysicianLastName(String attendingPhysicianLastName) {
        this.attendingPhysicianLastName = attendingPhysicianLastName;
    }

    public String getAttendingPhysicianFirstName() {
        return attendingPhysicianFirstName;
    }

    public void setAttendingPhysicianFirstName(String attendingPhysicianFirstName) {
        this.attendingPhysicianFirstName = attendingPhysicianFirstName;
    }

    public String getAttendingPhysicianMiddleName() {
        return attendingPhysicianMiddleName;
    }

    public void setAttendingPhysicianMiddleName(String attendingPhysicianMiddleName) {
        this.attendingPhysicianMiddleName = attendingPhysicianMiddleName;
    }
}
