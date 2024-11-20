package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_allowed_registration")
@DynamicInsert
@DynamicUpdate
public class AllowedRegistrationEntity extends CommonEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "registered_date_time")
    private LocalDateTime registeredDateTime;

    public AllowedRegistrationEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public AllowedRegistrationEntity setRegisteredDateTime(LocalDateTime registeredDateTime) {
        this.registeredDateTime = registeredDateTime;
        return this;
    }
}
