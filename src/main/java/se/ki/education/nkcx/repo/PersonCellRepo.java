package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonCellEntity;
import se.ki.education.nkcx.entity.PersonCellEntity;
import se.ki.education.nkcx.entity.CellEntity;

import java.util.List;

public interface PersonCellRepo extends JpaRepository<PersonCellEntity, Long>, JpaSpecificationExecutor<PersonCellEntity> {
    List<PersonCellEntity> findByPersonEntityId(Long personId);
    List<PersonCellEntity> findByCellEntityId(Long cellId);
    PersonCellEntity findByCellEntityAndPersonEntity(CellEntity cellEntity, PersonEntity personEntity);

    List<PersonCellEntity> findByCellEntityIdIn(List<Long> cellId);

}
