package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryRes {
    private Long id;
    private String name;

    public CountryRes setId(Long id) {
        this.id = id;
        return this;
    }

    public CountryRes setName(String name) {
        this.name = name;
        return this;
    }
}
