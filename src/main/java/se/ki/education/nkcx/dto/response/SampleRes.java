package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleRes {
    private Long id;
    private String type;
    private Long personId;


    public SampleRes setId(Long id) {
        this.id = id;
        return this;
    }

    public SampleRes setType(String type) {
        this.type = type;
        return this;
    }

    public SampleRes setPersonId(Long personId) {
        this.personId = personId;
        return this;
    }


}
