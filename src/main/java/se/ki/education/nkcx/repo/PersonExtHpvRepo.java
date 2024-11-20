package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.*;

import java.util.List;

public interface PersonExtHpvRepo extends JpaRepository<PersonExtHpvEntity, Long>, JpaSpecificationExecutor<PersonExtHpvEntity> {
    List<PersonExtHpvEntity> findByPersonEntityId(Long personId);
    List<PersonExtHpvEntity> findByExtHpvEntityId(Long hpvId);
    PersonExtHpvEntity findByExtHpvEntityAndPersonEntity(ExtHpvEntity extHpvEntity, PersonEntity personEntity);

    List<PersonExtHpvEntity> findByExtHpvEntityIdIn(List<Long> extHpvId);
}
