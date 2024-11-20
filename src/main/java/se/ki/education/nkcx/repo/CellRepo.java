package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.CellEntity;
import se.ki.education.nkcx.entity.CellEntity;

import java.util.List;
import java.util.Optional;

public interface CellRepo extends JpaRepository<CellEntity, Long> {
    Optional<CellEntity> findByName(String name);
    CellEntity findCellById(Long id);

    @Query("SELECT p FROM CellEntity p WHERE p.name LIKE %:name%")
    List<CellEntity> findByCellContaining(@Param("name") String name);

}
