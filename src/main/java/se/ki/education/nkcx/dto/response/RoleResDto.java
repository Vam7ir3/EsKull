package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResDto {
    private Long id;
    private String title;
    private List<AuthorityRes> authorities;

    public RoleResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleResDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public RoleResDto setAuthorities(List<AuthorityRes> authorities) {
        this.authorities = authorities;
        return this;
    }
}
