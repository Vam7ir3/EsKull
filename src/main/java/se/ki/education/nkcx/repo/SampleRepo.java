package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.SampleEntity;

import java.util.List;
import java.util.Optional;

public interface SampleRepo extends JpaRepository<SampleEntity, Long> {
    Optional<SampleEntity> findByType(String type);
    SampleEntity findSampleById(Long id);

    @Query("SELECT p FROM SampleEntity p WHERE p.type LIKE %:type%")
    List<SampleEntity> findBySampleContaining(@Param("type") String type);

}
