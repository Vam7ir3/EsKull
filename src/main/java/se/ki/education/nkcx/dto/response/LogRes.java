package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogRes {

    private Long id;
    private String operation;
    private String description;
    private UserResDto userId;
    private LocalDateTime timestamp;


    // Setters
    public LogRes setId(Long id) {
        this.id = id;
        return this;
    }

    public LogRes setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public LogRes setDescription(String description) {
        this.description = description;
        return this;
    }


    public LogRes setUserResDto(UserResDto userId) {
        this.userId = userId;
        return this;
    }

    public LogRes setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
