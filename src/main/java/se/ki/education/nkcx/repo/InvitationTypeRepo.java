package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.InvitationTypeEntity;

import java.util.List;
import java.util.Optional;

public interface InvitationTypeRepo extends JpaRepository<InvitationTypeEntity, Long> {

    Optional<InvitationTypeEntity> findByType(String type);

    @Query("SELECT p FROM InvitationTypeEntity p WHERE p.type LIKE %:type%")
    List<InvitationTypeEntity> findByInvitationContaining(@Param("type") String type);
}
