package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;


import javax.xml.bind.annotation.*;
import java.util.Calendar;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInformation", propOrder = {"id", "number", "divisionId", "ibNumber", "diagnosis", "componentTypeId", "volume", "doseCount", "indication", "transfusionType", "planDate", "registrationDate", "attendingPhysicianId", "attendingPhysicianLastName", "attendingPhysicianFirstName", "attendingPhysicianMiddleName", "bloodGroupId", "rhesusFactorId", "lastModifyDateTime"})
@XmlRootElement(name = "OrderInformation")
public class OrderInformation implements java.io.Serializable {
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
    @XmlElement(name = "bloodGroupId")
    private Integer bloodGroupId;
    @XmlElement(name = "rhesusFactorId")
    private Integer rhesusFactorId;
    @XmlElement(name = "lastModifyDateTime")
    private Date lastModifyDateTime;


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

    public Integer getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(final Integer bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public Integer getRhesusFactorId() {
        return rhesusFactorId;
    }

    public void setRhesusFactorId(final Integer rhesusFactorId) {
        this.rhesusFactorId = rhesusFactorId;
    }

    public Date getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(final Date lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderInformation{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append(", divisionId=").append(divisionId);
        sb.append(", ibNumber='").append(ibNumber).append('\'');
        sb.append(", diagnosis='").append(diagnosis).append('\'');
        sb.append(", componentTypeId=").append(componentTypeId);
        sb.append(", volume=").append(volume);
        sb.append(", doseCount=").append(doseCount);
        sb.append(", indication='").append(indication).append('\'');
        sb.append(", transfusionType=").append(transfusionType);
        sb.append(", planDate=").append(planDate);
        sb.append(", registrationDate=").append(registrationDate);
        sb.append(", attendingPhysicianId=").append(attendingPhysicianId);
        sb.append(", attendingPhysicianLastName='").append(attendingPhysicianLastName).append('\'');
        sb.append(", attendingPhysicianFirstName='").append(attendingPhysicianFirstName).append('\'');
        sb.append(", attendingPhysicianMiddleName='").append(attendingPhysicianMiddleName).append('\'');
        sb.append(", bloodGroupId=").append(bloodGroupId);
        sb.append(", rhesusFactorId=").append(rhesusFactorId);
        sb.append(", lastModifyDateTime=").append(lastModifyDateTime);
        sb.append('}');
        return sb.toString();
    }
}
