package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.CountyEntity;

import java.util.List;
import java.util.Optional;

public interface CountyRepo extends JpaRepository<CountyEntity, Long> {
    Optional<CountyEntity> findByName(String name);

    @Query("SELECT p FROM CountyEntity p WHERE p.name LIKE %:name%")
    List<CountyEntity> findByCountyContaining(@Param("name") String name);
}
