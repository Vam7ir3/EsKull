package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountyRes {
    private Long id;
    private String name;

    public CountyRes setId(Long id) {
        this.id = id;
        return this;
    }

    public CountyRes setName(String name) {
        this.name = name;
        return this;
    }
}
