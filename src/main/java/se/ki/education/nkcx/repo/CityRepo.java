package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.CityEntity;
import se.ki.education.nkcx.entity.StateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepo extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findByNameAndState(String name, StateEntity stateEntity);

    Page<CityEntity> findAllByState(StateEntity stateEntity, Pageable pageable);

    List<CityEntity> findAllByState(StateEntity stateEntity, Sort sort);
}
