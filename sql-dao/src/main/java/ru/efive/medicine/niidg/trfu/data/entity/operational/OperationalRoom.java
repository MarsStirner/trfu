package ru.efive.medicine.niidg.trfu.data.entity.operational;

import ru.efive.dao.sql.entity.AbstractEntity;
import ru.efive.dao.sql.entity.user.User;
import ru.efive.medicine.niidg.trfu.data.dictionary.BloodSystemType;

import javax.persistence.*;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 13.01.2015, 16:39 <br>
 * Description: Операционная <br>
 */
@Entity
@Table(name = "trfu_operational_room")
public class OperationalRoom extends AbstractEntity {

    /**
     * Уникальный идентификатор операционной
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * Дата+Время создания операционной
     */
    @Column(name="createdDateTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * пользователь, создавший операционную
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Наименование операционной
     */
    @Column(name="name", nullable = false)
    private String name;

    /**
     * Флаг удаления операционной
     */
    @Column(name="deleted", nullable = false)
    private boolean deleted;

    /**
     * Используемые системы крови в операционной
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name="trfu_operational_blood_system_type",
            joinColumns = @JoinColumn( name="operationalRoom_id"),
            inverseJoinColumns = @JoinColumn( name="bloodSystemType_id")
    )
    private Set<BloodSystemType> bloodTypes;

    /**
     * Назначенные врачи-трансфузиологи для операционной
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name="trfu_operational_doctors",
            joinColumns = @JoinColumn( name="operationalRoom_id"),
            inverseJoinColumns = @JoinColumn( name="user_id")
    )
    private Set<User> doctors;

    /**
     * Назначенные медсестры для операционной
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name="trfu_operational_nurses",
            joinColumns = @JoinColumn( name="operationalRoom_id"),
            inverseJoinColumns = @JoinColumn( name="user_id")
    )
    private Set<User> nurses;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public OperationalRoom() {
        this.created = new Date();
        this.deleted = false;
    }

    public OperationalRoom(User author, String name) {
        this();
        this.author = author;
        this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Mapped Collections Getters & Setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Set<BloodSystemType> getBloodTypes() {
        return bloodTypes;
    }

    public void setBloodTypes(Set<BloodSystemType> bloodTypes) {
        this.bloodTypes = bloodTypes;
    }

    public boolean addToBloodTypes(BloodSystemType bloodSystemType){
        if(bloodTypes == null){
            bloodTypes = new HashSet<BloodSystemType>(1);
        }
        return bloodTypes.add(bloodSystemType);
    }

    public List<BloodSystemType> getBloodTypesList(){
        return new ArrayList<BloodSystemType>(bloodTypes);
    }

    public Set<User> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<User> doctors) {
        this.doctors = doctors;
    }

    public boolean addToDoctors(User user){
        if(doctors == null){
            doctors = new HashSet<User>(1);
        }
        return doctors.add(user);
    }

    public List<User> getDoctorsList(){
        return new ArrayList<User>(doctors);
    }

    public Set<User> getNurses() {
        return nurses;
    }

    public void setNurses(Set<User> nurses) {
        this.nurses = nurses;
    }

    public boolean addToNurses(User user){
        if(nurses == null){
            nurses = new HashSet<User>(1);
        }
        return nurses.add(user);
    }

    public List<User> getNursesList(){
        return new ArrayList<User>(nurses);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Overrides
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OperationalRoom[");
        sb.append(id).append("]{");
        sb.append("created=").append(created);
        sb.append(", author=").append(author != null ? author.getId() : "null");
        sb.append(", name='").append(name).append('\'');
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationalRoom that = (OperationalRoom) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
