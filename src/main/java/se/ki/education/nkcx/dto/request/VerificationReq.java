package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class VerificationReq {
    private Long id;
    private String emailAddress;
    private String mobileNumber;
    private String verificationCode;
    private boolean emailSent;
    private boolean smsSent;
}
