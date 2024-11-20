package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorityRes {
    private Long id;
    private String title;
    private String description;

    public AuthorityRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AuthorityRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public AuthorityRes setDescription(String description) {
        this.description = description;
        return this;
    }
}
