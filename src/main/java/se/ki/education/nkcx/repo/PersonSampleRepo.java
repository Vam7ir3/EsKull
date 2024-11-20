package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonSampleEntity;
import se.ki.education.nkcx.entity.PersonSampleEntity;
import se.ki.education.nkcx.entity.SampleEntity;

import java.util.List;

public interface PersonSampleRepo extends JpaRepository<PersonSampleEntity, Long>, JpaSpecificationExecutor<PersonSampleEntity> {
    List<PersonSampleEntity> findByPersonEntityId(Long personId);
    List<PersonSampleEntity> findBySampleEntityId(Long sampleId);
    PersonSampleEntity findBySampleEntityAndPersonEntity(SampleEntity sampleEntity, PersonEntity personEntity);

    List<PersonSampleEntity> findBySampleEntityIdIn(List<Long> sampleId);

    Page<PersonSampleEntity> findByPersonEntityIdIn(List<Long> personId, Pageable pageable);

}
