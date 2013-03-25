package ru.efive.medicine.niidg.trfu.data.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.efive.dao.sql.entity.IdentifiedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trfu_email_templates")
public class EmailTemplate extends IdentifiedEntity {
    private String name;

    private boolean deleted;

    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @JoinTable(name = "trfu_email_template_send_to_text",
            joinColumns = { @JoinColumn(name = "email_template_id") },
            inverseJoinColumns = { @JoinColumn(name = "text_template_id") })
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TextTemplate> sendTo;

    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @JoinTable(name = "trfu_email_template_copy_to_text",
            joinColumns = { @JoinColumn(name = "email_template_id") },
            inverseJoinColumns = { @JoinColumn(name = "text_template_id") })
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TextTemplate> copyTo;

    @OneToMany
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @JoinTable(name = "trfu_email_template_blind_copy_to_text",
            joinColumns = { @JoinColumn(name = "email_template_id") },
            inverseJoinColumns = { @JoinColumn(name = "text_template_id") })
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TextTemplate> blindCopyTo;

    private String subject;

    @Column(columnDefinition="text")
    private String body;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TextTemplate> getSendTo() {
        return sendTo;
    }

    public void setSendTo(List<TextTemplate> sendTo) {
        this.sendTo = sendTo;
    }

    public List<TextTemplate> getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(List<TextTemplate> copyTo) {
        this.copyTo = copyTo;
    }

    public List<TextTemplate> getBlindCopyTo() {
        return blindCopyTo;
    }

    public void setBlindCopyTo(ArrayList<TextTemplate> blindCopyTo) {
        this.blindCopyTo = blindCopyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
