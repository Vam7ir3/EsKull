package se.ki.education.nkcx.repo.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.ki.education.nkcx.entity.student.StudentEntity;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    Optional<StudentEntity> findByFirstName(String firstName);

    Page<StudentEntity> findByLastName(String lastName, Pageable pageable);
}
