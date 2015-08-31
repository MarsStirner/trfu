package ru.efive.medicine.niidg.trfu.uifaces.ws.medical;

import javax.xml.bind.annotation.*;

/**
 * Author: Upatov Egor <br>
 * Date: 01.08.2014, 14:53 <br>
 * Company: Korus Consulting IT <br>
 * Description: <br>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BloodPhenotype", propOrder = {"phenotype","value"} )
@XmlRootElement(name = "BloodPhenotype")
public class BloodPhenotype {
    @XmlElement(name = "phenotype")
    private String phenotype;
    @XmlElement(name = "value")
    private Boolean value;

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BloodPhenotype{");
        sb.append("phenotype='").append(phenotype).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
