package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_person_ext_hpv")
public class PersonExtHpvEntity extends CommonEntity{

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "ext_hpv_id", nullable = false)
    private ExtHpvEntity extHpvEntity;

    public PersonExtHpvEntity setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
        return this;
    }

    public PersonExtHpvEntity setExtHpvEntity(ExtHpvEntity extHpvEntity) {
        this.extHpvEntity = extHpvEntity;
        return this;
    }
}