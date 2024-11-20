package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ki.education.nkcx.entity.LabEntity;

import java.util.Optional;

public interface LabRepo extends JpaRepository<LabEntity, Long> {
    Optional<LabEntity> findByName(String name);
}
