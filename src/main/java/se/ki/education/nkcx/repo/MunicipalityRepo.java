package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.MunicipalityEntity;
import se.ki.education.nkcx.entity.PersonSampleEntity;

import java.util.List;
import java.util.Optional;

public interface MunicipalityRepo extends JpaRepository<MunicipalityEntity, Long>, JpaSpecificationExecutor<MunicipalityEntity> {

    Optional<MunicipalityEntity> findByName(String name);

    Page<MunicipalityEntity> findByYear(Integer year, Pageable pageable);

}
