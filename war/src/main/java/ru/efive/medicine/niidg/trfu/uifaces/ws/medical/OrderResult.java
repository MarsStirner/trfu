package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;


import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResult", propOrder = {"requestId", "result", "number", "description", "orderComment"} )
@XmlRootElement(name = "OrderResult")
public class OrderResult implements java.io.Serializable {
    @XmlElement(name = "requestId")
    private Integer requestId;
    @XmlElement(name = "result")
    private Boolean result;
    @XmlElement(name = "number")
    private String number;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "orderComment")
    private String orderComment;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getOrderComment() {
		return orderComment;
	}

	public void setOrderComment(String orderComment) {
		this.orderComment = orderComment;
	}
}