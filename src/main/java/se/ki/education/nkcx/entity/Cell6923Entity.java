package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_cell_6923")
@DynamicInsert
@DynamicUpdate
public class Cell6923Entity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "laboratory_id", nullable = false)
    private LaboratoryEntity laboratoryEntity;

    @ManyToOne
    @JoinColumn(name = "county_id", nullable = false)
    private CountyEntity countyEntity;

    @Column(name = "sample_date", nullable = false)
    private Long sampleDate;

    @Column(name = "sample_type")
    private String sampleType;

    @Column(name = "referral_number", nullable = false)
    private Integer referralNumber;

    @ManyToOne
    @JoinColumn(name = "referral_type", nullable = false)
    private ReferenceTypeEntity referenceTypeEntity;

    @Column(name = "reference_site")
    private String referenceSite;

    @Column(name = "residc")
    private Integer residc;

    @Column(name = "residk")
    private Integer residk;

    @Column(name = "x_sample_date", nullable = false)
    private Long xSampleDate;

    @Column(name = "x_registration_date", nullable = false)
    private Long xRegistrationDate;

    @Column(name = "x_snomed", nullable = false)
    private String xSnomed;

    @Column(name = "diag_id")
    private String diagId;

    @Column(name = "ans_clinic")
    private String ansClinic;

    @Column(name = "deb_clinic")
    private String debClinic;

    @Column(name = "rem_clinic")
    private String remClinic;

    @Column(name = "registration_date", nullable = false)
    private Long registrationDate;

    @Column(name = "scr_type")
    private Integer scrType;

    @Column(name = "snomed", nullable = false)
    private String snomed;

    @Column(name = "response_date")
    private Long responseDate;

    @Column(name = "x_response_date")
    private Long xResponseDate;

    @Column(name = "diff_days")
    private Integer diffDays;


    public Cell6923Entity setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
        return this;
    }

    public Cell6923Entity setLaboratoryEntity(LaboratoryEntity laboratoryEntity) {
        this.laboratoryEntity = laboratoryEntity;
        return this;
    }

    public Cell6923Entity setCountyEntity(CountyEntity countyEntity) {
        this.countyEntity = countyEntity;
        return this;
    }

    public Cell6923Entity setSampleDate(Long sampleDate) {
        this.sampleDate = sampleDate;
        return this;
    }

    public Cell6923Entity setSampleType(String sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public Cell6923Entity setReferralNumber(Integer referralNumber) {
        this.referralNumber = referralNumber;
        return this;
    }

    public Cell6923Entity setReferenceTypeEntity(ReferenceTypeEntity referenceTypeEntity) {
        this.referenceTypeEntity = referenceTypeEntity;
        return this;
    }

    public Cell6923Entity setReferenceSite(String referenceSite) {
        this.referenceSite = referenceSite;
        return this;
    }

    public Cell6923Entity setResidc(Integer residc) {
        this.residc = residc;
        return this;
    }

    public Cell6923Entity setResidk(Integer residk) {
        this.residk = residk;
        return this;
    }

    public Cell6923Entity setXSampleDate(Long xSampleDate) {
        this.xSampleDate = xSampleDate;
        return this;
    }

    public Cell6923Entity setXRegistrationDate(Long xRegistrationDate) {
        this.xRegistrationDate = xRegistrationDate;
        return this;
    }

    public Cell6923Entity setXSnomed(String xSnomed) {
        this.xSnomed = xSnomed;
        return this;
    }

    public Cell6923Entity setDiagId(String diagId) {
        this.diagId = diagId;
        return this;
    }

    public Cell6923Entity setAnsClinic(String ansClinic) {
        this.ansClinic = ansClinic;
        return this;
    }

    public Cell6923Entity setDebClinic(String debClinic) {
        this.debClinic = debClinic;
        return this;
    }

    public Cell6923Entity setRemClinic(String remClinic) {
        this.remClinic = remClinic;
        return this;
    }

    public Cell6923Entity setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public Cell6923Entity setScrType(Integer scrType) {
        this.scrType = scrType;
        return this;
    }

    public Cell6923Entity setSnomed(String snomed) {
        this.snomed = snomed;
        return this;
    }

    public Cell6923Entity setResponseDate(Long responseDate) {
        this.responseDate = responseDate;
        return this;
    }

    public Cell6923Entity setXResponseDate(Long xResponseDate) {
        this.xResponseDate = xResponseDate;
        return this;
    }

    public Cell6923Entity setDiffDays(Integer diffDays) {
        this.diffDays = diffDays;
        return this;
    }
}
