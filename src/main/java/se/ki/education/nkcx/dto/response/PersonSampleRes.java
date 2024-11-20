package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonSampleRes {
    private Long id;
    private SampleRes sampleRes;
    private PersonRes personRes;


    public PersonSampleRes setId(Long id) {
        this.id = id;
        return this;
    }

    public PersonSampleRes setSampleRes(SampleRes sampleRes) {
        this.sampleRes = sampleRes;
        return this;
    }

    public PersonSampleRes setPersonRes(PersonRes personRes) {
        this.personRes = personRes;
        return this;
    }

    public PersonSampleRes(Long id, SampleRes sampleRes, PersonRes personRes) {
        this.id = id;
        this.sampleRes = sampleRes;
        this.personRes = personRes;
    }
}
