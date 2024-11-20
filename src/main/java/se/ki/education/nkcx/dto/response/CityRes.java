package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityRes {
    private Long id;
    private String name;
    private StateResDto state;

    public CityRes setId(Long id) {
        this.id = id;
        return this;
    }

    public CityRes setName(String name) {
        this.name = name;
        return this;
    }

    public CityRes setState(StateResDto state) {
        this.state = state;
        return this;
    }
}
