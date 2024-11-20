package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountyLabRes {
    private Long id;
    private CountyRes countyRes;
    private LabRes labRes;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;

    public CountyLabRes setId(Long id) {
        this.id = id;
        return this;
    }

    public CountyLabRes setCountyRes(CountyRes countyRes) {
        this.countyRes = countyRes;
        return this;
    }

    public CountyLabRes setLabRes(LabRes labRes) {
        this.labRes = labRes;
        return this;
    }

    public CountyLabRes (Long id, CountyRes countyRes, LabRes labRes) {
        this.id = id;
        this.countyRes = countyRes;
        this.labRes = labRes;
    }

    public CountyLabRes setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public CountyLabRes setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public CountyLabRes setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public CountyLabRes setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
