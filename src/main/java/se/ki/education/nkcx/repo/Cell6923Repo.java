package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.Cell6923Entity;

import java.util.Optional;

public interface Cell6923Repo extends JpaRepository<Cell6923Entity, Long>, JpaSpecificationExecutor<Cell6923Entity> {
    Optional<Cell6923Entity> findBySampleType(String sampleType);

    Page<Cell6923Entity> findBySampleType(String sampleType, Pageable pageable);
}
