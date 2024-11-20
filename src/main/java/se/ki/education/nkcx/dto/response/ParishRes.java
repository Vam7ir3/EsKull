package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParishRes {

    private Long id;
    private String name;
    private String registerDate;
    private String dividedOtherCounty;
    private MunicipalityRes municipalityRes;
    private CountyRes countyRes;

    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ParishRes setId(Long id) {
        this.id = id;
        return this;
    }

    public ParishRes setName(String name) {
        this.name = name;
        return this;
    }

    public ParishRes setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public ParishRes setDividedOtherCounty(String dividedOtherCounty) {
        this.dividedOtherCounty = dividedOtherCounty;
        return this;
    }

    public ParishRes setMunicipalityRes(MunicipalityRes municipalityRes) {
        this.municipalityRes = municipalityRes;
        return this;
    }

    public ParishRes setCountyRes(CountyRes countyRes) {
        this.countyRes = countyRes;
        return this;
    }

    public ParishRes setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ParishRes setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public ParishRes setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ParishRes setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }
}
