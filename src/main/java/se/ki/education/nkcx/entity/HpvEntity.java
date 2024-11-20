package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_hpv")
@DynamicUpdate
@DynamicInsert
public class HpvEntity extends CommonEntity{

    @Column(name = "name", nullable = false)
    private String name;

    public HpvEntity setName(String name) {
        this.name = name;
        return this;
    }

}
