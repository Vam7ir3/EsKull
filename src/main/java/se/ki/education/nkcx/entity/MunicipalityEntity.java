package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_municipality")
@DynamicInsert
@DynamicUpdate
public class MunicipalityEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year", nullable = false)
    private Integer year;

    public MunicipalityEntity setName(String name) {
        this.name = name;
        return this;
    }

    public MunicipalityEntity setYear(Integer year) {
        this.year = year;
        return this;
    }

}
