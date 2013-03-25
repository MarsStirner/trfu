package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatientCredentials", propOrder = {"id", "lastName", "firstName", "middleName", "birth", "bloodGroupId", "rhesusFactorId"} )
@XmlRootElement(name = "PatientCredentials")
public class PatientCredentials implements java.io.Serializable {
    @XmlElement(name = "id")
    private Integer id;
    @XmlElement(name = "lastName")
    private String lastName;
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "middleName")
    private String middleName;
    @XmlElement(name = "birth")
    private Date birth;
    @XmlElement(name = "bloodGroupId")
    private Integer bloodGroupId;
    @XmlElement(name = "rhesusFactorId")
    private Integer rhesusFactorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(Integer bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public Integer getRhesusFactorId() {
        return rhesusFactorId;
    }

    public void setRhesusFactorId(Integer rhesusFactorId) {
        this.rhesusFactorId = rhesusFactorId;
    }
}
