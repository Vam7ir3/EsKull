package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_verification")
public class VerificationEntity extends CommonEntity {

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;//Encoded verification code

    @Column(name = "verification_code_sent_count", nullable = false)
    private int verificationCodeSentCount;

    @Transient
    private String code;//Raw verification code

    public VerificationEntity setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public VerificationEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public VerificationEntity setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public VerificationEntity setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public VerificationEntity setVerificationCodeSentCount(int verificationCodeSentCount) {
        this.verificationCodeSentCount = verificationCodeSentCount;
        return this;
    }

    public VerificationEntity setCode(String code) {
        this.code = code;
        return this;
    }
}
