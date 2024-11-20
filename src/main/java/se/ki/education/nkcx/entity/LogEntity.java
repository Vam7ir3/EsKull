package se.ki.education.nkcx.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_log")
public class LogEntity extends CommonEntity{


    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "operation", nullable = false)
    private String operation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userEntity;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public LogEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LogEntity setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public LogEntity setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;

    }
}