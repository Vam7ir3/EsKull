package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class PersonReq {
    private Long id;
    private String dateOfBirth;
    private Integer pnr;
    private Boolean isValidPNR;
    private Boolean isByYear;
}

