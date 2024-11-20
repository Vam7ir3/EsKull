package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class PasswordReq {
    private Long userId;
    private String verificationCode;
    private String emailAddress;
    private String oldPassword;
    private String newPassword;
}
