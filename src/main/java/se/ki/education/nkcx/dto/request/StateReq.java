package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class StateReq {
    private Long id;
    private String name;
    private Long countryId;
}
