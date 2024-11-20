package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cell6923Res {
    private Long id;
    private PersonRes personRes;
    private LaboratoryRes laboratoryRes;
    private CountyRes countyRes;
    private String sampleDate;
    private String sampleType;
    private Integer referralNumber;
    private ReferenceTypeRes referenceTypeRes;
    private String referenceSite;
    private Integer residc;
    private Integer residk;
    private String xSampleDate;
    private String xRegistrationDate;
    private String xSnomed;
    private String diagId;
    private String ansClinic;
    private String debClinic;
    private String remClinic;
    private String registrationDate;
    private Integer scrType;
    private String snomed;
    private String responseDate;
    private String xResponseDate;
    private Integer diffDays;

    public Cell6923Res setId(Long id) {
        this.id = id;
        return this;
    }

    public Cell6923Res setPersonRes(PersonRes personRes) {
        this.personRes = personRes;
        return this;
    }

    public Cell6923Res setLaboratoryRes(LaboratoryRes laboratoryRes) {
        this.laboratoryRes = laboratoryRes;
        return this;
    }

    public Cell6923Res setCountyRes(CountyRes countyRes) {
        this.countyRes = countyRes;
        return this;
    }

    public Cell6923Res setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
        return this;
    }

    public Cell6923Res setSampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public Cell6923Res setReferralNumber(Integer referralNumber) {
        this.referralNumber = referralNumber;
        return this;
    }

    public Cell6923Res setReferralTypeId(ReferenceTypeRes referenceTypeRes) {
        this.referenceTypeRes = referenceTypeRes;
        return this;
    }

    public Cell6923Res setReferenceSite(String referenceSite) {
        this.referenceSite = referenceSite;
        return this;
    }

    public Cell6923Res setResidc(Integer residc) {
        this.residc = residc;
        return this;
    }

    public Cell6923Res setResidk(Integer residk) {
        this.residk = residk;
        return this;
    }

    public Cell6923Res setXSampleDate(String xSampleDate) {
        this.xSampleDate = xSampleDate;
        return this;
    }

    public Cell6923Res setXRegistrationDate(String xRegistrationDate) {
        this.xRegistrationDate = xRegistrationDate;
        return this;
    }

    public Cell6923Res setXSnomed(String xSnomed) {
        this.xSnomed = xSnomed;
        return this;
    }

    public Cell6923Res setDiagId(String diagId) {
        this.diagId = diagId;
        return this;
    }

    public Cell6923Res setAnsClinic(String ansClinic) {
        this.ansClinic = ansClinic;
        return this;
    }


    public Cell6923Res setDebClinic(String debClinic) {
        this.debClinic = debClinic;
        return this;
    }

    public Cell6923Res setRemClinic(String remClinic) {
        this.remClinic = remClinic;
        return this;
    }

    public Cell6923Res setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public Cell6923Res setScrType(Integer scrType) {
        this.scrType = scrType;
        return this;
    }

    public Cell6923Res setSnomed(String snomed) {
        this.snomed = snomed;
        return this;
    }

    public Cell6923Res setResponseDate(String responseDate) {
        this.responseDate = responseDate;
        return this;
    }

    public Cell6923Res setXResponseDate(String xResponseDate) {
        this.xResponseDate = xResponseDate;
        return this;
    }

    public Cell6923Res setDiffDays(Integer diffDays) {

        this.diffDays = diffDays;
        return this;
    }
}
