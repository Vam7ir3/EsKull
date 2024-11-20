package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.LaboratoryEntity;

import java.util.List;
import java.util.Optional;

public interface LaboratoryRepo extends JpaRepository<LaboratoryEntity, Long> {

    Optional<LaboratoryEntity> findByName(String name);
    Page<LaboratoryEntity> findByIsInUseTrue(Pageable pageable);

    List<LaboratoryEntity> findByIsInUse(Boolean isInUse);

    @Query("SELECT p FROM LaboratoryEntity p WHERE p.name LIKE %:name%")
    List<LaboratoryEntity> findByLaboratoryContaining(@Param("name") String name);


}
