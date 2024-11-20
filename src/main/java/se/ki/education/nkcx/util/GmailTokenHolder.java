package se.ki.education.nkcx.util;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GmailTokenHolder {
    private String accessToken;
    private LocalDateTime expireAt;
    private String scope;
    private String tokenType;
}
