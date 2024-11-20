package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_parish")
public class ParishEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "register_date", nullable = false)
    private Long registerDate;

    @Column(name = "divided_other_county", nullable = false)
    private String dividedOtherCounty;

    @ManyToOne(optional = true)
    @JoinColumn(name = "municipality_id", nullable = true, columnDefinition = "BIGINT DEFAULT NULL")
    private MunicipalityEntity municipalityEntity;

    @ManyToOne(optional = true)
    @JoinColumn(name = "county_id", nullable = true, columnDefinition = "BIGINT DEFAULT NULL")
    private CountyEntity countyEntity;

    public ParishEntity setName(String name) {
        this.name = name;
        return this;
    }

    public ParishEntity setRegisterDate(Long registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public ParishEntity setDividedOtherCounty(String dividedOtherCounty) {
        this.dividedOtherCounty = dividedOtherCounty;
        return this;
    }

    public ParishEntity setMunicipalityEntity(MunicipalityEntity municipalityEntity) {
        this.municipalityEntity = municipalityEntity;
        return this;
    }

    public ParishEntity setCountyEntity(CountyEntity countyEntity) {
        this.countyEntity = countyEntity;
        return this;
    }


}
