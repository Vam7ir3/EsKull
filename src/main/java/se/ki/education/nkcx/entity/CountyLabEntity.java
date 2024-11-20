package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_county_lab")
public class CountyLabEntity extends CommonEntity{

    @ManyToOne
    @JoinColumn(name = "county_id", nullable = false)
    private CountyEntity countyEntity;

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    private LabEntity labEntity;

    public CountyLabEntity setCountyEntity(CountyEntity countyEntity) {
        this.countyEntity = countyEntity;
        return this;
    }

    public CountyLabEntity setLabEntity(LabEntity labEntity) {
        this.labEntity = labEntity;
        return this;
    }
}
