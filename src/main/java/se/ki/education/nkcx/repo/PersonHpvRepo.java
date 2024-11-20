package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonHpvEntity;
import se.ki.education.nkcx.entity.PersonHpvEntity;
import se.ki.education.nkcx.entity.HpvEntity;

import java.util.List;

public interface PersonHpvRepo extends JpaRepository<PersonHpvEntity, Long>, JpaSpecificationExecutor<PersonHpvEntity> {
    List<PersonHpvEntity> findByPersonEntityId(Long personId);
    List<PersonHpvEntity> findByHpvEntityId(Long hpvId);
    PersonHpvEntity findByHpvEntityAndPersonEntity(HpvEntity hpvEntity, PersonEntity personEntity);

    List<PersonHpvEntity> findByHpvEntityIdIn(List<Long> hpvId);

    Page<PersonHpvEntity> findByHpvEntityId(Pageable pageable, Long hpvId);
}
