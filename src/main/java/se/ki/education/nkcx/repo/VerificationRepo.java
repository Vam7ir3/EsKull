package se.ki.education.nkcx.repo;

import se.ki.education.nkcx.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VerificationRepo extends JpaRepository<VerificationEntity, Long> {
    VerificationEntity findByEmailAddress(String emailAddress);

    VerificationEntity findByMobileNumber(String mobileNumber);

    VerificationEntity findByIdAndVerificationCodeAndDeadlineAfter(Long id, String verificationCode, LocalDateTime now);

    VerificationEntity findByEmailAddressAndMobileNumber(String emailAddress, String mobileNumber);
}
