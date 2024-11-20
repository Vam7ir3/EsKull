package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationRes {
    private Long id;
    private String emailAddress;
    private String mobileNumber;
    private String verificationCode;
    private boolean emailSent;
    private boolean smsSent;

    public VerificationRes setId(Long id) {
        this.id = id;
        return this;
    }

    public VerificationRes setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public VerificationRes setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public VerificationRes setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public VerificationRes setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
        return this;
    }

    public VerificationRes setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
        return this;
    }
}
