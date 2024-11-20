package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferenceTypeRes {
    private Long id;
    private String type;

    public ReferenceTypeRes setId(Long id) {
        this.id = id;
        return this;
    }

    public ReferenceTypeRes setType(String type) {
        this.type = type;
        return this;
    }
}
