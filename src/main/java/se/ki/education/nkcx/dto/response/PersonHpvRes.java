package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonHpvRes {
    private Long id;
    private HpvRes hpvRes;
    private PersonRes personRes;


    public PersonHpvRes setId(Long id) {
        this.id = id;
        return this;
    }

    public PersonHpvRes setHpvRes(HpvRes hpvRes) {
        this.hpvRes = hpvRes;
        return this;
    }

    public PersonHpvRes setPersonRes(PersonRes personRes) {
        this.personRes = personRes;
        return this;
    }

    public PersonHpvRes(Long id, HpvRes hpvRes, PersonRes personRes) {
        this.id = id;
        this.hpvRes = hpvRes;
        this.personRes = personRes;
    }
}
