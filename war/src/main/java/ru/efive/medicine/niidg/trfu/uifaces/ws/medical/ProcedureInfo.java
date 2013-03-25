package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedureInfo", propOrder = {"id", "operationType", "divisionId", "ibNumber", "planDate", "registrationDate", "attendingPhysicianId", "attendingPhysicianLastName", "attendingPhysicianFirstName", "attendingPhysicianMiddleName"} )
@XmlRootElement(name = "ProcedureInfo")
public class ProcedureInfo implements java.io.Serializable {
	
	@XmlElement(name = "id")
    private Integer id;
    @XmlElement(name = "operationType")
    private Integer operationType;
    @XmlElement(name = "divisionId")
    private Integer divisionId;
    @XmlElement(name = "ibNumber")
    private String ibNumber;
    @XmlElement(name = "planDate")
    private Date planDate;
    @XmlElement(name = "registrationDate")
    private Date registrationDate;
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
	public Integer getOperationType() {
		return operationType;
	}
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
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
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getAttendingPhysicianFirstName() {
		return attendingPhysicianFirstName;
	}
	public void setAttendingPhysicianFirstName(String attendingPhysicianFirstName) {
		this.attendingPhysicianFirstName = attendingPhysicianFirstName;
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
	public String getAttendingPhysicianMiddleName() {
		return attendingPhysicianMiddleName;
	}
	public void setAttendingPhysicianMiddleName(String attendingPhysicianMiddleName) {
		this.attendingPhysicianMiddleName = attendingPhysicianMiddleName;
	}
	
}