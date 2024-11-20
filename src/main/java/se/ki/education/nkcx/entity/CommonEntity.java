package se.ki.education.nkcx.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modified_by")
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CommonEntity)) {
            return false;
        }
        CommonEntity that = (CommonEntity) o;
        return this == that || Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

