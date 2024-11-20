package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.LogEntity;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepo  extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findByOperation(String operation);
    List<LogEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<LogEntity> findLogsByOperationAndDescriptionAndTimestampBetween(
            String operation, String description, LocalDateTime startTime, LocalDateTime endTime
    );
    List<LogEntity> findAllByOrderByTimestampDesc();


    @Query("SELECT l FROM LogEntity l WHERE l.userEntity.emailAddress LIKE %:emailAddress%")
    List<LogEntity> findLogsByUserEmailContaining(@Param("emailAddress") String emailAddress);

    Page<LogEntity> findByUserEntity_EmailAddressContaining(Long userId, Pageable pageable);

    Page<LogEntity> findByUserEntity(UserEntity userEntity, Pageable pageable);

    Page<LogEntity> findByUserEntity_Id(Long userId, Pageable pageable);
}
