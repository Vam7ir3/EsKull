package se.ki.education.nkcx.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

public interface PersonRepo extends JpaRepository<PersonEntity, Long> {


    Optional<PersonEntity> findByPnr(Integer pnr);

    Page<PersonEntity> findByIsValidPNRTrue(Pageable pageable);

    Page<PersonEntity> findByPnrInAndIsValidPNRIn(
            List<Integer> pnr,
            List<Boolean> isValidPNR,
            Pageable pageable
    );

    @Query("SELECT p FROM PersonEntity p WHERE p.isValidPNR = :isValidPNR")
    Page<PersonEntity> findByIsValidPNR(@Param("isValidPNR") Boolean isValidPNR, Pageable pageable);

    List<PersonEntity> findByIsValidPNR(boolean validPNR, Sort sort);






}
