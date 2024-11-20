package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MunicipalityRes {
    private Long id;
    private String name;
    private Integer year;


    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MunicipalityRes setId(Long id) {
        this.id = id;
        return this;
    }

    public MunicipalityRes setName(String name) {
        this.name = name;
        return this;
    }

    public MunicipalityRes setYear(Integer year) {
        this.year = year;
        return this;
    }

    public MunicipalityRes setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public MunicipalityRes setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public MunicipalityRes setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MunicipalityRes setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
