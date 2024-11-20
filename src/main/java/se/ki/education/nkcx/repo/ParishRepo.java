package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.*;

import java.util.List;
import java.util.Optional;

public interface ParishRepo extends JpaRepository<ParishEntity, Long>, JpaSpecificationExecutor<ParishEntity> {

    Optional<ParishEntity> findByName(String name);

    List<ParishEntity> findByMunicipalityEntityId(Long municipalityId);
    List<ParishEntity> findByCountyEntityId(Long countyId);
    ParishEntity findByMunicipalityEntityAndCountyEntity(MunicipalityEntity municipalityEntity, CountyEntity countyEntity);

    Page<ParishEntity> searchByName(String name, Pageable pageable);
}
