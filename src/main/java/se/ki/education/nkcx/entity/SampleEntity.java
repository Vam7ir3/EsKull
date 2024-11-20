package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Data
@Entity
@Table(name = "tbl_sample")
@DynamicUpdate
@DynamicInsert
public class SampleEntity extends CommonEntity{

    @Column(name = "type", nullable = false)
    private String type;

    public SampleEntity setType(String type) {
        this.type = type;
        return this;
    }
}
