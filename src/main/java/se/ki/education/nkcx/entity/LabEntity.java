package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_lab")
@DynamicInsert
@DynamicUpdate
public class LabEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_in_use" , nullable = false)
    private Boolean isInUse;

    public LabEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LabEntity setIsInUse(Boolean isInUse) {
        this.isInUse = isInUse;
        return this;
    }

}
