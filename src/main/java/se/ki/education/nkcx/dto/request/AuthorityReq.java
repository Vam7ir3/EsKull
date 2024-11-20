package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class AuthorityReq {
    private Long id;
    private String title;
    private String description;
}
