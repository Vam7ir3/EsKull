package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AboutUsRes {
    private Long id;
    private String name;
    private String phone;
    private String email;

    public AboutUsRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AboutUsRes setName(String name) {
        this.name = name;
        return this;
    }

    public AboutUsRes setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AboutUsRes setEmail(String email) {
        this.email = email;
        return this;
    }
}
