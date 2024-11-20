package se.ki.education.nkcx.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class Cell6923Req {

    private Long id;
    private Long personId;
    private Long laboratoryId;
    private Long countyId;
    private String sampleDate;
    private String sampleType;
    private Integer referralNumber;
    private Long referralTypeId;
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
}
