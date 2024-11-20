package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.DistrictEntity;

import java.util.List;
import java.util.Optional;

public interface DistrictRepo extends JpaRepository<DistrictEntity, Long> {

    Optional<DistrictEntity> findByDistrict(String district);

    @Query("SELECT p FROM DistrictEntity p WHERE p.district LIKE %:district%")
    List<DistrictEntity> findByDistrictContaining(@Param("district") String district);
}
