package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class InvitationTypeReq {
    private Long id;
    private String type;
    private String xtype;
    private String description;
}
