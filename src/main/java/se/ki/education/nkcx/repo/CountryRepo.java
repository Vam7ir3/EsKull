package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepo extends JpaRepository<CountryEntity, Long> {

    Optional<CountryEntity> findByName(String name);
}
