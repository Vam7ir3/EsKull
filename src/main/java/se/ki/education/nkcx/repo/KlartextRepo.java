package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.DistrictEntity;
import se.ki.education.nkcx.entity.KlartextEntity;

import java.util.List;
import java.util.Optional;

public interface KlartextRepo extends JpaRepository<KlartextEntity, Long> {


    Optional<KlartextEntity> findBySnomedText(String snomedText);

    @Query("SELECT p FROM KlartextEntity p WHERE p.snomedText LIKE %:snomedText%")
    List<KlartextEntity> findByKlartextContaining(@Param("snomedText") String snomedText);
}
