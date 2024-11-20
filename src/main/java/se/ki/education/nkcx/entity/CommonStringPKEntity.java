package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonStringPKEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

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
        if (!(o instanceof CommonStringPKEntity)) {
            return false;
        }
        CommonStringPKEntity that = (CommonStringPKEntity) o;
        return this == that || Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

