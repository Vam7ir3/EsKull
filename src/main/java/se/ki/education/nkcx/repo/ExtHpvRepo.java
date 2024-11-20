package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.ExtHpvEntity;
import se.ki.education.nkcx.entity.HpvEntity;

import java.util.List;
import java.util.Optional;

public interface ExtHpvRepo extends JpaRepository<ExtHpvEntity, Long> {

    Optional<ExtHpvEntity> findByName(String name);
    HpvEntity findExtHpvById(Long id);

    @Query("SELECT p FROM ExtHpvEntity p WHERE p.name LIKE %:name%")
    List<ExtHpvEntity> findByExtHpvContaining(@Param("name") String name);
}
