package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<AuthorityEntity, Long> {
}
