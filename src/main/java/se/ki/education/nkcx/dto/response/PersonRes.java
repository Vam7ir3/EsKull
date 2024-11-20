package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonRes {
    private Long id;
    private String dateOfBirth;
    private Integer pnr;
    private Boolean isValidPNR;
    private Boolean isByYear;

    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PersonRes setId(Long id) {
        this.id = id;
        return this;
    }

    public PersonRes setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public PersonRes setPnr(Integer pnr) {
        this.pnr = pnr;
        return this;
    }

    public PersonRes setValidPNR(Boolean validPNR) {
        this.isValidPNR = validPNR;
        return this;

    }

    public PersonRes setIsByYear(Boolean isByYear) {
        this.isByYear = isByYear;
        return this;
    }

    public PersonRes setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public PersonRes setModifiedBy(Long modifiedBy) {
        this.modifiedBy= modifiedBy;
        return this;
    }

    public PersonRes setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
        return this;
    }

    public PersonRes setModifiedAt(LocalDateTime modifiedAt){
        this.modifiedAt = modifiedAt;
        return this;
    }
}
