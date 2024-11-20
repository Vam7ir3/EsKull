package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class MunicipalityReq {
    private Long id;
    private String name;
    private Integer year;
}
