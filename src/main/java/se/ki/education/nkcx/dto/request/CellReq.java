package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class CellReq {
    private Long id;
    private String name;
    private Long personId;
}
