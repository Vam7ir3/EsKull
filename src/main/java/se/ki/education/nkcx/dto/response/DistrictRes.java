package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictRes {

    private Long id;
    private String district;
    private String districtName;

    public DistrictRes setId(Long id) {
        this.id = id;
        return this;
    }

    public DistrictRes setDistrict(String district) {
        this.district = district;
        return this;
    }

    public DistrictRes setDistrictName(String districtName) {
        this.districtName = districtName;
        return this;
    }
}
