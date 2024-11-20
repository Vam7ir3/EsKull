package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_person_sample")

public class PersonSampleEntity extends CommonEntity{

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "sample_id", nullable = false)
    private SampleEntity sampleEntity;

    public PersonSampleEntity setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
        return this;
    }

    public PersonSampleEntity setSampleEntity(SampleEntity sampleEntity) {
        this.sampleEntity = sampleEntity;
        return this;
    }
}
