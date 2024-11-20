package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class ParishReq {
    private Long id;
    private String name;
    private String registerDate;
    private String dividedOtherCounty;
    private Long municipalityId;
    private Long countyId;
}
