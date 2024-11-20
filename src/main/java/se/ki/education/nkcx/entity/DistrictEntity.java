package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_district")
@DynamicInsert
@DynamicUpdate
public class DistrictEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "district_name", nullable = false)
    private String districtName;


    public DistrictEntity setDistrict(String district) {
        this.district = district;
        return this;
    }

    public DistrictEntity setDistrictName(String districtName) {
        this.districtName = districtName;
        return this;
    }
}
