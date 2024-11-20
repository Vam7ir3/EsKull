package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonExtHpvRes {
    private Long id;
    private ExtHpvRes extHpvRes;
    private PersonRes personRes;


    public PersonExtHpvRes setId(Long id) {
        this.id = id;
        return this;
    }

    public PersonExtHpvRes setExtHpvRes(ExtHpvRes extHpvRes) {
        this.extHpvRes = extHpvRes;
        return this;
    }

    public PersonExtHpvRes setPersonRes(PersonRes personRes) {
        this.personRes = personRes;
        return this;
    }

    public PersonExtHpvRes(Long id, ExtHpvRes extHpvRes, PersonRes personRes) {
        this.id = id;
        this.extHpvRes = extHpvRes;
        this.personRes = personRes;
    }
}
