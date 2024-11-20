package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabRes {
    private Long id;
    private String name;
    private Boolean isInUse;

    public LabRes setId(Long id) {
        this.id = id;
        return this;
    }

    public LabRes setName(String name) {
        this.name = name;
        return this;
    }

    public LabRes setIsInUse(Boolean isInUse) {
        this.isInUse = isInUse;
        return this;
    }
}
