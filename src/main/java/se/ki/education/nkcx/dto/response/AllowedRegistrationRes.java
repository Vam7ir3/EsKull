package se.ki.education.nkcx.dto.response;

import se.ki.education.nkcx.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllowedRegistrationRes {
    private Long id;
    private String email;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDateTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime registeredDateTime;

    public AllowedRegistrationRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AllowedRegistrationRes setEmail(String email) {
        this.email = email;
        return this;
    }

    public AllowedRegistrationRes setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public AllowedRegistrationRes setRegisteredDateTime(LocalDateTime registeredDateTime) {
        this.registeredDateTime = registeredDateTime;
        return this;
    }
}
