package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.AllowedRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllowedRegistrationRepo extends JpaRepository<AllowedRegistrationEntity, Long> {
    Optional<AllowedRegistrationEntity> findByEmail(String email);
}
