package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_ext_hpv")
@DynamicUpdate
@DynamicInsert
public class ExtHpvEntity extends CommonEntity{

    @Column(name = "name", nullable = false)
    private String name;

    public ExtHpvEntity setName(String name) {
        this.name = name;
        return this;
    }
}
