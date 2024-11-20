package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.MultiLangMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MultiLangMessageRepo extends JpaRepository<MultiLangMessageEntity, Long> {
    Optional<MultiLangMessageEntity> findByCode(String code);
}
