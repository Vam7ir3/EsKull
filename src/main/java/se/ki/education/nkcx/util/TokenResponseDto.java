package se.ki.education.nkcx.util;

import lombok.Data;

@Data
public class TokenResponseDto {
    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
}
