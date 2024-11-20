package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaboratoryRes {
    private Long id;
    private String name;
    private Boolean isInUse;
    private Integer sosLab;
    private String sosLabName;
    private String sosLongName;
    private String region;

    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LaboratoryRes setId(Long id) {
        this.id = id;
        return this;
    }

    public LaboratoryRes setName(String name) {
        this.name = name;
        return this;
    }

    public LaboratoryRes setIsInUse(Boolean isInUse) {
        this.isInUse = isInUse;
        return this;
    }

    public LaboratoryRes setSosLab(Integer sosLab) {
        this.sosLab = sosLab;
        return this;
    }

    public LaboratoryRes setSosLabName(String sosLabName) {
        this.sosLabName = sosLabName;
        return this;
    }

    public LaboratoryRes setSosLongName(String sosLongName) {
        this.sosLongName = sosLongName;
        return this;
    }

    public LaboratoryRes setRegion(String region) {
        this.region = region;
        return this;
    }

    public LaboratoryRes setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LaboratoryRes setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public LaboratoryRes setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
