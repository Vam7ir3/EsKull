package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.InvitationTypeEntity;
import se.ki.education.nkcx.entity.ReferenceTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ReferenceTypeRepo extends JpaRepository<ReferenceTypeEntity, Long> {

    Optional<ReferenceTypeEntity> findByType(String type);

    @Query("SELECT p FROM ReferenceTypeEntity p WHERE p.type LIKE %:type%")
    List<ReferenceTypeEntity> findByReferenceContaining(@Param("type") String type);
}
