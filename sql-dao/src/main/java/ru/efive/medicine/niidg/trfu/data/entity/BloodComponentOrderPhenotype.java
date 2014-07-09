package ru.efive.medicine.niidg.trfu.data.entity;

import ru.efive.dao.sql.entity.IdentifiedEntity;
import ru.efive.medicine.niidg.trfu.data.dictionary.AnalysisType;

import javax.persistence.*;

/**
 * Author: Upatov Egor <br>
 * Date: 08.07.2014, 16:18 <br>
 * Company: VTR <br>
 * Description: Требуемые фенотипы для заявления на КК <br>
 */
@Entity
@Table(name = "trfu_blood_component_order_phenotype")
public class BloodComponentOrderPhenotype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    private AnalysisType phenotype;

    @Column(name="value")
    // "+" \ "-"
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bloodComponentOrder_id")
    private BloodComponentOrderRequest bloodComponentOrderRequest;

    public BloodComponentOrderPhenotype(BloodComponentOrderRequest bloodComponentOrderRequest, AnalysisType phenotype, String value) {
        this.phenotype = phenotype;
        this.value = value;
        this.bloodComponentOrderRequest = bloodComponentOrderRequest;
    }

    public BloodComponentOrderPhenotype() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BloodComponentOrderPhenotype that = (BloodComponentOrderPhenotype) o;

        if (value != that.value) return false;
        if (!phenotype.equals(that.phenotype)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + phenotype.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnalysisType getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(AnalysisType phenotype) {
        this.phenotype = phenotype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BloodComponentOrderRequest getBloodComponentOrderRequest() {
        return bloodComponentOrderRequest;
    }

    public void setBloodComponentOrderRequest(BloodComponentOrderRequest bloodComponentOrderRequest) {
        this.bloodComponentOrderRequest = bloodComponentOrderRequest;
    }
}
