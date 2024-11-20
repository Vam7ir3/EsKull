package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.CellEntity;
import se.ki.education.nkcx.entity.HpvEntity;
import se.ki.education.nkcx.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface HpvRepo extends JpaRepository<HpvEntity, Long> {
    Optional<HpvEntity> findByName(String name);

    @Query("SELECT p FROM HpvEntity p WHERE p.name LIKE %:name%")
    List<HpvEntity> findByHpvContaining(@Param("name") String name);

    Page<HpvEntity> findHpvById(Pageable pageable, Long id);

}
