package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_person_hpv")
public class PersonHpvEntity extends CommonEntity{

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "hpv_id", nullable = false)
    private HpvEntity hpvEntity;

    public PersonHpvEntity setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
        return this;
    }

    public PersonHpvEntity setHpvEntity(HpvEntity hpvEntity) {
        this.hpvEntity = hpvEntity;
        return this;
    }
}
