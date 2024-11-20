package se.ki.education.nkcx.dto.request;

import se.ki.education.nkcx.enums.Gender;
import se.ki.education.nkcx.enums.IOSNotificationMode;
import se.ki.education.nkcx.enums.Platform;
import se.ki.education.nkcx.util.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserReq {
    private Long id;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateOfBirth;

    private String mobileNumber;
    private String emailAddress;
    private String image;

    private VerificationReq verification;

    private String deviceToken;
    private Platform devicePlatform;
    private IOSNotificationMode iosNotificationMode;
    private Integer badgeCount;

    private Long roleId;
}
