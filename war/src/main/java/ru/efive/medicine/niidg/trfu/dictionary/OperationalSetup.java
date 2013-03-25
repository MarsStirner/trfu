package ru.efive.medicine.niidg.trfu.dictionary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;
import ru.efive.medicine.niidg.trfu.data.entity.BloodSystem;
import ru.efive.medicine.niidg.trfu.data.entity.OperationalCrew;

public class OperationalSetup {
    private List<BloodSystemType> systemTypes = new ArrayList<BloodSystemType>();
    private OperationalCrew crew;
    private Date created;
    private String name;
    private boolean unsaved;

    public List<BloodSystemType> getSystemTypes() {
        return systemTypes;
    }

    public void setSystemTypes(List<BloodSystemType> systemTypes) {
        this.systemTypes = systemTypes;
    }

    public OperationalCrew getCrew() {
		return crew;
	}
	
	public void setCrew(OperationalCrew crew) {
		this.crew = crew;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUnsaved() {
		return unsaved;
	}
	
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crew == null) ? 0 : crew.hashCode());
		result = prime * result + ((systemTypes == null) ? 0 : systemTypes.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationalSetup other = (OperationalSetup) obj;
		if (crew == null) {
			if (other.crew != null)
				return false;
		} else if (!crew.equals(other.crew))
			return false;
		if (systemTypes == null) {
			if (other.systemTypes != null)
				return false;
		} else if (!systemTypes.equals(other.systemTypes))
			return false;
		return true;
	}
}