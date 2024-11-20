package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_laboratory")
@DynamicInsert
@DynamicUpdate
public class LaboratoryEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_in_Use" , nullable = false )
    private Boolean isInUse;

    @Column(name = "sos_lab", nullable = false)
    private Integer sosLab;

    @Column(name = "sos_lab_name", nullable = false)
    private String sosLabName;

    @Column(name = "sos_long_name", nullable = false)
    private String sosLongName;

    @Column(name = "region", nullable = false)
    private String region;

    public LaboratoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LaboratoryEntity setIsInUse(Boolean isInUse) {
        this.isInUse = isInUse;
        return this;
    }

    public LaboratoryEntity setSosLab(Integer sosLab) {
        this.sosLab = sosLab;
        return this;
    }

    public LaboratoryEntity setSosLabName(String sosLabName) {
        this.sosLabName = sosLabName;
        return this;
    }

    public LaboratoryEntity setSosLongName(String sosLongName) {
        this.sosLongName = sosLongName;
        return this;
    }

    public LaboratoryEntity setRegion(String region) {
        this.region = region;
        return this;
    }


}
