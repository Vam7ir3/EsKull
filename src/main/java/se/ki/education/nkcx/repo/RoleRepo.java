package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByTitle(String title);
}
