package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class LabReq {
    private Long id;
    private String name;
    private Boolean isInUse;
}
