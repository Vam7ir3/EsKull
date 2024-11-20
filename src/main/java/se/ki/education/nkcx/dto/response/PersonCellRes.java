package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonCellRes {
    private Long id;
    private CellRes cellRes;
    private PersonRes personRes;


    public PersonCellRes setId(Long id) {
        this.id = id;
        return this;
    }

    public PersonCellRes setCellRes(CellRes cellRes) {
        this.cellRes = cellRes;
        return this;
    }

    public PersonCellRes setPersonRes(PersonRes personRes) {
        this.personRes = personRes;
        return this;
    }

    public PersonCellRes(Long id, CellRes cellRes, PersonRes personRes) {
        this.id = id;
        this.cellRes = cellRes;
        this.personRes = personRes;
    }
}
