package ru.efive.dao.sql.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

//import org.hibernate.search.annotations.DocumentId;

/**
 * Сущность, имеющая идентификатор
 */
@MappedSuperclass
public class IdentifiedEntity extends AbstractEntity {
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		IdentifiedEntity other = (IdentifiedEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@DocumentId
	private int id;

    private String uuid = UUID.randomUUID().toString();
	
	private static final long serialVersionUID = -5373498855516075305L;
}