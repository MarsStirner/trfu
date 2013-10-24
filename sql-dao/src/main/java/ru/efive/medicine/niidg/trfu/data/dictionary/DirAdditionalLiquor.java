package ru.efive.medicine.niidg.trfu.data.dictionary;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ru.efive.dao.sql.entity.IdentifiedEntity;

@Entity
@Table(name = "trfu_dir_additionalLiquor")
public class DirAdditionalLiquor extends IdentifiedEntity {
	private static final long serialVersionUID = -6070490785410797935L;
	 /**
     * Тип компонента крови
     */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private BloodComponentType componentType;
	/**
     * Тип раствора
     */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Classifier additionalLiquor;
	private int additionalVolume ;
	private boolean deleted;
	
	public BloodComponentType getComponentType() {
		return componentType;
	}
	public void setComponentType(BloodComponentType componentType) {
		this.componentType = componentType;
	}
	public Classifier getAdditionalLiquor() {
		return additionalLiquor;
	}
	public void setAdditionalLiquor(Classifier additionalLiquor) {
		this.additionalLiquor = additionalLiquor;
	}
	public int getAdditionalVolume() {
		return additionalVolume ;
	}
	public void setAdditionalVolume(int additionalVolume) {
		this.additionalVolume  = additionalVolume ;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
