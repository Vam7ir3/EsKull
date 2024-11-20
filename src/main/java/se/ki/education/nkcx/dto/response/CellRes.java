package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellRes {

    private Long id;
    private String name;
    private Long personId;

    public CellRes setId(Long id) {
        this.id = id;
        return this;
    }

    public CellRes setName(String name) {
        this.name = name;
        return this;
    }

    public CellRes setPersonId(Long personId) {
        this.personId = personId;
        return this;
    }
}
