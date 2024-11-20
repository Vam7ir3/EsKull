package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class CityReq {
    private Long id;
    private String name;
    private Long stateId;
}
