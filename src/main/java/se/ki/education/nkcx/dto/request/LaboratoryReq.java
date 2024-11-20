package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class LaboratoryReq {
    private Long id;
    private String name;
    private Boolean isInUse;
    private Integer sosLab;
    private String sosLabName;
    private String sosLongName;
    private String region;
}
