package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.RoleEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.repo.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByMobileNumber(String mobileNumber);

    UserEntity findByEmailAddress(String emailAddress);

    Page<UserEntity> findAllByRole(RoleEntity roleEntity, Pageable pageable);

    List<UserEntity> findAllByRole(RoleEntity roleEntity, Sort sort);

    Page<UserProjection> findAllByRole_TitleIn(List<String> roleTitles, Pageable pageable);

    List<UserProjection> findAllByRole_TitleIn(List<String> roleTitles, Sort sort);

    UserEntity findByEmailAddressAndEmailAddressVerifiedTrue(String emailAddress);

    UserEntity findByMobileNumberAndMobileNumberVerifiedTrue(String mobileNumber);

    UserEntity findByVerificationCodeAndVerificationCodeDeadlineAfter(String verificationCode, LocalDateTime localDateTime);

    UserEntity findByEmailAddressAndMobileNumber(String emailAddress, String mobileNumber);

    Page<UserProjection> findAllByEmailAddressNotNull(Pageable pageable);

    List<UserProjection> findAllByEmailAddressNotNull(Sort sort);
}
