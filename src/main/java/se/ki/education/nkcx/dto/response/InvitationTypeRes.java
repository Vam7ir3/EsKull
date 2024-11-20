package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvitationTypeRes {

    private Long id;
    private String type;
    private String xtype;
    private String description;

    public InvitationTypeRes setId(Long id) {
        this.id = id;
        return this;
    }

    public InvitationTypeRes setType(String type) {
        this.type = type;
        return this;
    }

    public InvitationTypeRes setXtype(String xtype) {
        this.xtype = xtype;
        return this;
    }

    public InvitationTypeRes setDescription(String description) {
        this.description = description;
        return this;
    }
}
