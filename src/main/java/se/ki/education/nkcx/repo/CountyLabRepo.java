package se.ki.education.nkcx.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.CountyLabEntity;

public interface CountyLabRepo extends JpaRepository<CountyLabEntity, Long>, JpaSpecificationExecutor<CountyLabEntity> {
}
